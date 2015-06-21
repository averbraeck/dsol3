package nl.tudelft.simulation.dsol.animation.D2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.naming.event.EventContext;
import javax.naming.event.NamespaceChangeListener;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.vecmath.Point4i;

import nl.tudelft.simulation.dsol.animation.D2.mouse.InputListener;
import nl.tudelft.simulation.dsol.simulators.AnimatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.logger.Logger;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The AnimationPanel <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:37:49 $
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class AnimationPanel extends GridPanel implements EventListenerInterface, NamespaceChangeListener
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the elements of this panel. */
    private SortedSet<Renderable2DInterface> elements = Collections
            .synchronizedSortedSet(new TreeSet<Renderable2DInterface>(new Renderable2DComparator()));

    /** the simulator. */
    private SimulatorInterface<?, ?, ?> simulator;

    /** the eventContext. */
    private EventContext context = null;

    /** a line that helps the user to see where he is dragging. */
    private Point4i dragLine = new Point4i();

    /** enable drag line. */
    private boolean dragLineEnabled = false;

    /**
     * constructs a new AnimationPanel.
     * @param extent the extent of the panel
     * @param size the size of the panel.
     * @param simulator the simulator of which we want to know the events for animation
     */
    public AnimationPanel(final Rectangle2D extent, final Dimension size, final SimulatorInterface<?, ?, ?> simulator)
    {
        super(extent, size);
        super.showGrid = true;
        InputListener listener = new InputListener(this);
        this.simulator = simulator;
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.addMouseWheelListener(listener);
        this.addKeyListener(listener);
        try
        {
            simulator.addListener(this, AnimatorInterface.UPDATE_ANIMATION_EVENT);
            simulator.addListener(this, SimulatorInterface.START_REPLICATION_EVENT);
        }
        catch (RemoteException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void paintComponent(final Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        // draw the grid.
        super.paintComponent(g2);
        
        // draw the animation elements.
        synchronized (this.elements)
        {
            for (Renderable2DInterface element : this.elements)
            {
                element.paint(g2, this.getExtent(), this.getSize(), this);
            }
        }

        // draw drag line if enabled.
        if (this.dragLineEnabled)
        {
            g.setColor(Color.BLACK);
            g.drawLine(this.dragLine.w, this.dragLine.x, this.dragLine.y, this.dragLine.z);
            this.dragLineEnabled = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notify(final EventInterface event) throws RemoteException
    {
        if (event.getSource() instanceof AnimatorInterface
                && event.getType().equals(AnimatorInterface.UPDATE_ANIMATION_EVENT) && this.isShowing())
        {
            if (this.getWidth() > 0 || this.getHeight() > 0)
            {
                this.repaint();
            }
            return;
        }

        if (event.getSource() instanceof AnimatorInterface
                && event.getType().equals(SimulatorInterface.START_REPLICATION_EVENT))
        {
            this.elements.clear();
            try
            {
                if (this.context != null)
                {
                    this.context.removeNamingListener(this);
                }

                this.context = (EventContext) ContextUtil.lookup(this.simulator.getReplication().getContext(), "/animation/2D");
                this.context.addNamingListener("", EventContext.SUBTREE_SCOPE, this);
                NamingEnumeration<Binding> list = this.context.listBindings("");
                while (list.hasMore())
                {
                    Binding binding = list.next();
                    this.objectAdded(new NamingEvent(this.context, -1, binding, binding, null));
                }
                this.repaint();
            }
            catch (Exception exception)
            {
                Logger.warning(this, "notify", exception);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void objectAdded(final NamingEvent namingEvent)
    {
        Renderable2DInterface element = (Renderable2DInterface) namingEvent.getNewBinding().getObject();
        this.elements.add(element);
    }

    /** {@inheritDoc} */
    @Override
    public void objectRemoved(final NamingEvent namingEvent)
    {
        Renderable2DInterface element = (Renderable2DInterface) namingEvent.getOldBinding().getObject();
        this.elements.remove(element);
    }

    /** {@inheritDoc} */
    @Override
    public void objectRenamed(final NamingEvent namingEvent)
    {
        this.objectRemoved(namingEvent);
        this.objectAdded(namingEvent);
    }

    /** {@inheritDoc} */
    @Override
    public void namingExceptionThrown(final NamingExceptionEvent namingEvent)
    {
        Logger.warning(this, "namingExceptionThrown", namingEvent.getException());
    }

    /**
     * @return the set of animation elements.
     */
    public final SortedSet<Renderable2DInterface> getElements()
    {
        return this.elements;
    }

    /**
     * @return returns the dragLine.
     */
    public final Point4i getDragLine()
    {
        return this.dragLine;
    }

    /**
     * @return returns the dragLineEnabled.
     */
    public final boolean isDragLineEnabled()
    {
        return this.dragLineEnabled;
    }

    /**
     * @param dragLineEnabled the dragLineEnabled to set.
     */
    public final void setDragLineEnabled(final boolean dragLineEnabled)
    {
        this.dragLineEnabled = dragLineEnabled;
    }

}
