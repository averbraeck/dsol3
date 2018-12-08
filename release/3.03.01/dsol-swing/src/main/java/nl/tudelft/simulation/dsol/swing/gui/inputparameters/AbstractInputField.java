package nl.tudelft.simulation.dsol.swing.gui.inputparameters;

import nl.tudelft.simulation.dsol.model.inputparameters.InputParameter;

/**
 * Abstract InputField to avoid code duplication. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public abstract class AbstractInputField implements InputField
{
    /** key for the field. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final String key;

    /** field for the input parameter. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final InputParameter<?> parameter;

    /**
     * Abstract constructor for the field on the screen.
     * @param parameter the parameter
     */
    public AbstractInputField(final InputParameter<?> parameter)
    {
        this.parameter = parameter;
        this.key = parameter.getKey();
    }

    /** {@inheritDoc} */
    @Override
    public final String getKey()
    {
        return this.key;
    }

    /** {@inheritDoc} */
    @Override
    public InputParameter<?> getParameter()
    {
        return this.parameter;
    }

}
