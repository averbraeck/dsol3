/*
 * Scalebar.java
 * 
 * Created on April 17, 2002, 7:23 PM Last edited on October 12, 2002
 */
package nl.javel.gisbeans.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * This class implements the ScalebarInterface
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.0
 * @version 1.0
 */
public class Scalebar implements ScalebarInterface
{
    private Color backgroundColor = new Color(255, 255, 255, 255);

    private Color color = new Color(0, 0, 0, 255);

    private boolean embed = false;

    private Font font = new Font("arial", Font.TRUETYPE_FONT, 10);

    private Color fontColor = new Color(0, 0, 0, 255);

    private int intervals = 4;

    private int position = MapInterface.UC;

    private Dimension size = new java.awt.Dimension(100, 100);

    private boolean status = false;

    private int units = MapInterface.METERS;

    /**
     * contructs a new instance of ScaleBar
     */
    public Scalebar()
    {
        super();
    }

    /** {@inheritDoc} */
    public java.awt.Color getBackgroundColor()
    {
        return this.backgroundColor;
    }

    /** {@inheritDoc} */
    public java.awt.Color getColor()
    {
        return this.color;
    }

    /** {@inheritDoc} */
    public int getIntervals()
    {
        return this.intervals;
    }

    /** {@inheritDoc} */
    public int getPosition()
    {
        return this.position;
    }

    /** {@inheritDoc} */
    public Dimension getSize()
    {
        return this.size;
    }

    /** {@inheritDoc} */
    public int getUnits()
    {
        return this.units;
    }

    /** {@inheritDoc} */
    public boolean isEmbed()
    {
        return this.embed;
    }

    /** {@inheritDoc} */
    public boolean isStatus()
    {
        return this.status;
    }

    /** {@inheritDoc} */
    public void setBackgroundColor(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    /** {@inheritDoc} */
    public void setColor(Color color)
    {
        this.color = color;
    }

    /** {@inheritDoc} */
    public void setEmbed(boolean embed)
    {
        this.embed = embed;
    }

    /** {@inheritDoc} */
    public void setIntervals(int intervals)
    {
        this.intervals = intervals;
    }

    /** {@inheritDoc} */
    public void setPosition(int position)
    {
        this.position = position;
    }

    /** {@inheritDoc} */
    public void setSize(Dimension size)
    {
        this.size = size;
    }

    /** {@inheritDoc} */
    public void setStatus(boolean status)
    {
        this.status = status;
    }

    /** {@inheritDoc} */
    public void setUnits(int units)
    {
        this.units = units;
    }

    /**
     * Returns the font.
     * @return Font
     */
    public Font getFont()
    {
        return this.font;
    }

    /**
     * Returns the fontColor.
     * @return Color
     */
    public Color getFontColor()
    {
        return this.fontColor;
    }

    /**
     * Sets the font.
     * @param font The font to set
     */
    public void setFont(Font font)
    {
        this.font = font;
    }

    /**
     * Sets the fontColor.
     * @param fontColor The fontColor to set
     */
    public void setFontColor(Color fontColor)
    {
        this.fontColor = fontColor;
    }

}
