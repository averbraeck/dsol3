/*
 * @(#)DSOLApplication.java May 4, 2014
 * 
 * Copyright (c) 2014 Delft University of Technology Jaffalaan 5, 
 * 2628 BX Delft, the Netherlands All rights reserved.
 * The code is published under the BSD License
 */
package nl.tudelft.simulation.dsol.gui.swing;

import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.JFrame;

/**
 * <br>
 * Copyright (c) 2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * The MEDLABS project (Modeling Epidemic Disease with Large-scale Agent-Based Simulation) is aimed at providing policy
 * analysis tools to predict and help contain the spread of epidemics. It makes use of the DSOL simulation engine and
 * the agent-based modeling formalism. See for project information <a href="http://www.simulation.tudelft.nl/">
 * www.simulation.tudelft.nl</a>. The project is a co-operation between TU Delft, Systems Engineering and Simulation
 * Department (Netherlands) and NUDT, Simulation Engineering Department (China). This software is licensed under the BSD
 * license. See license.txt in the main project.
 * @version May 4, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck </a>
 */
public class DSOLApplication extends JFrame
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for DSOLApplication.
     * @param title
     * @param panel
     * @throws HeadlessException
     */
    public DSOLApplication(String title, DSOLPanel panel)
    {
        super(title);
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }
}