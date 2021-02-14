package nl.tudelft.simulation.dsol.swing.gui.control;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.RunState;

/**
 * ControlPanel container for the a DEVS simulator, with clocks for different time units.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @param <S> the simulator type to use
 */
public class DEVSControlPanel<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>, S extends DEVSSimulatorInterface<A, R, T>> extends AbstractControlPanel<A, R, T, S>
{
    /** */
    private static final long serialVersionUID = 20201227L;

    /** The currently registered stop at event. */
    private SimEvent<T> stopAtEvent = null;

    /**
     * Generic control panel with a different set of control buttons. The control panel assumes a DEVSSimulator that can be
     * paused, but it does not assume animation.
     * @param model DSOLModel&lt;A, R, T, ? extends DEVSSimulationInterface&lt;A, R, T&gt;&gt;; the model for the control panel,
     *            to allow a reset of the model
     * @param simulator S; the simulator. Specified separately, because the model can have been specified with a superclass of
     *            the simulator that the ControlPanel actually needs (e.g., model has been specified with a DEVSAnimator,
     *            whereas the panel needs a RealTimeControlAnimator)
     * @throws RemoteException when simulator cannot be accessed for listener attachment
     */
    public DEVSControlPanel(final DSOLModel<A, R, T, ? extends DEVSSimulatorInterface<A, R, T>> model, final S simulator)
            throws RemoteException
    {
        super(model, simulator);

        // add the buttons to step the simulation
        getControlButtonsPanel().add(makeButton("stepButton", "/Step.png", "Step", "Execute one event", true));
        getControlButtonsPanel().add(makeButton("nextTimeButton", "/StepTime.png", "NextTime",
                "Execute all events scheduled for the current time", true));
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final ActionEvent actionEvent)
    {
        String actionCommand = actionEvent.getActionCommand();
        try
        {
            if (actionCommand.equals("Step"))
            {
                if (getSimulator().isStartingOrRunning())
                {
                    getSimulator().stop();
                }
                getSimulator().step();
            }
            if (actionCommand.equals("NextTime"))
            {
                if (getSimulator().isStartingOrRunning())
                {
                    getSimulator().stop();
                }
                T now = getSimulator().getSimTime();
                try
                {
                    this.stopAtEvent =
                            new SimEvent<T>(now, SimEventInterface.MIN_PRIORITY, this, this, "autoPauseSimulator", null);
                    getSimulator().scheduleEvent(this.stopAtEvent);
                }
                catch (SimRuntimeException exception)
                {
                    getSimulator().getLogger().always()
                            .error("Caught an exception while trying to schedule an autoPauseSimulator event "
                                    + "at the current simulator time");
                }
                getSimulator().start();
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        super.actionPerformed(actionEvent); // includes fixButtons()
    }

    /** {@inheritDoc} */
    @Override
    protected void fixButtons()
    {
        final boolean moreWorkToDo = getSimulator().getRunState() != RunState.ENDED;
        for (JButton button : getControlButtons())
        {
            final String actionCommand = button.getActionCommand();
            if (actionCommand.equals("Step"))
            {
                button.setEnabled(moreWorkToDo && isControlButtonsEnabled());
            }
            else if (actionCommand.equals("NextTime"))
            {
                button.setEnabled(moreWorkToDo && isControlButtonsEnabled());
            }
        }
        super.fixButtons(); // handles the start/stop button
    }

    /** {@inheritDoc} */
    @Override
    protected void invalidateButtons()
    {
        for (JButton button : getControlButtons())
        {
            final String actionCommand = button.getActionCommand();
            if (actionCommand.equals("Step"))
            {
                button.setEnabled(false);
            }
            else if (actionCommand.equals("NextTime"))
            {
                button.setEnabled(false);
            }
        }
        super.invalidateButtons(); // handles the start/stop button
    }

    /**
     * Pause the simulator.
     */
    public void autoPauseSimulator()
    {
        if (getSimulator().isStartingOrRunning())
        {
            try
            {
                getSimulator().stop();
            }
            catch (SimRuntimeException exception1)
            {
                exception1.printStackTrace();
            }
            T currentTick = getSimulator().getSimTime();
            T nextTick = getSimulator().getEventList().first().getAbsoluteExecutionTime();
            if (nextTick.gt(currentTick))
            {
                // The clock is now just beyond where it was when the user requested the NextTime operation
                // Insert another autoPauseSimulator event just before what is now the time of the next event
                // and let the simulator time increment to that time
                try
                {
                    this.stopAtEvent =
                            new SimEvent<T>(nextTick, SimEventInterface.MAX_PRIORITY, this, this, "autoPauseSimulator", null);
                    getSimulator().scheduleEvent(this.stopAtEvent);
                    getSimulator().start();
                }
                catch (SimRuntimeException exception)
                {
                    getSimulator().getLogger().always()
                            .error("Caught an exception while trying to re-schedule an autoPauseEvent at the next real event");
                }
            }
            else
            {
                if (SwingUtilities.isEventDispatchThread())
                {
                    fixButtons();
                }
                else
                {
                    try
                    {
                        SwingUtilities.invokeAndWait(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                fixButtons();
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        if (e instanceof InterruptedException)
                        {
                            System.out.println("Caught " + e);
                        }
                        else
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * DEVS ControlPanel for a Double timeunit.
     * <p>
     * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeDouble extends DEVSControlPanel<Double, Double, SimTimeDouble, DEVSSimulatorInterface.TimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a DEVS control panel for a Double time unit, with a different set of control buttons. The control panel
         * assumes a DEVSSimulator, but not animation.
         * @param model DSOLModel.TimeDouble; the model for the control panel, to allow a reset of the model
         * @param simulator DEVSSimulatorInterface.TimeDouble; the simulator. Specified separately, because the model can have
         *            been specified with a superclass of the simulator that the ControlPanel actually needs (e.g., model has
         *            been specified with a DEVSAnimator, whereas the panel needs a RealTimeControlAnimator)
         * @throws RemoteException when simulator cannot be accessed for listener attachment
         */
        public TimeDouble(final DSOLModel.TimeDouble<? extends DEVSSimulatorInterface.TimeDouble> model,
                final DEVSSimulatorInterface.TimeDouble simulator) throws RemoteException
        {
            super(model, simulator);
            setClockSpeedPanel(new ClockSpeedPanel.TimeDouble(getSimulator()));
            setRunUntilPanel(new RunUntilPanel.TimeDouble(getSimulator()));
        }

    }
}
