package nl.tudelft.simulation.examples.dsol.port;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * A Port <br>
 * (c) copyright 2003 <a href="http://www.simulation.tudelft.nl"> Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @version 1.0 Jan 19, 2004 <br>
 * @author <a href="http://www.simulation.tudelft.nl/people/jacobs.html">Peter Jacobs </a>
 */
public class Port
{
    /** the jetties working in the harbor. */
    private Resource<SimTimeDouble> jetties = null;

    /** the tugs working in the port. */
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
