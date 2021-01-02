package nl.tudelft.simulation.dsol.animation.D2;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.animation.Editable;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * EditableRenderable2D is an abstract class that implements EditableRenderable2DInterface. This class can be extended by
 * classes that animate editable simulation objects.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 * @param <T> the Editable class of the source that indicates the location of the Renderable on the screen
 */
public abstract class EditableRenderable2D<T extends Editable> extends Renderable2D<T>
        implements EditableRenderable2DInterface<T>
{
    /** */
    private static final long serialVersionUID = 20200108L;

    /**
     * constructs a new EditableRenderable2D.
     * @param source T; the source and target
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator
     * @throws NamingException when animation context cannot be retrieved
     * @throws RemoteException when remote animation context cannot be found
     */
    public EditableRenderable2D(final T source, final SimulatorInterface<?, ?, ?> simulator)
            throws RemoteException, NamingException
    {
        super(source, simulator);
    }

    /** {@inheritDoc} */
    @Override
    public T getSource()
    {
        return super.getSource();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isClosedShape()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean allowMove()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean allowRotate()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean allowScale()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean allowEditPoints()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean allowDelete()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean allowAddOrDeletePoints()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int getMaxNumberOfPoints()
    {
        return Integer.MAX_VALUE;
    }

    /** {@inheritDoc} */
    @Override
    public int getMinNumberOfPoints()
    {
        return 1;
    }
}
