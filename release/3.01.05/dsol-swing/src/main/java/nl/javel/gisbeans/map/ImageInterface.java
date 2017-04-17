/*
 * Image.java
 * 
 * Created on April 17, 2002, 7:23 PM Last edited on October 11, 2002
 */
package nl.javel.gisbeans.map;

import java.awt.Color;
import java.awt.Dimension;

/**
 * This interface defines the image as defined in the mapInterface
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.0
 * @version 1.0
 */
public interface ImageInterface extends java.io.Serializable
{
    /**
     * Getter for property backgroundColor.
     * @return Color the value of property backgroundColor.
     */
    public Color getBackgroundColor();

    /**
     * Setter for property backgroundColor.
     * @param backgroundColor New value of property backgroundColor.
     */
    public void setBackgroundColor(Color backgroundColor);

    /**
     * Getter for property size.
     * @return Dimension the value of property size.
     */
    public java.awt.Dimension getSize();

    /**
     * Setter for property size.
     * @param size New value of property size.
     */
    public void setSize(Dimension size);

    /**
     * Getter for property legend.
     * @return LegendInterface the value of property legend.
     */
    public LegendInterface getLegend();

    /**
     * Setter for property legend.
     * @param legend New value of property legend.
     */
    public void setLegend(LegendInterface legend);

    /**
     * Getter for property scalebar.
     * @return SclaebarInterface the value of property scalebar.
     */
    public ScalebarInterface getScalebar();

    /**
     * Setter for property scalebar.
     * @param scalebar New value of property scalebar.
     */
    public void setScalebar(ScalebarInterface scalebar);
}
