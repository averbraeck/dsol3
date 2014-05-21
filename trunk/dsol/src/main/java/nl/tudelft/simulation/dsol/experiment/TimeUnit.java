/*
 * @(#)TimeUnit.java Feb 1, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.experiment;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * The TimeUnitInterface defines the simulator time units. <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class TimeUnit implements TimeUnitInterface
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** value represents the value relative to the number of miliseconds */
    private long value;

    /** name represents the name of the time unit */
    private String name;

    /**
     * Method Speed.
     * @param value represents the number of miliseconds
     * @param name The name that will show up in reports and animation
     */
    public TimeUnit(final long value, final String name)
    {
        super();
        this.value = value;
        this.name = name;
    }

    /**
     * @see nl.tudelft.simulation.dsol.experiment.TimeUnitInterface#getValue()
     */
    public long getValue()
    {
        return this.value;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.name;
    }

    /**
     * converts amount units to the units of the simulator
     * @param amount the amount to convert
     * @param units the units of the amount
     * @param simulator the simulator
     * @return double the amount in simulator units
     * @throws RemoteException on network exception
     */
    public static double convert(final double amount, final TimeUnitInterface units, final SimulatorInterface simulator)
            throws RemoteException
    {
        return TimeUnit.convert(amount, units, simulator.getReplication().getTreatment().getTimeUnit());
    }

    /**
     * converts amount units to the target units
     * @param amount the amount to convert
     * @param units the units of the amount
     * @param targetUnits the units to convert to
     * @return double the amount in simulator units
     */
    public static double convert(final double amount, final TimeUnitInterface units, final TimeUnitInterface targetUnits)
    {
        double fraction = (double) targetUnits.getValue() / (double) units.getValue();
        return amount / fraction;
    }
}