package nl.tudelft.simulation.dsol.swing.animation.D3;

import java.io.Serializable;

/**
 * Renderable3DInterface, an interface for 3d renderables.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="mailto:royc@tbm.tudelft.nl">Roy Chin </a>
 */
public interface Renderable3DInterface extends Serializable 
{
    /**
     * Static (non dynamic) objects that describe the world or landscape, possibly containing other static or animated objects.
     */
    int STATIC_OBJECT = 0;

    /** Simulated objects are objects that move, rotate or change shape. */
    int DYNAMIC_OBJECT = 1;

    /**
     * Update the representation of the model
     */
    void update();

    /**
     * Get the type.
     * @return Type of renderable
     */
    int getType();
}
