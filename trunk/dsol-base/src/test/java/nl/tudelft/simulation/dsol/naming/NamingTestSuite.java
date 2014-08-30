/*
 * @(#) NamingTestSuite.java Aug 26, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.naming;

import java.util.Properties;

import javax.naming.Context;

import junit.framework.Test;
import junit.framework.TestSuite;
import nl.tudelft.simulation.naming.InitialEventContext;

/**
 * Tests the NamingSuite.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.4 2004-04-08
 * @since 1.5
 */
public final class NamingTestSuite
{
    /**
     * constructs a new NamingTestSuite
     */
    private NamingTestSuite()
    {
        super();
    }

    /**
     * constructs the test suite
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

            properties.put("java.naming.factory.initial", "nl.tudelft.simulation.naming.FileContextFactory");
            properties.put("java.naming.provider.url", "file:/tmp/context.jpo");
            context = new InitialEventContext(properties);
            suite.addTest(new ContextTest(context));

            properties.put("java.naming.factory.initial", "nl.tudelft.simulation.naming.RemoteContextFactory");
            properties.put("java.naming.provider.url", "http://localhost:1099/remoteContext");
            properties.put("wrapped.naming.factory.initial", "nl.tudelft.simulation.naming.JVMContextFactory");
            context = new InitialEventContext(properties);
            suite.addTest(new ContextTest(context));
        }
        catch (Exception exception)
        {
            exception = null;
        }
        return suite;
    }
}