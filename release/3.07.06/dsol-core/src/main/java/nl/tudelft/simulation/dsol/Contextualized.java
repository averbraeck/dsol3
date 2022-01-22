package nl.tudelft.simulation.dsol;

import nl.tudelft.simulation.naming.context.ContextInterface;

/**
 * Contextualized is an interface that indicates that an element is stored in a Context.
 * <p>
 * Copyright (c) 2021-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface Contextualized
{
    /**
     * Return the context for this object. For a Replication or Experiment, the Context indicates, e.g., where to store
     * statistics and animation uniquely belonging to that replication or experiment.
     * @return ContextInterface; the specific context for this replication
     */
    ContextInterface getContext();

}
