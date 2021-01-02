package nl.tudelft.simulation.dsol.swing.animation.D2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import org.djutils.draw.bounds.Bounds;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point;
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

    /** a line that helps the user to see where she/he is dragging. */
    private int[] dragLine = new int[4];

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
     * @param homeExtent Bounds2d; the home (initial) extent of the panel
     * @param simulator SimulatorInterface&lt;?, ?, ?&gt;; the simulator of which we want to know the events for animation
     * @throws RemoteException on network error for one of the listeners
     * @throws DSOLException when the simulator is not implementing the AnimatorInterface
     */
    public AnimationPanel(final Bounds2d homeExtent, final SimulatorInterface<?, ?, ?> simulator)
            throws RemoteException, DSOLException
    {
        super(homeExtent);
        if (!(simulator instanceof AnimatorInterface))
        {
            throw new DSOLException("Simulator must implement the AnimatorInterface");
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
            g.drawLine(this.dragLine[0], this.dragLine[1], this.dragLine[2], this.dragLine[3]);
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
            objectAdded((Renderable2DInterface<? extends Locatable>) ((Object[]) event.getContent())[2]);
        }

        else if (event.getType().equals(ContextInterface.OBJECT_REMOVED_EVENT))
        {
            objectRemoved((Renderable2DInterface<? extends Locatable>) ((Object[]) event.getContent())[2]);
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
                        if (element instanceof Renderable2DInterface)
                        {
                            objectAdded((Renderable2DInterface<? extends Locatable>) element);
                        }
                        else
                        {
                            System.err.println("odd object in context: " + element);
                        }
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
     * @return Bounds2d; the full extent of the animation.
     */
    public synchronized Bounds2d fullExtent()
    {
        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        try
        {
            for (Renderable2DInterface<? extends Locatable> renderable : this.elementList)
            {
                Point<?, ?> l = renderable.getSource().getLocation();
                Bounds<?, ?> b = renderable.getSource().getBounds();
                minX = Math.min(minX, l.getX() + b.getMinX());
                minY = Math.min(minY, l.getY() + b.getMinY());
                maxX = Math.max(maxX, l.getX() + b.getMaxX());
                maxY = Math.max(maxY, l.getY() + b.getMaxY());
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

        return new Bounds2d(minX, maxX, minY, maxY);
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
    public final int[] getDragLine()
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
