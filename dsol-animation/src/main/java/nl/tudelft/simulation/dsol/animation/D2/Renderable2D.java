package nl.tudelft.simulation.dsol.animation.D2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djutils.logger.CategoryLogger;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.simulators.AnimatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d2.Shape;
import nl.tudelft.simulation.language.d3.BoundsUtil;
import nl.tudelft.simulation.naming.context.util.ContextUtil;

/**
 * The Renderable2D provides an easy accessible renderable object.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <L> the Locatable class of the source that indicates the location of the Renderable on the screen
 */
public abstract class Renderable2D<L extends Locatable> implements Renderable2DInterface<L>
{
    /** */
    private static final long serialVersionUID = 20200108L;

    /**
     * Storage of the boolean flags, to prevent each flag from taking 32 bits... The initial value is binary 1011 = 0B: rotate =
     * true, flip = false, scale = true, translate = true.
     */
    private byte flags = 0x0B;

    /** whether to rotate the renderable. Flag is 1000 */
    private static final byte ROTATE_FLAG = 0x08;

    /** whether to flip the renderable after rotating 180 degrees. Flag is 0100 */
    private static final byte FLIP_FLAG = 0x04;

    /** whether to scale the renderable when zooming in or out. Flag is 0010 */
    private static final byte SCALE_FLAG = 0x02;

    /** whether to translate the renderable when panning. Flag is 0001 */
    private static final byte TRANSLATE_FLAG = 0x01;

    /** the source of the renderable. */
    private final L source;

    /**
     * constructs a new Renderable2D.
     * @param source T; the source
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator
     * @throws NamingException when animation context cannot be created or retrieved
     * @throws RemoteException when remote context cannot be found
     */
    public Renderable2D(final L source, final SimulatorInterface<?, ?, ?> simulator) throws NamingException, RemoteException
    {
        this.source = source;
        if (!(simulator instanceof AnimatorInterface))
        {
            // We are currently running without animation
            return;
        }
        this.bind2Context(simulator);
    }

    /**
     * binds a renderable2D to the context. The reason for specifying this in an independent method instead of adding the code
     * in the constructor is related to the RFE submitted by van Houten that in specific distributed context, such binding must
     * be overwritten.
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator used for binding the object into the context
     * @throws NamingException when animation context cannot be created or retrieved
     * @throws RemoteException when remote context cannot be found
     */
    protected final void bind2Context(final SimulatorInterface<?, ?, ?> simulator) throws NamingException, RemoteException
    {
        ContextUtil.lookupOrCreateSubContext(simulator.getReplication().getContext(), "animation/2D")
                .bind(Integer.toString(System.identityHashCode(this.source)), this);
    }

    /**
     * @return Returns the flip.
     */
    public final boolean isFlip()
    {
        return (this.flags & FLIP_FLAG) != 0;
    }

    /**
     * @param flip boolean; The flip to set.
     */
    @SuppressWarnings("checkstyle:needbraces")
    public final void setFlip(final boolean flip)
    {
        if (flip)
            this.flags |= FLIP_FLAG;
        else
            this.flags &= (~FLIP_FLAG);
    }

    /**
     * @return Returns the rotate.
     */
    public final boolean isRotate()
    {
        return (this.flags & ROTATE_FLAG) != 0;
    }

    /**
     * @param rotate boolean; The rotate to set.
     */
    @SuppressWarnings("checkstyle:needbraces")
    public final void setRotate(final boolean rotate)
    {
        if (rotate)
            this.flags |= ROTATE_FLAG;
        else
            this.flags &= (~ROTATE_FLAG);
    }

    /**
     * @return Returns the scale.
     */
    public final boolean isScale()
    {
        return (this.flags & SCALE_FLAG) != 0;
    }

    /**
     * @param scale boolean; The scale to set.
     */
    @SuppressWarnings("checkstyle:needbraces")
    public final void setScale(final boolean scale)
    {
        if (scale)
            this.flags |= SCALE_FLAG;
        else
            this.flags &= (~SCALE_FLAG);
    }

    /**
     * @return Returns the translate.
     */
    public final boolean isTranslate()
    {
        return (this.flags & TRANSLATE_FLAG) != 0;
    }

    /**
     * @param translate boolean; The translate to set.
     */
    @SuppressWarnings("checkstyle:needbraces")
    public final void setTranslate(final boolean translate)
    {
        if (translate)
            this.flags |= TRANSLATE_FLAG;
        else
            this.flags &= (~TRANSLATE_FLAG);
    }

    /** {@inheritDoc} */
    @Override
    public L getSource()
    {
        return this.source;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void paint(final Graphics2D graphics, final Rectangle2D extent, final Dimension screenSize,
            final ImageObserver observer)
    {
        try
        {
            Rectangle2D rectangle =
                    BoundsUtil.zIntersect(this.source.getLocation(), this.source.getBounds(), this.source.getZ());
            if (rectangle == null || (!Shape.overlaps(extent, rectangle) && isTranslate()))
            {
                return;
            }
            Point2D screenCoordinates =
                    Renderable2DInterface.Util.getScreenCoordinates(this.source.getLocation(), extent, screenSize);
            // Let's transform
            if (isTranslate())
            {
                graphics.translate(screenCoordinates.getX(), screenCoordinates.getY());
            }
            double scaleFactor = Renderable2DInterface.Util.getScale(extent, screenSize);
            if (isScale())
            {
                graphics.scale(1.0 / scaleFactor, 1.0 / scaleFactor);
            }
            double angle = -this.source.getDirZ();
            if (isFlip() && angle > Math.PI)
            {
                angle = angle - Math.PI;
            }
            if (isRotate() && angle != 0.0)
            {
                graphics.rotate(angle);
            }
            // Now we paint
            this.paint(graphics, observer);
            // Let's untransform
            if (isRotate() && angle != 0.0)
            {
                graphics.rotate(-angle);
            }
            if (isScale())
            {
                graphics.scale(scaleFactor, scaleFactor);
            }
            if (isTranslate())
            {
                graphics.translate(-screenCoordinates.getX(), -screenCoordinates.getY());
            }
        }
        catch (Exception exception)
        {
            CategoryLogger.always().warn(exception, "paint");
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public boolean contains(final Point2D pointWorldCoordinates, final Rectangle2D extent, final Dimension screenSize)
    {
        try
        {
            Rectangle2D intersect =
                    BoundsUtil.zIntersect(this.source.getLocation(), this.source.getBounds(), this.source.getZ());
            if (intersect == null)
            {
                throw new NullPointerException(
                        "empty intersect!: location.z is not in bounds. This is probably due to a modeling error. "
                                + "See the javadoc of LocatableInterface.");
            }
            return intersect.contains(pointWorldCoordinates);
        }
        catch (RemoteException exception)
        {
            CategoryLogger.always().warn(exception, "contains");
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void destroy(final SimulatorInterface<?, ?, ?> simulator) throws NamingException, RemoteException
    {
        ContextUtil.lookupOrCreateSubContext(simulator.getReplication().getContext(), "animation/2D")
                .unbind(Integer.toString(System.identityHashCode(this.source)));
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "Renderable2D [source=" + this.source + "]";
    }

    /**
     * draws an animation on a world coordinates around [x,y=0,0].
     * @param graphics Graphics2D; the graphics object
     * @param observer ImageObserver; the observer
     * @throws RemoteException on network exception
     */
    public abstract void paint(final Graphics2D graphics, final ImageObserver observer) throws RemoteException;

}
