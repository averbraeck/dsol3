/*
 * Image.java
 * 
 * Created on April 17, 2002, 7:23 PM Last edited on October 11, 2002
 */
package nl.javel.gisbeans.map;

import java.awt.Color;
import java.awt.Dimension;

/**
 * This class defines the image as defined in the map
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.0
 * @version 1.0
 */
public class Image implements ImageInterface
{

    private java.awt.Color backgroundColor = new java.awt.Color(255, 255, 255, 255);

    private LegendInterface legend;

    private ScalebarInterface scalebar;

    private java.awt.Dimension size = new java.awt.Dimension(500, 500);

    /**
     * constructs a new Image.
     */
    public Image()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public java.awt.Color getBackgroundColor()
    {
        return this.backgroundColor;
    }

    /** {@inheritDoc} */
    @Override
    public LegendInterface getLegend()
    {
        return this.legend;
    }

    /** {@inheritDoc} */
    @Override
    public ScalebarInterface getScalebar()
    {
        return this.scalebar;
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getSize()
    {
        return this.size;
    }

    /** {@inheritDoc} */
    @Override
    public void setBackgroundColor(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    /** {@inheritDoc} */
    @Override
    public void setLegend(LegendInterface legend)
    {
        this.legend = legend;
    }

    /** {@inheritDoc} */
    @Override
    public void setScalebar(ScalebarInterface scalebar)
    {
        this.scalebar = scalebar;
    }

    /** {@inheritDoc} */
    @Override
    public void setSize(Dimension size)
    {
        this.size = size;
    }
}
