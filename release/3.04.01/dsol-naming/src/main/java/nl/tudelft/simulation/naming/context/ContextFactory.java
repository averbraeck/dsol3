package nl.tudelft.simulation.naming.context;

import java.util.Hashtable;

import javax.naming.NamingException;

/**
 * This interface represents a factory that creates an initial context. It is based on the InitialContentFactory from
 * javax.naming but creates a lightweight ContextInterface rather than the heavyweight JNDI context.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public interface ContextFactory
{
    /**
     * Creates an Initial Context for beginning name resolution. Special requirements of this context are supplied using
     * <code>environment</code>.
     * @param environment The possibly null environment specifying information to be used in the creation of the initial context
     * @return A non-null initial context object that implements the ContextInterface
     * @throws NamingException when the initial context could not be created
     */
    ContextInterface getInitialContext(Hashtable<?, ?> environment) throws NamingException;
}
