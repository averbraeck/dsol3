package nl.tudelft.simulation.jstats.distributions.unit;

import org.djunits.unit.AreaUnit;
import org.djunits.value.vdouble.scalar.Area;

import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * DistContinuousArea is class defining a distribution for a Area scalar. <br>
 * <br>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class DistContinuousArea extends DistContinuousUnit<AreaUnit, Area>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new continuous distribution that draws Area scalars.
     * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
     * @param unit AreaUnit; the unit for the values of the distribution
     */
    public DistContinuousArea(final DistContinuous wrappedDistribution, final AreaUnit unit)
    {
        super(wrappedDistribution, unit);
    }

    /**
     * Constructs a new continuous distribution that draws Area scalars in SI units.
     * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
     */
    public DistContinuousArea(final DistContinuous wrappedDistribution)
    {
        super(wrappedDistribution, AreaUnit.SI);
    }

    /** {@inheritDoc} */
    @Override
    public Area draw()
    {
        return new Area(this.wrappedDistribution.draw(), this.unit);
    }
}
