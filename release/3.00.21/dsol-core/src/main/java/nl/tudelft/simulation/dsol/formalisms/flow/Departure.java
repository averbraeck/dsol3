package nl.tudelft.simulation.dsol.formalisms.flow;

import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The exit station on which statistics are updated and entities destroyed. <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author Peter Jacobs, Alexander Verbraeck
 */
public class Departure extends Station
{
    /**
     * Constructor for Departure.
     * @param simulator on which is scheduled
     */
    public Departure(final DEVSSimulatorInterface simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void receiveObject(final Object object)
    {
        this.fireEvent(StationInterface.RECEIVE_EVENT, object);
        this.fireEvent(StationInterface.RELEASE_EVENT, object);
    }
}
