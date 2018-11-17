package nl.tudelft.simulation.dsol.swing.gui.inputparameters;

import javax.swing.JPanel;

import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterDouble;

/**
 * Swing InputField for Double. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class InputFieldDouble extends InputFieldString
{
    /**
     * Create a double field on the screen.
     * @param panel panel to add the field to
     * @param parameter the parameter
     */
    public InputFieldDouble(final JPanel panel, final InputParameterDouble parameter)
    {
        super(panel, parameter);
    }

    /** {@inheritDoc} */
    @Override
    public InputParameterDouble getParameter()
    {
        return (InputParameterDouble) super.getParameter();
    }

    /** @return the double value of the field in the gui. */
    public double getDoubleValue()
    {
        return Double.valueOf(this.textField.getText());
    }

}
