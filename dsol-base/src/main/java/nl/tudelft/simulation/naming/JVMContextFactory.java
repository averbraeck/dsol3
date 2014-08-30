/*
 * @(#)JVMContextFactory.java Feb 1, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.naming;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;

import nl.tudelft.simulation.logger.Logger;

/**
 * A factory for JVMContext instances, automatically invoked by JNDI when the correct jndi.properties file has been
 * used.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 2004-03-24
 * @since 1.5
 */
public class JVMContextFactory implements InitialContextFactory
{
    /** context refers to the static JVMContext */
    private static JVMContext context = null;

    /**
     * @see javax.naming.spi.InitialContextFactory #getInitialContext(Hashtable)
     */
    public synchronized Context getInitialContext(final Hashtable<?, ?> environment)
    {
        if (JVMContextFactory.context == null)
        {
            environment.remove("java.naming.factory.initial");
            if (environment.size() != 0)
            {
                Logger.warning(this, "getInitialContext", "unused environment variables in jndi.properties");
            }
            JVMContextFactory.context = new JVMContext();
        }
        return context;
    }
}