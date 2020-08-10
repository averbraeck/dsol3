package nl.tudelft.simulation.dsol.model.inputparameters;

/**
 * InputParameterBoolean.java. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class InputParameterString extends AbstractInputParameter<String>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Construct a new InputParameterString.
     * @param key String; unique (within the parent's input parameter map) name of the new InputParameterString
     * @param shortName String; concise description of the input parameter
     * @param description String; long description of the input parameter (may use HTML markup)
     * @param defaultValue boolean; the default value of this input parameter
     * @param displayPriority double; sorting order when properties are displayed to the user
     */
    public InputParameterString(final String key, final String shortName, final String description, final String defaultValue,
            final double displayPriority)
    {
        super(key, shortName, description, defaultValue, displayPriority);
    }

    /** {@inheritDoc} */
    @Override
    public InputParameterString clone() throws CloneNotSupportedException
    {
        return (InputParameterString) super.clone();
    }
}