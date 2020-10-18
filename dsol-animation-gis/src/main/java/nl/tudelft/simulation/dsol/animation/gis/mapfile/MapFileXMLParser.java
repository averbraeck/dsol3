package nl.tudelft.simulation.dsol.animation.gis.mapfile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.xerces.parsers.DOMParser;
import org.djutils.io.URLResource;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.DOMBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import nl.javel.gisbeans.io.esri.ShapeFile;
import nl.tudelft.simulation.dsol.animation.gis.ScreenPosition;
import nl.tudelft.simulation.dsol.animation.gis.SerializableRectangle2D;
import nl.tudelft.simulation.dsol.animation.gis.map.AbstractAttribute;
import nl.tudelft.simulation.dsol.animation.gis.map.Attribute;
import nl.tudelft.simulation.dsol.animation.gis.map.AttributeInterface;
import nl.tudelft.simulation.dsol.animation.gis.map.Layer;
import nl.tudelft.simulation.dsol.animation.gis.map.LayerInterface;
import nl.tudelft.simulation.dsol.animation.gis.map.Map;
import nl.tudelft.simulation.dsol.animation.gis.map.MapImage;
import nl.tudelft.simulation.dsol.animation.gis.map.MapImageInterface;
import nl.tudelft.simulation.dsol.animation.gis.map.MapInterface;
import nl.tudelft.simulation.dsol.animation.gis.map.StaticAttribute;
import nl.tudelft.simulation.dsol.animation.gis.transform.CoordinateTransform;

/**
 * This class parses XML-mapfiles and constructs appropriate map objects. <br>
 * Validation is set with the system property nl.javel.gisbeans.map.mapfile.validate =[true|false] and the xmlschema is set with
 * the systemproperty nl.javel.gisbeans.map.mapfile.schema = [file]
 * <p>
 * copyright (c) 2002-2019 <a href="http://www.javel.nl">Javel b.v. </a>, the Netherlands.
 * </p>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no warranty.
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 */
public final class MapFileXMLParser
{
    /** the default mapfile. */
    public static final URL MAPFILE_SCHEMA = URLResource.getResource("/mapfile.xsd");

    /**
     * parses a Mapfile URL to a mapFile.
     * @param url URL; the mapfile url.
     * @param coordinateTransform CoordinateTransform; the transformation of (x, y) coordinates to (x', y') coordinates.
     * @return MapInterface the parsed mapfile.
     * @throws IOException on failure.
     */
    public static MapInterface parseMapFile(final URL url, final CoordinateTransform coordinateTransform) throws IOException
    {
        try
        {
            Map map = new Map();
            DOMParser domParser = new DOMParser();
            domParser.setFeature("http://xml.org/sax/features/validation", true);
            domParser.setFeature("http://apache.org/xml/features/validation/schema", true);
            domParser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation",
                    "http://www.javel.nl/gisbeans " + MAPFILE_SCHEMA.toExternalForm());
            domParser.setErrorHandler(new ValidHandler());
            domParser.parse(url.toExternalForm());
            Document document = domParser.getDocument();
            DOMBuilder builder = new DOMBuilder();
            org.jdom2.Document jdomDocument = builder.build(document);
            Element xmlMapFileElement = jdomDocument.getRootElement();
            Element element = null;
            Namespace nameSpace = xmlMapFileElement.getNamespace();
            map.setName(xmlMapFileElement.getChildText("name"));
            if ((element = xmlMapFileElement.getChild("units")) != null)
            {
                map.setUnits(parseUnits(element));
            }
            double[] extent = new double[4];
            double minx = Double.valueOf(xmlMapFileElement.getChild("extent").getChildText("minX")).doubleValue();
            double miny = Double.valueOf(xmlMapFileElement.getChild("extent").getChildText("minY")).doubleValue();
            double maxx = Double.valueOf(xmlMapFileElement.getChild("extent").getChildText("maxX")).doubleValue();
            double maxy = Double.valueOf(xmlMapFileElement.getChild("extent").getChildText("maxY")).doubleValue();
            double[] p = coordinateTransform.doubleTransform(minx, miny);
            double[] q = coordinateTransform.doubleTransform(maxx, maxy);
            extent[MapInterface.MINX] = Math.min(p[0], q[0]);
            extent[MapInterface.MINY] = Math.min(p[1], q[1]);
            extent[MapInterface.MAXX] = Math.max(p[0], q[0]);
            extent[MapInterface.MAXY] = Math.max(p[1], q[1]);
            map.setExtent(
                    new SerializableRectangle2D.Double(extent[0], extent[1], (extent[2] - extent[0]), (extent[3] - extent[1])));
            if ((element = xmlMapFileElement.getChild("image", nameSpace)) != null)
            {
                map.setImage(parseImage(element, nameSpace));
            }
            java.util.List layerElements = xmlMapFileElement.getChildren("layer", nameSpace);
            ArrayList layers = new ArrayList();
            for (Iterator iterator = layerElements.iterator(); iterator.hasNext();)
            {
                Element layerElement = (Element) iterator.next();
                layers.add(parseLayer(layerElement, coordinateTransform));
            }
            map.setLayers(layers);
            return map;
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }

    /**
     * returns the columnNumber.
     * @param columnNames String[]; the names
     * @param columnName String; the name
     * @return the number
     */
    private static int getColumnNumber(final String[] columnNames, final String columnName)
    {
        for (int i = 0; i < columnNames.length; i++)
        {
            if (columnNames[i].equalsIgnoreCase(columnName))
                return i;
        }
        return -1;
    }

    /**
     * parses a xml-element representing an attribute.
     * @param element Element; The j-dom element
     * @param layer LayerInterface; the layer.
     * @return AttributeInterface value
     * @throws IOException
     */
    private static AttributeInterface parseAttribute(final Element element, final LayerInterface layer) throws IOException
    {
        AbstractAttribute result = null;
        if (element.getChild("textColumn") != null)
        {
            int textColumnNumber = MapFileXMLParser.getColumnNumber(layer.getDataSource().getColumnNames(),
                    element.getChildText("textColumn"));
            int angleColumnNumber = -1;
            if (element.getChild("degreesColumn") != null)
            {
                angleColumnNumber = MapFileXMLParser.getColumnNumber(layer.getDataSource().getColumnNames(),
                        element.getChildText("degreesColumn"));
                result = new Attribute(layer, Attribute.DEGREES, angleColumnNumber, textColumnNumber);
            }
            else if (element.getChild("radiansColumn") != null)
            {
                angleColumnNumber = MapFileXMLParser.getColumnNumber(layer.getDataSource().getColumnNames(),
                        element.getChildText("radiansColumn"));
                result = new Attribute(layer, Attribute.RADIANS, angleColumnNumber, textColumnNumber);
            }
            else
            {
                result = new Attribute(layer, Attribute.RADIANS, -1, textColumnNumber);
            }
        }
        else
        {
            double angle = 0.0;
            if (element.getChild("degrees") != null)
            {
                angle = Math.toRadians(Double.valueOf(element.getChildText("degrees")).doubleValue());
            }
            if (element.getChild("radians") != null)
            {
                angle = Double.valueOf(element.getChildText("radians")).doubleValue();
            }
            String value = "";
            if (element.getChild("text") != null)
            {
                value = element.getChildText("text");
            }
            result = new StaticAttribute(layer, angle, value);
        }
        if (element.getChild("minScale") != null)
            result.setMinScale(Integer.valueOf(element.getChildText("minScale")).intValue());
        else
            result.setMinScale(layer.getMinScale());
        if (element.getChild("maxScale") != null)
            result.setMaxScale(Integer.valueOf(element.getChildText("maxScale")).intValue());
        else
            result.setMaxScale(layer.getMaxScale());
        if (element.getChild("position") != null)
            result.setPosition(parsePosition(element.getChild("position")));
        if (element.getChild("font") != null)
        {
            result.setFont(parseFont(element.getChild("font")));
            result.setFontColor(parseColor(element.getChild("font").getChild("fontColor")));
        }
        return result;
    }

    /**
     * parses a xml-element representing a boolean
     * @param element Element; The j-dom element
     * @return Boolean value
     * @throws IOException
     */
    private static boolean parseBoolean(final Element element) throws IOException
    {
        try
        {
            boolean result = false;
            if (element.getText().equals("true"))
                result = true;
            return result;
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }

    /**
     * parses a xml-element representing a Color
     * @param element Element; The j-dom element
     * @return Color of element
     * @throws IOException
     */
    private static Color parseColor(final Element element) throws IOException
    {
        try
        {
            int r = Integer.valueOf(element.getChildText("r")).intValue();
            int g = Integer.valueOf(element.getChildText("g")).intValue();
            int b = Integer.valueOf(element.getChildText("b")).intValue();
            if (element.getChildText("a") != null)
            {
                int a = Integer.valueOf(element.getChildText("a")).intValue();
                return new java.awt.Color(r, g, b, a);
            }
            return new java.awt.Color(r, g, b);
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }

    /**
     * parses a xml-element representing a Dimension.
     * @param element Element; The j-dom element
     * @return Dimension of element
     * @throws IOException
     */
    private static Dimension parseDimension(final Element element) throws IOException
    {
        try
        {
            int height = Integer.valueOf(element.getChildText("height")).intValue();
            int width = Integer.valueOf(element.getChildText("width")).intValue();
            return new Dimension(width, height);
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }

    /**
     * parses a xml-element representing a double[].
     * @param element Element; The j-dom element
     * @return Value of extent
     * @throws IOException
     */
    private static double[] parseExtent(final Element element, final CoordinateTransform coordinateTransform) throws IOException
    {
        try
        {
            double[] result = new double[4];
            double minx = Double.valueOf(element.getChildText("minX")).doubleValue();
            double miny = Double.valueOf(element.getChildText("minY")).doubleValue();
            double maxx = Double.valueOf(element.getChildText("maxX")).doubleValue();
            double maxy = Double.valueOf(element.getChildText("maxY")).doubleValue();
            double[] p = coordinateTransform.doubleTransform(minx, miny);
            double[] q = coordinateTransform.doubleTransform(maxx, maxy);
            result[MapInterface.MINX] = Math.min(p[0], q[0]);
            result[MapInterface.MINY] = Math.min(p[1], q[1]);
            result[MapInterface.MAXY] = Math.max(p[0], q[0]);
            result[MapInterface.MAXY] = Math.max(p[1], q[1]);
            return result;
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }

    /**
     * parses a xml-element representing a font.
     * @param element Element; The j-dom element
     * @return the font
     * @throws IOException
     */
    private static Font parseFont(final Element element) throws IOException
    {
        try
        {
            String fontName = element.getChildText("fontName");
            int fontSize = Integer.valueOf(element.getChildText("fontSize")).intValue();
            return new Font(fontName, Font.TRUETYPE_FONT, fontSize);
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }

    /**
     * parses a xml-element representing the Image.
     * @param element org.jdom2.Element; The j-dom element
     * @param nameSpace Namespace; The gisbeans namespace
     * @return Value of image
     * @throws IOException
     */
    private static MapImageInterface parseImage(final Element element, final Namespace nameSpace) throws IOException
    {
        MapImageInterface result = new MapImage();
        try
        {
            if (element.getChild("backgroundColor") != null)
                result.setBackgroundColor(parseColor(element.getChild("backgroundColor")));
            if (element.getChild("size") != null)
                result.setSize(parseDimension(element.getChild("size")));
            return result;
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }

    /**
     * parses a xml-element representing a Layer.
     * @param element Element; The j-dom element
     * @param coordinateTransform CoordinateTransform; the transformation of (x, y) coordinates to (x', y') coordinates.
     * @return Layer of element
     * @throws IOException
     */
    private static LayerInterface parseLayer(final Element element, final CoordinateTransform coordinateTransform)
            throws IOException
    {
        LayerInterface result = new Layer();
        try
        {
            if (element.getChild("color") != null)
                result.setColor(parseColor(element.getChild("color")));
            if (element.getChild("data").getChild("shapeFile") != null)
            {
                String resourceName = element.getChild("data").getChildText("shapeFile");
                URL resource = URLResource.getResource(resourceName);
                if (resource == null)
                {
                    throw new IOException("Cannot locate shapeFile: " + resourceName);
                }
                ShapeFile dataSource = new ShapeFile(resource, coordinateTransform);
                if (element.getAttribute("cache") != null && element.getAttribute("cache").getValue().equals("false"))
                {
                    dataSource.setCache(false);
                }
                else
                {
                    dataSource.setCache(true);
                }
                result.setDataSource(dataSource);
            }
            if (element.getChild("data").getChild("SDI") != null)
            {
                throw new IOException("SDI not yet supported");
            }
            if (element.getChild("maxScale") != null)
                result.setMaxScale(Integer.valueOf(element.getChildText("maxScale")).intValue());
            if (element.getChild("minScale") != null)
                result.setMinScale(Integer.valueOf(element.getChildText("minScale")).intValue());
            result.setName(element.getChildText("name"));
            if (element.getChild("outlineColor") != null)
                result.setOutlineColor(parseColor(element.getChild("outlineColor")));
            if (element.getChild("status") != null)
                result.setStatus(parseBoolean(element.getChild("status")));
            if (element.getChild("transform") != null)
                result.setTransform(parseBoolean(element.getChild("transform")));
            java.util.List attributesElements = element.getChildren("attribute");
            ArrayList attributes = new ArrayList();
            for (Iterator iterator = attributesElements.iterator(); iterator.hasNext();)
            {
                Element attributeElement = (Element) iterator.next();
                attributes.add(parseAttribute(attributeElement, result));
            }
            result.setAttributes(attributes);
            return result;
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }

    /**
     * parses a xml-element representing a Position.
     * @param element org.jdom2.Element; The j-dom element
     * @return Position of element
     */
    @SuppressWarnings("checkstyle:needbraces")
    private static ScreenPosition parsePosition(final Element element)
    {
        if (element.getText().equals("ul"))
            return ScreenPosition.UpperLeft;
        if (element.getText().equals("uc"))
            return ScreenPosition.UpperCenter;
        if (element.getText().equals("ur"))
            return ScreenPosition.UpperRight;
        if (element.getText().equals("cl"))
            return ScreenPosition.CenterLeft;
        if (element.getText().equals("cc"))
            return ScreenPosition.CenterCenter;
        if (element.getText().equals("cr"))
            return ScreenPosition.CenterRight;
        if (element.getText().equals("ll"))
            return ScreenPosition.LowerLeft;
        if (element.getText().equals("lc"))
            return ScreenPosition.LowerCenter;
        if (element.getText().equals("lr"))
            return ScreenPosition.LowerRight;
        return ScreenPosition.UpperLeft;
    }

    /**
     * parses a xml-element representing a Units.
     * @param element org.jdom2.Element; The j-dom element
     * @return Units of element
     */
    @SuppressWarnings("checkstyle:needbraces")
    private static int parseUnits(final Element element)
    {
        if (element.getText().equals("feet"))
            return MapInterface.FEET;
        if (element.getText().equals("dd"))
            return MapInterface.DD;
        if (element.getText().equals("inches"))
            return MapInterface.INCHES;
        if (element.getText().equals("kilometers"))
            return MapInterface.KILOMETERS;
        if (element.getText().equals("meters"))
            return MapInterface.METERS;
        if (element.getText().equals("miles"))
            return MapInterface.MILES;
        return MapInterface.METERS;
    }

    /**
     * The validationHandler.
     */
    protected static class ValidHandler extends DefaultHandler
    {
        /**
         * format the exception with line number, column number, etc.
         * @param exception SAXParseException;
         * @return the format error
         */
        private String formatError(final SAXParseException exception)
        {
            return "SAXParseException: \nsystemId=" + exception.getSystemId() + "\npublicId=" + exception.getSystemId()
                    + "\nMessage=" + exception.getMessage() + "\nline,col=" + exception.getLineNumber() + ","
                    + exception.getColumnNumber();
        }

        /** {@inheritDoc} */
        @Override
        public void error(final SAXParseException e) throws SAXException
        {
            throw new SAXException(formatError(e));
        }

        /** {@inheritDoc} */
        @Override
        public void fatalError(final SAXParseException e) throws SAXException
        {
            throw new SAXException(formatError(e));
        }
    }
}
