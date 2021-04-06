package nl.tudelft.simulation.dsol.animation.gis.map;

import java.awt.Color;
import java.awt.Dimension;
import java.io.Serializable;

/**
 * This interface defines the image as defined in the mapInterface.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface MapImageInterface extends Serializable
{
    /**
     * Getter for property backgroundColor.
     * @return Color the value of property backgroundColor.
     */
    Color getBackgroundColor();

    /**
     * Setter for property backgroundColor.
     * @param backgroundColor Color; New value of property backgroundColor.
     */
    void setBackgroundColor(Color backgroundColor);

    /**
     * Getter for property size.
     * @return Dimension the value of property size.
     */
    Dimension getSize();

    /**
     * Setter for property size.
     * @param size Dimension; New value of property size.
     */
    void setSize(Dimension size);

}
