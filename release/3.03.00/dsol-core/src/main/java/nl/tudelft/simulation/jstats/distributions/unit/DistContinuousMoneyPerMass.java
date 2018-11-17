package nl.tudelft.simulation.jstats.distributions.unit;

import org.djunits.unit.MoneyPerMassUnit;
import org.djunits.value.vdouble.scalar.MoneyPerMass;

import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * DistContinuousMoneyPerMass is class defining a distribution for a MoneyPerMass scalar. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class DistContinuousMoneyPerMass extends DistContinuousUnit<MoneyPerMassUnit, MoneyPerMass>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new continuous distribution that draws MoneyPerMass scalars.
     * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
     * @param unit MoneyPerMassUnit; the unit for the values of the distribution
     */
    public DistContinuousMoneyPerMass(final DistContinuous wrappedDistribution, final MoneyPerMassUnit unit)
    {
        super(wrappedDistribution, unit);
    }

    /** {@inheritDoc} */
    @Override
    public MoneyPerMass draw()
    {
        return new MoneyPerMass(this.wrappedDistribution.draw(), this.unit);
    }
}
