package nl.tudelft.simulation.dsol.swing.gui.control;

import java.io.Serializable;
import java.rmi.RemoteException;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * Generic ControlPanel container for the different types of control panel, with different clocks. These control panels do not
 * assume a DEVSSimulator, nor animation.
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
public class GenericControlPanel<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>, S extends SimulatorInterface<A, R, T>> extends AbstractControlPanel<A, R, T, S>
{
    /** */
    private static final long serialVersionUID = 20201227L;

    /**
     * Generic control panel with a different set of control buttons. The control panel does not assume a DEVSSimulator, nor
     * animation.
     * @param model DSOLModel&lt;A, R, T, ? extends SimulationInterface&lt;A, R, T&gt;&gt;; the model for the control panel, to
     *            allow a reset of the model
     * @param simulator S; the simulator. Specified separately, because the model can have been specified with a superclass of
     *            the simulator that the ControlPanel actually needs (e.g., model has been specified with a DEVSAnimator,
     *            whereas the panel needs a RealTimeControlAnimator)
     * @throws RemoteException when simulator cannot be accessed for listener attachment
     */
    public GenericControlPanel(final DSOLModel<A, R, T, ? extends SimulatorInterface<A, R, T>> model, final S simulator)
            throws RemoteException
    {
        super(model, simulator);
    }

    /**
     * Generic ControlPanel for a Double time unit. The control panel does not assume a DEVSSimulator, nor animation.
     * <p>
     * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeDouble extends GenericControlPanel<Double, Double, SimTimeDouble, SimulatorInterface.TimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a generic control panel for a Double time unit, with a different set of control buttons. The control panel
         * does not assume a DEVSSimulator, nor animation.
         * @param model DSOLModel.TimeDouble; the model for the control panel, to allow a reset of the model
         * @param simulator SimulatorInterface.TimeDouble; the simulator. Specified separately, because the model can have been
         *            specified with a superclass of the simulator that the ControlPanel actually needs (e.g., model has been
         *            specified with a DEVSAnimator, whereas the panel needs a RealTimeControlAnimator)
         * @throws RemoteException when simulator cannot be accessed for listener attachment
         */
        public TimeDouble(final DSOLModel.TimeDouble<? extends SimulatorInterface.TimeDouble> model,
                final SimulatorInterface.TimeDouble simulator) throws RemoteException
        {
            super(model, simulator);
            setClockPanel(new ClockPanel.TimeDouble(getSimulator()));
            setSpeedPanel(new SpeedPanel.TimeDouble(getSimulator()));
            setRunUntilPanel(new RunUntilPanel.TimeDouble(getSimulator()));
        }
    }

    /**
     * Generic ControlPanel for a Float time unit. The control panel does not assume a DEVSSimulator, nor animation.
     * <p>
     * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeFloat extends GenericControlPanel<Float, Float, SimTimeFloat, SimulatorInterface.TimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a generic control panel for a Float time unit, with a different set of control buttons. The control panel
         * does not assume a DEVSSimulator, nor animation.
         * @param model DSOLModel.TimeFloat; the model for the control panel, to allow a reset of the model
         * @param simulator SimulatorInterface.TimeFloat; the simulator. Specified separately, because the model can have been
         *            specified with a superclass of the simulator that the ControlPanel actually needs (e.g., model has been
         *            specified with a DEVSAnimator, whereas the panel needs a RealTimeControlAnimator)
         * @throws RemoteException when simulator cannot be accessed for listener attachment
         */
        public TimeFloat(final DSOLModel.TimeFloat<? extends SimulatorInterface.TimeFloat> model,
                final SimulatorInterface.TimeFloat simulator) throws RemoteException
        {
            super(model, simulator);
            setClockPanel(new ClockPanel.TimeFloat(getSimulator()));
            setSpeedPanel(new SpeedPanel.TimeFloat(getSimulator()));
            setRunUntilPanel(new RunUntilPanel.TimeFloat(getSimulator()));
        }
    }

    /**
     * Generic ControlPanel for a Long time unit. The control panel does not assume a DEVSSimulator, nor animation.
     * <p>
     * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeLong extends GenericControlPanel<Long, Long, SimTimeLong, SimulatorInterface.TimeLong>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a generic control panel for a Long time unit, with a different set of control buttons. The control panel
         * does not assume a DEVSSimulator, nor animation.
         * @param model DSOLModel.TimeLong; the model for the control panel, to allow a reset of the model
         * @param simulator SimulatorInterface.TimeLong; the simulator. Specified separately, because the model can have been
         *            specified with a superclass of the simulator that the ControlPanel actually needs (e.g., model has been
         *            specified with a DEVSAnimator, whereas the panel needs a RealTimeControlAnimator)
         * @throws RemoteException when simulator cannot be accessed for listener attachment
         */
        public TimeLong(final DSOLModel.TimeLong<? extends SimulatorInterface.TimeLong> model,
                final SimulatorInterface.TimeLong simulator) throws RemoteException
        {
            super(model, simulator);
            setClockPanel(new ClockPanel.TimeLong(getSimulator()));
            setSpeedPanel(new SpeedPanel.TimeLong(getSimulator()));
            setRunUntilPanel(new RunUntilPanel.TimeLong(getSimulator()));
        }
    }

    /**
     * Generic ControlPanel for a djunits double time unit. The control panel does not assume a DEVSSimulator, nor animation.
     * <p>
     * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeDoubleUnit
            extends GenericControlPanel<Time, Duration, SimTimeDoubleUnit, SimulatorInterface.TimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a generic control panel for a djunits double time unit, with a different set of control buttons. The
         * control panel does not assume a DEVSSimulator, nor animation.
         * @param model DSOLModel.TimeDoubleUnit; the model for the control panel, to allow a reset of the model
         * @param simulator SimulatorInterface.TimeDoubleUnit; the simulator. Specified separately, because the model can have
         *            been specified with a superclass of the simulator that the ControlPanel actually needs (e.g., model has
         *            been specified with a DEVSAnimator, whereas the panel needs a RealTimeControlAnimator)
         * @throws RemoteException when simulator cannot be accessed for listener attachment
         */
        public TimeDoubleUnit(final DSOLModel.TimeDoubleUnit<? extends SimulatorInterface.TimeDoubleUnit> model,
                final SimulatorInterface.TimeDoubleUnit simulator) throws RemoteException
        {
            super(model, simulator);
            setClockPanel(new ClockPanel.TimeDoubleUnit(getSimulator()));
            setSpeedPanel(new SpeedPanel.TimeDoubleUnit(getSimulator()));
            setRunUntilPanel(new RunUntilPanel.TimeDoubleUnit(getSimulator()));
        }
    }

    /**
     * Generic ControlPanel for a djunits float time unit. The control panel does not assume a DEVSSimulator, nor animation.
     * <p>
     * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeFloatUnit
            extends GenericControlPanel<FloatTime, FloatDuration, SimTimeFloatUnit, SimulatorInterface.TimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a generic control panel for a djunits float time unit, with a different set of control buttons. The
         * control panel does not assume a DEVSSimulator, nor animation.
         * @param model DSOLModel.TimeFloatUnit; the model for the control panel, to allow a reset of the model
         * @param simulator SimulatorInterface.TimeFloatUnit; the simulator. Specified separately, because the model can have
         *            been specified with a superclass of the simulator that the ControlPanel actually needs (e.g., model has
         *            been specified with a DEVSAnimator, whereas the panel needs a RealTimeControlAnimator)
         * @throws RemoteException when simulator cannot be accessed for listener attachment
         */
        public TimeFloatUnit(final DSOLModel.TimeFloatUnit<? extends SimulatorInterface.TimeFloatUnit> model,
                final SimulatorInterface.TimeFloatUnit simulator) throws RemoteException
        {
            super(model, simulator);
            setClockPanel(new ClockPanel.TimeFloatUnit(getSimulator()));
            setSpeedPanel(new SpeedPanel.TimeFloatUnit(getSimulator()));
            setRunUntilPanel(new RunUntilPanel.TimeFloatUnit(getSimulator()));
        }
    }

}
