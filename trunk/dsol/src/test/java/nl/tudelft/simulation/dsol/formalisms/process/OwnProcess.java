/*
 * @(#) OwnProcess.java Sep 28, 2004 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.formalisms.process;

import junit.framework.Assert;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * A process used for testing.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:45 $
 * @since 1.5
 */
public class OwnProcess extends Process
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /**
     * @param simulator
     */
    public OwnProcess(DEVSSimulatorInterface simulator)
    {
        super(simulator);
    }

    /**
     * processes the process
     */
    @Override
    public void process()
    {
        try
        {
            double time = super.simulator.getSimulatorTime();
            System.out.println(this + " started @ " + time);
            super.hold(10.0);
            double newTime = super.simulator.getSimulatorTime();
            System.out.println(this + " finished @ " + newTime);
            Assert.assertTrue(newTime - time == 10.0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
