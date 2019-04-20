package nl.tudelft.simulation.dsol.naming;

import java.util.Properties;

import javax.naming.Context;

import junit.framework.Test;
import junit.framework.TestSuite;
import nl.tudelft.simulation.naming.InitialEventContext;

/**
 * Tests the NamingSuite..
 * <p>
 * Copyright (c) 2004-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public final class NamingTestSuite
{
    /**
     * constructs a new NamingTestSuite.
     */
    private NamingTestSuite()
    {
        super();
    }

    /**
     * constructs the test suite.
     * @return Test the main DSOL Test Suite
     */
    public static Test suite()
    {

        TestSuite suite = new TestSuite("Naming Test Suite");

        try
        {
            Properties properties = new Properties();
            properties.put("java.naming.factory.initial", "nl.tudelft.simulation.naming.JVMContextFactory");
            Context context = new InitialEventContext(properties);
            suite.addTest(new ContextTest(context));

            // TODO: create working tests for the FileContextFactory an RemoteContextFactory

            /*-
            properties.put("java.naming.factory.initial", "nl.tudelft.simulation.naming.FileContextFactory");
            properties.put("java.naming.provider.url", "file:/tmp/context.jpo");
            context = new InitialEventContext(properties);
            suite.addTest(new ContextTest(context)); 
            
            properties.put("java.naming.factory.initial", "nl.tudelft.simulation.naming.RemoteContextFactory");
            properties.put("java.naming.provider.url", "http://localhost:1099/remoteContext");
            properties.put("wrapped.naming.factory.initial", "nl.tudelft.simulation.naming.JVMContextFactory");
            context = new InitialEventContext(properties);
            suite.addTest(new ContextTest(context));
            */
        }
        catch (Exception exception)
        {
            exception = null;
        }
        return suite;
    }
}
