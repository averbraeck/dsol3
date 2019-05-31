package nl.tudelft.simulation.jstats.distributions.unit;

import org.djunits.unit.MoneyPerAreaUnit;
import org.djunits.value.vdouble.scalar.MoneyPerArea;

import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * DistContinuousMoneyPerArea is class defining a distribution for a MoneyPerArea scalar. <br>
 * <br>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class DistContinuousMoneyPerArea extends DistContinuousUnit<MoneyPerAreaUnit, MoneyPerArea>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new continuous distribution that draws MoneyPerArea scalars.
     * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
     * @param unit MoneyPerAreaUnit; the unit for the values of the distribution
     */
    public DistContinuousMoneyPerArea(final DistContinuous wrappedDistribution, final MoneyPerAreaUnit unit)
    {
        super(wrappedDistribution, unit);
    }

    /** {@inheritDoc} */
    @Override
    public MoneyPerArea draw()
    {
        return new MoneyPerArea(this.wrappedDistribution.draw(), this.unit);
    }
}
