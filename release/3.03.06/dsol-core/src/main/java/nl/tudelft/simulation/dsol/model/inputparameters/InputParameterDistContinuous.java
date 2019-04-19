package nl.tudelft.simulation.dsol.model.inputparameters;

import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * InputParameterDistContinuous provides a choice for a continuous distribution.<br>
 * <br>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class InputParameterDistContinuous extends AbstractInputParameter<DistContinuous, DistContinuous>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the random number stream to use for the distribution. */
    private StreamInterface stream;

    /**
     * Construct a new InputParameterDistContinuous.
     * @param key String; unique (within the parent's input parameter map) name of the new InputParameterDistContinuous
     * @param shortName String; concise description of the input parameter
     * @param description String; long description of the input parameter (may use HTML markup)
     * @param stream StreamInterface; the random number stream to use for the chosen distribution
     * @param defaultValue boolean; the default value of this input parameter
     * @param displayPriority double; sorting order when properties are displayed to the user
     * @throws InputParameterException in case the default value is not part of the list
     */
    public InputParameterDistContinuous(final String key, final String shortName, final String description,
            final StreamInterface stream, final DistContinuous defaultValue, final double displayPriority)
            throws InputParameterException
    {
        super(key, shortName, description, defaultValue, displayPriority);
        this.stream = stream;
    }

    /** {@inheritDoc} */
    @Override
    public DistContinuous getCalculatedValue() throws InputParameterException
    {
        return getValue();
    }

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

    /**
     * Set the value of the distribution.
     * @param dist the distribution to set the value to
     * @throws InputParameterException when this InputParameter is read-only, or newValue is not valid
     */
    public final void setDistValue(final DistContinuous dist) throws InputParameterException
    {
        setValue(dist);
    }

    /** {@inheritDoc} */
    @Override
    public InputParameterDistContinuous clone() throws CloneNotSupportedException
    {
        return (InputParameterDistContinuous) super.clone();
    }
}
