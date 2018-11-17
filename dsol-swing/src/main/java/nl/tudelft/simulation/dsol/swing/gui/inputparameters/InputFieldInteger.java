package nl.tudelft.simulation.dsol.swing.gui.inputparameters;

import javax.swing.JPanel;

import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterInteger;

/**
 * Swing InputField for Integer. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class InputFieldInteger extends InputFieldString
{
    /**
     * Create a integer field on the screen.
     * @param panel panel to add the field to
     * @param parameter the parameter
     */
    public InputFieldInteger(final JPanel panel, final InputParameterInteger parameter)
    {
        super(panel, parameter);
    }

    /** {@inheritDoc} */
    @Override
    public InputParameterInteger getParameter()
    {
        return (InputParameterInteger) super.getParameter();
    }

    /** @return the int value of the field in the gui. */
    public int getIntValue()
    {
        return Integer.valueOf(this.textField.getText());
    }

}
