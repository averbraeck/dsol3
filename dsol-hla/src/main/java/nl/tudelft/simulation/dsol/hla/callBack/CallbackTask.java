/*
 * @(#)CallbackTask.java Feb 1, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * The code is published under the Lesser General Public License
 */
package nl.tudelft.simulation.dsol.hla.callBack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.language.reflection.ClassUtil;

/**
 * The CallbackTask forms the essential scheduling mechanism for D-SOL. Objects
 * do not invoke methods directly on eachother; they bundle the object on which
 * the method is planned to be invoked together with the arguments and the name
 * of the method in a simEvent. The CallbackTask is then stored in the eventList
 * and executed.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">
 * www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2005/03/16 15:46:34 $
 * @since 1.0
 */
public class CallbackTask
{
    /**
     * source reflects the source that created the simevent
     */
    protected Object source = null;

    /**
     * target reflects the target on which a state change is scheduled
     */
    protected Object target = null;

    /**
     * method is the method which embodies the state change
     */
    protected String method = null;

    /**
     * args are the arguments which are used to invoke the method with
     */
    protected Object[] args = null;

    /**
     * The constuctor of the event stores the time the event must be executed
     * and the object and method to invoke
     * 
     * @param executionTime reflects the time the event has to be executed.
     * @param priority reflects the priority of the event
     * @param source reflects the source that created the method
     * @param target reflects the object on which the method must be invoked.
     * @param method reflects the method to invoke
     * @param args reflects the argumenst the method to invoke with
     */
    public CallbackTask(final Object source, final Object target,
            final String method, final Object[] args)
    {
        super();
        if (source == null || target == null || method == null)
        {
            throw new IllegalArgumentException(
                    "either source, target or method==null");
        }
        this.source = source;
        this.target = target;
        this.method = method;
        this.args = args;
    }

    /**
     * @see SimEventInterface#execute()
     */
    public synchronized void execute() throws SimRuntimeException
    {
        try
        {
            if (this.method.equals("<init>"))
            {
                if (!(this.target instanceof Class))
                {
                    throw new SimRuntimeException(
                            "Invoking a constructor implies that target should be instance of Class");
                }
                Constructor constructor = ClassUtil.resolveConstructor(
                        (Class) this.target, this.args);
                if (!ClassUtil.isVisible(constructor, this.source.getClass()))
                {
                    throw new SimRuntimeException(this.method
                            + " is not accessible for " + this.source);
                }
                constructor.setAccessible(true);
                constructor.newInstance(this.args);
            } else
            {
                Method methodRef = ClassUtil.resolveMethod(this.target,
                        this.method, this.args);
                if (!ClassUtil.isVisible(methodRef, this.source.getClass()))
                {
                    throw new SimRuntimeException(this.method
                            + " is not accessible for " + this.source);
                }
                methodRef.setAccessible(true);
                methodRef.invoke(this.target, this.args);
            }
        } catch (Exception exception)
        {
            throw new SimRuntimeException(exception);
        }
    }

    /**
     * @return Returns the args.
     */
    public Object[] getArgs()
    {
        return this.args;
    }

    /**
     * @return Returns the method.
     */
    public String getMethod()
    {
        return this.method;
    }

    /**
     * @return Returns the source.
     */
    public Object getSource()
    {
        return this.source;
    }

    /**
     * @return Returns the target.
     */
    public Object getTarget()
    {
        return this.target;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "CallbackTask[source=" + this.source + "; target=" + this.target
                + "; method=" + this.method + "; args=" + this.args + "]";
    }
}