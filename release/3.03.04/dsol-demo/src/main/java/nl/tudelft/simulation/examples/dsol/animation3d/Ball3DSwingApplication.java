package nl.tudelft.simulation.examples.dsol.animation3d;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulator;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.swing.animation.D2.AnimationPanel;
import nl.tudelft.simulation.dsol.swing.gui.DSOLApplication;
import nl.tudelft.simulation.dsol.swing.gui.DSOLPanel;

/**
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class Ball3DSwingApplication extends DSOLApplication
{
    /**
     * @param title String; the title
     * @param panel DSOLPanel&lt;Double,Double,SimTimeDouble&gt;; the panel
     */
    public Ball3DSwingApplication(final String title, final DSOLPanel<Double, Double, SimTimeDouble> panel)
    {
        super(title, panel);
    }

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * @param args String[]; arguments, expected to be empty
     * @throws SimRuntimeException on error
     * @throws RemoteException on error
     * @throws NamingException on error
     */
    public static void main(final String[] args) throws SimRuntimeException, RemoteException, NamingException
    {
        // TODO: Make 3D examples work with Java-8 and provide good instructions
        DESSSimulator.TimeDouble simulator = new DESSSimulator.TimeDouble(0.01);
        BallModel3D model = new BallModel3D(simulator);
        Replication.TimeDouble<DESSSimulatorInterface.TimeDouble> replication =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 1000000.0, model);
        DSOLPanel<Double, Double, SimTimeDouble> panel = new DSOLPanel<Double, Double, SimTimeDouble>(model, simulator);
        panel.getTabbedPane().add("animation",
                new AnimationPanel(new Rectangle2D.Double(-100, -100, 200, 200), new Dimension(200, 200), simulator));
        panel.getTabbedPane().setSelectedIndex(1);
        simulator.initialize(replication, ReplicationMode.TERMINATING);
        new Ball3DSwingApplication("Ball 3D Animation model", panel);
    }

}
