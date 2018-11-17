package nl.tudelft.simulation.dsol.naming;

import javax.naming.NamingException;
import javax.naming.event.EventContext;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;

import nl.tudelft.simulation.naming.InitialEventContext;
import nl.tudelft.simulation.naming.listener.ContextListenerInterface;

/**
 * The ServerTest.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class ServerTest implements ContextListenerInterface
{
    /**
     * constructs a new ServerTest.
     * @param context the context to use.
     * @throws NamingException on subscription
     */
    public ServerTest(final InitialEventContext context) throws NamingException
    {
        super();
        context.addNamingListener("/test", EventContext.OBJECT_SCOPE, this);
    }

    /**
     * executes the ServerTest
     * @param args the commandline arguments
     */
    public static void main(String[] args)
    {
        try
        {
            InitialEventContext context = new InitialEventContext();
            context.bind("/test", "test");
            new ServerTest(context);
        }
        catch (NamingException e)
        {
            e.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void objectChanged(NamingEvent evt)
    {
        System.out.println("changed " + evt);
    }

    /** {@inheritDoc} */
    @Override
    public void namingExceptionThrown(NamingExceptionEvent evt)
    {
        System.out.println("exception " + evt);
    }

    /** {@inheritDoc} */
    @Override
    public void objectAdded(NamingEvent evt)
    {
        System.out.println("added" + evt);
    }

    /** {@inheritDoc} */
    @Override
    public void objectRemoved(NamingEvent evt)
    {
        System.out.println("removed" + evt);
    }

    /** {@inheritDoc} */
    @Override
    public void objectRenamed(NamingEvent evt)
    {
        System.out.println("renamed : " + evt);
    }
}
