/*
 * @(#)TimeUnitInterface.java Feb 1, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;

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
public interface TimeUnitInterface extends Serializable
{

    /** UNIT reflects the non actual time related unit */
    TimeUnitInterface UNIT = new TimeUnit(1L, "units");

    /** MILLISECOND reflects the MILLISECONDS */
    TimeUnitInterface MILLISECOND = new TimeUnit(1L, "milliseconds");

    /** SECOND reflects the SECOND */
    TimeUnitInterface SECOND = new TimeUnit(1000L * TimeUnitInterface.MILLISECOND.getValue(), "seconds");

    /** MINUTE reflects the MINUTE */
    TimeUnitInterface MINUTE = new TimeUnit(60L * TimeUnitInterface.SECOND.getValue(), "minutes");

    /** HOUR reflects the HOUR */
    TimeUnitInterface HOUR = new TimeUnit(60L * TimeUnitInterface.MINUTE.getValue(), "hours");

    /** DAY reflects the DAY */
    TimeUnitInterface DAY = new TimeUnit(24L * TimeUnitInterface.HOUR.getValue(), "days");

    /** WEEK reflects the WEEK */
    TimeUnitInterface WEEK = new TimeUnit(7L * TimeUnitInterface.DAY.getValue(), "weeks");

    /**
     * Method getValue.This method returns the number of timeunits relative to milliseconds
     * @return long
     */
    long getValue();
}