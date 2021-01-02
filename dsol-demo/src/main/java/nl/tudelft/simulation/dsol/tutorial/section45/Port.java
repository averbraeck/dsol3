package nl.tudelft.simulation.dsol.tutorial.section45;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * A Port.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Port
{
    /** the jetties working in the harbor. */
    private Resource<Double, Double, SimTimeDouble> jetties = null;

    /** the tugs working in the port. */
    private Resource<Double, Double, SimTimeDouble> tugs = null;

    /**
     * constructs a new Port.
     * @param simulator DEVSSimulatorInterface.TimeDouble; the simulator
     */
    public Port(final DEVSSimulatorInterface.TimeDouble simulator)
    {
        super();
        this.jetties = new Resource<>(simulator, "Jetties", 2.0);
        this.tugs = new Resource<>(simulator, "Tugs", 3.0);
    }

    /**
     * @return Returns the jetties.
     */
    public Resource<Double, Double, SimTimeDouble> getJetties()
    {
        return this.jetties;
    }

    /**
     * @return Returns the tugs.
     */
    public Resource<Double, Double, SimTimeDouble> getTugs()
    {
        return this.tugs;
    }
}
