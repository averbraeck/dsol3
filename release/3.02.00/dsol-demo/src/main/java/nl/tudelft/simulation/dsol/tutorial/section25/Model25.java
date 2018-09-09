package nl.tudelft.simulation.dsol.tutorial.section25;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * The Customer Ordering model class as presented in section 2.5 in the DSOL tutorial.
 * <p>
 * copyright (c) 2002-2018 <a href="http://www.simulation.tudelft.nl"> Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.1 Sep 6, 2004 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Model25 implements DSOLModel.TimeDouble
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface.TimeDouble simulator;

    /** Construct a new customer ordering model. */
    public Model25()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public final void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> simulatorDouble)
    {
        System.out.println("\nReplication starts...");
        DEVSSimulatorInterface.TimeDouble devsSimulator = (DEVSSimulatorInterface.TimeDouble) simulatorDouble;
        this.simulator = devsSimulator;
        new Customer(devsSimulator);
    }

    /** {@inheritDoc} */
    @Override
    public final SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }
}
