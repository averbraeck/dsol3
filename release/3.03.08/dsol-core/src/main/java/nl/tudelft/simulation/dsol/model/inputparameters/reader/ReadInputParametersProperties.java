package nl.tudelft.simulation.dsol.model.inputparameters.reader;

/**
 * Read InputParameters from a Properties file. The format is one parameter per line. keys for the sub-parameters are indicated
 * with a dot notation. The general format is key=value. The value for an integer value called 'nrServers' in the root category:
 * 
 * <pre>
 * 
 * nrServers = 2
 * </pre>
 * 
 * The key for a Duration parameter called 'serviceTime' in the category 'server':<br>
 * 
 * <pre>
 * server.serviceTime.
 * </pre>
 * 
 * <br>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public final class ReadInputParametersProperties
{
    /** */
    private ReadInputParametersProperties()
    {
        // utility class
    }

    /**
     * Read the input parameters from a properties file. Note: the input parameter map given as input will be used to store the
     * values read.
     * @param filename String; the file to read (URLResource wil be used to resolve the file)
     * @param InputParameterMap; the map of input parameters to be read
     */
}
