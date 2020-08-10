package nl.tudelft.simulation.dsol.hla.simulators;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.hla.Barrier;
import nl.tudelft.simulation.dsol.hla.DSOLFederateAmbassador;
import nl.tudelft.simulation.dsol.hla.callBack.RTIAmbassador;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.logger.Logger;
import se.pitch.prti.LogicalTimeDouble;
import se.pitch.prti.LogicalTimeIntervalDouble;

/**
 * The HLA-DEVS defines the implementation of the IEEE1516 simulator. DEVS
 * stands for the Discrete Event System Specification. More information on
 * Discrete Event Simulation can be found in "Theory of Modeling and Simulation"
 * by Bernard Zeigler et. al.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">
 * www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja">Peter Jacobs </a>
 * @version $Revision: 1.6 $ $Date: 2005/03/17 17:45:22 $
 * @since 1.0
 */
public class HLADEVSSimulator extends DEVSSimulator implements
                DEVSSimulatorInterface, EventListenerInterface
{
    /** RUNNING IS REQUESTEd. */
    private static final short START_REQUEST = 1;

    /** STEPPING IS REQUESTEd. */
    private static final short STEP_REQUEST = 2;

    /** the timeAdvanceMode. */
    protected short timeAdvanceMode = HLASimulatorInterface.EVENT_BASED_TIME_ADVANCE_FUNCTION;

    /** the request state of this simulator. */
    protected short requestState = START_REQUEST;

    /** the rtiAmbassador. */
    protected RTIAmbassador rtiAmbassador = null;

    /** time until which is granted. */
    protected double grant = Double.NaN;

    /** the barrier to use. */
    protected Barrier barrier = new Barrier();

    /**
     * constructs a new HLADEVSSimulator.
     * 
     * @param rtiAmbassador
     *            the RTIAmbassador to use
     * @param dsolFederateAmbassador
     *            the dsolFederateAmbassador to use
     */
    public HLADEVSSimulator(final RTIAmbassador rtiAmbassador,
                    final DSOLFederateAmbassador dsolFederateAmbassador)
    {
        this(rtiAmbassador, dsolFederateAmbassador,
                        HLASimulatorInterface.EVENT_BASED_TIME_ADVANCE_FUNCTION);
    }

    /**
     * constructs a new HLADEVSSimulator.
     * 
     * @param rtiAmbassador
     *            the RTIAmbassador to use
     * @param dsolFederateAmbassador
     *            the dsolFederateAmbassador to use
     */
    public HLADEVSSimulator(final RTIAmbassador rtiAmbassador,
                    final DSOLFederateAmbassador dsolFederateAmbassador,
                    final short timeAdvanceMode)
    {
        super();
        this.timeAdvanceMode = timeAdvanceMode;
        this.rtiAmbassador = rtiAmbassador;
        dsolFederateAmbassador.addListener(this,
                        DSOLFederateAmbassador.TIME_ADVANCE_GRANT_EVENT);
        dsolFederateAmbassador.addListener(this,
                        DSOLFederateAmbassador.INTERACTION_REC_EVENT);
    }

    /** {@inheritDoc} */ @Override public  void step() throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            if (this.isRunning())
            {
                throw new SimRuntimeException("Cannot step a running simulator");
            }
            if (this.replication == null)
            {
                throw new SimRuntimeException("Cannot step a simulator "
                                + "without replication details");
            }
            if (this.simulatorTime >= this.replication.getRunControl()
                            .getRunLength())
            {
                throw new SimRuntimeException("Cannot step simulator: "
                                + "SimulatorTime = runControl.runLength");
            }
            logger.debugr(this, "step", "OK");
            this.fireEvent(SimulatorInterface.STEP_EVENT, null);
            if (!this.eventList.isEmpty() && this.grant >= super.simulatorTime)
            {
                this.running = true;
                SimEventInterface event = this.eventList.removeFirst();
                this.simulatorTime = event.getAbsoluteExecutionTime();
                this.fireEvent(SimulatorInterface.TIME_CHANGED_EVENT,
                                this.simulatorTime, this.simulatorTime);
                event.execute();
                this.running = false;
            } else
            {
                this.requestState = STEP_REQUEST;
                this.timeAdvanceRequest();
            }
        }
    }

    /**
     * requests a grant to continue until the time of the next event on the
     * eventList
     */
    protected void timeAdvanceRequest()
    {
        if (!this.eventList.isEmpty())
        {
            this.timeAdvanceRequest(this.eventList.first()
                            .getAbsoluteExecutionTime());
        } else
        {
            throw new IllegalStateException(
                            "Eventlist=empty. Why request a new time?");
        }
    }

    /**
     * requests a grant to continue until time
     * 
     * @param time
     *            the time to advance to
     */
    protected void timeAdvanceRequest(double time)
    {
        // This is all executed AS PART OF THE WORKER'S THREAD RUN METHOD!
        // Brings the potential danger of a premature Worker Thread
        // interruption, as the GRANT may
        // be received before this methods actually gets finished (which
        // normally causes the Worker thread
        // to enter the WAIT state)
        time = Math.max(time, this.simulatorTime + getLookAheadTime());
        try
        {
            if (this.isRunning())
            {
                this.stop();
            }
            logger.debugr(this, "timeAdvanceRequest", "requested time until "
                            + time);
            switch (this.timeAdvanceMode)
            {
            //TimeAvailable allows Zero Lookahead
            case HLASimulatorInterface.STEPPED_TIME_ADVANCE_FUNCTION:
                this.rtiAmbassador.timeAdvanceRequestAvailable(new LogicalTimeDouble(
                                time));
                break;
            //RequestAvailable allows Zero Lookahead
            case HLASimulatorInterface.EVENT_BASED_TIME_ADVANCE_FUNCTION:
                this.rtiAmbassador
                                .nextEventRequestAvailable(new LogicalTimeDouble(
                                                time));
                break;
            case HLASimulatorInterface.OPTIMISTIC_TIME_ADVANCE_FUNCTION:
                throw new IllegalStateException(
                                "Positive time advance not yet supported");
            default:
                throw new IllegalStateException("Unknown time advance function");
            }
        } catch (Exception exception)
        {
            logger.error("timeAdvanceRequest", exception);
        }
    }

    /**
     * Returns the look ahead time of the simulator. Is queried from the RTI to
     * make sure the ACTUAL lookahead, rather than the REQUESTED lookahead is
     * returned. See Kuhl (2000) section 8.2.3.
     * 
     * @return the look ahead time of the simulator.
     */
    public double getLookAheadTime()
    {
        try
        {
            double lookahead = ((LogicalTimeIntervalDouble) rtiAmbassador
                            .queryLookahead()).getValue();
            return lookahead;
        } catch (Exception e)
        {
            logger.error("Failed to retrieve lookahead: " + e, e);
            return -1;
        }
    }

    /**
     * sets the look ahead time of the simulator
     * 
     * @param lookAheadTime
     *            the look ahead time of the simulator
     */
    public void setLookAheadTime(final double lookAheadTime)
    {
        try
        {
            this.rtiAmbassador.modifyLookahead(new LogicalTimeIntervalDouble(
                            lookAheadTime));
        } catch (Exception exception)
        {
            logger.warn("setLookAheadTime", exception);
        }
    }

    /**
     * Synchronized: to prevent Worker and RTI Thread to access the simulator
     * SIMULTANEOUSLY. Helps prevening premature Worker Thread interruption.
     * 
     * @see nl.tudelft.simulation.dsol.simulators.Simulator#run()
     */
    public synchronized void run()
    {
        logger.trace(this, "run", "Commenced run");
        while (isRunning()
                        && (this.grant >= this.eventList.first()
                                        .getAbsoluteExecutionTime()))
        {
            synchronized (super.semaphore)
            {
                SimEventInterface event = this.eventList.removeFirst();
                this.simulatorTime = event.getAbsoluteExecutionTime();
                fireEvent(SimulatorInterface.TIME_CHANGED_EVENT,
                                this.simulatorTime, this.simulatorTime);
                try
                {
                    event.execute();
                } catch (Exception exception)
                {
                    logger.error("run", exception);
                }
            }
        }
        if (isRunning())
        {
            this.requestState = START_REQUEST;
            this.timeAdvanceRequest();
        }
    }

    /**
     * Invocation of the Time Advance Grant � service shall indicate that a
     * prior request to advance the federate�s logical time has been honored.
     * The argument of this service shall indicate that the logical time for the
     * federate has been advanced to this value. If the grant is issued in
     * response to invocation of Next Event Request or Time Advance Request, the
     * RTI shall guarantee that no additional TSO messages shall be delivered in
     * the future with time stamps less than or equal to this value. If the
     * grant is in response to an invocation of Time Advance Request Available,
     * Next Event Request Available, or Flush Queue Request, the RTI shall
     * guarantee that no additional TSO messages shall be delivered in the
     * future with time stamps less than the value of the grant.
     * 
     * @param arg0
     *            the time until granted
     */
    protected synchronized void timeAdvanceGrant(final double arg0)
    {
        // NB: This synchronization is NOT enought to prevent premature
        // WorkerThread interruption.
        // After WT leaves this object's methods, it rambles a bit before
        // calling wait. In between, this method can still be invoked, resulting
        // in a premature interruption.
        this.grant = arg0;
        try
        {
            if (this.requestState == START_REQUEST)
            {
                logger.trace(this, "", "GRANTED to " + arg0
                                + ": starting. Thread name: "
                                + Thread.currentThread().getName());
                this.start();
            } else
            {
                logger.trace(this, "", "GRANTED to " + arg0 + ": stepping");
                this.step();
            }
        } catch (SimRuntimeException exception)
        {
            logger.error("timeAdvanceGrant", exception);
        }
    }
    
    /**
     * Returns the time currently granted by the RTI 
     */
    protected double getGrant()
    {
        if (Double.isNaN(grant))
            return getSimulatorTime();
        return grant;
    }
    
    /** {@inheritDoc} */ @Override public  void notify(final EventInterface event)
    {
        Logger.info(this, "notify", "Handling incoming DSOL event: " + event);
        if (event.getType()
                        .equals(DSOLFederateAmbassador.INTERACTION_REC_EVENT))
        {
            logger.debug(this, "notify", "Received Interaction Received Event");
            // Schedule interaction processing: detach from RTI thread
            HLAInteractionEvent content = (HLAInteractionEvent) event
                            .getContent();
            try
            {
                // Should be OK: Interactions are delivered in between a time
                // REQUEST and a GRANT. As a result, the
                // simulator thread should be idle when we schedule this event.
                Logger.info(this, "", "Scheduling interaction handling at "
                                + content.getScheduledTime());
                scheduleEvent(new SimEvent(content.getScheduledTime(), this,
                                this, "processInteractionEvent", new Object[]
                                { content }));
            } catch (Exception e)
            {
                Logger.severe(this,
                                "FAILURE during internal interaction processing scheduling:"
                                                + e, e);
                e.printStackTrace();
            }
            return;
        }
        // Otherwise, a time event is expected.
        double time = ((Double) event.getContent()).doubleValue();
        if (event.getType().equals(
                        DSOLFederateAmbassador.TIME_ADVANCE_GRANT_EVENT))
        {
            this.timeAdvanceGrant(time);
            return;
        }
        super.simulatorTime = time;
        super.fireEvent(SimulatorInterface.TIME_CHANGED_EVENT, time, time);
        this.barrier.lower();
    }

    /**
     * Invoked during the processing of self-scheduled interaction processing
     * evts.
     */
    protected void processInteractionEvent(HLAInteractionEvent evt)
    {
        logger.debug(this, "", "Processing received interaction event: " + evt
                        + " @ " + getSimulatorTime());
        // In this class, no work is done. Check sub-classes for the handling of
        // specific interactions.
    }
}