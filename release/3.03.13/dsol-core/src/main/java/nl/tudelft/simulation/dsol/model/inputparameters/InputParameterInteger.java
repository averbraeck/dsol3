package nl.tudelft.simulation.dsol.model.inputparameters;

/**
 * InputParameterInteger.java. <br>
 * <br>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class InputParameterInteger extends AbstractInputParameter<Integer, Integer>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** Format string to display the value of the input parameter. */
    private String format = "%d";

    /** The minimum value of the input parameter. */
    private int minimumValue = -Integer.MAX_VALUE;

    /** The maximum value of the input parameter. */
    private int maximumValue = Integer.MAX_VALUE;

    /**
     * Construct a new InputParameterInteger.
     * @param key String; unique (within the parent's input parameter map) name of the new InputParameterInteger
     * @param shortName String; concise description of the input parameter
     * @param description String; long description of the input parameter (may use HTML markup)
     * @param defaultValue int; the default value of this input parameter
     * @param displayPriority double; sorting order when properties are displayed to the user
     */
    public InputParameterInteger(final String key, final String shortName, final String description, final int defaultValue,
            final double displayPriority)
    {
        super(key, shortName, description, defaultValue, displayPriority);
    }

    /**
     * Construct a new InputParameterInteger.
     * @param key String; unique (within the parent's input parameter map) name of the new InputParameterInteger
     * @param shortName String; concise description of the input parameter
     * @param description String; long description of the input parameter (may use HTML markup)
     * @param defaultValue int; the default value of this input parameter
     * @param minimumValue int; the lowest value allowed as input
     * @param maximumValue int; the highest value allowed as input
     * @param format String; the format to use in displaying the integer
     * @param displayPriority double; sorting order when properties are displayed to the user
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public InputParameterInteger(final String key, final String shortName, final String description, final int defaultValue,
            final int minimumValue, final int maximumValue, final String format, final double displayPriority)
    {
        super(key, shortName, description, defaultValue, displayPriority);
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.format = format;
    }

    /** {@inheritDoc} */
    @Override
    public final Integer getCalculatedValue()
    {
        return getValue();
    }

    /**
     * Check and set the typed value, and call super.setValue to make the actual allocation.
     * @param newValue int; the new value for the input parameter
     * @throws InputParameterException when this InputParameter is read-only, or newValue is not valid
     */
    public final void setIntValue(final int newValue) throws InputParameterException
    {
        if (this.minimumValue > newValue || this.maximumValue < newValue)
        {
            throw new InputParameterException("new value for InputParameterInteger with key " + getKey() + " with value "
                    + newValue + " is out of valid range [" + this.minimumValue + ".." + this.maximumValue + "]");
        }
        super.setValue(newValue);
    }

    /**
     * @return format
     */
    public final String getFormat()
    {
        return this.format;
    }

    /**
     * @param format String; set format
     */
    public final void setFormat(final String format)
    {
        this.format = format;
    }

    /**
     * @return minimumValue
     */
    public final Integer getMinimumValue()
    {
        return this.minimumValue;
    }

    /**
     * @param minimumValue int; set minimumValue
     */
    public final void setMinimumValue(final int minimumValue)
    {
        this.minimumValue = minimumValue;
    }

    /**
     * @return maximumValue
     */
    public final Integer getMaximumValue()
    {
        return this.maximumValue;
    }

    /**
     * @param maximumValue int; set maximumValue
     */
    public final void setMaximumValue(final int maximumValue)
    {
        this.maximumValue = maximumValue;
    }

    /** {@inheritDoc} */
    @Override
    public InputParameterInteger clone() throws CloneNotSupportedException
    {
        return (InputParameterInteger) super.clone();
    }

}
