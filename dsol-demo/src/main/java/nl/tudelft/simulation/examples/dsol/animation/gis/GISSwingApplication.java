package nl.tudelft.simulation.examples.dsol.animation.gis;

import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.io.URLResource;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.animation.D2.RenderableScale;
import nl.tudelft.simulation.dsol.animation.gis.D2.GisRenderable2D;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSRealTimeAnimator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.swing.gui.DSOLPanel;
import nl.tudelft.simulation.dsol.swing.gui.animation.DSOLAnimationApplication;
import nl.tudelft.simulation.dsol.swing.gui.animation.DSOLAnimationGisTab;
import nl.tudelft.simulation.dsol.swing.gui.control.RealTimeControlPanel;
import nl.tudelft.simulation.language.DSOLException;

/**
 * GIS demo to show a map.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
     * @param title String; the title
     * @param panel DSOLPanel; the panel
     * @param animationTab DSOLAnimationGisTab; the (custom) animation tab
     * @throws DSOLException when simulator is not an animator
     * @throws IllegalArgumentException for illegal bounds
     * @throws RemoteException on network error
     */
    public GISSwingApplication(final String title, final DSOLPanel panel, final DSOLAnimationGisTab animationTab)
            throws RemoteException, IllegalArgumentException, DSOLException
    {
        super(panel, title, animationTab);
        getAnimationTab().getAnimationPanel().setRenderableScale(new RenderableScale(2.0));
        panel.enableSimulationControlButtons();
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
        simulator.initialize(replication);

        DSOLPanel panel = new DSOLPanel(new RealTimeControlPanel.TimeDouble(model, simulator));
        DSOLAnimationGisTab animationTab =
                new DSOLAnimationGisTab(new Bounds2d(-122.065, -121.997, 37.402, 37.433), simulator);
        animationTab.addAllToggleGISButtonText("MAP LAYERS", model.getGisMap(), "hide or show this GIS layer");
        new GISSwingApplication("GISSwingApplication", panel, animationTab);
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
