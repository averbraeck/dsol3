package nl.tudelft.simulation.dsol.model.inputparameters;

import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * InputParameterMapDistContinuous is a InputParameterMap with a getDist() function. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public abstract class InputParameterMapDistContinuous extends InputParameterMap
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the random number stream to use for the distribution. */
    private StreamInterface stream;

    /**
     * Construct a new InputParameterMap.
     * @param key String; unique (within the parent's input parameter map) name of the new InputParameterMap
     * @param shortName String; concise description of the input parameter
     * @param description String; long description of the input parameter (may use HTML markup)
     * @param displayPriority double; sorting order when properties are displayed to the user
     */
    public InputParameterMapDistContinuous(final String key, final String shortName, final String description,
            final double displayPriority)
    {
        super(key, shortName, description, displayPriority);
    }

    /**
     * Return the distribution function corresponding to the set parameters. 
     * @return DistContinuous; the distribution function corresponding to the set parameters
     * @throws InputParameterException on error retrieving the values for the distribution
     */
    public abstract DistContinuous getDist() throws InputParameterException;

    /**
     * @return stream
     */
    public final StreamInterface getStream()
    {
        return this.stream;
    }

    /**
     * @param stream set stream
     */
    public final void setStream(final StreamInterface stream)
    {
        this.stream = stream;
    } 
    
}
