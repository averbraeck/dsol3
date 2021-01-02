package nl.tudelft.simulation.dsol.tutorial.section44;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djutils.draw.point.DirectedPoint3d;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVDESSSimulator;

/**
 * BallModel, the ball example in 3D.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/royc/index.htm">Roy Chin </a>
 */
public class Model extends AbstractDSOLModel.TimeDouble<DEVDESSSimulator.TimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs new BallModel.
     * @param simulator DEVDESSSimulator.TimeDouble; the simulator
     */
    public Model(final DEVDESSSimulator.TimeDouble simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel() throws SimRuntimeException
    {
        new World(new DirectedPoint3d(0, 0, -5.5), this.simulator);
        for (int i = 0; i < 10; i++)
        {
            try
            {
                new DiscreteBall(this.simulator);
                new ContinuousBall(this.simulator);
            }
            catch (NamingException | RemoteException exception)
            {
                getSimulator().getLogger().always().error(exception);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return "Model";
    }
}
