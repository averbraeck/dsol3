package nl.tudelft.simulation.dsol.swing.gui.control;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.RunState;

/**
 * ControlPanel container for the a DEVS simulator, with clocks for different time units.
 * <p>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
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
        getControlButtonsPanel().add(makeButton("stepButton", "/resources/Step.png", "Step", "Execute one event", true));
        getControlButtonsPanel().add(makeButton("nextTimeButton", "/resources/StepTime.png", "NextTime",
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
     * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
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
            setClockPanel(new ClockPanel.TimeDouble(getSimulator()));
            setSpeedPanel(new SpeedPanel.TimeDouble(getSimulator()));
            setRunUntilPanel(new RunUntilPanel.TimeDouble(getSimulator()));
        }
    }

    /**
     * DEVS ControlPanel for a Float timeunit.
     * <p>
     * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeFloat extends DEVSControlPanel<Float, Float, SimTimeFloat, DEVSSimulatorInterface.TimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a DEVS control panel for a Float time unit, with a different set of control buttons. The control panel
         * assumes a DEVSSimulator, but not animation.
         * @param model DSOLModel.TimeFloat; the model for the control panel, to allow a reset of the model
         * @param simulator DEVSSimulatorInterface.TimeFloat; the simulator. Specified separately, because the model can have
         *            been specified with a superclass of the simulator that the ControlPanel actually needs (e.g., model has
         *            been specified with a DEVSAnimator, whereas the panel needs a RealTimeControlAnimator)
         * @throws RemoteException when simulator cannot be accessed for listener attachment
         */
        public TimeFloat(final DSOLModel.TimeFloat<? extends DEVSSimulatorInterface.TimeFloat> model,
                final DEVSSimulatorInterface.TimeFloat simulator) throws RemoteException
        {
            super(model, simulator);
            setClockPanel(new ClockPanel.TimeFloat(getSimulator()));
            setSpeedPanel(new SpeedPanel.TimeFloat(getSimulator()));
            setRunUntilPanel(new RunUntilPanel.TimeFloat(getSimulator()));
        }
    }

    /**
     * DEVS ControlPanel for a Long timeunit.
     * <p>
     * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeLong extends DEVSControlPanel<Long, Long, SimTimeLong, DEVSSimulatorInterface.TimeLong>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a DEVS control panel for a Long time unit, with a different set of control buttons. The control panel
         * assumes a DEVSSimulator, but not animation.
         * @param model DSOLModel.TimeLong; the model for the control panel, to allow a reset of the model
         * @param simulator DEVSSimulatorInterface.TimeLong; the simulator. Specified separately, because the model can have
         *            been specified with a superclass of the simulator that the ControlPanel actually needs (e.g., model has
         *            been specified with a DEVSAnimator, whereas the panel needs a RealTimeControlAnimator)
         * @throws RemoteException when simulator cannot be accessed for listener attachment
         */
        public TimeLong(final DSOLModel.TimeLong<? extends DEVSSimulatorInterface.TimeLong> model,
                final DEVSSimulatorInterface.TimeLong simulator) throws RemoteException
        {
            super(model, simulator);
            setClockPanel(new ClockPanel.TimeLong(getSimulator()));
            setSpeedPanel(new SpeedPanel.TimeLong(getSimulator()));
            setRunUntilPanel(new RunUntilPanel.TimeLong(getSimulator()));
        }
    }

    /**
     * DEVS ControlPanel for a djunits double timeunit.
     * <p>
     * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeDoubleUnit
            extends DEVSControlPanel<Time, Duration, SimTimeDoubleUnit, DEVSSimulatorInterface.TimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a DEVS control panel for a djunits double time unit, with a different set of control buttons. The control
         * panel assumes a DEVSSimulator, but not animation.
         * @param model DSOLModel.TimeDoubleUnit; the model for the control panel, to allow a reset of the model
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; the simulator. Specified separately, because the model can
         *            have been specified with a superclass of the simulator that the ControlPanel actually needs (e.g., model
         *            has been specified with a DEVSAnimator, whereas the panel needs a RealTimeControlAnimator)
         * @throws RemoteException when simulator cannot be accessed for listener attachment
         */
        public TimeDoubleUnit(final DSOLModel.TimeDoubleUnit<? extends DEVSSimulatorInterface.TimeDoubleUnit> model,
                final DEVSSimulatorInterface.TimeDoubleUnit simulator) throws RemoteException
        {
            super(model, simulator);
            setClockPanel(new ClockPanel.TimeDoubleUnit(getSimulator()));
            setSpeedPanel(new SpeedPanel.TimeDoubleUnit(getSimulator()));
            setRunUntilPanel(new RunUntilPanel.TimeDoubleUnit(getSimulator()));
        }
    }

    /**
     * DEVS ControlPanel for a djunits float timeunit.
     * <p>
     * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeFloatUnit
            extends DEVSControlPanel<FloatTime, FloatDuration, SimTimeFloatUnit, DEVSSimulatorInterface.TimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a DEVS control panel for a djunits float time unit, with a different set of control buttons. The control
         * panel assumes a DEVSSimulator, but not animation.
         * @param model DSOLModel.TimeFloatUnit; the model for the control panel, to allow a reset of the model
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; the simulator. Specified separately, because the model can
         *            have been specified with a superclass of the simulator that the ControlPanel actually needs (e.g., model
         *            has been specified with a DEVSAnimator, whereas the panel needs a RealTimeControlAnimator)
         * @throws RemoteException when simulator cannot be accessed for listener attachment
         */
        public TimeFloatUnit(final DSOLModel.TimeFloatUnit<? extends DEVSSimulatorInterface.TimeFloatUnit> model,
                final DEVSSimulatorInterface.TimeFloatUnit simulator) throws RemoteException
        {
            super(model, simulator);
            setClockPanel(new ClockPanel.TimeFloatUnit(getSimulator()));
            setSpeedPanel(new SpeedPanel.TimeFloatUnit(getSimulator()));
            setRunUntilPanel(new RunUntilPanel.TimeFloatUnit(getSimulator()));
        }
    }

}
