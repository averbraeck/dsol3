package nl.tudelft.simulation.dsol.simulators;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

/**
 * The DEVSSimulatorTestmodel specifies the model.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class DEVSTestModel extends TestModel
{
    /** simulator refers to the target. */
    protected DEVSSimulatorInterface simulator;

    /**
     * constructs a new DEVSTestModel.
     */
    public DEVSTestModel()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface simulator)
    {
        super.constructModel(simulator);
        this.simulator = (DEVSSimulator) simulator;
        for (int i = 0; i < 100; i++)
        {
            try
            {
                this.simulator
                        .scheduleEvent(new SimEvent(new SimTimeDouble(Math.random() * i), this, this, "run", null));
            }
            catch (Exception exception)
            {
                SimLogger.always().warn(exception, "constructModel");
            }
        }
    }

    /**
     * the method which is scheduled
     */
    public void run()
    {
        // Testing method
    }
}
