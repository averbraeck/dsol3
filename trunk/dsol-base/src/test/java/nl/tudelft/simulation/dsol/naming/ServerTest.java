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
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Apr 16, 2004
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
    public void objectChanged(NamingEvent evt)
    {
        System.out.println("changed " + evt);
    }

    /** {@inheritDoc} */
    public void namingExceptionThrown(NamingExceptionEvent evt)
    {
        System.out.println("exception " + evt);
    }

    /** {@inheritDoc} */
    public void objectAdded(NamingEvent evt)
    {
        System.out.println("added" + evt);
    }

    /** {@inheritDoc} */
    public void objectRemoved(NamingEvent evt)
    {
        System.out.println("removed" + evt);
    }

    /** {@inheritDoc} */
    public void objectRenamed(NamingEvent evt)
    {
        System.out.println("renamed : " + evt);
    }
}
