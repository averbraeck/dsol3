package nl.tudelft.simulation.dsol.swing.gui.control;

import java.io.Serializable;
import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * Generic ControlPanel container for the different types of control panel, with different clocks. These control panels do not
 * assume a DEVSSimulator, nor animation.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
     * @param model DSOLModel<?, ?, ?, ?>; if non-null, the restart button should work
     * @throws RemoteException when simulator cannot be accessed for listener attachment
     */
    public GenericControlPanel(final DSOLModel<A, R, T, ? extends S> model) throws RemoteException
    {
        super(model);
    }

    /**
     * Generic ControlPanel for a Double timeunit. The control panel does not assume a DEVSSimulator, nor animation.
     * <p>
     * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
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
         * @param model DSOLModel<?, ?, ?, ?>; if non-null, the restart button should work
         * @throws RemoteException
         */
        public TimeDouble(final DSOLModel.TimeDouble<SimulatorInterface.TimeDouble> model) throws RemoteException
        {
            super(model);
            setClockPanel(new ClockSpeedPanel.TimeDouble(getSimulator()));
        }

    }
}
