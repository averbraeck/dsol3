/*
 * ScalebarInterface.java
 * 
 * Created on April 17, 2002, 7:23 PM Last edited on October 12, 2002
 */
package nl.javel.gisbeans.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * This interface defines the image as defined in the mapInterface
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.0
 * @version 1.0
 */
public interface ScalebarInterface extends java.io.Serializable
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
     * Getter for property color.
     * @return Color the value of property color.
     */
    public Color getColor();

    /**
     * Setter for property color.
     * @param color New value of property color.
     */
    public void setColor(Color color);

    /**
     * Getter for property font.
     * @return Font the value of property font.
     */
    public Font getFont();

    /**
     * Setter for property font.
     * @param font New value of the font.
     */
    public void setFont(Font font);

    /**
     * Getter for property fontColor.
     * @return Color the value of property fontColor.
     */
    public Color getFontColor();

    /**
     * Setter for property fontColor.
     * @param color New value of property fontColor.
     */
    public void setFontColor(Color color);

    /**
     * Getter for property embed.
     * @return boolean the value of property embed.
     */
    public boolean isEmbed();

    /**
     * Setter for property embed.
     * @param embed New value of property embed.
     */
    public void setEmbed(boolean embed);

    /**
     * Getter for property intervals.
     * @return int the value of property intervals.
     */
    public int getIntervals();

    /**
     * Setter for property intervals.
     * @param intervals New value of property intervals.
     */
    public void setIntervals(int intervals);

    /**
     * Getter for property position.
     * @return int the value of property position.
     */
    public int getPosition();

    /**
     * Setter for property position.
     * @param position New value of property position.
     */
    public void setPosition(int position);

    /**
     * Getter for property size.
     * @return Dimension the value of property size.
     */
    public Dimension getSize();

    /**
     * Setter for property size.
     * @param size New value of property size.
     */
    public void setSize(Dimension size);

    /**
     * Getter for property units.
     * @return int the value of property units.
     */
    public int getUnits();

    /**
     * Setter for property units.
     * @param units New value of property units.
     */
    public void setUnits(int units);

    /**
     * Getter for property status.
     * @return boolean the value of property status.
     */
    public boolean isStatus();

    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(boolean status);
}
