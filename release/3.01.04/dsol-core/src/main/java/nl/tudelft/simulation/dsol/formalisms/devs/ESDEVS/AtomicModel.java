package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.rmi.RemoteException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * AtomicModel class. Implements the Classic Parallel DEVS Atomic Model with Ports cf Zeigler et al (2000), section
 * 4.2.2. and section 4.3 (pp. 84 ff). The algorithms for parallel DEVS are explained in Chapters 6 and 7.
 * <p>
 * Copyright (c) 2002-2009 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Oct 17, 2009 <br>
 * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a><br>
 */
public abstract class AtomicModel extends AbstractDEVSPortModel
{
    /** the default serialVersionUId. */
    private static final long serialVersionUID = 1L;

    /** future Execution of the Internal Transition. */
    private SimEvent<SimTimeDouble> nextEvent;

    /** remaining TimeAdvance. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double sigma;

    /** the current phase (if applicable). */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Phase phase = new Phase("");

    /** the time of the previous event in this component. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double timeLastEvent;

    /** the time of the next scheduled event in this component. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double timeNextEvent;

    /** the time span since the last event. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double elapsedTime;

    /** the active input port that is currently processed in Parallel DEVS. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected InputPort<?> activePort = null;

    /** conflict handling static: first the internal event. */
    public static final boolean INTERNAL_FIRST = true;

    /** conflict handling static: first the external event. */
    public static final boolean EXTERNAL_FIRST = false;

    /** applied conflict handling strategy in this component. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected boolean conflictStrategy = AtomicModel.INTERNAL_FIRST;

    /** the logger./ */
    private static Logger logger = LogManager.getLogger(AtomicModel.class);

    /**
     * conflict means that both an external event and an internal event happen at the same time; the strategy applied
     * indicates what to do when this happens.
     */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected boolean conflict = false;

    // ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS AND INITIALIZATION
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * Constructor for a stand-alone atomic model with explicit phases.
     * @param modelName the name of this component
     * @param simulator the simulator to schedule on
     * @param e initial elapsed time
     * @param initphase the initial phase of the model
     */
    public AtomicModel(final String modelName, final DEVSSimulatorInterface.TimeDouble simulator, final double e,
            final Phase initphase)
    {
        this(modelName, simulator, e, initphase, AtomicModel.INTERNAL_FIRST);
    }

    /**
     * Constructor for an atomic model within a coupled model with explicit phases.
     * @param modelName the name of this component
     * @param parentModel the coupled model this atomic model is part of
     * @param e initial elapsed time
     * @param initphase the initial phase of the model
     */
    public AtomicModel(final String modelName, final CoupledModel parentModel, final double e, final Phase initphase)
    {
        this(modelName, parentModel, e, initphase, AtomicModel.INTERNAL_FIRST);
    }

    /**
     * @param modelName the name of this component
     * @param parentModel the coupled model this atomic model is part of
     */
    public AtomicModel(final String modelName, final CoupledModel parentModel)
    {
        this(modelName, parentModel, 0, new Phase(""), AtomicModel.INTERNAL_FIRST);
    }

    /**
     * @param modelName the name of this component
     * @param simulator the simulator to schedule on
     */
    public AtomicModel(final String modelName, final DEVSSimulatorInterface.TimeDouble simulator)
    {
        this(modelName, simulator, 0, new Phase(""), AtomicModel.INTERNAL_FIRST);
    }

    /**
     * Constructor for a stand-alone atomic model with explicit phases and a conflict strategy.
     * @param modelName the name of this component
     * @param simulator the simulator to schedule on
     * @param e initial elapsed time
     * @param initphase the initial phase of the model to use for explicit phase models
     * @param conflictStrategy the conflict strategy to use when internal and external events take place at the same
     *            time
     */
    public AtomicModel(final String modelName, final DEVSSimulatorInterface.TimeDouble simulator, final double e,
            final Phase initphase, final boolean conflictStrategy)
    {
        super(modelName, simulator, null);
        this.elapsedTime = e;
        this.timeLastEvent = 0;
        this.phase = initphase;
        this.conflictStrategy = conflictStrategy;
    }

    /**
     * Constructor for an atomic model within a coupled model with explicit phases and a conflict strategy.
     * @param modelName the name of this component
     * @param parentModel the coupled model this atomic model is part of
     * @param e initial elapsed time
     * @param initphase the initial phase of the model to use for explicit phase models
     * @param conflictStrategy the conflict strategy to use when internal and external events take place at the same
     *            time
     */
    public AtomicModel(final String modelName, final CoupledModel parentModel, final double e, final Phase initphase,
            final boolean conflictStrategy)
    {
        super(modelName, parentModel.getSimulator(), parentModel);
        this.elapsedTime = e;
        this.phase = initphase;
        this.timeLastEvent = 0;
        this.conflictStrategy = conflictStrategy;
        // adding to the parent model's components' list
        this.parentModel.addModelComponent(this);
    }

    /**
     * Initialize the atomic model. Start the first internal event based on the time 'e'. See Zeigler's model definition
     * for the definition of 'e'.
     * @param e elapsed time since the last state transition
     */
    @SuppressWarnings("checkstyle:designforextension")
    public void initialize(final double e)
    {
        if (this.timeAdvance() != Double.POSITIVE_INFINITY)
        {
            try
            {
                this.nextEvent =
                        new SimEvent<SimTimeDouble>(this.getSimulator().getSimulatorTime().plus(this.timeAdvance() - e),
                                this, this, "deltaInternalEventHandler", null);
                this.timeLastEvent = this.getSimulator().getSimulatorTime().get();
                this.simulator.scheduleEvent(this.nextEvent);
            }
            catch (RemoteException | SimRuntimeException exception)
            {
                logger.error("initialize", exception);
            }
        }
        else
        {
            this.nextEvent = null;
        }
    }

    // ///////////////////////////////////////////////////////////////////////////
    // BASIC ATOMIC MODEL FUNCTIONALITY
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the elapsed time (e) since the last event.
     * @param eventTime the time of the event for which we want to calculate the elapsed time.
     * @return the elapsed time (e) since the last event.
     * @throws RemoteException a remote exception occurred
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected double elapsedTime(final double eventTime) throws RemoteException
    {
        return (eventTime - this.timeLastEvent);
    }

    /**
     * Schedule the next event.
     */
    private void schedule()
    {

        if (this.timeAdvance() != Double.POSITIVE_INFINITY && !this.conflict)
        {
            try
            {
                if (this.timeAdvance() != Double.POSITIVE_INFINITY)
                {
                    this.nextEvent = new SimEvent<SimTimeDouble>(
                            (this.simulator.getSimulatorTime().plus(this.timeAdvance() - this.elapsedTime)), this, this,
                            "deltaInternalEventHandler", null);
                    this.timeLastEvent = this.simulator.getSimulatorTime().get();
                    logger.trace("schedule " + this.nextEvent.toString());
                    this.simulator.scheduleEvent(this.nextEvent);
                    // this.simulator.setAuthorization(false);
                }
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }
        else
        {
            this.nextEvent = null;
        }
    }

    /**
     * This method handles an incoming external event. As part of its function, it calls the deltaExternal method that
     * is defined in an extension of this class.
     * @param e the elapsed time since the last state transition
     * @param value the value that is passed through the port, which triggered the external event
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected void deltaExternalEventHandler(final double e, final Object value)
    {
        this.deltaExternal(e, value);
        this.schedule();
        this.fireUpdatedState();
    }

    /**
     * @param e the elapsed time since the last state transition
     * @param value the value that is passed through the port, which triggered the external event
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected void deltaConfluent(final double e, final Object value)
    {
        logger.debug("deltaConfluent: CONFLUENT");
        if (this.conflictStrategy == AtomicModel.INTERNAL_FIRST)
        {
            this.deltaInternalEventHandler();
            this.conflict = false;
            this.deltaExternalEventHandler(0, value);
        }
        else
        {
            this.deltaExternalEventHandler(e, value);
            this.conflict = false;
            this.deltaInternalEventHandler();
        }
    }

    /**
     * This method handles an internal event. As part of its function, it calls the deltaInternal method that is defined
     * in an extension of this class.
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected void deltaInternalEventHandler()
    {
        this.lambda();
        this.deltaInternal();
        this.schedule();
        this.fireUpdatedState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void printModel(final String space)
    {
        System.out.println(space + "Atomicmodel: " + this.getClass().getName());
    }

    // ///////////////////////////////////////////////////////////////////////////
    // GETTER AND SETTER METHODS
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * @return the next simulation event for this atomic model.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public SimEvent<SimTimeDouble> getNextEvent()
    {
        return this.nextEvent;
    }

    /**
     * @return the timestamp of the last executed simulation event.
     */
    public final double getTimeLastEvent()
    {
        return this.timeLastEvent;
    }

    /**
     * @return the timestamp of the simulation event to execute next.
     */
    public final double getTimeNextEvent()
    {
        return this.timeNextEvent;
    }

    /**
     * @return if there is a conflict between an intenal event and an external event that take place at the same time.
     */
    protected final boolean isConflict()
    {
        return this.conflict;
    }

    /**
     * @param conflict indicate whether there is a conflict between an intenal event and an external event that take
     *            place at the same time.
     */
    protected final void setConflict(final boolean conflict)
    {
        this.conflict = conflict;
    }

    // ///////////////////////////////////////////////////////////////////////////
    // ABSTRACT METHODS TO BE DEFINED IN AN EXTENSION CLASS
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * the delta internal function that should be implemented by the extending class.
     */
    protected abstract void deltaInternal();

    /**
     * The user defined deltaExternal method that is defined in an extension of this class.
     * @param e the elapsed time since the last state transition
     * @param value the value that has been passed through the port
     */
    protected abstract void deltaExternal(final double e, final Object value);

    /**
     * the lambda function that should be implemented by the extending class.
     */
    protected abstract void lambda();

    /**
     * the time advance function that should be implemented by the extending class.
     * @return the ta, which is the time advance from one state to the next.
     */
    protected abstract double timeAdvance();
}
