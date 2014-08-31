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

    /** {@inheritDoc} */
    public Color getBackgroundColor()
    {
        return this.backgroundColor;
    }

    /** {@inheritDoc} */
    public void setBackgroundColor(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    /** {@inheritDoc} */
    public Color getOutlineColor()
    {
        return this.outlineColor;
    }

    /** {@inheritDoc} */
    public void setOutlineColor(Color outlineColor)
    {
        this.outlineColor = outlineColor;
    }

    /** {@inheritDoc} */
    public Color getFontColor()
    {
        return this.fontColor;
    }

    /** {@inheritDoc} */
    public void setFontColor(Color fontColor)
    {
        this.fontColor = fontColor;
    }

    /** {@inheritDoc} */
    public void setFont(Font font)
    {
        this.font = font;
    }

    /** {@inheritDoc} */
    public Font getFont()
    {
        return this.font;
    }

    /** {@inheritDoc} */
    public boolean isEmbed()
    {
        return this.embed;
    }

    /** {@inheritDoc} */
    public void setEmbed(boolean embed)
    {
        this.embed = embed;
    }

    /** {@inheritDoc} */
    public int getPosition()
    {
        return this.position;
    }

    /** {@inheritDoc} */
    public void setPosition(int position)
    {
        this.position = position;
    }

    /** {@inheritDoc} */
    public Dimension getSize()
    {
        return this.size;
    }

    /** {@inheritDoc} */
    public void setSize(Dimension size)
    {
        this.size = size;
    }

    /** {@inheritDoc} */
    public boolean isStatus()
    {
        return this.status;
    }

    /** {@inheritDoc} */
    public void setStatus(boolean status)
    {
        this.status = status;
    }
}
