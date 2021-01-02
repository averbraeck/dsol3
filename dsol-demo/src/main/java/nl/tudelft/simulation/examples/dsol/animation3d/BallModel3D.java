package nl.tudelft.simulation.examples.dsol.animation3d;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djutils.draw.point.DirectedPoint3d;

import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;

/**
 * BallModel3D, the ball example in 3D.
 * <p>
 * Copyright (c) 2003-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/royc/index.htm">Roy Chin </a>
 */
public class BallModel3D extends AbstractDSOLModel.TimeDouble<DESSSimulatorInterface.TimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs new BallModel3D.
     * @param simulator DESSSimulatorInterface.TimeDouble; the simulator
     */
    public BallModel3D(final DESSSimulatorInterface.TimeDouble simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel()
    {
        System.out.println("*** Ball 3D Model ***");
        for (int i = 0; i < 10; i++)
        {
            try
            {
                new Ball3D(this.simulator);
            }
            catch (NamingException | RemoteException exception)
            {
                this.simulator.getLogger().always().error(exception);
            }
        }
        new World(new DirectedPoint3d(0, 0, -5.5), this.simulator);
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return "BallModel3D";
    }
}
