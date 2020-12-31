package nl.tudelft.simulation.examples.dsol.animation.gis;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;

import javax.naming.NamingException;
import javax.swing.JLabel;

import org.djutils.io.URLResource;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.animation.gis.D2.GisRenderable2D;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSRealTimeAnimator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.swing.animation.D2.AnimationPanel;
import nl.tudelft.simulation.dsol.swing.gui.animation.DSOLAnimationApplication;
import nl.tudelft.simulation.dsol.swing.gui.animation.DSOLAnimationPanel;
import nl.tudelft.simulation.dsol.swing.gui.control.AnimationControlPanel;
import nl.tudelft.simulation.dsol.swing.gui.control.ClockSpeedPanel;
import nl.tudelft.simulation.language.DSOLException;

/**
 * GIS demo to show a map.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class GISSwingApplication extends DSOLAnimationApplication
{
    /**
     * @param model DSOLModel; the model
     * @param title String; the title
     * @param panel DSOLPanel&lt;Double,Double,SimTimeDouble&gt;; the panel
     */
    public GISSwingApplication(final EmptyModel model,
            final String title,
            final DSOLAnimationPanel<Double, Double, SimTimeDouble, DEVSSimulatorInterface.TimeDouble> panel)
    {
        super(model, panel, title);
    }

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * @param args String[]; arguments, expected to be empty
     * @throws SimRuntimeException on error
     * @throws RemoteException on error
     * @throws NamingException on error
     * @throws DSOLException when simulator is not an animator
     */
    public static void main(final String[] args) throws SimRuntimeException, RemoteException, NamingException, DSOLException
    {
        DEVSRealTimeAnimator.TimeDouble simulator = new DEVSRealTimeAnimator.TimeDouble("GISSwingApplication", 0.001);
        EmptyModel model = new EmptyModel(simulator);
        Replication.TimeDouble<DEVSSimulatorInterface.TimeDouble> replication =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 1000000.0, model);
        simulator.initialize(replication, ReplicationMode.TERMINATING);
        DSOLAnimationPanel<Double, Double, SimTimeDouble, DEVSSimulatorInterface.TimeDouble> panel =
                new DSOLAnimationPanel<Double, Double, SimTimeDouble, DEVSSimulatorInterface.TimeDouble>(
                        new Rectangle2D.Double(0, 0, 400, 200), new Dimension(400, 200), simulator, model,
                        new AnimationControlPanel.TimeDouble(simulator, model,
                                new ClockSpeedPanel.TimeDouble(new JLabel(""), simulator), null));
        panel.getTabbedPane().add("animation",
                new AnimationPanel(new Rectangle2D.Double(-122, 37.4, 0.2, 0.5), new Dimension(200, 200), simulator));
        panel.getTabbedPane().setSelectedIndex(1);
        new GISSwingApplication(model, "GIS Animation demo", panel);
    }

    /** The empty model -- this demo is just to show a map on the screen. */
    static class EmptyModel extends AbstractDSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble>
    {
        /** The default serial version UID for serializable classes. */
        private static final long serialVersionUID = 1L;

        /** the GIS map. */
        private GisRenderable2D gisMap;

        /**
         * constructs a new EmptyModel.
         * @param simulator DEVSSimulatorInterface.TimeDouble; the simulator
         */
        EmptyModel(final DEVSSimulatorInterface.TimeDouble simulator)
        {
            super(simulator);
        }

        /** {@inheritDoc} */
        @Override
        public void constructModel() throws SimRuntimeException
        {
            URL gisURL = URLResource.getResource("/gis/map.xml");
            System.out.println("GIS-map file: " + gisURL.toString());
            this.gisMap = new GisRenderable2D(getSimulator(), gisURL);
        }

        /** {@inheritDoc} */
        @Override
        public Serializable getSourceId()
        {
            return "EmptyModel";
        }

        /**
         * @return gisMap
         */
        public final GisRenderable2D getGisMap()
        {
            return this.gisMap;
        }
    }
}
