/*
 * @(#) Process.java Dec 7, 2004 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.serialize;

import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The histogram specifies a histogram chart for the DSOL framework.
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm"> Peter Jacobs </a>
 * @version 1.0 Dec 7, 2004
 * @since 1.5
 */
public class Process extends nl.tudelft.simulation.dsol.formalisms.process.Process
{

    /**
     * constructs a new Process
     * @param simulator
     */
    public Process(DEVSSimulatorInterface simulator)
    {
        super(simulator);
    }

    /**
     * @see nl.tudelft.simulation.dsol.formalisms.process.Process#process()
     */
    @Override
    public void process()
    {
        // we do not specify the process
    }

}
