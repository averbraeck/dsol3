/*
 * LegendInterface.java
 * 
 * Created on April 17, 2002, 7:23 PM Last edited on October 12, 2002
 */
package nl.javel.gisbeans.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.Serializable;

/**
 * This interface defines the image as defined in the mapInterface
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.0
 */
public interface LegendInterface extends Serializable
{
    /**
     * Getter for property backgroundColor.
     * @return Color the value of property backgroundColor.
     */
    public Color getBackgroundColor();

    /**
     * Setter for property backgroundColor.
     * @param backgroundColor Color; New value of property backgroundColor.
     */
    public void setBackgroundColor(Color backgroundColor);

    /**
     * Getter for property outlineColor.
     * @return Color the value of property outlineColor.
     */
    public Color getOutlineColor();

    /**
     * Setter for property outlineColor.
     * @param outlineColor Color; New value of property outlineColor.
     */
    public void setOutlineColor(Color outlineColor);

    /**
     * Getter for property outlineColor.
     * @return Color the value of property outlineColor.
     */
    public Color getFontColor();

    /**
     * Setter for property outlineColor.
     * @param fontColor Color; New value of property fontColor.
     */
    public void setFontColor(Color fontColor);

    /**
     * Getter for property embed.
     * @return boolean the value of property embed.
     */
    public boolean isEmbed();

    /**
     * Setter for property embed.
     * @param embed boolean; New value of property embed.
     */
    public void setEmbed(boolean embed);

    /**
     * Getter for property position.
     * @return int the value of property position.
     */
    public int getPosition();

    /**
     * Setter for property position.
     * @param position int; New value of property position.
     */
    public void setPosition(int position);

    /**
     * Setter for property font
     * @param font Font; the font for the legendInterface
     */
    public void setFont(Font font);

    /**
     * Getter for the property font
     * @return Font the font for the legend
     */
    public Font getFont();

    /**
     * Getter for property size.
     * @return Dimension the value of property size.
     */
    public Dimension getSize();

    /**
     * Setter for property size.
     * @param size Dimension; New value of property size.
     */
    public void setSize(Dimension size);

    /**
     * Getter for property statuss.
     * @return boolean the value of property status.
     */
    public boolean isStatus();

    /**
     * Setter for property status.
     * @param status boolean; New value of property status.
     */
    public void setStatus(boolean status);
}
