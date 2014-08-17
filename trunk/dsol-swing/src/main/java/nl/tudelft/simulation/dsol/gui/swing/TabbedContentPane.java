/*
 * @(#)TabbedContentPane.java May 4, 2014
 * 
 * Copyright (c) 2014 Delft University of Technology Jaffalaan 5, 
 * 2628 BX Delft, the Netherlands All rights reserved.
 * The code is published under the BSD License
 */
package nl.tudelft.simulation.dsol.gui.swing;

import java.awt.Component;

import javax.swing.JTabbedPane;

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
public class TabbedContentPane extends JTabbedPane
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for TabbedContentPane.
     */
    public TabbedContentPane()
    {
        super();
    }

    /**
     * Constructor for TabbedContentPane.
     * @param tabPlacement
     */
    public TabbedContentPane(int tabPlacement)
    {
        super(tabPlacement);
    }

    /**
     * Constructor for TabbedContentPane.
     * @param tabPlacement
     * @param tabLayoutPolicy
     */
    public TabbedContentPane(int tabPlacement, int tabLayoutPolicy)
    {
        super(tabPlacement, tabLayoutPolicy);
    }

    /**
     * Method addTab.
     * @param position
     * @param title
     * @param component
     * @throws IndexOutOfBoundsException
     */
    public void addTab(int position, String title, Component component) throws IndexOutOfBoundsException
    {
        component.setName(title);
        this.add(component, position);
    }
}