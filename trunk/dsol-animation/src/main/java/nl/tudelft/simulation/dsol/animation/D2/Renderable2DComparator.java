package nl.tudelft.simulation.dsol.animation.D2;

import java.util.Comparator;

/**
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @since 1.5
 */
public class Renderable2DComparator implements Comparator<Renderable2DInterface<?>>
{
    /**
     * constructs a new Renderable2DComparator
     */
    public Renderable2DComparator()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public int compare(final Renderable2DInterface<?> r1, final Renderable2DInterface<?> r2)
    {
        try
        {
            double z1 = r1.getSource().getDirZ();
            double z2 = r2.getSource().getDirZ();
            if (z1 > z2)
            {
                return 1;
            }
            if (z1 < z2)
            {
                return -1;
            }
        }
        catch (Exception exception)
        {
            // ignore as this can happen when the source is in the process of deletion
            // and therefore it cannot return a proper location.
        }
        return Integer.valueOf(r1.hashCode()).compareTo(Integer.valueOf(r2.hashCode()));
    }
}
