package nl.tudelft.simulation.logger;

/**
 * Predefined categories for Category logging. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public final class Cat
{
    /** */
    private Cat()
    {
        // Utility class
    }

    /** DSOL project. Category is used for dsol-base, dsol-core, dsol-demo, dsol-interpreter, dsol-introspection. */
    public static final LogCategory DSOL = new LogCategory("DSOL");
    
    /** DSOL-NAMING project. */
    public static final LogCategory NAMING = new LogCategory("NAMING");

    /** DSOL-EVENT project. */
    public static final LogCategory EVENT = new LogCategory("ROAD");

    /** DSOL-SWING project. */
    public static final LogCategory SWING = new LogCategory("SWING");

    /** DSOL-WEB project. */
    public static final LogCategory WEB = new LogCategory("WEB");

    /** DSOL-HLA project. */
    public static final LogCategory HLA = new LogCategory("HLA");

}

