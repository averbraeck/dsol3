/*
 * AttributeInterface.java
 * 
 * Created on April 17, 2002, 7:23 PM Last edited on October 11, 2002
 */
package nl.javel.gisbeans.map;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

/**
 * This class defines the attribute interface
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.0
 * @version 1.0
 */
public interface AttributeInterface extends Serializable
{
    /**
     * returns the angle of the attribute
     * @param shapeIndex the shapeIndex
     * @return the angle
     */
    public double getAngle(int shapeIndex);

    /**
     * @return the font
     */
    public Font getFont();

    /**
     * @return the color
     */
    public Color getFontColor();

    /**
     * @return the position
     */
    public int getPosition();

    /**
     * @param shapeIndex the shapeIndex
     * @return the value of the attribute
     */
    public String getValue(int shapeIndex);

    /**
     * @param font the font to set
     */
    public void setFont(Font font);

    /**
     * @param fontColor the font color to set
     */
    public void setFontColor(Color fontColor);

    /**
     * @param position the position to set
     */
    public void setPosition(int position);

    /**
     * returns the layer
     * @return the layer of this attribute
     */
    public LayerInterface getLayer();

    /**
     * @return the maxScale
     */
    public double getMaxScale();

    /**
     * @return the minScale
     */
    public double getMinScale();

    /**
     * sets the minScale of the attribute
     * @param minScale the minscale to set
     */
    public void setMinScale(double minScale);

    /**
     * sets the maxScale of the attribute
     * @param maxScale the maxscale to set
     */
    public void setMaxScale(double maxScale);
}
