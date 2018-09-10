package nl.tudelft.simulation.dsol.simulators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

/**
 * The DEVSSimulatorTestmodel specifies the model <br>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class DEVSTestModel extends TestModel
{
    /** simulator refers to the target. */
    protected DEVSSimulatorInterface simulator;

    /** the logger. */
    private static Logger logger = LogManager.getLogger(DEVSTestModel.class);

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
                logger.warn("constructModel", exception);
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
