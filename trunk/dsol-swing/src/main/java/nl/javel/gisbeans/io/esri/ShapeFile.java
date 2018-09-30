/*
 * Shapefile.java
 * 
 * Created on October 11, 2002 Last edited on October 11, 2002
 */
package nl.javel.gisbeans.io.esri;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import nl.javel.gisbeans.geom.GisObject;
import nl.javel.gisbeans.geom.SerializableGeneralPath;
import nl.javel.gisbeans.geom.SerializableRectangle2D;
import nl.javel.gisbeans.io.DataSourceInterface;
import nl.javel.gisbeans.io.EndianInterface;
import nl.javel.gisbeans.io.ObjectEndianInputStream;
import nl.javel.gisbeans.map.MapInterface;
import nl.tudelft.simulation.language.d2.Shape;
import nl.tudelft.simulation.logger.CategoryLogger;

/**
 * This class reads ESRI-shapefiles and returns the shape object
 * @author <a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a> <br>
 *         <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.2
 */
public class ShapeFile implements DataSourceInterface
{
    /** the URLS */
    private URL shpFile = null, shxFile = null, dbfFile = null;

    /** the number of shapes. */
    private int numShapes = 0;

    /** the type. */
    private int type = MapInterface.POLYGON;

    /** our DBF reader. */
    private DbfReader dbfReader;

    /** the NULLSHAPE as defined by ESRI */
    public static final int NULLSHAPE = 0;

    /** the POINT as defined by ESRI */
    public static final int POINT = 1;

    /** the POLYLINE as defined by ESRI */
    public static final int POLYLINE = 3;

    /** the POLYGON as defined by ESRI */
    public static final int POLYGON = 5;

    /** the MULTIPOINT as defined by ESRI */
    public static final int MULTIPOINT = 8;

    /** the POINTZ as defined by ESRI */
    public static final int POINTZ = 11;

    /** the POLYLINEZ as defined by ESRI */
    public static final int POLYLINEZ = 13;

    /** the POLYGONZ as defined by ESRI */
    public static final int POLYGONZ = 15;

    /** the MULTIPOINTZ as defined by ESRI */
    public static final int MULTIPOINTZ = 18;

    /** the POINM as defined by ESRI */
    public static final int POINTM = 21;

    /** the POLYLINEM as defined by ESRI */
    public static final int POLYLINEM = 23;

    /** the POLYGONM as defined by ESRI */
    public static final int POLYGONM = 25;

    /** the MULTIPOINTM as defined by ESRI */
    public static final int MULTIPOINTM = 28;

    /** the MULTIPATCH as defined by ESRI */
    public static final int MULTIPATCH = 31;

    /** may we cache parsed data?.. */
    private boolean cache = true;

    /** the cachedContent. */
    private ArrayList cachedContent = null;

    /** an optional transformation of the lat/lon (or other) coordinates. */
    private final CoordinateTransform coordinateTransform;

    /**
     * constructs a new ESRI ShapeFile.
     * @param url URL may or may not end with their extension.
     * @param coordinateTransform the transformation of (x, y) coordinates to (x', y') coordinates.
     * @throws IOException throws an IOException if the shxFile is not accessable
     */
    public ShapeFile(final java.net.URL url, final CoordinateTransform coordinateTransform) throws IOException
    {
        this.coordinateTransform = coordinateTransform;
        String fileName = url.toString();
        if (fileName.endsWith(".shp") || fileName.endsWith(".shx") || fileName.endsWith(".dbf"))
        {
            fileName = fileName.substring(0, fileName.length() - 4);
        }
        this.shpFile = new URL(fileName + ".shp");
        this.shxFile = new URL(fileName + ".shx");
        this.dbfFile = new URL(fileName + ".dbf");
        try
        {
            URLConnection connection = this.shxFile.openConnection();
            connection.connect();
            this.numShapes = (connection.getContentLength() - 100) / 8;
            this.dbfReader = new DbfReader(this.dbfFile);
        }
        catch (IOException exception)
        {
            throw new IOException("Can't read " + this.shxFile.toString());
        }
    }

    /**
     * @return Returns the cache.
     */
    public boolean isCache()
    {
        return this.cache;
    }

    /**
     * @param cache The cache to set.
     */
    public void setCache(boolean cache)
    {
        this.cache = cache;
        this.dbfReader.setCache(cache);
    }

    /** {@inheritDoc} */
    @Override
    public String[] getColumnNames()
    {
        return this.dbfReader.getColumnNames();
    }

    /** {@inheritDoc} */
    @Override
    public String[][] getAttributes() throws IOException
    {
        return this.dbfReader.getRows();
    }

    /** {@inheritDoc} */
    @Override
    public URL getDataSource()
    {
        return this.shpFile;
    }

    /** {@inheritDoc} */
    @Override
    public int getNumShapes()
    {
        return this.numShapes;
    }

    /**
     * getter for a specific shape at a certain index point in shapefile.
     * @param index the index of the shape
     * @return Object shape
     * @throws IOException on IOfailure
     */
    public synchronized GisObject getShape(int index) throws IOException
    {
        if (index > this.numShapes || index < 0)
        {
            throw new IndexOutOfBoundsException("Index =" + index + " number of shapes in layer :" + this.numShapes);
        }

        // May we use the cache?
        if (this.cache && this.cachedContent != null)
        {
            return (GisObject) this.cachedContent.get(index);
        }

        ObjectEndianInputStream indexInput = new ObjectEndianInputStream(this.shxFile.openStream());
        indexInput.skipBytes(8 * index + 100);
        int offset = 2 * indexInput.readInt();
        indexInput.close();
        ObjectEndianInputStream shapeInput = new ObjectEndianInputStream(this.shpFile.openStream());
        shapeInput.skipBytes(offset);
        Object shape = this.readShape(shapeInput);
        shapeInput.close();
        return new GisObject(shape, this.dbfReader.getRow(index));
    }

    /**
     * getter for all shapes in a shapefile.
     * @return HashMap (Object shape)
     * @throws IOException on IOfailure
     */
    public synchronized List getShapes() throws IOException
    {
        // May we use the cache?
        if (this.cache && this.cachedContent != null)
        {
            return this.cachedContent;
        }

        ObjectEndianInputStream shapeInput = new ObjectEndianInputStream(this.shpFile.openStream());

        shapeInput.skipBytes(100);
        ArrayList results = new ArrayList(this.numShapes);
        String[][] attributes = this.dbfReader.getRows();
        for (int i = 0; i < this.numShapes; i++)
        {
            results.add(new GisObject(this.readShape(shapeInput), attributes[i]));
        }
        shapeInput.close();

        // May we use the cache?
        if (this.cache)
        {
            this.cachedContent = results;
        }
        return results;
    }

    /**
     * getter for all shapes intersecting with a certain extent
     * @param extent the extent to get
     * @return HashMap (Object shape)
     * @throws IOException on IOfailure
     */
    public synchronized List getShapes(SerializableRectangle2D extent) throws IOException
    {
        // May we use the cache?
        if (this.cache)
        {
            if (this.cachedContent == null)
            {
                this.getShapes();
            }
            List result = new ArrayList();
            for (Iterator i = this.cachedContent.iterator(); i.hasNext();)
            {
                GisObject shape = (GisObject) i.next();
                if (shape.getShape() instanceof SerializableGeneralPath)
                {
                    if (Shape.overlaps(extent, ((SerializableGeneralPath) shape.getShape()).getBounds2D()))
                    {
                        result.add(shape);
                    }
                }
                else if (shape.getShape() instanceof Point2D)
                {
                    if (extent.contains((Point2D) shape.getShape()))
                    {
                        result.add(shape);
                    }
                }
                else
                {
                    CategoryLogger.always().error("unknown shape in cached content " + shape);
                }
            }
            return result;
        }

        ObjectEndianInputStream shapeInput = new ObjectEndianInputStream(this.shpFile.openStream());

        shapeInput.skipBytes(100);
        ArrayList results = new ArrayList();

        String[][] attributes = this.dbfReader.getRows();
        for (int i = 0; i < this.numShapes; i++)
        {
            shapeInput.setEncode(EndianInterface.BIG_ENDIAN);
            int shapeNumber = shapeInput.readInt();
            int contentLength = shapeInput.readInt();
            shapeInput.setEncode(EndianInterface.LITTLE_ENDIAN);
            int type = shapeInput.readInt();
            if (type != 0 && type != 1 && type != 11 && type != 21)
            {
                double[] min =
                        this.coordinateTransform.doubleTransform(shapeInput.readDouble(), shapeInput.readDouble());
                double[] max =
                        this.coordinateTransform.doubleTransform(shapeInput.readDouble(), shapeInput.readDouble());
                double minX = Math.min(min[0], max[0]);
                double minY = Math.min(min[1], max[1]);
                double width = Math.max(min[0], max[0]) - minX;
                double height = Math.max(min[1], max[1]) - minY;
                SerializableRectangle2D bounds = new SerializableRectangle2D.Double(minX, minY, width, height);
                if (Shape.overlaps(extent, bounds))
                {
                    results.add(new GisObject(this.readShape(shapeInput, shapeNumber, contentLength, type, false),
                            attributes[i]));
                }
                else
                {
                    shapeInput.skipBytes((2 * contentLength) - 36);
                }
            }
            else if (type != 0)
            {

                Point2D temp = (Point2D) this.readShape(shapeInput, shapeNumber, contentLength, type, false);
                if (extent.contains(temp))
                {
                    results.add(new GisObject(temp, attributes[i]));
                }
            }
        }
        shapeInput.close();
        return results;
    }

    /**
     * getter for all shapes intersecting with a certain extent
     * @param attribute the attribute
     * @param columnName the name of the dbfColumn
     * @throws IOException on IO exception
     * @return the list of shapes
     */
    public synchronized List getShapes(String attribute, String columnName) throws IOException
    {
        ArrayList result = new ArrayList();
        int[] shapeNumbers = this.dbfReader.getRowNumbers(attribute, columnName);
        for (int i = 0; i < shapeNumbers.length; i++)
        {
            result.add(this.getShape(i));
        }
        return result;
    }

    /**
     * getter for the type
     * @return int
     */
    public int getType()
    {
        return this.type;
    }

    /**
     * reads a shape
     * @param input the inputStream
     * @return the shape
     * @throws IOException on IOException
     */
    private Object readShape(ObjectEndianInputStream input) throws IOException
    {
        return readShape(input, -1, -1, -1, true);
    }

    /**
     * @param input the inputstream
     * @param shapeNumber the number
     * @param contentLength the length of the content
     * @param type
     * @param skipBox
     * @return the shape
     * @throws IOException
     */
    private Object readShape(ObjectEndianInputStream input, int shapeNumber, int contentLength, int type,
            boolean skipBox) throws IOException
    {
        input.setEncode(EndianInterface.BIG_ENDIAN);
        if (shapeNumber == -1)
            shapeNumber = input.readInt();

        if (contentLength == -1)
            contentLength = input.readInt();

        input.setEncode(EndianInterface.LITTLE_ENDIAN);
        if (type == -1)
            type = input.readInt();
        switch (type)
        {
            case 0:
                return readNullShape(input);
            case 1:
                return readPoint(input);
            case 3:
                return readPolyLine(input, skipBox);
            case 5:
                return readPolygon(input, skipBox);
            case 8:
                return readMultiPoint(input, skipBox);
            case 11:
                return readPointZ(input, contentLength);
            case 13:
                return readPolyLineZ(input, contentLength, skipBox);
            case 15:
                return readPolygonZ(input, contentLength, skipBox);
            case 18:
                return readMultiPointZ(input, contentLength, skipBox);
            case 21:
                return readPointM(input, contentLength);
            case 23:
                return readPolyLineM(input, contentLength, skipBox);
            case 25:
                return readPolygonM(input, contentLength, skipBox);
            case 28:
                return readMultiPointM(input, contentLength, skipBox);
            case 31:
                return readMultiPatch(input, contentLength, skipBox);
            default:
                throw new IOException("Unknown shape type or shape type not supported");
        }
    }

    /**
     * reads a nullshape
     * @param input the inputStream
     * @return a nullobject
     * @throws IOException on IOException
     */
    private synchronized Object readNullShape(ObjectEndianInputStream input) throws IOException
    {
        if (input != null)
            throw new IOException("readNullShape inputStream is is not null");
        return null;
    }

    /**
     * reads a Point
     * @param input the inputStream
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPoint(ObjectEndianInputStream input) throws IOException
    {
        this.type = MapInterface.POINT;
        input.setEncode(EndianInterface.LITTLE_ENDIAN);
        double[] point = this.coordinateTransform.doubleTransform(input.readDouble(), input.readDouble());
        return new Point2D.Double(point[0], point[1]);
    }

    /**
     * reads a PolyLine
     * @param input the inputStream
     * @param skipBox whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPolyLine(ObjectEndianInputStream input, boolean skipBox) throws IOException
    {
        this.type = MapInterface.LINE;
        if (skipBox == true)
            input.skipBytes(32);
        input.setEncode(EndianInterface.LITTLE_ENDIAN);
        int numParts = input.readInt();
        int numPoints = input.readInt();
        int[] partBegin = new int[numParts + 1];

        for (int i = 0; i < partBegin.length - 1; i++)
        {
            partBegin[i] = input.readInt();

        }
        partBegin[partBegin.length - 1] = numPoints;

        SerializableGeneralPath result = new SerializableGeneralPath(GeneralPath.WIND_NON_ZERO, numPoints);
        for (int i = 0; i < numParts; i++)
        {
            float[] mf = this.coordinateTransform.floatTransform(input.readDouble(), input.readDouble());
            result.moveTo(mf[0], mf[1]);
            for (int ii = (partBegin[i] + 1); ii < partBegin[i + 1]; ii++)
            {
                float[] lf = this.coordinateTransform.floatTransform(input.readDouble(), input.readDouble());
                result.lineTo(lf[0], lf[1]);
            }
        }
        return result;
    }

    /**
     * reads a Polygon
     * @param input the inputStream
     * @param skipBox whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPolygon(ObjectEndianInputStream input, boolean skipBox) throws IOException
    {
        this.type = MapInterface.POLYGON;
        if (skipBox == true)
            input.skipBytes(32);
        input.setEncode(EndianInterface.LITTLE_ENDIAN);
        int numParts = input.readInt();
        int numPoints = input.readInt();
        int[] partBegin = new int[numParts + 1];

        for (int i = 0; i < partBegin.length - 1; i++)
        {
            partBegin[i] = input.readInt();
        }
        partBegin[partBegin.length - 1] = numPoints;

        SerializableGeneralPath result = new SerializableGeneralPath(GeneralPath.WIND_NON_ZERO, numPoints);
        for (int i = 0; i < numParts; i++)
        {
            float[] mf = this.coordinateTransform.floatTransform(input.readDouble(), input.readDouble());
            result.moveTo(mf[0], mf[1]);
            for (int ii = (partBegin[i] + 1); ii < partBegin[i + 1]; ii++)
            {
                float[] lf = this.coordinateTransform.floatTransform(input.readDouble(), input.readDouble());
                result.lineTo(lf[0], lf[1]);
            }
        }

        return result;
    }

    /**
     * reads a readMultiPoint
     * @param input the inputStream
     * @param skipBox whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readMultiPoint(ObjectEndianInputStream input, boolean skipBox) throws IOException
    {
        this.type = MapInterface.POINT;
        if (skipBox == true)
            input.skipBytes(32);
        input.setEncode(EndianInterface.LITTLE_ENDIAN);
        Point2D[] result = new Point2D.Double[input.readInt()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = (Point2D) readPoint(input);
        }

        return result;
    }

    /**
     * reads a readPointZ
     * @param input the inputStream
     * @param contentLength the contentLength
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPointZ(ObjectEndianInputStream input, int contentLength) throws IOException
    {
        this.type = MapInterface.POINT;
        Object point = this.readPoint(input);
        input.skipBytes((contentLength * 2) - 20);

        return point;
    }

    /**
     * reads a readPolyLineZ
     * @param input the inputStream
     * @param contentLength the contentLength
     * @param skipBox whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPolyLineZ(ObjectEndianInputStream input, int contentLength, boolean skipBox)
            throws IOException
    {
        this.type = MapInterface.LINE;
        if (skipBox == true)
            input.skipBytes(32);
        input.setEncode(EndianInterface.LITTLE_ENDIAN);
        int numParts = input.readInt();
        int numPoints = input.readInt();
        int byteCounter = 44;
        int[] partBegin = new int[numParts + 1];

        for (int i = 0; i < partBegin.length - 1; i++)
        {
            partBegin[i] = input.readInt();
            byteCounter += 4;
        }
        partBegin[partBegin.length - 1] = numPoints;

        SerializableGeneralPath result = new SerializableGeneralPath(GeneralPath.WIND_NON_ZERO, numPoints);
        for (int i = 0; i < numParts; i++)
        {
            float[] mf = this.coordinateTransform.floatTransform(input.readDouble(), input.readDouble());
            result.moveTo(mf[0], mf[1]);
            byteCounter += 16;
            for (int ii = (partBegin[i] + 1); ii < partBegin[i + 1]; ii++)
            {
                float[] lf = this.coordinateTransform.floatTransform(input.readDouble(), input.readDouble());
                result.lineTo(lf[0], lf[1]);
                byteCounter += 16;
            }
        }
        input.skipBytes((contentLength * 2) - byteCounter);

        return result;
    }

    /**
     * reads a readPolygonZ
     * @param input the inputStream
     * @param contentLength the contentLength
     * @param skipBox whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPolygonZ(ObjectEndianInputStream input, int contentLength, boolean skipBox)
            throws IOException
    {
        this.type = MapInterface.POLYGON;
        if (skipBox == true)
            input.skipBytes(32);
        input.setEncode(EndianInterface.LITTLE_ENDIAN);
        int numParts = input.readInt();
        int numPoints = input.readInt();
        int byteCounter = 44;
        int[] partBegin = new int[numParts + 1];
        for (int i = 0; i < partBegin.length - 1; i++)
        {
            partBegin[i] = input.readInt();
            byteCounter += 4;
        }
        partBegin[partBegin.length - 1] = numPoints;

        SerializableGeneralPath result = new SerializableGeneralPath(GeneralPath.WIND_NON_ZERO, numPoints);
        for (int i = 0; i < numParts; i++)
        {
            float[] mf = this.coordinateTransform.floatTransform(input.readDouble(), input.readDouble());
            result.moveTo(mf[0], mf[1]);
            byteCounter += 16;
            for (int ii = (partBegin[i] + 1); ii < partBegin[i + 1]; ii++)
            {
                float[] lf = this.coordinateTransform.floatTransform(input.readDouble(), input.readDouble());
                result.lineTo(lf[0], lf[1]);
                byteCounter += 16;
            }
        }
        input.skipBytes((contentLength * 2) - byteCounter);

        return result;
    }

    /**
     * reads a readMultiPointZ
     * @param input the inputStream
     * @param contentLength the contentLength
     * @param skipBox whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readMultiPointZ(ObjectEndianInputStream input, int contentLength, boolean skipBox)
            throws IOException
    {
        this.type = MapInterface.POINT;
        if (skipBox == true)
            input.skipBytes(32);
        input.setEncode(EndianInterface.LITTLE_ENDIAN);
        Point2D[] result = new Point2D.Double[input.readInt()];
        int byteCounter = 40;
        for (int i = 0; i < result.length; i++)
        {
            result[i] = (Point2D) readPoint(input);
            byteCounter += 16;
        }
        input.skipBytes((contentLength * 2) - byteCounter);

        return result;
    }

    /**
     * reads a readPointM
     * @param input the inputStream
     * @param contentLength the contentLength
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPointM(ObjectEndianInputStream input, int contentLength) throws IOException
    {
        this.type = MapInterface.POINT;
        Object point = this.readPoint(input);
        input.skipBytes((contentLength * 2) - 20);
        return point;
    }

    /**
     * reads a readPolyLineM
     * @param input the inputStream
     * @param contentLength the contentLength
     * @param skipBox whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPolyLineM(ObjectEndianInputStream input, int contentLength, boolean skipBox)
            throws IOException
    {
        this.type = MapInterface.LINE;
        if (skipBox == true)
        {
            input.skipBytes(32);
        }
        input.setEncode(EndianInterface.LITTLE_ENDIAN);
        int numParts = input.readInt();
        int numPoints = input.readInt();
        int byteCounter = 44;
        int[] partBegin = new int[numParts + 1];
        for (int i = 0; i < partBegin.length - 1; i++)
        {
            partBegin[i] = input.readInt();
            byteCounter += 4;
        }
        partBegin[partBegin.length - 1] = numPoints;

        SerializableGeneralPath result = new SerializableGeneralPath(GeneralPath.WIND_NON_ZERO, numPoints);
        for (int i = 0; i < numParts; i++)
        {
            float[] mf = this.coordinateTransform.floatTransform(input.readDouble(), input.readDouble());
            result.moveTo(mf[0], mf[1]);
            byteCounter += 16;
            for (int ii = (partBegin[i] + 1); ii < partBegin[i + 1]; ii++)
            {
                float[] lf = this.coordinateTransform.floatTransform(input.readDouble(), input.readDouble());
                result.lineTo(lf[0], lf[1]);
                byteCounter += 16;
            }
        }
        input.skipBytes((contentLength * 2) - byteCounter);
        return result;
    }

    /**
     * reads a readPolyLineM
     * @param input the inputStream
     * @param contentLength the contentLength
     * @param skipBox whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPolygonM(ObjectEndianInputStream input, int contentLength, boolean skipBox)
            throws IOException
    {
        this.type = MapInterface.POLYGON;
        if (skipBox == true)
            input.skipBytes(32);
        input.setEncode(EndianInterface.LITTLE_ENDIAN);
        int numParts = input.readInt();
        int numPoints = input.readInt();
        int byteCounter = 44;
        int[] partBegin = new int[numParts + 1];

        for (int i = 0; i < partBegin.length - 1; i++)
        {
            partBegin[i] = input.readInt();
            byteCounter += 4;
        }
        partBegin[partBegin.length - 1] = numPoints;

        SerializableGeneralPath result = new SerializableGeneralPath(GeneralPath.WIND_NON_ZERO, numPoints);
        for (int i = 0; i < numParts; i++)
        {
            float[] mf = this.coordinateTransform.floatTransform(input.readDouble(), input.readDouble());
            result.moveTo(mf[0], mf[1]);
            byteCounter += 16;
            for (int ii = (partBegin[i] + 1); ii < partBegin[i + 1]; ii++)
            {
                float[] lf = this.coordinateTransform.floatTransform(input.readDouble(), input.readDouble());
                result.lineTo(lf[0], lf[1]);
                byteCounter += 16;
            }
        }
        input.skipBytes((contentLength * 2) - byteCounter);
        return result;
    }

    /**
     * reads a readMultiPointM
     * @param input the inputStream
     * @param contentLength the contentLength
     * @param skipBox whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readMultiPointM(ObjectEndianInputStream input, int contentLength, boolean skipBox)
            throws IOException
    {
        this.type = MapInterface.POINT;
        if (skipBox == true)
            input.skipBytes(32);
        input.setEncode(EndianInterface.LITTLE_ENDIAN);
        Point2D[] result = new Point2D.Double[input.readInt()];
        int byteCounter = 40;
        for (int i = 0; i < result.length; i++)
        {
            result[i] = (Point2D) readPoint(input);
            byteCounter += 16;
        }
        input.skipBytes((contentLength * 2) - byteCounter);

        return result;
    }

    /**
     * reads a readMultiPointM
     * @param input the inputStream
     * @param contentLength the contentLength
     * @param skipBox whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readMultiPatch(ObjectEndianInputStream input, int contentLength, boolean skipBox)
            throws IOException
    {
        if (input != null || contentLength != 0 || skipBox != false)
        {
            throw new IOException(
                    "Please inform <a href=\"mailto:support@javel.nl\">support@javel.nl</a> that you need MultiPatch support");
        }
        return null;
    }
}
