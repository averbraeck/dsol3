package nl.tudelft.simulation.dsol.tutorial.section45;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * A Port
 * <p>
 * (c) copyright 2002-2004 <a href="http://www.simulation.tudelft.nl"> Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Jan 19, 2004 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Port
{
    /**
     * the jetties working in the harbor
     */
    private Resource<SimTimeDouble> jetties = null;

    /**
     * the tugs working in the port
     */
    private Resource<SimTimeDouble> tugs = null;

    /**
     * constructs a new Port.
     * @param simulator the simulator
     */
    public Port(final DEVSSimulatorInterface.TimeDouble simulator)
    {
        super();
        this.jetties = new Resource(simulator, "Jetties", 2.0);
        this.tugs = new Resource(simulator, "Tugs", 3.0);
    }

    /**
     * @return Returns the jetties.
     */
    public Resource<SimTimeDouble> getJetties()
    {
        return this.jetties;
    }

    /**
     * @return Returns the tugs.
     */
    public Resource<SimTimeDouble> getTugs()
    {
        return this.tugs;
    }
}
