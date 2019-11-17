package nl.tudelft.simulation.dsol.hla.simulators;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * The specifies
 * <p>
 * copyright (c) 2004-2019 <a href="https://simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm"> Peter Jacobs </a>
 * @since 1.2
 */
public interface HLASimulatorInterface extends SimulatorInterface
{
    /**
     * Time-stepped federates will calculate values based on a point in time and then process all events that occur up
     * to the next point in time (current time + time step).
     */
    public static final short STEPPED_TIME_ADVANCE_FUNCTION = 0;

    /**
     * Event-based federates will calculate values based on each event received from the federation execution. After an
     * event is processed, the federate may need to send new events to the federation execution. This implies that the
     * events may not happen on set time intervals but the times of events will be based on the time of the received
     * events.
     */
    public static final short EVENT_BASED_TIME_ADVANCE_FUNCTION = 1;

    /**
     * Optimistic federates do not want to be constrained by the time advancement of regulating federates but instead
     * will proceed ahead of LBTS to calculate and send events in the future. These federates will want to receive all
     * of the events that have been sent in the federation execution regardless of the time-stamp ordering. A federate
     * that uses the flushQueueRequest() service is likely to generate events that are in the future of messages that it
     * has yet to receive. The messages that are received with a time-stamp less than messages already sent may
     * invalidate the previous messages. In this case, the optimistic federate will need to retract the messages that
     * have been invalidated and all federates that have received the invalid messages will receive a
     * requestRetraction() callback on their FederateAmbassador. See the programmer reference pages for a detailed
     * discussion of the retract() and requestRetraction() services.
     */
    public static final short OPTIMISTIC_TIME_ADVANCE_FUNCTION = 2;

    /**
     * returns the look ahead time of the simulator
     * @return the look ahead time of the simulator
     * @throws RemoteException on remote failure
     */
    public double getLookAheadTime() throws RemoteException;

    /**
     * sets the look ahead time of the simulator
     * @param lookAheadTime double; the look ahead time of the simulator
     * @throws RemoteException on remote failure
     */
    public void setLookAheadTime(double lookAheadTime) throws RemoteException;
}
