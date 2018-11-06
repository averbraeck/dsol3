package nl.tudelft.simulation.dsol.model.inputparameters;

/**
 * InputParameterFloat.java. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class InputParameterFloat extends AbstractInputParameter<Float>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** Format string to display the value of the input parameter. */
    private String format = "%d";

    /** The minimum value of the input parameter. */
    private float minimumValue = -Float.MAX_VALUE;

    /** The maximum value of the input parameter. */
    private float maximumValue = Float.MAX_VALUE;

    /**
     * Construct a new InputParameterFloat.
     * @param key String; unique (within the parent's input parameter map) name of the new InputParameterFloat
     * @param shortName String; concise description of the input parameter
     * @param description String; float description of the input parameter (may use HTML markup)
     * @param defaultValue float; the default value of this input parameter
     * @param displayPriority double; sorting order when properties are displayed to the user
     */
    public InputParameterFloat(final String key, final String shortName, final String description, final float defaultValue,
            final double displayPriority)
    {
        super(key, shortName, description, defaultValue, displayPriority);
    }

    /**
     * Construct a new InputParameterFloat.
     * @param key String; unique (within the parent's input parameter map) name of the new InputParameterFloat
     * @param shortName String; concise description of the input parameter
     * @param description String; float description of the input parameter (may use HTML markup)
     * @param defaultValue float; the default value of this input parameter
     * @param minimumValue float; the lowest value allowed as input
     * @param maximumValue float; the highest value allowed as input
     * @param format the format to use in displaying the float
     * @param displayPriority double; sorting order when properties are displayed to the user
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public InputParameterFloat(final String key, final String shortName, final String description, final float defaultValue,
            final float minimumValue, final float maximumValue, final String format, final double displayPriority)
    {
        super(key, shortName, description, defaultValue, displayPriority);
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.format = format;
    }

    /** {@inheritDoc} */
    @Override
    public final void setValue(final Float newValue) throws InputParameterException
    {
        if (this.minimumValue > newValue || this.maximumValue < newValue)
        {
            throw new InputParameterException("new value for InputParameterFloat with key " + getKey() + " with value "
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
     * @param format set format
     */
    public final void setFormat(final String format)
    {
        this.format = format;
    }

    /**
     * @return minimumValue
     */
    public final Float getMinimumValue()
    {
        return this.minimumValue;
    }

    /**
     * @param minimumValue set minimumValue
     */
    public final void setMinimumValue(final float minimumValue)
    {
        this.minimumValue = minimumValue;
    }

    /**
     * @return maximumValue
     */
    public final Float getMaximumValue()
    {
        return this.maximumValue;
    }

    /**
     * @param maximumValue set maximumValue
     */
    public final void setMaximumValue(final float maximumValue)
    {
        this.maximumValue = maximumValue;
    }

    /** {@inheritDoc} */
    @Override
    public InputParameterFloat clone() throws CloneNotSupportedException
    {
        return (InputParameterFloat) super.clone();
    }

}
