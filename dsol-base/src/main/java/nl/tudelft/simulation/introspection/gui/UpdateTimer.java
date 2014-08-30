/*
 * @(#) UpdateTimer.java Apr 16, 2004 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.introspection.gui;

import java.awt.Component;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * provides a timed update mechanism for components
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Apr 16, 2004
 * @since 1.5
 */
public class UpdateTimer extends TimerTask
{
    /** the tables to update */
    @SuppressWarnings("unchecked")
    private WeakReference<Component>[] components = new WeakReference[0];

    /** the timer */
    private Timer timer = null;

    /** the period for this timer */
    private long period = 300L;

    /**
     * constructs a new UpdateTimer
     * @param period the period in milliseconds
     */
    public UpdateTimer(final long period)
    {
        super();
        this.period = period;
    }

    /**
     * adds a component to the list.
     * @param component the component
     */
    @SuppressWarnings("unchecked")
    public synchronized void add(final Component component)
    {
        List<WeakReference<Component>> arrayList =
                new ArrayList<WeakReference<Component>>(Arrays.asList(this.components));
        arrayList.add(new WeakReference<Component>(component));
        this.components = arrayList.toArray(new WeakReference[arrayList.size()]);
        // The first table added
        if (this.timer == null)
        {
            this.timer = new Timer(true);
            this.timer.scheduleAtFixedRate(this, 0L, this.period);
        }
    }

    /**
     * removes a component from a list
     * @param component the component
     */
    public synchronized void remove(final Component component)
    {
        for (int i = (this.components.length - 1); i > -1; i--)
        {
            if (this.components[i].get().equals(component))
            {
                this.remove(this.components[i]);
            }
        }
    }

    /**
     * removes a reference from a list
     * @param reference the reference
     */
    @SuppressWarnings("unchecked")
    private synchronized void remove(final WeakReference reference)
    {
        List<WeakReference<Component>> arrayList =
                new ArrayList<WeakReference<Component>>(Arrays.asList(this.components));
        arrayList.remove(reference);
        this.components = arrayList.toArray(new WeakReference[arrayList.size()]);
        if (this.components.length == 0)
        {
            // The last component is removed. Let's cancel the timer
            this.timer.cancel();
        }
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
        for (int i = (this.components.length - 1); i > -1; i--)
        {
            Component component = this.components[i].get();
            if (component != null)
            {
                component.repaint();
            }
            else
            {
                this.remove(this.components[i]);
            }
        }
    }
}