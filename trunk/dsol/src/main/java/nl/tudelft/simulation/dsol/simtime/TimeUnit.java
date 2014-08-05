package nl.tudelft.simulation.dsol.simtime;

/**
 * <p>
 * Copyright (c) 2002-2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Jul 25, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public enum TimeUnit {
    /** UNIT reflects the non actual time related unit */
    UNIT(1.0, "units", "u"),

    /** MILLISECOND reflects the MILLISECONDS */
    MILLISECOND(1.0, "milliseconds", "ms"),

    /** SECOND reflects the SECOND */
    SECOND(1000.0, "seconds", "s"),

    /** MINUTE reflects the MINUTE */
    MINUTE(60000.0, "minutes", "m"),

    /** HOUR reflects the HOUR */
    HOUR(60000.0 * 60.0, "hours", "h"),

    /** DAY reflects the DAY */
    DAY(24.0 * 60000.0 * 60.0, "days", "d"),

    /** WEEK reflects the WEEK */
    WEEK(7.0 * 24.0 * 60000.0 * 60.0, "weeks", "w"),

    /** YEAR reflects the YEAR */
    YEAR(365.0 * 24.0 * 60000.0 * 60.0, "years", "y");

    /** conversion factor to msec */
    private final double factor;

    /** name for reporting */
    private final String name;

    /** abbreviation for reporting */
    private final String abbreviation;

    /**
     * @param factor
     * @param name
     * @param abbreviation
     */
    private TimeUnit(final double factor, final String name, final String abbreviation)
    {
        this.factor = factor;
        this.name = name;
        this.abbreviation = abbreviation;
    }

    /**
     * @return the factor
     */
    public double getFactor()
    {
        return this.factor;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return the abbreviation
     */
    public String getAbbreviation()
    {
        return this.abbreviation;
    }

}
