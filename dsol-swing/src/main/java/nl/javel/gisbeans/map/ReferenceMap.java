/*
 * ReferenceMap.java
 * 
 * Created on April 17, 2002, 7:23 PM Last edited on October 12, 2002
 */
package nl.javel.gisbeans.map;

import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;

/**
 * This class implements the ReferenceMap
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.0
 * @version 1.0
 */
public class ReferenceMap implements ReferenceMapInterface
{
    private URL image;

    private double[] extent;

    private Color outLineColor = new Color(255, 255, 255, 255);

    private Dimension size = new Dimension(200, 200);

    private boolean status = false;

    /**
     * constructs a new ReferenceMap
     */
    public ReferenceMap()
    {
        super();
    }

    /**
     * @see ReferenceMapInterface#getImage()
     */
    public URL getImage()
    {
        return this.image;
    }

    /**
     * @see ReferenceMapInterface#setImage(URL image)
     */
    public void setImage(URL image)
    {
        this.image = image;
    }

    /**
     * @see ReferenceMapInterface#getExtent()
     */
    public double[] getExtent()
    {
        return this.extent;
    }

    /**
     * @see ReferenceMapInterface#setExtent(double[] extent)
     */
    public void setExtent(double[] extent)
    {
        this.extent = extent;
    }

    /**
     * @see ReferenceMapInterface#getOutlineColor()
     */
    public Color getOutlineColor()
    {
        return this.outLineColor;
    }

    /**
     * @see ReferenceMapInterface#setOutlineColor(Color outlineColor)
     */
    public void setOutlineColor(Color outlineColor)
    {
        this.outLineColor = outlineColor;
    }

    /**
     * @see ReferenceMapInterface#getSize()
     */
    public Dimension getSize()
    {
        return this.size;
    }

    /**
     * @see ReferenceMapInterface#setSize(Dimension size)
     */
    public void setSize(Dimension size)
    {
        this.size = size;
    }

    /**
     * @see ReferenceMapInterface#isStatus()
     */
    public boolean isStatus()
    {
        return this.status;
    }

    /**
     * @see ReferenceMapInterface#setStatus(boolean status)
     */
    public void setStatus(boolean status)
    {
        this.status = status;
    }
}