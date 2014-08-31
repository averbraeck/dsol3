package nl.tudelft.simulation.dsol.animation.D2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.rmi.RemoteException;

import javax.naming.Context;

import nl.tudelft.simulation.dsol.animation.LocatableInterface;
import nl.tudelft.simulation.dsol.simulators.AnimatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d2.Shape;
import nl.tudelft.simulation.language.d3.BoundsUtil;
import nl.tudelft.simulation.language.d3.DirectedPoint;
import nl.tudelft.simulation.logger.Logger;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The Renderable2D provides an easy accessible renderable object.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:37:20 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public abstract class Renderable2D implements Renderable2DInterface
{
    /**
     * whether to rotate the renderable
     */
    protected boolean rotate = true;

    /**
     * whether to flip the renderable after rotating 180 degrees
     */
    protected boolean flip = false;

    /**
     * whether to scale the renderable when zooming in or out
     */
    protected boolean scale = true;

    /**
     * whether to translate the renderable when panning
     */
    protected boolean translate = true;

    /**
     * simulator
     */
    protected SimulatorInterface simulator = null;

    /**
     * the source of the renderable
     */
    protected LocatableInterface source = null;

    /**
     * constructs a new Renderable2D.
     * @param source the source
     * @param simulator the simulator
     */
    public Renderable2D(final LocatableInterface source, final SimulatorInterface simulator)
    {
        this.source = source;
        this.simulator = simulator;
        if (!(simulator instanceof AnimatorInterface))
        {
            // We are currently running without animation
            return;
        }
        this.bind2Context(simulator);
    }

    /**
     * binds a renderable2D to the context. The reason for specifying this in an independent method instead of adding
     * the code in the constructor is related to the RFE submitted by van Houten that in specific distributed context,
     * such binding must be overwritten.
     * @param simulator the simulator used for binding the object.
     */
    protected void bind2Context(final SimulatorInterface simulator)
    {
        try
        {
            Context context = ContextUtil.lookup(this.simulator.getReplication().getContext(), "/animation/2D");
            ContextUtil.bind(context, this);
        }
        catch (RemoteException exception)
        {
            Logger.warning(this, "<init>", exception);
        }
    }

    /**
     * @return Returns the flip.
     */
    public boolean isFlip()
    {
        return this.flip;
    }

    /**
     * @param flip The flip to set.
     */
    public void setFlip(final boolean flip)
    {
        this.flip = flip;
    }

    /**
     * @return Returns the rotate.
     */
    public boolean isRotate()
    {
        return this.rotate;
    }

    /**
     * @param rotate The rotate to set.
     */
    public void setRotate(final boolean rotate)
    {
        this.rotate = rotate;
    }

    /**
     * @return Returns the scale.
     */
    public boolean isScale()
    {
        return this.scale;
    }

    /**
     * @param scale The scale to set.
     */
    public void setScale(final boolean scale)
    {
        this.scale = scale;
    }

    /** {@inheritDoc} */
    public LocatableInterface getSource()
    {
        return this.source;
    }

    /**
     * @return Returns the translate.
     */
    public boolean isTranslate()
    {
        return this.translate;
    }

    /**
     * @param translate The translate to set.
     */
    public void setTranslate(final boolean translate)
    {
        this.translate = translate;
    }

    /** {@inheritDoc} */
    public synchronized void paint(final Graphics2D graphics, final Rectangle2D extent, final Dimension screen,
            final ImageObserver observer)
    {
        try
        {
            DirectedPoint location = this.source.getLocation();
            Rectangle2D rectangle =
                    BoundsUtil.getIntersect(this.source.getLocation(), this.source.getBounds(), location.z);
            if (!Shape.overlaps(extent, rectangle) && this.translate)
            {
                return;
            }
            Point2D screenCoordinates =
                    Renderable2DInterface.Util.getScreenCoordinates(this.source.getLocation().to2D(), extent, screen);
            // Let's transform
            if (this.translate)
            {
                graphics.translate(screenCoordinates.getX(), screenCoordinates.getY());
            }
            double scale = Renderable2DInterface.Util.getScale(extent, screen);
            if (this.scale)
            {
                graphics.scale(1.0 / scale, 1.0 / scale);
            }
            double angle = -location.getRotZ();
            if (this.flip && angle > Math.PI)
            {
                angle = angle - Math.PI;
            }
            if (this.rotate && angle != 0.0)
            {
                graphics.rotate(angle);
            }
            // Now we paint
            this.paint(graphics, observer);
            // Let's untransform
            if (this.rotate && angle != 0.0)
            {
                graphics.rotate(-angle);
            }
            if (this.scale)
            {
                graphics.scale(scale, scale);
            }
            if (this.translate)
            {
                graphics.translate(-screenCoordinates.getX(), -screenCoordinates.getY());
            }
        }
        catch (Exception exception)
        {
            Logger.warning(this, "paint", exception);
        }
    }

    /** {@inheritDoc} */
    public boolean contains(final Point2D pointWorldCoordinates, final Rectangle2D extent, final Dimension screen)
    {
        try
        {
            Rectangle2D intersect =
                    BoundsUtil.getIntersect(this.source.getLocation(), this.source.getBounds(),
                            this.source.getLocation().z);
            if (intersect == null)
            {
                throw new NullPointerException(
                        "empty intersect!: location.z is not in bounds. This is probably due to a modeling error. See the javadoc off LocatableInterface.");
            }
            return intersect.contains(pointWorldCoordinates);
        }
        catch (RemoteException exception)
        {
            Logger.warning(this, "contains", exception);
            return false;
        }
    }

    /**
     * destroys an RenderableObject by unsubscribing it from the context.
     */
    public void destroy()
    {
        try
        {
            nl.tudelft.simulation.naming.context.ContextUtil.unbindFromContext(this);
        }
        catch (Throwable throwable)
        {
            Logger.warning(this, "finalize", throwable);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        if (this != this.source)
        {
            return super.toString() + "-OF-" + this.source.toString();
        }
        return super.toString() + "-OF-" + super.toString();
    }

    /**
     * draws an animation on a worldcoordinates around [x,y=0,0]
     * @param graphics the graphics object
     * @param observer the observer
     * @throws RemoteException on network exception
     */
    public abstract void paint(final Graphics2D graphics, final ImageObserver observer) throws RemoteException;
}
