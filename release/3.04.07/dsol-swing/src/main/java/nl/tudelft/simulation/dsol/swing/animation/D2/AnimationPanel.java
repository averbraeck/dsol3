package nl.tudelft.simulation.dsol.swing.animation.D2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.media.j3d.BoundingBox;
import javax.vecmath.Point3d;
import javax.vecmath.Point4i;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.animation.D2.Renderable2DComparator;
import nl.tudelft.simulation.dsol.animation.D2.Renderable2DInterface;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.simulators.AnimatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.swing.animation.D2.mouse.InputListener;
import nl.tudelft.simulation.language.DSOLException;
import nl.tudelft.simulation.language.d3.DirectedPoint;
import nl.tudelft.simulation.naming.context.ContextInterface;
import nl.tudelft.simulation.naming.context.util.ContextUtil;

/**
 * The AnimationPanel to display animated (Locatable) objects. Added the possibility to witch layers on and off. By default all
 * layers will be drawn, so no changes to existing software need to be made.<br>
 * copyright (c) 2002-2019 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a>.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class AnimationPanel extends GridPanel implements EventListenerInterface
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the elements of this panel. */
    private SortedSet<Renderable2DInterface<? extends Locatable>> elements =
            new TreeSet<Renderable2DInterface<? extends Locatable>>(new Renderable2DComparator());

    /** filter for types to be shown or not. */
    private Map<Class<? extends Locatable>, Boolean> visibilityMap = Collections.synchronizedMap(new HashMap<>());

    /** cache of the classes that are hidden. */
    private Set<Class<? extends Locatable>> hiddenClasses = new HashSet<>();

    /** cache of the classes that are shown. */
    private Set<Class<? extends Locatable>> shownClasses = new HashSet<>();

    /** the simulator. */
    private SimulatorInterface<?, ?, ?> simulator;

    /** the context with the path /experiment/replication/animation/2D. */
    private ContextInterface context = null;

    /** a line that helps the user to see where he is dragging. */
    private Point4i dragLine = new Point4i();

    /** enable drag line. */
    private boolean dragLineEnabled = false;

    /** List of drawable objects. */
    private List<Renderable2DInterface<? extends Locatable>> elementList = new ArrayList<>();

    /** dirty flag for the list. */
    private boolean dirty = false;

    /** the margin factor 'around' the extent. */
    public static final double EXTENT_MARGIN_FACTOR = 0.05;

    /**
     * constructs a new AnimationPanel.
     * @param extent Rectangle2D; the extent of the panel
     * @param size Dimension; the size of the panel.
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator of which we want to know the events for animation
     * @throws RemoteException on network error for one of the listeners
     * @throws DSOLException when the simulator is not implementing the AnimatorInterface
     */
    public AnimationPanel(final Rectangle2D extent, final Dimension size, final SimulatorInterface<?, ?, ?> simulator)
            throws RemoteException, DSOLException
    {
        super(extent, size);
        if (!(simulator instanceof AnimatorInterface))
        {
            throw new DSOLException("Simulator is not implementing AnimatorInterface");
        }
        super.showGrid = true;
        InputListener listener = new InputListener(this);
        this.simulator = simulator;
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.addMouseWheelListener(listener);
        this.addKeyListener(listener);
        simulator.addListener(this, AnimatorInterface.UPDATE_ANIMATION_EVENT);
        simulator.addListener(this, Replication.START_REPLICATION_EVENT);
    }

    /** {@inheritDoc} */
    @Override
    public void paintComponent(final Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        // draw the grid.
        super.paintComponent(g2);

        // update drawable elements when necessary
        if (this.dirty)
        {
            synchronized (this.elementList)
            {
                this.elementList.clear();
                this.elementList.addAll(this.elements);
                this.dirty = false;
            }
        }

        // draw the animation elements.
        for (Renderable2DInterface<? extends Locatable> element : this.elementList)
        {
            if (isShowElement(element))
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
     * Test whether the element needs to be shown on the screen or not.
     * @param element Renderable2DInterface&lt;? extends Locatable&gt;; the renderable element to test
     * @return whether the element needs to be shown or not
     */
    public boolean isShowElement(final Renderable2DInterface<? extends Locatable> element)
    {
        return isShowClass(element.getSource().getClass());
    }

    /**
     * Test whether a certain class needs to be shown on the screen or not. The class needs to implement Locatable, otherwise it
     * cannot be shown at all.
     * @param locatableClass Class&lt;? extends Locatable&gt;; the class to test
     * @return whether the class needs to be shown or not
     */
    public boolean isShowClass(final Class<? extends Locatable> locatableClass)
    {
        if (this.hiddenClasses.contains(locatableClass))
        {
            return false;
        }
        else
        {
            boolean show = true;
            if (!this.shownClasses.contains(locatableClass))
            {
                synchronized (this.visibilityMap)
                {
                    for (Class<? extends Locatable> lc : this.visibilityMap.keySet())
                    {
                        if (lc.isAssignableFrom(locatableClass))
                        {
                            if (!this.visibilityMap.get(lc))
                            {
                                show = false;
                            }
                        }
                    }
                    // add to the right cache
                    if (show)
                    {
                        this.shownClasses.add(locatableClass);
                    }
                    else
                    {
                        this.hiddenClasses.add(locatableClass);
                    }
                }
            }
            return show;
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public void notify(final EventInterface event) throws RemoteException
    {
        if // (this.simulator.getSourceId().equals(event.getSourceId()) && // TODO: improve check
                (event.getType().equals(AnimatorInterface.UPDATE_ANIMATION_EVENT) && this.isShowing())
        {
            if (this.getWidth() > 0 || this.getHeight() > 0)
            {
                this.repaint();
            }
            return;
        }

        else if (event.getType().equals(ContextInterface.OBJECT_ADDED_EVENT))
        {
            objectAdded((Renderable2DInterface<? extends Locatable>) ((Object[])event.getContent())[2]);
        }
        
        else if (event.getType().equals(ContextInterface.OBJECT_REMOVED_EVENT))
        {
            objectRemoved((Renderable2DInterface<? extends Locatable>) ((Object[])event.getContent())[2]);
        }
        
        else if // (this.simulator.getSourceId().equals(event.getSourceId()) && // TODO: improve check
                (event.getType().equals(Replication.START_REPLICATION_EVENT))
        {
            synchronized (this.elementList)
            {
                this.elements.clear();
                try
                {
                    if (this.context != null)
                    {
                        this.context.removeListener(this, ContextInterface.OBJECT_ADDED_EVENT);
                        this.context.removeListener(this, ContextInterface.OBJECT_REMOVED_EVENT);
                    }

                    this.context =
                            ContextUtil.lookupOrCreateSubContext(this.simulator.getReplication().getContext(), "animation/2D");
                    this.context.addListener(this, ContextInterface.OBJECT_ADDED_EVENT);
                    this.context.addListener(this, ContextInterface.OBJECT_REMOVED_EVENT);
                    for (Object element : this.context.values())
                    {
                        objectAdded((Renderable2DInterface<? extends Locatable>) element);
                    }
                    this.repaint();
                }
                catch (Exception exception)
                {
                    this.simulator.getLogger().always().warn(exception, "notify");
                }
            }
        }
    }

    /**
     * Add a locatable object to the animation.
     * @param element Renderable2DInterface&lt;? extends Locatable&gt;; the element to add to the animation
     */
    public void objectAdded(final Renderable2DInterface<? extends Locatable> element)
    {
        synchronized (this.elementList)
        {
            this.elements.add(element);
            this.dirty = true;
        }
    }

    /**
     * Remove a locatable object from the animation.
     * @param element Renderable2DInterface&lt;? extends Locatable&gt;; the element to add to the animation
     */
    public void objectRemoved(final Renderable2DInterface<? extends Locatable> element)
    {
        synchronized (this.elementList)
        {
            this.elements.remove(element);
            this.dirty = true;
        }
    }

    /**
     * Calculate the full extent based on the current positions of the objects.
     * @return the full extent of the animation.
     */
    public synchronized Rectangle2D fullExtent()
    {
        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        Point3d p3dL = new Point3d();
        Point3d p3dU = new Point3d();
        try
        {
            for (Renderable2DInterface<? extends Locatable> renderable : this.elementList)
            {
                DirectedPoint l = renderable.getSource().getLocation();
                BoundingBox b = new BoundingBox(renderable.getSource().getBounds());
                b.getLower(p3dL);
                b.getUpper(p3dU);
                minX = Math.min(minX, l.x + Math.min(p3dL.x, p3dU.x));
                minY = Math.min(minY, l.y + Math.min(p3dL.y, p3dU.y));
                maxX = Math.max(maxX, l.x + Math.max(p3dL.x, p3dU.x));
                maxY = Math.max(maxY, l.y + Math.max(p3dL.y, p3dU.y));
            }
        }
        catch (Exception e)
        {
            // ignore
        }

        minX -= EXTENT_MARGIN_FACTOR * Math.abs(maxX - minX);
        minY -= EXTENT_MARGIN_FACTOR * Math.abs(maxY - minY);
        maxX += EXTENT_MARGIN_FACTOR * Math.abs(maxX - minX);
        maxY += EXTENT_MARGIN_FACTOR * Math.abs(maxY - minY);

        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }

    /**
     * resets the panel to its an extent that covers all displayed objects.
     */
    public synchronized void zoomAll()
    {
        this.extent = Renderable2DInterface.Util.computeVisibleExtent(fullExtent(), this.getSize());
        this.repaint();
    }

    /**
     * Set a class to be shown in the animation to true.
     * @param locatableClass Class&lt;? extends Locatable&gt;; the class for which the animation has to be shown.
     */
    public void showClass(final Class<? extends Locatable> locatableClass)
    {
        synchronized (this.visibilityMap)
        {
            this.visibilityMap.put(locatableClass, true);
        }
        this.shownClasses.clear();
        this.hiddenClasses.clear();
        this.repaint();
    }

    /**
     * Set a class to be hidden in the animation to true.
     * @param locatableClass Class&lt;? extends Locatable&gt;; the class for which the animation has to be hidden.
     */
    public void hideClass(final Class<? extends Locatable> locatableClass)
    {
        synchronized (this.visibilityMap)
        {
            this.visibilityMap.put(locatableClass, false);
        }
        this.shownClasses.clear();
        this.hiddenClasses.clear();
        this.repaint();
    }

    /**
     * Toggle a class to be displayed in the animation to its reverse value.
     * @param locatableClass Class&lt;? extends Locatable&gt;; the class for which a visible animation has to be turned off or
     *            vice versa.
     */
    public void toggleClass(final Class<? extends Locatable> locatableClass)
    {
        synchronized (this.visibilityMap)
        {
            if (!this.visibilityMap.containsKey(locatableClass))
            {
                showClass(locatableClass);
            }
            this.visibilityMap.put(locatableClass, !this.visibilityMap.get(locatableClass));
        }
        this.shownClasses.clear();
        this.hiddenClasses.clear();
        this.repaint();
    }

    /**
     * @return the set of animation elements.
     */
    public final SortedSet<Renderable2DInterface<? extends Locatable>> getElements()
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
     * @param dragLineEnabled boolean; the dragLineEnabled to set.
     */
    public final void setDragLineEnabled(final boolean dragLineEnabled)
    {
        this.dragLineEnabled = dragLineEnabled;
    }
}
