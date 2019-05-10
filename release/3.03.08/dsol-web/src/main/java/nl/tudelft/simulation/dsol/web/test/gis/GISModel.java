package nl.tudelft.simulation.dsol.web.test.gis;

import java.net.URL;

import org.djutils.io.URLResource;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSRealTimeClock;

/**
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 */
public class GISModel extends AbstractDSOLModel.TimeDouble<DEVSRealTimeClock.TimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * constructs a new GISModel.
     * @param simulator DEVSRealTimeClock.TimeDouble; the simulator
     */
    public GISModel(final DEVSRealTimeClock.TimeDouble simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel() throws SimRuntimeException
    {
        URL gisURL = URLResource.getResource("/gis/map.xml");
        System.err.println("GIS-map file: " + gisURL.toString());
        new GisRenderableNoCache2D(this.simulator, gisURL);
    }
}
