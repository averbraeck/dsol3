package nl.tudelft.simulation.dsol.hla.callBack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.djutils.reflection.ClassUtil;

import nl.tudelft.simulation.dsol.SimRuntimeException;

/**
 * The CallbackTask forms the essential scheduling mechanism for D-SOL. Objects do not invoke methods directly on each
 * other; they bundle the object on which the method is planned to be invoked together with the arguments and the name
 * of the method in a simEvent. The CallbackTask is then stored in the eventList and executed.
 * <p>
 * Copyright (c) 2004-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja">Peter Jacobs </a>
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
     * The constuctor of the event stores the time the event must be executed and the object and method to invoke
     * @param executionTime reflects the time the event has to be executed.
     * @param priority reflects the priority of the event
     * @param source Object; reflects the source that created the method
     * @param target Object; reflects the object on which the method must be invoked.
     * @param method String; reflects the method to invoke
     * @param args Object[]; reflects the argumenst the method to invoke with
     */
    public CallbackTask(final Object source, final Object target, final String method, final Object[] args)
    {
        super();
        if (source == null || target == null || method == null)
        {
            throw new IllegalArgumentException("either source, target or method==null");
        }
        this.source = source;
        this.target = target;
        this.method = method;
        this.args = args;
    }

    /** {@inheritDoc} */
    @Override
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
                Constructor constructor = ClassUtil.resolveConstructor((Class) this.target, this.args);
                if (!ClassUtil.isVisible(constructor, this.source.getClass()))
                {
                    throw new SimRuntimeException(this.method + " is not accessible for " + this.source);
                }
                constructor.setAccessible(true);
                constructor.newInstance(this.args);
            }
            else
            {
                Method methodRef = ClassUtil.resolveMethod(this.target, this.method, this.args);
                if (!ClassUtil.isVisible(methodRef, this.source.getClass()))
                {
                    throw new SimRuntimeException(this.method + " is not accessible for " + this.source);
                }
                methodRef.setAccessible(true);
                methodRef.invoke(this.target, this.args);
            }
        }
        catch (Exception exception)
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

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "CallbackTask[source=" + this.source + "; target=" + this.target + "; method=" + this.method + "; args="
                + this.args + "]";
    }
}
