package nl.tudelft.simulation.dsol.swing.gui;

import java.awt.Component;

import javax.swing.JTabbedPane;

/**
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck </a>
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
     * @param tabPlacement int; the position in the tab
     */
    public TabbedContentPane(int tabPlacement)
    {
        super(tabPlacement);
    }

    /**
     * Constructor for TabbedContentPane.
     * @param tabPlacement int; the position in the tab
     * @param tabLayoutPolicy int; the policy for laying out tabs when all tabs will not fit on one run
     */
    public TabbedContentPane(int tabPlacement, int tabLayoutPolicy)
    {
        super(tabPlacement, tabLayoutPolicy);
    }

    /**
     * Method addTab.
     * @param position int; the position in the tabs
     * @param title String; the title of the tab
     * @param component Component; the swing component to link to the tab
     * @throws IndexOutOfBoundsException when the tab number smaller than 0 or larger than size
     */
    public void addTab(int position, String title, Component component) throws IndexOutOfBoundsException
    {
        component.setName(title);
        this.add(component, position);
    }
}
