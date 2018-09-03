package nl.tudelft.simulation.dsol.serialize;

import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The histogram specifies a histogram chart for the DSOL framework.
 * <p>
 * copyright (c) 2004-2018 <a href="http://www.simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a>
 * <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version 1.0 Dec 7, 2004
 * @since 1.5
 */
public class Process extends nl.tudelft.simulation.dsol.formalisms.process.Process
{

    /**
     * constructs a new Process.
     * @param simulator
     */
    public Process(DEVSSimulatorInterface simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public void process()
    {
        // we do not specify the process
    }

}
