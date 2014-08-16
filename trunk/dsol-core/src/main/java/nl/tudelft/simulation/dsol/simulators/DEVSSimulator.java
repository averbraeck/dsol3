/*
 * @(#)DEVSSimulator.java Aug 18, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.dsol.simulators;

import java.rmi.RemoteException;
import java.util.Calendar;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.eventlists.EventListInterface;
import nl.tudelft.simulation.dsol.eventlists.RedBlackTree;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeLongUnit;
import nl.tudelft.simulation.dsol.simtime.UnitTimeDouble;
import nl.tudelft.simulation.dsol.simtime.UnitTimeFloat;
import nl.tudelft.simulation.dsol.simtime.UnitTimeLong;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.logger.Logger;

/**
 * The DEVS defines the interface of the DEVS simulator. DEVS stands for the Discrete Event System Specification. More
 * information on Discrete Event Simulation can be found in "Theory of Modeling and Simulation" by Bernard Zeigler et.
 * al.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, such as
 *            Double or UnitTimeLong, the absolute and relative types are the same.
 * @param <T> the simulation time type based on the absolute and relative time.
 * @since 1.5
 */
public class DEVSSimulator<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Simulator<A, R, T> implements DEVSSimulatorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** eventList represents the future event list */
    protected EventListInterface<T> eventList = new RedBlackTree<T>();

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface #cancelEvent(SimEventInterface)
     */
    public boolean cancelEvent(final SimEventInterface<T> event)
    {
        return this.eventList.remove(event);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface #getEventList()
     */
    public EventListInterface<T> getEventList()
    {
        return this.eventList;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.SimulatorInterface
     *      #initialize(nl.tudelft.simulation.dsol.experiment.Replication,short)
     */
    @Override
    public void initialize(final Replication<A, R, T> initReplication, final ReplicationMode replicationMode)
            throws RemoteException, SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            super.initialize(initReplication, replicationMode);
            this.eventList.clear();
            this.replication.getTreatment().getExperiment().getModel().constructModel(this);
            this.scheduleEvent(new SimEvent<T>(this.getReplication().getTreatment().getEndTime(),
                    (short) (SimEventInterface.MIN_PRIORITY - 1), this, this, "stop", null));
            Object[] args = {new Event(SimulatorInterface.WARMUP_EVENT, this, null)};
            this.scheduleEvent(new SimEvent<T>(this.getReplication().getTreatment().getWarmupTime(),
                    (short) (SimEventInterface.MAX_PRIORITY + 1), this, this, "fireEvent", args));
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface #scheduleEvent(SimEventInterface)
     */
    public void scheduleEvent(final SimEventInterface<T> event) throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            if (event.getAbsoluteExecutionTime().lt(super.simulatorTime)) // TODO: can the time be NaN? If so, exclude!
            {
                throw new SimRuntimeException("cannot schedule event " + event.toString() + " in past "
                        + this.simulatorTime + ">" + event.getAbsoluteExecutionTime());
            }
            this.eventList.add(event);
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface#scheduleEventRel(java.lang.Comparable, short,
     *      java.lang.Object, java.lang.Object, java.lang.String, java.lang.Object[])
     */
    @Override
    public void scheduleEventRel(final R relativeDelay, final short priority, final Object source, final Object target,
            final String method, final Object[] args) throws RemoteException, SimRuntimeException
    {
        T absEventTime = this.simulatorTime.copy();
        absEventTime.add(relativeDelay);
        scheduleEvent(new SimEvent<T>(absEventTime, priority, source, target, method, args));
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface#scheduleEventRel(java.lang.Comparable,
     *      java.lang.Object, java.lang.Object, java.lang.String, java.lang.Object[])
     */
    @Override
    public void scheduleEventRel(final R relativeDelay, final Object source, final Object target, final String method,
            final Object[] args) throws RemoteException, SimRuntimeException
    {
        scheduleEventRel(relativeDelay, SimEventInterface.NORMAL_PRIORITY, source, target, method, args);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface#scheduleEventAbs(nl.tudelft.simulation.dsol.simtime.SimTime,
     *      short, java.lang.Object, java.lang.Object, java.lang.String, java.lang.Object[])
     */
    @Override
    public void scheduleEventAbs(final T absoluteTime, final short priority, final Object source, final Object target,
            final String method, final Object[] args) throws RemoteException, SimRuntimeException
    {
        scheduleEvent(new SimEvent<T>(absoluteTime, priority, source, target, method, args));
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface#scheduleEventAbs(nl.tudelft.simulation.dsol.simtime.SimTime,
     *      java.lang.Object, java.lang.Object, java.lang.String, java.lang.Object[])
     */
    @Override
    public void scheduleEventAbs(final T absoluteTime, final Object source, final Object target, final String method,
            final Object[] args) throws RemoteException, SimRuntimeException
    {
        scheduleEventAbs(absoluteTime, SimEventInterface.NORMAL_PRIORITY, source, target, method, args);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface#scheduleEventAbs(java.lang.Comparable, short,
     *      java.lang.Object, java.lang.Object, java.lang.String, java.lang.Object[])
     */
    @Override
    public void scheduleEventAbs(final A absoluteTime, final short priority, final Object source, final Object target,
            final String method, final Object[] args) throws RemoteException, SimRuntimeException
    {
        T absTime = this.simulatorTime.copy();
        absTime.set(absoluteTime);
        scheduleEvent(new SimEvent<T>(absTime, priority, source, target, method, args));
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface#scheduleEventAbs(java.lang.Comparable,
     *      java.lang.Object, java.lang.Object, java.lang.String, java.lang.Object[])
     */
    @Override
    public void scheduleEventAbs(final A absoluteTime, final Object source, final Object target, final String method,
            final Object[] args) throws RemoteException, SimRuntimeException
    {
        scheduleEventAbs(absoluteTime, SimEventInterface.NORMAL_PRIORITY, source, target, method, args);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface#scheduleEventNow(short, java.lang.Object,
     *      java.lang.Object, java.lang.String, java.lang.Object[])
     */
    @Override
    public void scheduleEventNow(final short priority, final Object source, final Object target, final String method,
            final Object[] args) throws RemoteException, SimRuntimeException
    {
        T absEventTime = this.simulatorTime.copy();
        scheduleEvent(new SimEvent<T>(absEventTime, priority, source, target, method, args));
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface#scheduleEventNow(java.lang.Object,
     *      java.lang.Object, java.lang.String, java.lang.Object[])
     */
    @Override
    public void scheduleEventNow(Object source, Object target, String method, Object[] args) throws RemoteException,
            SimRuntimeException
    {
        scheduleEventNow(SimEventInterface.NORMAL_PRIORITY, source, target, method, args);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface #setEventList(EventListInterface)
     */
    public synchronized void setEventList(final EventListInterface<T> eventList)
    {
        this.eventList = eventList;
        this.fireEvent(EVENTLIST_CHANGED_EVENT, null);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.SimulatorInterface#step()
     */
    @Override
    public void step() throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            super.step();
            if (!this.eventList.isEmpty())
            {
                this.running = true;
                SimEventInterface<T> event = this.eventList.removeFirst();
                this.simulatorTime = event.getAbsoluteExecutionTime();
                this.fireEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime);
                event.execute();
                this.running = false;
            }
        }
    }

    /**
     * the specification of the time advancing function of the discrete event simulator.
     * @see nl.tudelft.simulation.dsol.simulators.Simulator#run()
     */
    @Override
    public void run()
    {
        while (super.isRunning())
        {
            synchronized (super.semaphore)
            {
                SimEventInterface<T> event = this.eventList.removeFirst();
                super.simulatorTime = event.getAbsoluteExecutionTime();
                super.fireEvent(SimulatorInterface.TIME_CHANGED_EVENT, super.simulatorTime, super.simulatorTime);
                try
                {
                    event.execute();
                }
                catch (Exception exception)
                {
                    Logger.severe(this, "run", exception);
                }
            }
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.SimulatorInterface#stop()
     */
    @Override
    public void stop()
    {
        super.stop();
        if (this.getReplication() != null && this.simulatorTime.ge(this.getReplication().getTreatment().getEndTime()))
        {
            this.eventList.clear();
        }
        if (this.eventList.isEmpty())
        {
            this.fireEvent(new Event(SimulatorInterface.END_OF_REPLICATION_EVENT, this, null));
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class DEVSSimulator.Double */
    public static class Double extends DEVSSimulator<java.lang.Double, java.lang.Double, SimTimeDouble> implements
            DEVSSimulatorInterface.Double
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.Float */
    public static class Float extends DEVSSimulator<java.lang.Float, java.lang.Float, SimTimeFloat> implements
            DEVSSimulatorInterface.Float
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.Long */
    public static class Long extends DEVSSimulator<java.lang.Long, java.lang.Long, SimTimeLong> implements
            DEVSSimulatorInterface.Long
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.DoubleUnit */
    public static class DoubleUnit extends DEVSSimulator<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit> implements
            DEVSSimulatorInterface.DoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.FloatUnit */
    public static class FloatUnit extends DEVSSimulator<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit> implements
            DEVSSimulatorInterface.FloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.LongUnit */
    public static class LongUnit extends DEVSSimulator<UnitTimeLong, UnitTimeLong, SimTimeLongUnit> implements
            DEVSSimulatorInterface.LongUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.CalendarDouble */
    public static class CalendarDouble extends DEVSSimulator<Calendar, UnitTimeDouble, SimTimeCalendarDouble> implements
            DEVSSimulatorInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.CalendarFloat */
    public static class CalendarFloat extends DEVSSimulator<Calendar, UnitTimeFloat, SimTimeCalendarFloat> implements
            DEVSSimulatorInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.CalendarLong */
    public static class CalendarLong extends DEVSSimulator<Calendar, UnitTimeLong, SimTimeCalendarLong> implements
            DEVSSimulatorInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

}