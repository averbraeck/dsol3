/*
 * @(#) InterpretationThread.java Jan 5, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.interpreter;

import nl.tudelft.simulation.logger.Logger;

/**
 * A InterpretationThread <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:24 $
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public final class InterpretationThread extends Thread
{
    /** the target of this interpretation */
    private Runnable target = null;

    /**
     * constructs a new InterpretationThread
     * 
     * @param target the target.
     */
    public InterpretationThread(final Runnable target)
    {
        super();
        this.target = target;
    }

    /**
     * constructs a new InterpretationThread
     * 
     * @param target the target.
     * @param name the name of the thread
     */
    public InterpretationThread(final Runnable target, final String name)
    {
        super(name);
        this.target = target;
    }

    /**
     * constructs a new InterpretationThread
     * 
     * @param group the threadGroup
     * @param target the target.
     */
    public InterpretationThread(final ThreadGroup group, final Runnable target)
    {
        super(group, target);
        this.target = target;
    }

    /**
     * constructs a new InterpretationThread
     * 
     * @param group the threadGroup
     * @param target the target.
     * @param name the name of the thread
     */
    public InterpretationThread(final ThreadGroup group, final Runnable target,
            final String name)
    {
        super(group, target, name);
        this.target = target;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
        try
        {
            Interpreter.invoke(this.target, this.target.getClass()
                    .getDeclaredMethod("run"), null);
        } catch (Exception exception)
        {
            Logger.warning(this, "run", exception);
        }
    }
}