package nl.tudelft.simulation.naming;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;

import nl.tudelft.simulation.logger.CategoryLogger;

/**
 * A factory for JVMContext instances, automatically invoked by JNDI when the correct jndi.properties file has been
 * used.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class JVMContextFactory implements InitialContextFactory
{
    /** context refers to the static JVMContext. */
    private static JVMContext context = null;

    /** {@inheritDoc} */
    @Override
    public synchronized Context getInitialContext(final Hashtable<?, ?> environment)
    {
        if (JVMContextFactory.context == null)
        {
            environment.remove("java.naming.factory.initial");
            if (environment.size() != 0)
            {
                CategoryLogger.always().warn("unused environment variables in jndi.properties");
            }
            JVMContextFactory.context = new JVMContext();
        }
        return context;
    }
}
