/*
 * SerializableGeneralPath.java
 * 
 * Created on December 8, 2001, 12:57 PM Last edited on October 11, 2002
 */
package nl.javel.gisbeans.geom;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * The SerializableRectangle2D class is a serializable version of the <code>java.awt.geom.Rectangle2D</code> class.
 * @author <a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a> <br>
 *         <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.2
 * @version 1.0
 */
public abstract class SerializableRectangle2D extends java.awt.geom.Rectangle2D implements java.io.Serializable,
        java.awt.Shape, java.lang.Cloneable
{
    /**
     * constructs a new nl.javel.gisbeans.geom.SerializableRectangle2D
     */
    protected SerializableRectangle2D()
    {
        super();
    }

    /**
     * The SerializableRectangle2D.Double class is a serializable version of the
     * <code>java.awt.geom.Rectangle2D.Double</code> class.
     * @author <a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a> <br>
     *         <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
     * @since JDK 1.2
     * @version 1.0
     */
    public static class Double extends SerializableRectangle2D
    {

        /** the rectangle */
        private Rectangle2D rectangle;

        /**
         * constructs a new nl.javel.gisbeans.geom.SerializableRectangle2D.Double
         */
        public Double()
        {
            this.rectangle = new Rectangle2D.Double();
        }

        /**
         * constructs a new Double
         * @param x
         * @param y
         * @param w
         * @param h
         */
        public Double(final double x, final double y, final double w, final double h)
        {
            this.rectangle = new Rectangle2D.Double(x, y, w, h);
        }

        /**
         * @see java.awt.geom.Rectangle2D#createIntersection(java.awt.geom.Rectangle2D)
         */
        public Rectangle2D createIntersection(final Rectangle2D r)
        {
            return this.rectangle.createIntersection(r);
        }

        /**
         * @see java.awt.geom.Rectangle2D#createUnion(java.awt.geom.Rectangle2D)
         */
        public Rectangle2D createUnion(final Rectangle2D r)
        {
            return this.rectangle.createUnion(r);
        }

        /**
         * @see java.awt.Shape#getBounds2D()
         */
        public Rectangle2D getBounds2D()
        {
            return this.rectangle.getBounds2D();
        }

        /**
         * @see java.awt.geom.RectangularShape#getHeight()
         */
        public double getHeight()
        {
            return this.rectangle.getHeight();
        }

        /**
         * @see java.awt.geom.RectangularShape#getWidth()
         */
        public double getWidth()
        {
            return this.rectangle.getWidth();
        }

        /**
         * @see java.awt.geom.RectangularShape#getX()
         */
        public double getX()
        {
            return this.rectangle.getX();
        }

        /**
         * @see java.awt.geom.RectangularShape#getY()
         */
        public double getY()
        {
            return this.rectangle.getY();
        }

        /**
         * @see java.awt.geom.RectangularShape#isEmpty()
         */
        public boolean isEmpty()
        {
            return this.rectangle.isEmpty();
        }

        /**
         * @see java.awt.geom.Rectangle2D#outcode(double, double)
         */
        public int outcode(final double x, final double y)
        {
            return this.rectangle.outcode(x, y);
        }

        /**
         * @see java.awt.geom.Rectangle2D#setRect(double, double, double, double)
         */
        public void setRect(final double x, final double y, final double w, final double h)
        {
            this.rectangle.setRect(x, y, w, h);
        }

        /**
         * @see java.awt.geom.Rectangle2D#setRect(java.awt.geom.Rectangle2D)
         */
        public void setRect(final Rectangle2D r)
        {
            this.rectangle.setRect(r);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            return this.rectangle.toString();
        }

        /**
         * Now the private serialization methods
         * @param out the outputstream
         * @throws java.io.IOException on exception
         */
        private void writeObject(final java.io.ObjectOutputStream out) throws java.io.IOException
        {
            out.writeDouble(this.rectangle.getX());
            out.writeDouble(this.rectangle.getY());
            out.writeDouble(this.rectangle.getWidth());
            out.writeDouble(this.rectangle.getHeight());
        }

        /**
         * we read the stream
         * @param in the input
         * @throws java.io.IOException on exception
         */
        private void readObject(final java.io.ObjectInputStream in) throws java.io.IOException
        {
            this.rectangle = new Rectangle2D.Double(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble());
        }
    }

    /**
     * The SerializableRectangle2D.Float class is a serializable version of the
     * <code>java.awt.geom.Rectangle2D.Double</code> class.
     * @author <a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a> <br>
     *         <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
     * @since JDK 1.2
     * @version 1.0
     */
    public static class Float extends SerializableRectangle2D
    {

        /** the rectangle */
        private Rectangle2D rectangle;

        /**
         * constructs a new nl.javel.gisbeans.geom.SerializableRectangle2D.Float
         */
        public Float()
        {
            this.rectangle = new Rectangle2D.Float();
        }

        /**
         * constructs a new nl.javel.gisbeans.geom.SerializableRectangle2D.Float
         * @param x the x
         * @param y the y
         * @param w the width
         * @param h the height
         */
        public Float(final float x, final float y, final float w, final float h)
        {
            this.rectangle = new Rectangle2D.Float(x, y, w, h);
        }

        /**
         * @see java.awt.geom.Rectangle2D#createIntersection(java.awt.geom.Rectangle2D)
         */
        public Rectangle2D createIntersection(final Rectangle2D r)
        {
            return this.rectangle.createIntersection(r);
        }

        /**
         * @see java.awt.geom.Rectangle2D#createUnion(java.awt.geom.Rectangle2D)
         */
        public Rectangle2D createUnion(final Rectangle2D r)
        {
            return this.rectangle.createUnion(r);
        }

        /**
         * @see java.awt.Shape#getBounds2D()
         */
        public Rectangle2D getBounds2D()
        {
            return this.rectangle.getBounds2D();
        }

        /**
         * @see java.awt.geom.RectangularShape#getHeight()
         */
        public double getHeight()
        {
            return this.rectangle.getHeight();
        }

        /**
         * @see java.awt.geom.RectangularShape#getWidth()
         */
        public double getWidth()
        {
            return this.rectangle.getWidth();
        }

        /**
         * @see java.awt.geom.RectangularShape#getX()
         */
        public double getX()
        {
            return this.rectangle.getX();
        }

        /**
         * @see java.awt.geom.RectangularShape#getY()
         */
        public double getY()
        {
            return this.rectangle.getY();
        }

        /**
         * @see java.awt.geom.RectangularShape#isEmpty()
         */
        public boolean isEmpty()
        {
            return this.rectangle.isEmpty();
        }

        /**
         * @see java.awt.geom.Rectangle2D#outcode(double, double)
         */
        public int outcode(final double x, final double y)
        {
            return this.rectangle.outcode(x, y);
        }

        /**
         * @param x
         * @param y
         * @param w
         * @param h
         */
        public void setRect(final float x, final float y, final float w, final float h)
        {
            this.rectangle.setRect(x, y, w, h);
        }

        /**
         * @see java.awt.geom.Rectangle2D#setRect(double, double, double, double)
         */
        public void setRect(final double x, final double y, final double w, final double h)
        {
            this.rectangle.setRect(x, y, w, h);
        }

        /**
         * @see java.awt.geom.Rectangle2D#setRect(java.awt.geom.Rectangle2D)
         */
        public void setRect(final Rectangle2D r)
        {
            this.rectangle.setRect(r);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            return this.rectangle.toString();
        }

        /**
         * writes to the stream
         * @param out the stream
         * @throws IOException on IOException
         */
        private void writeObject(final java.io.ObjectOutputStream out) throws IOException
        {
            out.writeDouble(this.rectangle.getX());
            out.writeDouble(this.rectangle.getY());
            out.writeDouble(this.rectangle.getWidth());
            out.writeDouble(this.rectangle.getHeight());
        }

        /**
         * Now the private serialization methods
         * @param in the stream
         * @throws IOException on IOException
         */
        private void readObject(final java.io.ObjectInputStream in) throws IOException
        {
            this.rectangle = new Rectangle2D.Float();
            this.rectangle.setRect(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble());
        }
    }
}