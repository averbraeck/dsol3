/*
 * SerializableGeneralPath.java
 * 
 * Created on December 8, 2001, 12:57 PM Last edited on October 11, 2002
 */
package nl.javel.gisbeans.geom;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.io.IOException;

/**
 * The SerializableGeneralPath class is a serializable version of the <code>java.awt.geom.GeneralPath</code> class.
 * @author <a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a> <br>
 *         <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.2
 */
public class SerializableGeneralPath implements java.io.Serializable, java.awt.Shape, java.lang.Cloneable
{

    /** the general path. */
    private GeneralPath generalPath;

    /**
     * Construct a default general path that can be serialized.
     */
    public SerializableGeneralPath()
    {
        this.generalPath = new GeneralPath();
    }

    /**
     * constructs a new SerializableGeneralPath.
     * @param rule the windingRule
     */
    public SerializableGeneralPath(final int rule)
    {
        this.generalPath = new GeneralPath(rule);
    }

    /**
     * constructs a new SerializableGeneralPath.
     * @param rule the windingRule
     * @param initialCapacity the initialCapacity
     */
    public SerializableGeneralPath(final int rule, final int initialCapacity)
    {
        this.generalPath = new GeneralPath(rule, initialCapacity);
    }

    /**
     * constructs a new SerializableGeneralPath.
     * @param s the shape
     */
    public SerializableGeneralPath(final Shape s)
    {
        this.generalPath = new GeneralPath(s);
    }

    /**
     * @param pi the pathIterator
     * @param connect the connect
     */
    public void append(final PathIterator pi, final boolean connect)
    {
        this.generalPath.append(pi, connect);
    }

    /**
     * @param s the shape
     * @param connect whether to connect
     */
    public void append(final Shape s, final boolean connect)
    {
        this.generalPath.append(s, connect);
    }

    /** {@inheritDoc} */
    @Override
    public Object clone()
    {
        SerializableGeneralPath clone = new SerializableGeneralPath();
        clone.generalPath = (GeneralPath) this.generalPath.clone();
        return clone;
    }

    /**
     * 
     */
    public void closePath()
    {
        this.generalPath.closePath();
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final double param, final double param1, final double param2, final double param3)
    {
        return this.generalPath.contains(param, param1, param2, param3);
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final double param, final double param1)
    {
        return this.generalPath.contains(param, param1);
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final java.awt.geom.Rectangle2D rectangle2D)
    {
        return this.generalPath.contains(rectangle2D);
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final java.awt.geom.Point2D point2D)
    {
        return this.generalPath.contains(point2D);
    }

    /**
     * @param at the effinetransform
     * @return the new shape
     */
    public java.awt.Shape createTranformedShape(final java.awt.geom.AffineTransform at)
    {
        return this.generalPath.createTransformedShape(at);
    }

    /**
     * curves to.
     * @param x1 x1
     * @param y1 y1
     * @param x2 x2
     * @param y2 y2
     * @param x3 x3
     * @param y3 y3
     */
    public void curveTo(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3)
    {
        this.generalPath.curveTo(x1, y1, x2, y2, x3, y3);
    }

    /** {@inheritDoc} */
    @Override
    public java.awt.Rectangle getBounds()
    {
        return this.generalPath.getBounds();
    }

    /** {@inheritDoc} */
    @Override
    public java.awt.geom.Rectangle2D getBounds2D()
    {
        return this.generalPath.getBounds2D();
    }

    /**
     * @return the current point in the generalPath
     */
    public java.awt.geom.Point2D getCurrentPoint()
    {
        return this.generalPath.getCurrentPoint();
    }

    /** {@inheritDoc} */
    @Override
    public java.awt.geom.PathIterator getPathIterator(final java.awt.geom.AffineTransform affineTransform)
    {
        return this.generalPath.getPathIterator(affineTransform);
    }

    /** {@inheritDoc} */
    @Override
    public java.awt.geom.PathIterator getPathIterator(final java.awt.geom.AffineTransform affineTransform,
            final double param)
    {
        return this.generalPath.getPathIterator(affineTransform, param);
    }

    /**
     * @return the winding rule
     */
    public int getWindingRule()
    {
        return this.generalPath.getWindingRule();
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(final double param, final double param1, final double param2, final double param3)
    {
        return this.generalPath.intersects(param, param1, param2, param3);
    }

    /** {@inheritDoc} */
    @Override
    public boolean intersects(final java.awt.geom.Rectangle2D rectangle2D)
    {
        return this.generalPath.intersects(rectangle2D);
    }

    /**
     * @param x x
     * @param y y
     */
    public void lineTo(final float x, final float y)
    {
        this.generalPath.lineTo(x, y);
    }

    /**
     * @param x x
     * @param y y
     */
    public void moveTo(final float x, final float y)
    {
        this.generalPath.moveTo(x, y);
    }

    /**
     * @param x1 x1
     * @param y1 y1
     * @param x2 x2
     * @param y2 y2
     */
    public void quadTo(final float x1, final float y1, final float x2, final float y2)
    {
        this.generalPath.quadTo(x1, y1, x2, y2);
    }

    /**
     * 
     */
    public void reset()
    {
        this.generalPath.reset();
    }

    /**
     * @param rule rule
     */
    public void setWindingRule(final int rule)
    {
        this.generalPath.setWindingRule(rule);
    }

    /**
     * @param at affinity transform
     */
    public void transform(final java.awt.geom.AffineTransform at)
    {
        this.generalPath.transform(at);
    }

    /**
     * writes a float array
     * @param out the output stream
     * @param array the array
     * @param length the length
     * @throws java.io.IOException on exception
     */
    private void writeFloatArray(final java.io.ObjectOutputStream out, final float[] array, final int length)
            throws java.io.IOException
    {
        for (int i = 0; i < length; i++)
        {
            out.writeFloat(array[i]);
        }
    }

    /**
     * writes to the stream
     * @param out the stream
     * @throws IOException on IOfailure
     */
    private void writeObject(final java.io.ObjectOutputStream out) throws IOException
    {
        out.writeInt(this.generalPath.getWindingRule());
        float[] coords = new float[6];
        PathIterator i = this.generalPath.getPathIterator(null);
        // Now the Path iterator is present, we simply walk along the shape and
        // serialize the points..
        while (!i.isDone())
        {
            int segment = i.currentSegment(coords);
            out.writeInt(segment);
            switch (segment)
            {
                case PathIterator.SEG_CLOSE:
                    writeFloatArray(out, coords, 0);
                    // no float is serialized.. Keeps the bytestream as
                    // minimal as possible
                    break;
                case PathIterator.SEG_CUBICTO:
                    writeFloatArray(out, coords, 6);
                    // All 6 floats are used and therfore serialized.
                    break;
                case PathIterator.SEG_LINETO:
                    writeFloatArray(out, coords, 2);
                    // 2 floats are used and serialized.. Keeps the
                    // bytestream as minimal as possible
                    break;
                case PathIterator.SEG_MOVETO:
                    writeFloatArray(out, coords, 2);
                    // 2 floats are used and serialized.. Keeps the
                    // bytestream as minimal as possible
                    break;
                case PathIterator.SEG_QUADTO:
                    writeFloatArray(out, coords, 4);
                    // 2 floats are used and serialized.. Keeps the
                    // bytestream as minimal as possible
                    break;
                default:
                    throw new RuntimeException("unkown segment");
            }
            i.next();
        }
        out.writeInt(-1); // We are ready and give an end-signal
    }

    /**
     * reads an object
     * @param in the inputstream
     * @throws IOException on IOException
     */
    private void readObject(final java.io.ObjectInputStream in) throws IOException
    {
        this.generalPath = new GeneralPath(in.readInt());
        int segment;
        while ((segment = in.readInt()) != -1)
        // The -1 value was our ending point..
        {
            switch (segment)
            {
                case PathIterator.SEG_CLOSE:
                    this.generalPath.closePath();
                    break;
                case PathIterator.SEG_CUBICTO:
                    this.generalPath.curveTo(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat(),
                            in.readFloat(), in.readFloat());
                    break;
                case PathIterator.SEG_LINETO:
                    this.generalPath.lineTo(in.readFloat(), in.readFloat());
                    break;
                case PathIterator.SEG_MOVETO:
                    this.generalPath.moveTo(in.readFloat(), in.readFloat());
                    break;
                case PathIterator.SEG_QUADTO:
                    this.generalPath.quadTo(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
                    break;
                default:
                    throw new RuntimeException("unkown segment");
            }
        }
    }
}
