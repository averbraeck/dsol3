package nl.tudelft.simulation.dsol.formalisms.dess;

import nl.tudelft.simulation.event.EventType;

/**
 * The Differential equation interface.
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
@SuppressWarnings("checkstyle:interfaceistype")
public interface DifferentialEquationInterface extends nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface
{
    /** VALUE_CHANGED_EVENT is fired on value changes. */
    // TODO: for the differential equations, 30 state variables are reserved. This should be expandable.
    EventType[] VALUE_CHANGED_EVENT = new EventType[30];

    /** FUNCTION_CHANGED_EVENT is firedd on function changes. */
    EventType FUNCTION_CHANGED_EVENT = new EventType("FUNCTION_CHANGED_EVENT");
}
