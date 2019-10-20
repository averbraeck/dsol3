package nl.tudelft.simulation.dsol.animation.D2;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.net.URL;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 * @param <T> the Locatable class of the source that indicates the location of the Renderable on the screen
 */
public class SingleImageRenderable<T extends Locatable> extends ImageRenderable<T>
{
    /**
     * constructs a new SingleImageRenderable
     * @param source T; the moving source
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator
     * @param image URL; the image to animate
     * @throws NamingException when animation context cannot be created or retrieved
     * @throws RemoteException when remote context cannot be found
     */
    public SingleImageRenderable(final T source, final SimulatorInterface<?, ?, ?> simulator, final URL image)
            throws RemoteException, NamingException
    {
        super(source, simulator, new URL[] {image});
    }

    /**
     * constructs a new SingleImageRenderable
     * @param staticLocation Point2D; the static location
     * @param size Dimension; the size
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator
     * @param image URL; the image
     * @throws NamingException when animation context cannot be created or retrieved
     * @throws RemoteException when remote context cannot be found
     */
    public SingleImageRenderable(final Point2D staticLocation, final Dimension size,
            final SimulatorInterface<?, ?, ?> simulator, final URL image) throws RemoteException, NamingException
    {
        super(staticLocation, size, simulator, new URL[] {image});
    }

    /**
     * constructs a new SingleImageRenderable
     * @param staticLocation DirectedPoint; the static location
     * @param size Dimension; the size of the image
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator
     * @param image URL; the image
     * @throws NamingException when animation context cannot be created or retrieved
     * @throws RemoteException when remote context cannot be found
     */
    public SingleImageRenderable(final DirectedPoint staticLocation, final Dimension size,
            final SimulatorInterface<?, ?, ?> simulator, final URL image) throws RemoteException, NamingException
    {
        super(staticLocation, size, simulator, new URL[] {image});
    }

    /** {@inheritDoc} */
    @Override
    public int selectImage()
    {
        // We only have one image to show. Let's use this one.
        return 0;
    }
}
