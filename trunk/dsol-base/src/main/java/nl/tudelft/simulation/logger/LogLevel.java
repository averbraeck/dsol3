package nl.tudelft.simulation.logger;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;

/**
 * Easy way to set the LogLevel for the entire simulation. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information
 * <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The source code and
 * binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public final class LogLevel
{
    /** */
    private LogLevel()
    {
        // utility class
    }

    /**
     * Set a new logging level for the default logger.
     * @param level the new logging level to use
     */
    public static void setDefaultLevel(final Level level)
    {
        Configurator.defaultConfig().level(level).activate();
    }

    /**
     * Set a new logging level for the current logger.
     * @param level the new logging level to use
     */
    public static void setCurrentLevel(final Level level)
    {
        Configurator.currentConfig().level(level).activate();
    }

}
