package nl.tudelft.simulation.introspection;

/**
 * DelegateIntrospection takes care that an intermediate object delegates the introspection to its parent object. <br>
 * <br>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * <br>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public interface DelegateIntrospection
{
    /**
     * Return an intermediate object to which the introspection is delegated.
     * @return Object; the intermediate object to which the introspection is delegated
     */
    Object getParentIntrospectionObject();
}
