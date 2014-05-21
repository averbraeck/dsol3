package nl.tudelft.simulation.dsol.naming;

import javax.naming.NamingException;
import javax.naming.event.EventContext;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;

import nl.tudelft.simulation.naming.InitialEventContext;
import nl.tudelft.simulation.naming.listener.ContextListenerInterface;

/*
 * @(#) ServerTest.java Apr 16, 2004 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */

/**
 * The ServerTest.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version 1.2 Apr 16, 2004
 * @since 1.5
 */
public class ServerTest implements ContextListenerInterface
{
    /**
     * constructs a new ServerTest
     * 
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
     * 
     * @param args the commandline arguments
     */
    public static void main(String[] args)
    {
        try
        {
            InitialEventContext context = new InitialEventContext();
            context.bind("/test", "test");
            new ServerTest(context);
        } catch (NamingException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @see javax.naming.event.ObjectChangeListener#objectChanged(javax.naming.event.NamingEvent)
     */
    public void objectChanged(NamingEvent evt)
    {
        System.out.println("changed " + evt);
    }

    /**
     * @see javax.naming.event.NamingListener#namingExceptionThrown(javax.naming.event.NamingExceptionEvent)
     */
    public void namingExceptionThrown(NamingExceptionEvent evt)
    {
        System.out.println("exception " + evt);
    }

    /**
     * @see javax.naming.event.NamespaceChangeListener#objectAdded(javax.naming.event.NamingEvent)
     */
    public void objectAdded(NamingEvent evt)
    {
        System.out.println("added" + evt);
    }

    /**
     * @see javax.naming.event.NamespaceChangeListener#objectRemoved(javax.naming.event.NamingEvent)
     */
    public void objectRemoved(NamingEvent evt)
    {
        System.out.println("removed" + evt);
    }

    /**
     * @see javax.naming.event.NamespaceChangeListener#objectRenamed(javax.naming.event.NamingEvent)
     */
    public void objectRenamed(NamingEvent evt)
    {
        System.out.println("renamed : " + evt);
    }
}