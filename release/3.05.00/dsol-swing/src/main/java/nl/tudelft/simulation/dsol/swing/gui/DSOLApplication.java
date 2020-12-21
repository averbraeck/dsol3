package nl.tudelft.simulation.dsol.swing.gui;

import java.awt.Frame;

import javax.swing.JFrame;

/**
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck </a>
 */
public class DSOLApplication extends JFrame
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for DSOLApplication.
     * @param title String; the title in the top bar
     * @param panel DSOLPanel; the panel to put in the frame
     */
    public DSOLApplication(final String title, final DSOLPanel panel)
    {
        super(title);
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }
}
