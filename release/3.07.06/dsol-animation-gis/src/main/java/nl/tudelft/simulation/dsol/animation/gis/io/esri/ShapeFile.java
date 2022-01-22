package nl.tudelft.simulation.dsol.animation.gis.io.esri;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.logger.CategoryLogger;

import nl.tudelft.simulation.dsol.animation.gis.GisObject;
import nl.tudelft.simulation.dsol.animation.gis.SerializablePath;
import nl.tudelft.simulation.dsol.animation.gis.SerializableRectangle2D;
import nl.tudelft.simulation.dsol.animation.gis.io.DataSourceInterface;
import nl.tudelft.simulation.dsol.animation.gis.io.Endianness;
import nl.tudelft.simulation.dsol.animation.gis.io.ObjectEndianInputStream;
import nl.tudelft.simulation.dsol.animation.gis.map.GisMapInterface;
import nl.tudelft.simulation.dsol.animation.gis.transform.CoordinateTransform;
import nl.tudelft.simulation.language.d2.Shape;

/**
 * This class reads ESRI-shapefiles and returns the shape objects.
 * <p>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * <p>
 * The dsol-animation-gis project is based on the gisbeans project that has been part of DSOL since 2002, originally by Peter
 * Jacobs and Paul Jacobs.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class ShapeFile implements DataSourceInterface
{
    /** */
    private static final long serialVersionUID = 20201223L;

    /** the URL for the shape file to be read. */
    private URL shpFile = null;

    /** the URL for the shape index file to be read. */
    private URL shxFile = null;

    /** the URL for the dbase-III format file with texts to be read. */
    private URL dbfFile = null;

    /** the number of shapes we have read until now. */
    private int numShapes = 0;

    /** the type of shape we are working on. */
    private int currentType = GisMapInterface.POLYGON;

    /** our DBF reader. */
    private DbfReader dbfReader;

    /** the NULLSHAPE as defined by ESRI. */
    public static final int NULLSHAPE = 0;

    /** the POINT as defined by ESRI. */
    public static final int POINT = 1;

    /** the POLYLINE as defined by ESRI. */
    public static final int POLYLINE = 3;

    /** the POLYGON as defined by ESRI. */
    public static final int POLYGON = 5;

    /** the MULTIPOINT as defined by ESRI. */
    public static final int MULTIPOINT = 8;

    /** the POINTZ as defined by ESRI. */
    public static final int POINTZ = 11;

    /** the POLYLINEZ as defined by ESRI. */
    public static final int POLYLINEZ = 13;

    /** the POLYGONZ as defined by ESRI. */
    public static final int POLYGONZ = 15;

    /** the MULTIPOINTZ as defined by ESRI. */
    public static final int MULTIPOINTZ = 18;

    /** the POINM as defined by ESRI. */
    public static final int POINTM = 21;

    /** the POLYLINEM as defined by ESRI. */
    public static final int POLYLINEM = 23;

    /** the POLYGONM as defined by ESRI. */
    public static final int POLYGONM = 25;

    /** the MULTIPOINTM as defined by ESRI. */
    public static final int MULTIPOINTM = 28;

    /** the MULTIPATCH as defined by ESRI. */
    public static final int MULTIPATCH = 31;

    /** may we cache parsed data? */
    private boolean cache = true;

    /** the cachedContent. */
    private ArrayList<GisObject> cachedContent = null;

    /** an optional transformation of the lat/lon (or other) coordinates. */
    private final CoordinateTransform coordinateTransform;

    /**
     * constructs a new ESRI ShapeFile.
     * @param url URL; URL may or may not end with their extension.
     * @param coordinateTransform CoordinateTransform; the transformation of (x, y) coordinates to (x', y') coordinates.
     * @throws IOException throws an IOException if the shxFile is not accessable
     */
    public ShapeFile(final URL url, final CoordinateTransform coordinateTransform) throws IOException
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
     * Set whether caching is allowed or not.
     * @param cache boolean; The caching preference to set
     */
    public void setCache(final boolean cache)
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

    /** {@inheritDoc} */
    @Override
    public synchronized GisObject getShape(final int index) throws IOException
    {
        if (index > this.numShapes || index < 0)
        {
            throw new IndexOutOfBoundsException("Index =" + index + " number of shapes in layer :" + this.numShapes);
        }

        // May we use the cache?
        if (this.cache && this.cachedContent != null)
        {
            return this.cachedContent.get(index);
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

    /** {@inheritDoc} */
    @Override
    public synchronized List<GisObject> getShapes() throws IOException
    {
        // May we use the cache?
        if (this.cache && this.cachedContent != null)
        {
            return this.cachedContent;
        }

        ObjectEndianInputStream shapeInput = new ObjectEndianInputStream(this.shpFile.openStream());

        shapeInput.skipBytes(100);
        ArrayList<GisObject> results = new ArrayList<>(this.numShapes);
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

    /** {@inheritDoc} */
    @Override
    public synchronized List<GisObject> getShapes(final Bounds2d extent) throws IOException
    {
        // May we use the cache?
        if (this.cache)
        {
            if (this.cachedContent == null)
            {
                this.getShapes();
            }
            List<GisObject> result = new ArrayList<>();
            for (Iterator<GisObject> i = this.cachedContent.iterator(); i.hasNext();)
            {
                GisObject shape = i.next();
                if (shape.getShape() instanceof SerializablePath)
                {
                    if (Shape.overlaps(extent.toRectangle2D(), ((SerializablePath) shape.getShape()).getBounds2D()))
                    {
                        result.add(shape);
                    }
                }
                else if (shape.getShape() instanceof Point2D)
                {
                    if (extent.toRectangle2D().contains((Point2D) shape.getShape()))
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
        ArrayList<GisObject> results = new ArrayList<>();

        String[][] attributes = this.dbfReader.getRows();
        for (int i = 0; i < this.numShapes; i++)
        {
            shapeInput.setEndianness(Endianness.BIG_ENDIAN);
            int shapeNumber = shapeInput.readInt();
            int contentLength = shapeInput.readInt();
            shapeInput.setEndianness(Endianness.LITTLE_ENDIAN);
            int type = shapeInput.readInt();
            if (type != 0 && type != 1 && type != 11 && type != 21)
            {
                double[] min = this.coordinateTransform.doubleTransform(shapeInput.readDouble(), shapeInput.readDouble());
                double[] max = this.coordinateTransform.doubleTransform(shapeInput.readDouble(), shapeInput.readDouble());
                double minX = Math.min(min[0], max[0]);
                double minY = Math.min(min[1], max[1]);
                double width = Math.max(min[0], max[0]) - minX;
                double height = Math.max(min[1], max[1]) - minY;
                SerializableRectangle2D bounds = new SerializableRectangle2D.Double(minX, minY, width, height);
                if (Shape.overlaps(extent.toRectangle2D(), bounds))
                {
                    results.add(
                            new GisObject(this.readShape(shapeInput, shapeNumber, contentLength, type, false), attributes[i]));
                }
                else
                {
                    shapeInput.skipBytes((2 * contentLength) - 36);
                }
            }
            else if (type != 0)
            {

                Point2D temp = (Point2D) this.readShape(shapeInput, shapeNumber, contentLength, type, false);
                if (extent.toRectangle2D().contains(temp))
                {
                    results.add(new GisObject(temp, attributes[i]));
                }
            }
        }
        shapeInput.close();
        return results;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized List<GisObject> getShapes(final String attribute, final String columnName) throws IOException
    {
        List<GisObject> result = new ArrayList<>();
        int[] shapeNumbers = this.dbfReader.getRowNumbers(attribute, columnName);
        for (int i = 0; i < shapeNumbers.length; i++)
        {
            result.add(this.getShape(i));
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public int getType()
    {
        return this.currentType;
    }

    /**
     * reads a shape.
     * @param input ObjectEndianInputStream; the inputStream
     * @return the shape
     * @throws IOException on IOException
     */
    private Object readShape(final ObjectEndianInputStream input) throws IOException
    {
        return readShape(input, -1, -1, -1, true);
    }

    /**
     * @param input ObjectEndianInputStream; the input stream.
     * @param fixedShapeNumber int; the shape number, if -1, read from input
     * @param fixedContentLength int; the length of the content, if -1, read from input
     * @param fixedType int; shape type; if -1, read from input
     * @param skipBox boolean;
     * @return the shape
     * @throws IOException on I/O error reading from the shape file
     */
    private Object readShape(final ObjectEndianInputStream input, final int fixedShapeNumber, final int fixedContentLength,
            final int fixedType, final boolean skipBox) throws IOException
    {
        input.setEndianness(Endianness.BIG_ENDIAN);
        @SuppressWarnings("unused")
        int shapeNumber = fixedShapeNumber == -1 ? input.readInt() : fixedShapeNumber;

        int contentLength = fixedContentLength == -1 ? input.readInt() : fixedContentLength;

        input.setEndianness(Endianness.LITTLE_ENDIAN);
        int type = fixedType == -1 ? input.readInt() : fixedType;

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
     * reads a nullshape.
     * @param input ObjectEndianInputStream; the inputStream
     * @return a nullobject
     * @throws IOException on IOException
     */
    private synchronized Object readNullShape(final ObjectEndianInputStream input) throws IOException
    {
        if (input != null)
        {
            throw new IOException("readNullShape inputStream is is not null");
        }
        return null;
    }

    /**
     * reads a Point.
     * @param input ObjectEndianInputStream; the inputStream
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPoint(final ObjectEndianInputStream input) throws IOException
    {
        this.currentType = GisMapInterface.POINT;
        input.setEndianness(Endianness.LITTLE_ENDIAN);
        double[] point = this.coordinateTransform.doubleTransform(input.readDouble(), input.readDouble());
        return new Point2D.Double(point[0], point[1]);
    }

    /**
     * reads a PolyLine.
     * @param input ObjectEndianInputStream; the inputStream
     * @param skipBox boolean; whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPolyLine(final ObjectEndianInputStream input, final boolean skipBox) throws IOException
    {
        this.currentType = GisMapInterface.LINE;
        if (skipBox)
        {
            input.skipBytes(32);
        }
        input.setEndianness(Endianness.LITTLE_ENDIAN);
        int numParts = input.readInt();
        int numPoints = input.readInt();
        int[] partBegin = new int[numParts + 1];

        for (int i = 0; i < partBegin.length - 1; i++)
        {
            partBegin[i] = input.readInt();

        }
        partBegin[partBegin.length - 1] = numPoints;

        SerializablePath result = new SerializablePath(Path2D.WIND_NON_ZERO, numPoints);
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
     * reads a Polygon.
     * @param input ObjectEndianInputStream; the inputStream
     * @param skipBox boolean; whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPolygon(final ObjectEndianInputStream input, final boolean skipBox) throws IOException
    {
        this.currentType = GisMapInterface.POLYGON;
        if (skipBox)
        {
            input.skipBytes(32);
        }
        input.setEndianness(Endianness.LITTLE_ENDIAN);
        int numParts = input.readInt();
        int numPoints = input.readInt();
        int[] partBegin = new int[numParts + 1];

        for (int i = 0; i < partBegin.length - 1; i++)
        {
            partBegin[i] = input.readInt();
        }
        partBegin[partBegin.length - 1] = numPoints;

        SerializablePath result = new SerializablePath(Path2D.WIND_NON_ZERO, numPoints);
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
     * reads a readMultiPoint.
     * @param input ObjectEndianInputStream; the inputStream
     * @param skipBox boolean; whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readMultiPoint(final ObjectEndianInputStream input, final boolean skipBox) throws IOException
    {
        this.currentType = GisMapInterface.POINT;
        if (skipBox)
        {
            input.skipBytes(32);
        }
        input.setEndianness(Endianness.LITTLE_ENDIAN);
        Point2D[] result = new Point2D.Double[input.readInt()];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = (Point2D) readPoint(input);
        }

        return result;
    }

    /**
     * reads a readPointZ.
     * @param input ObjectEndianInputStream; the inputStream
     * @param contentLength int; the contentLength
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPointZ(final ObjectEndianInputStream input, final int contentLength) throws IOException
    {
        this.currentType = GisMapInterface.POINT;
        Object point = this.readPoint(input);
        input.skipBytes((contentLength * 2) - 20);

        return point;
    }

    /**
     * reads a readPolyLineZ.
     * @param input ObjectEndianInputStream; the inputStream
     * @param contentLength int; the contentLength
     * @param skipBox boolean; whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPolyLineZ(final ObjectEndianInputStream input, final int contentLength,
            final boolean skipBox) throws IOException
    {
        this.currentType = GisMapInterface.LINE;
        if (skipBox)
        {
            input.skipBytes(32);
        }
        input.setEndianness(Endianness.LITTLE_ENDIAN);
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

        SerializablePath result = new SerializablePath(Path2D.WIND_NON_ZERO, numPoints);
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
     * reads a readPolygonZ.
     * @param input ObjectEndianInputStream; the inputStream
     * @param contentLength int; the contentLength
     * @param skipBox boolean; whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPolygonZ(final ObjectEndianInputStream input, final int contentLength,
            final boolean skipBox) throws IOException
    {
        this.currentType = GisMapInterface.POLYGON;
        if (skipBox)
        {
            input.skipBytes(32);
        }
        input.setEndianness(Endianness.LITTLE_ENDIAN);
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

        SerializablePath result = new SerializablePath(Path2D.WIND_NON_ZERO, numPoints);
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
     * reads a readMultiPointZ.
     * @param input ObjectEndianInputStream; the inputStream
     * @param contentLength int; the contentLength
     * @param skipBox boolean; whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readMultiPointZ(final ObjectEndianInputStream input, final int contentLength,
            final boolean skipBox) throws IOException
    {
        this.currentType = GisMapInterface.POINT;
        if (skipBox)
        {
            input.skipBytes(32);
        }
        input.setEndianness(Endianness.LITTLE_ENDIAN);
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
     * reads a readPointM.
     * @param input ObjectEndianInputStream; the inputStream
     * @param contentLength int; the contentLength
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPointM(final ObjectEndianInputStream input, final int contentLength) throws IOException
    {
        this.currentType = GisMapInterface.POINT;
        Object point = this.readPoint(input);
        input.skipBytes((contentLength * 2) - 20);
        return point;
    }

    /**
     * reads a readPolyLineM.
     * @param input ObjectEndianInputStream; the inputStream
     * @param contentLength int; the contentLength
     * @param skipBox boolean; whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPolyLineM(final ObjectEndianInputStream input, final int contentLength,
            final boolean skipBox) throws IOException
    {
        this.currentType = GisMapInterface.LINE;
        if (skipBox)
        {
            input.skipBytes(32);
        }
        input.setEndianness(Endianness.LITTLE_ENDIAN);
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

        SerializablePath result = new SerializablePath(Path2D.WIND_NON_ZERO, numPoints);
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
     * reads a readPolyLineM.
     * @param input ObjectEndianInputStream; the inputStream
     * @param contentLength int; the contentLength
     * @param skipBox boolean; whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readPolygonM(final ObjectEndianInputStream input, final int contentLength,
            final boolean skipBox) throws IOException
    {
        this.currentType = GisMapInterface.POLYGON;
        if (skipBox)
        {
            input.skipBytes(32);
        }
        input.setEndianness(Endianness.LITTLE_ENDIAN);
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

        SerializablePath result = new SerializablePath(Path2D.WIND_NON_ZERO, numPoints);
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
     * reads a readMultiPointM.
     * @param input ObjectEndianInputStream; the inputStream
     * @param contentLength int; the contentLength
     * @param skipBox boolean; whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readMultiPointM(final ObjectEndianInputStream input, final int contentLength,
            final boolean skipBox) throws IOException
    {
        this.currentType = GisMapInterface.POINT;
        if (skipBox)
        {
            input.skipBytes(32);
        }
        input.setEndianness(Endianness.LITTLE_ENDIAN);
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
     * reads a readMultiPointM.
     * @param input ObjectEndianInputStream; the inputStream
     * @param contentLength int; the contentLength
     * @param skipBox boolean; whether to skip the box
     * @return the java2D PointShape
     * @throws IOException on IOException
     */
    private synchronized Object readMultiPatch(final ObjectEndianInputStream input, final int contentLength,
            final boolean skipBox) throws IOException
    {
        if (input != null || contentLength != 0 || skipBox)
        {
            throw new IOException(
                    "Please inform <a href=\"mailto:support@javel.nl\">support@javel.nl</a> that you need MultiPatch support");
        }
        return null;
    }
}
