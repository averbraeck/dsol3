package nl.tudelft.simulation.dsol.formalisms.flow;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.logger.Logger;

/**
 * The Delay object is a station which delays an entity by some time units. When an entity arrives at a delay object,
 * dsol delays the entity by the resulting time period. During the time delay, the entity is held in the delay object.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class Delay<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>> extends
        Station<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /**
     * delayDistribution which is the distribution defining the delay
     */
    protected DistContinuousTime<R> delayDistribution;

    /**
     * Constructor for Delay.
     * @param simulator is the simulator
     * @param delayDistribution is the delayDistribution
     */
    public Delay(final DEVSSimulatorInterface<A, R, T> simulator, final DistContinuousTime<R> delayDistribution)
    {
        super(simulator);
        this.delayDistribution = delayDistribution;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void receiveObject(final Object object) throws RemoteException
    {
        super.receiveObject(object);
        try
        {
            this.simulator.scheduleEventRel(this.delayDistribution.draw(), this, this, "releaseObject",
                    new Object[]{object});
        }
        catch (Exception exception)
        {
            Logger.warning(this, "receiveObject", exception);
        }
    }
}
