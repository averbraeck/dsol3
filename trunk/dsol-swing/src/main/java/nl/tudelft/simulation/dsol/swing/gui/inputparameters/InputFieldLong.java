package nl.tudelft.simulation.dsol.swing.gui.inputparameters;

import javax.swing.JPanel;

import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterLong;

/**
 * Swing InputField for Long. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class InputFieldLong extends InputFieldString
{
    /**
     * Create a long field on the screen.
     * @param panel panel to add the field to
     * @param parameter the parameter
     */
    public InputFieldLong(final JPanel panel, final InputParameterLong parameter)
    {
        super(panel, parameter);
    }

    /** {@inheritDoc} */
    @Override
    public InputParameterLong getParameter()
    {
        return (InputParameterLong) super.getParameter();
    }

    /** @return the long value of the field in the gui. */
    public long getLongValue()
    {
        return Long.valueOf(this.textField.getText());
    }

}
