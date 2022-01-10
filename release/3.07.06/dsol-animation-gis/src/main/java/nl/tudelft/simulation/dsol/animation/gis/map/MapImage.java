package nl.tudelft.simulation.dsol.animation.gis.map;

import java.awt.Color;
import java.awt.Dimension;

/**
 * This class defines the image as defined in the map.
 * <p>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class MapImage implements MapImageInterface
{
    /** */
    private static final long serialVersionUID = 20201015L;

    /** background color. */
    private Color backgroundColor = new Color(255, 255, 255, 255);

    /** image size. */
    private Dimension size = new Dimension(500, 500);

    /**
     * constructs a new Image.
     */
    public MapImage()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public Color getBackgroundColor()
    {
        return this.backgroundColor;
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getSize()
    {
        return this.size;
    }

    /** {@inheritDoc} */
    @Override
    public void setBackgroundColor(final Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    /** {@inheritDoc} */
    @Override
    public void setSize(final Dimension size)
    {
        this.size = size;
    }
}
