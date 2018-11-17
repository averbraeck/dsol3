package nl.tudelft.simulation.jstats.distributions.unit;

import org.djunits.unit.MoneyPerEnergyUnit;
import org.djunits.value.vdouble.scalar.MoneyPerEnergy;

import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * DistContinuousMoneyPerEnergy is class defining a distribution for a MoneyPerEnergy scalar. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class DistContinuousMoneyPerEnergy extends DistContinuousUnit<MoneyPerEnergyUnit, MoneyPerEnergy>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new continuous distribution that draws MoneyPerEnergy scalars.
     * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
     * @param unit MoneyPerEnergyUnit; the unit for the values of the distribution
     */
    public DistContinuousMoneyPerEnergy(final DistContinuous wrappedDistribution, final MoneyPerEnergyUnit unit)
    {
        super(wrappedDistribution, unit);
    }

    /** {@inheritDoc} */
    @Override
    public MoneyPerEnergy draw()
    {
        return new MoneyPerEnergy(this.wrappedDistribution.draw(), this.unit);
    }
}
