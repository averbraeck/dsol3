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
     * constructs a new ReferenceMap.
     */
    public ReferenceMap()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public URL getImage()
    {
        return this.image;
    }

    /** {@inheritDoc} */
    @Override
    public void setImage(URL image)
    {
        this.image = image;
    }

    /** {@inheritDoc} */
    @Override
    public double[] getExtent()
    {
        return this.extent;
    }

    /** {@inheritDoc} */
    @Override
    public void setExtent(double[] extent)
    {
        this.extent = extent;
    }

    /** {@inheritDoc} */
    @Override
    public Color getOutlineColor()
    {
        return this.outLineColor;
    }

    /** {@inheritDoc} */
    @Override
    public void setOutlineColor(Color outlineColor)
    {
        this.outLineColor = outlineColor;
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getSize()
    {
        return this.size;
    }

    /** {@inheritDoc} */
    @Override
    public void setSize(Dimension size)
    {
        this.size = size;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isStatus()
    {
        return this.status;
    }

    /** {@inheritDoc} */
    @Override
    public void setStatus(boolean status)
    {
        this.status = status;
    }
}
