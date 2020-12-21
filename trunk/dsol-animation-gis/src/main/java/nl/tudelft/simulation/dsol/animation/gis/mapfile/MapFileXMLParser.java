package nl.tudelft.simulation.dsol.animation.gis.mapfile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.djutils.exceptions.Throw;
import org.djutils.io.URLResource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import nl.tudelft.simulation.dsol.animation.gis.MapUnits;
import nl.tudelft.simulation.dsol.animation.gis.ScreenPosition;
import nl.tudelft.simulation.dsol.animation.gis.SerializableRectangle2D;
import nl.tudelft.simulation.dsol.animation.gis.io.DataSourceInterface;
import nl.tudelft.simulation.dsol.animation.gis.map.AttributeInterface;
import nl.tudelft.simulation.dsol.animation.gis.map.GisMap;
import nl.tudelft.simulation.dsol.animation.gis.map.GisMapInterface;
import nl.tudelft.simulation.dsol.animation.gis.map.Layer;
import nl.tudelft.simulation.dsol.animation.gis.map.LayerInterface;
import nl.tudelft.simulation.dsol.animation.gis.map.MapImage;
import nl.tudelft.simulation.dsol.animation.gis.map.MapImageInterface;
import nl.tudelft.simulation.dsol.animation.gis.transform.CoordinateTransform;

/**
 * This class parses an XML-mapfile that contains  and constructs appropriate map objects. <br>
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

    /** Utility class, no constructor. */
    private MapFileXMLParser()
    {
        // Utility class
    }

    /**
     * parses a Mapfile URL to a mapFile.
     * @param url URL; the mapfile url.
     * @param coordinateTransform CoordinateTransform; the transformation of (x, y) coordinates to (x', y') coordinates.
     * @return MapInterface the parsed mapfile.
     * @throws IOException on failure
     */
    public static GisMapInterface parseMapFile(final URL url, final CoordinateTransform coordinateTransform) throws IOException
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            SAXParser saxParser = factory.newSAXParser();
            saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource",
                    new File(MAPFILE_SCHEMA.toExternalForm()));
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setErrorHandler(new ValidationHandler());
            GisMapHandler gisMapHandler = new GisMapHandler(coordinateTransform);
            saxParser.parse(new FileInputStream(url.toExternalForm()), gisMapHandler);
            return gisMapHandler.getMap();
        }
        catch (SAXException | ParserConfigurationException exception)
        {
            throw new IOException(exception);
        }
    }

    /**
     * The actual parser of the map file XML-document.
     * <p>
     * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    protected static class GisMapHandler extends DefaultHandler
    {
        /** the map to return. */
        private final GisMap map;

        /** the coordinate transformation to use. */
        private final CoordinateTransform coordinateTransform;

        /** the value of the last parsed simple element as a String. */
        private String elementValue;

        /** the current state. */
        private String state = "";

        /** storage for the map extent, order is minX, minY, maxX, maxY. */
        private double[] extent;

        /** storage for the layers. */
        private List<LayerInterface> layers;
        
        /** storage for the current layer. */
        private LayerInterface layer;

        /** storage for the map image. */
        private MapImageInterface mapImage;

        /** int[2] storage for a dimension: [width, height]. */
        private int[] dimension;

        /** int[4] storage for a color: [r, g, b, a]. */
        private int[] color;
        
        /** storage for the dataSource. */
        private DataSourceInterface dataSource;
        
        /** storage for the layer attributes. */
        private List<? extends AttributeInterface> layerAttributes;

        /**
         * /** Make a new handler to parse the GisMap xml file.
         * @param coordinateTransform CoordinateTransform; the coordinate transformation to use
         */
        public GisMapHandler(final CoordinateTransform coordinateTransform)
        {
            this.map = new GisMap();
            this.coordinateTransform = coordinateTransform;
        }

        /**
         * Push a token to the state. E.g., when the element "layer" is parsed, the token "layer" will be added to the state
         * "map", leading to the state "map.layer".
         * @param token String; token to push to the state
         */
        private void statePush(final String token)
        {
            this.state += "." + token;
        }

        /**
         * Pop a token from the state. E.g., when the element "layer" hsa finished, the token "layer" will be popped from the
         * state "map.layer", leading to the state "map" again.
         */
        private void statePop()
        {
            this.state = this.state.substring(0, this.state.lastIndexOf('.'));
        }

        /** {@inheritDoc} */
        @Override
        public void characters(final char[] ch, final int start, final int length) throws SAXException
        {
            this.elementValue = new String(ch, start, length);
        }

        /** {@inheritDoc} */
        @Override
        public void startDocument() throws SAXException
        {
            this.state = "map";
            this.layers = new ArrayList<>();
        }

        /** {@inheritDoc} */
        @Override
        public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
                throws SAXException
        {
            switch (this.state)
            {
                case "map":
                    switch (qName)
                    {
                        case "name":
                            statePush("name");
                            break;
                        case "unit":
                            statePush("unit");
                            break;
                        case "extent":
                            initExtent();
                            statePush("extent");
                            break;
                        case "image":
                            this.mapImage = new MapImage();
                            statePush("image");
                            break;
                        case "layer":
                            this.layer = new Layer();
                            this.layerAttributes = new ArrayList<>();
                            statePush("layer");
                            break;
                        default:
                            break;
                    }

                case "map.extent":
                    statePush(qName);
                    break;

                case "map.image":
                    switch (qName)
                    {
                        case "backgroundColor":
                            initColor();
                            statePush("backgroundColor");
                            break;
                        case "size":
                            initDimension();
                            statePush("size");
                            break;
                        default:
                            break;
                    }

                case "map.image.size":
                    switch (qName)
                    {
                        case "height":
                            statePush("height");
                            break;
                        case "width":
                            statePush("width");
                            break;
                        default:
                            break;
                    }

                case "map.image.backgroundColor":
                    statePush(qName);
                    break;

                case "map.layer":
                    switch (qName)
                    {
                        case "name":
                            statePush("name");
                            break;
                        case "data":
                            // TODO initDataSource();
                            statePush("data");
                            break;
                        case "minScale":
                            statePush("minScale");
                            break;
                        case "maxScale":
                            statePush("maxScale");
                            break;
                        case "color":
                            initColor();
                            statePush("color");
                            break;
                        case "outlineColor":
                            initColor();
                            statePush("outlineColor");
                            break;
                        case "status":
                            statePush("status");
                            break;
                        case "tolerance":
                            // TODO initTolerance();
                            statePush("tolerance");
                            break;
                        case "transform":
                            statePush("transform");
                            break;
                        case "group":
                            statePush("group");
                            break;
                        case "attribute":
                            statePush("attribute");
                            break;
                        default:
                            break;
                    }
                    
                case "map.layer.color":
                    statePush(qName);
                    break;

                case "map.layer.outlineColor":
                    statePush(qName);
                    break;

                default:
                    break;
            }
        }

        /** {@inheritDoc} */
        @Override
        public void endElement(final String uri, final String localName, final String qName) throws SAXException
        {
            switch (this.state)
            {
                case "map.name":
                    this.map.setName(this.elementValue);
                    break;
                case "map.unit":
                    this.map.setUnits(parseUnits(this.elementValue));
                    break;
                case "map.extent":
                    this.map.setExtent(makeExtent());
                    break;
                case "map.image":
                    this.map.setImage(this.mapImage);
                    break;
                case "map.layer":
                    this.layers.add(this.layer);
                    break;

                case "map.extent.minX":
                    this.extent[0] = Double.valueOf(this.elementValue);
                    break;
                case "map.extent.minY":
                    this.extent[1] = Double.valueOf(this.elementValue);
                    break;
                case "map.extent.maxX":
                    this.extent[2] = Double.valueOf(this.elementValue);
                    break;
                case "map.extent.maxY":
                    this.extent[3] = Double.valueOf(this.elementValue);
                    break;

                case "map.image.backgroundColor":
                    this.mapImage.setBackgroundColor(makeColor());
                    break;
                case "map.image.size":
                    this.mapImage.setSize(makeDimension());
                    break;
                    
                case "map.image.size.width":
                    this.dimension[0] = Integer.parseInt(this.elementValue);
                    break;
                case "map.image.size.height":
                    this.dimension[1] = Integer.parseInt(this.elementValue);
                    break;

                case "map.layer.name":
                    this.layer.setName(this.elementValue);
                    break;
                case "map.layer.data":
                    // TODO this.layer.setDataSource(makeDataSource());
                    break;
                case "map.layer.minScale":
                    int minScale = Integer.parseInt(this.elementValue);
                    Throw.when(minScale < 0, SAXException.class, "layer.minScale < 0");
                    this.layer.setMinScale(minScale);
                    break;
                case "map.layer.maxScale":
                    int maxScale = Integer.parseInt(this.elementValue);
                    Throw.when(maxScale < 0, SAXException.class, "layer.maxScale < 0");
                    this.layer.setMaxScale(maxScale);
                    break;
                case "map.layer.color":
                    this.layer.setColor(makeColor());
                    break;
                case "map.layer.outlineColor":
                    this.layer.setOutlineColor(makeColor());
                    break;
                case "map.layer.status":
                    this.layer.setStatus(this.elementValue.equals("true"));
                    break;
                case "map.layer.tolerance":
                    // ignore for now
                    break;
                case "map.layer.transform":
                    this.layer.setTransform(this.elementValue.equals("true"));
                    break;
                    
                case "map.image.backgroundColor.r":
                case "map.layer.color.r":
                case "map.layer.outlineColor.r":
                    this.color[0] = Integer.parseInt(this.elementValue);
                    break;
                case "map.image.backgroundColor.g":
                case "map.layer.color.g":
                case "map.layer.outlineColor.g":
                    this.color[1] = Integer.parseInt(this.elementValue);
                    break;
                case "map.image.backgroundColor.b":
                case "map.layer.color.b":
                case "map.layer.outlineColor.b":
                    this.color[2] = Integer.parseInt(this.elementValue);
                    break;
                case "map.image.backgroundColor.a":
                case "map.layer.color.a":
                case "map.layer.outlineColor.a":
                    this.color[3] = Integer.parseInt(this.elementValue);
                    break;

                default:
                    //
            }
            statePop();
        }

        /** {@inheritDoc} */
        @Override
        public void endDocument() throws SAXException
        {
            this.map.setLayers(this.layers);
        }

        /**
         * Parses a xml-element representing the units for the map.
         * @param units String; the string representation of the units
         * @return MapUnits enum
         */
        @SuppressWarnings("checkstyle:needbraces")
        private MapUnits parseUnits(final String units)
        {
            if (units.equals("feet"))
                return MapUnits.FEET;
            if (units.equals("dd"))
                return MapUnits.DD;
            if (units.equals("inches"))
                return MapUnits.INCHES;
            if (units.equals("kilometers"))
                return MapUnits.KILOMETERS;
            if (units.equals("meters"))
                return MapUnits.METERS;
            if (units.equals("miles"))
                return MapUnits.MILES;
            return MapUnits.METERS;
        }

        /**
         * Initializes the extent storage.
         */
        private void initExtent()
        {
            this.extent = new double[4];
            this.extent[0] = Double.NaN;
            this.extent[1] = Double.NaN;
            this.extent[2] = Double.NaN;
            this.extent[3] = Double.NaN;
        }

        /**
         * Creates the extent for the map, in transformed units.
         * @return Rectangle2D; the extent for the map, in transformed units
         * @throws SAXException when minX, maxX, minY, maxY is missing in the extent
         */
        private Rectangle2D makeExtent() throws SAXException
        {
            Throw.when(
                    Double.isNaN(this.extent[0]) || Double.isNaN(this.extent[1]) || Double.isNaN(this.extent[2])
                            || Double.isNaN(this.extent[3]),
                    SAXException.class, "extent misses one or more of mimX, minY, maxX, maxY");
            double[] p = this.coordinateTransform.doubleTransform(this.extent[0], this.extent[1]);
            double[] q = this.coordinateTransform.doubleTransform(this.extent[2], this.extent[3]);
            this.extent[0] = Math.min(p[0], q[0]);
            this.extent[1] = Math.min(p[1], q[1]);
            this.extent[2] = Math.max(p[0], q[0]);
            this.extent[3] = Math.max(p[1], q[1]);
            return new SerializableRectangle2D.Double(this.extent[0], this.extent[1], (this.extent[2] - this.extent[0]),
                    (this.extent[3] - this.extent[1]));
        }

        /**
         * parses a xml-element representing a Position.
         * @param position String; the screen position represented as a String
         * @return Position as an enum
         */
        @SuppressWarnings("checkstyle:needbraces")
        private ScreenPosition parsePosition(final String position)
        {
            if (position.equals("ul"))
                return ScreenPosition.UpperLeft;
            if (position.equals("uc"))
                return ScreenPosition.UpperCenter;
            if (position.equals("ur"))
                return ScreenPosition.UpperRight;
            if (position.equals("cl"))
                return ScreenPosition.CenterLeft;
            if (position.equals("cc"))
                return ScreenPosition.CenterCenter;
            if (position.equals("cr"))
                return ScreenPosition.CenterRight;
            if (position.equals("ll"))
                return ScreenPosition.LowerLeft;
            if (position.equals("lc"))
                return ScreenPosition.LowerCenter;
            if (position.equals("lr"))
                return ScreenPosition.LowerRight;
            return ScreenPosition.UpperLeft;
        }

        /**
         * Initializes the color storage.
         */
        private void initColor()
        {
            this.color = new int[4];
            this.color[0] = -1;
            this.color[1] = -1;
            this.color[2] = -1;
            this.color[3] = -1;
        }

        /**
         * parses a xml-element representing a Color.
         * @return Color of element
         * @throws SAXException when r, g, or b is missing from the color definition
         */
        private Color makeColor() throws SAXException
        {
            Throw.when(this.color[0] < 0 || this.color[1] < 0 || this.color[2] < 0, SAXException.class,
                    "color misses one or more of r, g, b");
            Throw.when(this.color[0] > 255 || this.color[1] > 255 || this.color[2] > 255 || this.color[3] > 255,
                    SAXException.class, "color has value > 255 for r, g, b, or a");
            if (this.color[3] < 0)
            {
                return new Color(this.color[0], this.color[1], this.color[2]);
            }
            else
            {
                return new Color(this.color[0], this.color[1], this.color[2], this.color[3]);
            }
        }

        /**
         * Initializes the dimension storage.
         */
        private void initDimension()
        {
            this.dimension = new int[2];
            this.dimension[0] = -1;
            this.dimension[1] = -1;
        }

        /**
         * parses a xml-element representing a Dimension.
         * @return Dimension of element
         * @throws SAXException when width or height is missing
         */
        private Dimension makeDimension() throws SAXException
        {
            Throw.when(this.dimension[0] < 0 || this.dimension[1] < 0, SAXException.class,
                    "dimension misses one or more of width, height");
            return new Dimension(this.dimension[0], this.dimension[1]);
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

//        /**
//         * parses a xml-element representing an attribute.
//         * @param element Element; The j-dom element
//         * @param layer LayerInterface; the layer.
//         * @return AttributeInterface value
//         * @throws IOException
//         */
//        private static AttributeInterface parseAttribute(final Element element, final LayerInterface layer) throws IOException
//        {
//            AbstractAttribute result = null;
//            if (element.getChild("textColumn") != null)
//            {
//                int textColumnNumber = MapFileXMLParser.getColumnNumber(layer.getDataSource().getColumnNames(),
//                        element.getChildText("textColumn"));
//                int angleColumnNumber = -1;
//                if (element.getChild("degreesColumn") != null)
//                {
//                    angleColumnNumber = MapFileXMLParser.getColumnNumber(layer.getDataSource().getColumnNames(),
//                            element.getChildText("degreesColumn"));
//                    result = new Attribute(layer, Attribute.DEGREES, angleColumnNumber, textColumnNumber);
//                }
//                else if (element.getChild("radiansColumn") != null)
//                {
//                    angleColumnNumber = MapFileXMLParser.getColumnNumber(layer.getDataSource().getColumnNames(),
//                            element.getChildText("radiansColumn"));
//                    result = new Attribute(layer, Attribute.RADIANS, angleColumnNumber, textColumnNumber);
//                }
//                else
//                {
//                    result = new Attribute(layer, Attribute.RADIANS, -1, textColumnNumber);
//                }
//            }
//            else
//            {
//                double angle = 0.0;
//                if (element.getChild("degrees") != null)
//                {
//                    angle = Math.toRadians(Double.valueOf(element.getChildText("degrees")).doubleValue());
//                }
//                if (element.getChild("radians") != null)
//                {
//                    angle = Double.valueOf(element.getChildText("radians")).doubleValue();
//                }
//                String value = "";
//                if (element.getChild("text") != null)
//                {
//                    value = element.getChildText("text");
//                }
//                result = new StaticAttribute(layer, angle, value);
//            }
//            if (element.getChild("minScale") != null)
//                result.setMinScale(Integer.valueOf(element.getChildText("minScale")).intValue());
//            else
//                result.setMinScale(layer.getMinScale());
//            if (element.getChild("maxScale") != null)
//                result.setMaxScale(Integer.valueOf(element.getChildText("maxScale")).intValue());
//            else
//                result.setMaxScale(layer.getMaxScale());
//            if (element.getChild("position") != null)
//                result.setPosition(parsePosition(element.getChild("position")));
//            if (element.getChild("font") != null)
//            {
//                result.setFont(parseFont(element.getChild("font")));
//                result.setFontColor(parseColor(element.getChild("font").getChild("fontColor")));
//            }
//            return result;
//        }
//
//        /**
//         * parses a xml-element representing a font.
//         * @param element Element; The j-dom element
//         * @return the font
//         * @throws IOException
//         */
//        private static Font parseFont(final Element element) throws IOException
//        {
//            try
//            {
//                String fontName = element.getChildText("fontName");
//                int fontSize = Integer.valueOf(element.getChildText("fontSize")).intValue();
//                return new Font(fontName, Font.TRUETYPE_FONT, fontSize);
//            }
//            catch (Exception exception)
//            {
//                throw new IOException(exception.getMessage());
//            }
//        }
//
//        /**
//         * parses a xml-element representing a Layer.
//         * @param element Element; The j-dom element
//         * @param coordinateTransform CoordinateTransform; the transformation of (x, y) coordinates to (x', y') coordinates.
//         * @return Layer of element
//         * @throws IOException
//         */
//        private static LayerInterface parseLayer(final Element element, final CoordinateTransform coordinateTransform)
//                throws IOException
//        {
//            LayerInterface result = new Layer();
//            try
//            {
//                if (element.getChild("data").getChild("shapeFile") != null)
//                {
//                    String resourceName = element.getChild("data").getChildText("shapeFile");
//                    URL resource = URLResource.getResource(resourceName);
//                    if (resource == null)
//                    {
//                        throw new IOException("Cannot locate shapeFile: " + resourceName);
//                    }
//                    ShapeFile dataSource = new ShapeFile(resource, coordinateTransform);
//                    if (element.getAttribute("cache") != null && element.getAttribute("cache").getValue().equals("false"))
//                    {
//                        dataSource.setCache(false);
//                    }
//                    else
//                    {
//                        dataSource.setCache(true);
//                    }
//                    result.setDataSource(dataSource);
//                }
//
//                java.util.List attributesElements = element.getChildren("attribute");
//                ArrayList attributes = new ArrayList();
//                for (Iterator iterator = attributesElements.iterator(); iterator.hasNext();)
//                {
//                    Element attributeElement = (Element) iterator.next();
//                    attributes.add(parseAttribute(attributeElement, result));
//                }
//                result.setAttributes(attributes);
//                return result;
//            }
//            catch (Exception exception)
//            {
//                throw new IOException(exception.getMessage());
//            }
//        }
//
        /**
         * @return map
         */
        public final GisMap getMap()
        {
            return this.map;
        }

    }

    /**
     * The validationHandler.
     */
    protected static class ValidationHandler extends DefaultHandler
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
