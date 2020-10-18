package nl.tudelft.simulation.dsol.animation.gis.map;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

import nl.tudelft.simulation.dsol.animation.gis.ScreenPosition;

/**
 * This class defines the attributes for a map layer.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface AttributeInterface extends Serializable
{
    /**
     * returns the angle of the attribute.
     * @param shapeIndex int; the shapeIndex
     * @return the angle
     */
    double getAngle(int shapeIndex);

    /**
     * @return the font
     */
    Font getFont();

    /**
     * @return the color
     */
    Color getFontColor();

    /**
     * @return the position
     */
    ScreenPosition getPosition();

    /**
     * @param shapeIndex int; the shapeIndex
     * @return the value of the attribute
     */
    String getValue(int shapeIndex);

    /**
     * @param font Font; the font to set
     */
    void setFont(Font font);

    /**
     * @param fontColor Color; the font color to set
     */
    void setFontColor(Color fontColor);

    /**
     * @param position int; the position to set
     */
    void setPosition(ScreenPosition position);

    /**
     * returns the layer.
     * @return the layer of this attribute
     */
    LayerInterface getLayer();

    /**
     * @return the maxScale
     */
    double getMaxScale();

    /**
     * @return the minScale
     */
    double getMinScale();

    /**
     * sets the minScale of the attribute.
     * @param minScale double; the minscale to set
     */
    void setMinScale(double minScale);

    /**
     * sets the maxScale of the attribute.
     * @param maxScale double; the maxscale to set
     */
    void setMaxScale(double maxScale);
}
