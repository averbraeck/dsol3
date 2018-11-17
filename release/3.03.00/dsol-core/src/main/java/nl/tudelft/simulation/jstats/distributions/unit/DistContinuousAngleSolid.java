package nl.tudelft.simulation.jstats.distributions.unit;

import org.djunits.unit.AngleSolidUnit;
import org.djunits.value.vdouble.scalar.AngleSolid;

import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * DistContinuousAngleSolid is class defining a distribution for a AngleSolid scalar. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class DistContinuousAngleSolid extends DistContinuousUnit<AngleSolidUnit, AngleSolid>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new continuous distribution that draws AngleSolid scalars.
     * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
     * @param unit AngleSolidUnit; the unit for the values of the distribution
     */
    public DistContinuousAngleSolid(final DistContinuous wrappedDistribution, final AngleSolidUnit unit)
    {
        super(wrappedDistribution, unit);
    }

    /**
     * Constructs a new continuous distribution that draws AngleSolid scalars in SI units.
     * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
     */
    public DistContinuousAngleSolid(final DistContinuous wrappedDistribution)
    {
        super(wrappedDistribution, AngleSolidUnit.SI);
    }

    /** {@inheritDoc} */
    @Override
    public AngleSolid draw()
    {
        return new AngleSolid(this.wrappedDistribution.draw(), this.unit);
    }
}
