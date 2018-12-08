package nl.tudelft.simulation.jstats.distributions.unit;

import org.djunits.unit.MoneyPerLengthUnit;
import org.djunits.value.vdouble.scalar.MoneyPerLength;

import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * DistContinuousMoneyPerLength is class defining a distribution for a MoneyPerLength scalar. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class DistContinuousMoneyPerLength extends DistContinuousUnit<MoneyPerLengthUnit, MoneyPerLength>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new continuous distribution that draws MoneyPerLength scalars.
     * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
     * @param unit MoneyPerLengthUnit; the unit for the values of the distribution
     */
    public DistContinuousMoneyPerLength(final DistContinuous wrappedDistribution, final MoneyPerLengthUnit unit)
    {
        super(wrappedDistribution, unit);
    }

    /** {@inheritDoc} */
    @Override
    public MoneyPerLength draw()
    {
        return new MoneyPerLength(this.wrappedDistribution.draw(), this.unit);
    }
}
