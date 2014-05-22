/*
 * Legend.java
 * 
 * Created on April 17, 2002, 7:23 PM Last edited on October 12, 2002
 */
package nl.javel.gisbeans.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * This class implements the LegendInterface
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.0
 * @version 1.0
 */
public class Legend implements LegendInterface
{
    private Color backgroundColor = new Color(255, 255, 255, 255);

    private boolean embed = false;

    private Color outlineColor = new Color(0, 0, 0, 255);

    private Color fontColor = new Color(0, 0, 0, 255);

    private int position = MapInterface.UC;

    private Dimension size = new Dimension(200, 100);

    private boolean status = false;

    private Font font = new Font("arial", Font.TRUETYPE_FONT, 10);

    /**
     * @see LegendInterface#getBackgroundColor()
     */
    public Color getBackgroundColor()
    {
        return this.backgroundColor;
    }

    /**
     * @see LegendInterface#setBackgroundColor(Color)
     */
    public void setBackgroundColor(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @see LegendInterface#getOutlineColor()
     */
    public Color getOutlineColor()
    {
        return this.outlineColor;
    }

    /**
     * @see LegendInterface#setOutlineColor(Color)
     */
    public void setOutlineColor(Color outlineColor)
    {
        this.outlineColor = outlineColor;
    }

    /**
     * @see LegendInterface#getFontColor()
     */
    public Color getFontColor()
    {
        return this.fontColor;
    }

    /**
     * @see LegendInterface#setFontColor(Color fontColor)
     */
    public void setFontColor(Color fontColor)
    {
        this.fontColor = fontColor;
    }

    /**
     * @see LegendInterface#setFont(Font font)
     */
    public void setFont(Font font)
    {
        this.font = font;
    }

    /**
     * @see LegendInterface#getFont()
     */
    public Font getFont()
    {
        return this.font;
    }

    /**
     * @see LegendInterface#isEmbed()
     */
    public boolean isEmbed()
    {
        return this.embed;
    }

    /**
     * @see LegendInterface#setEmbed(boolean)
     */
    public void setEmbed(boolean embed)
    {
        this.embed = embed;
    }

    /**
     * @see LegendInterface#getPosition()
     */
    public int getPosition()
    {
        return this.position;
    }

    /**
     * @see LegendInterface#setPosition(int)
     */
    public void setPosition(int position)
    {
        this.position = position;
    }

    /**
     * @see LegendInterface#getSize()
     */
    public Dimension getSize()
    {
        return this.size;
    }

    /**
     * @see LegendInterface#setSize(Dimension)
     */
    public void setSize(Dimension size)
    {
        this.size = size;
    }

    /**
     * @see LegendInterface#isStatus()
     */
    public boolean isStatus()
    {
        return this.status;
    }

    /**
     * @see LegendInterface#setStatus(boolean)
     */
    public void setStatus(boolean status)
    {
        this.status = status;
    }
}