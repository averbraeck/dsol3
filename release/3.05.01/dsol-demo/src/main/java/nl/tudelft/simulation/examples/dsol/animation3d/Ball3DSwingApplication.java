package nl.tudelft.simulation.examples.dsol.animation3d;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djutils.draw.bounds.Bounds2d;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.DEVDESSAnimator;
import nl.tudelft.simulation.dsol.swing.gui.DSOLApplication;
import nl.tudelft.simulation.dsol.swing.gui.DSOLPanel;
import nl.tudelft.simulation.dsol.swing.gui.animation.DSOLAnimationTab;
import nl.tudelft.simulation.dsol.swing.gui.control.GenericControlPanel;
import nl.tudelft.simulation.language.DSOLException;

/**
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
    public Ball3DSwingApplication(final String title, final DSOLPanel panel)
    {
        super(panel, title);
        panel.enableSimulationControlButtons();
    }

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * @param args String[]; arguments, expected to be empty
     * @throws SimRuntimeException on error
     * @throws RemoteException on error
     * @throws NamingException on error
     * @throws DSOLException when simulator does not implement AnimatorInterface
     */
    public static void main(final String[] args) throws SimRuntimeException, RemoteException, NamingException, DSOLException
    {
        // TODO: Make 3D examples work with Java-8 and provide good instructions
        DEVDESSAnimator.TimeDouble simulator = new DEVDESSAnimator.TimeDouble("Ball3DSwingApplication", 0.01);
        BallModel3D model = new BallModel3D(simulator);
        Replication.TimeDouble<DESSSimulatorInterface.TimeDouble> replication =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 1000000.0, model);
        simulator.initialize(replication, ReplicationMode.TERMINATING);

        DSOLPanel panel = new DSOLPanel(new GenericControlPanel.TimeDouble(model, simulator));
        panel.getTabbedPane().add("animation",
                new DSOLAnimationTab(new Bounds2d(-100, 100, -100, 100), simulator));
        new Ball3DSwingApplication("Ball 3D Animation model", panel);
    }

}
