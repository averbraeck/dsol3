package nl.tudelft.simulation.dsol.tutorial.section45;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * A Port
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl"> Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Jan 19, 2004 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Port
{
    /** the jetties working in the harbor. */
    private Resource<Double, Double, SimTimeDouble> jetties = null;

    /** the tugs working in the port. */
    private Resource<Double, Double, SimTimeDouble> tugs = null;

    /**
     * constructs a new Port.
     * @param simulator the simulator
     */
    public Port(final DEVSSimulatorInterface.TimeDouble simulator)
    {
        super();
        this.jetties = new Resource<>(simulator, "Jetties", 2.0);
        this.tugs = new Resource<>(simulator, "Tugs", 3.0);
    }

    /**
     * @return Returns the jetties.
     */
    public Resource<Double, Double, SimTimeDouble> getJetties()
    {
        return this.jetties;
    }

    /**
     * @return Returns the tugs.
     */
    public Resource<Double, Double, SimTimeDouble> getTugs()
    {
        return this.tugs;
    }
}
