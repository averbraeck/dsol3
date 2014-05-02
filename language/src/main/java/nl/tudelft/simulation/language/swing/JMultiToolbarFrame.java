/*
 * @(#) JMultiToolbarFrame.java 20-jul-2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.language.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

/**
 * JMultiToolbarFrame.java .
 * <p>
 * Copyright (c) 2002-2009 Delft University of Technology, Jaffalaan 5, 2628 BX
 * Delft, the Netherlands. All rights reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/">
 * www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is"
 * and any express or implied warranties, including, but not limited to, the
 * implied warranties of merchantability and fitness for a particular purpose
 * are disclaimed. In no event shall the copyright holder or contributors be
 * liable for any direct, indirect, incidental, special, exemplary, or
 * consequential damages (including, but not limited to, procurement of
 * substitute goods or services; loss of use, data, or profits; or business
 * interruption) however caused and on any theory of liability, whether in
 * contract, strict liability, or tort (including negligence or otherwise)
 * arising in any way out of the use of this software, even if advised of the
 * possibility of such damage.
 * 
 * @version $Revision: 1.2 $ $Date: 2009/10/21 07:32:42 $
 * @author <a href="mailto:royc@tbm.tudelft.nl">Roy Chin </a>
 */
public class JMultiToolbarFrame extends JFrame implements SwingConstants
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;
    
    /**
     * The pane you can fiddle with -- to which content can be added, of which
     * the layout can be changed, and so on. It's returned by <TT>
     * getContentPane()</TT>.
     */
    protected Container currentContentPane;

    /**
     * A stack of all the toolbar containers -- those "outside" of <TT>
     * currentContentPane</TT>.
     * The indices match those of <TT>toolbarStack
     * </TT>.
     */
    protected Vector<Container> containerStack;

    /**
     * A stack of all the toolbars. The indices match those of <TT>
     * containerStack</TT>.
     */
    protected Vector<JToolBar> toolbarStack;

    /**
     * The container of <TT>currentContentPane</TT>.
     */
    protected Container topContainer;

    /**
     * Whether the window is empty (i.e., has only an unedited, blank 'untitled'
     * document in it).
     */
    protected boolean isEmpty;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Returns a new <TT>JMultiToolbarFrame</TT>.
     */
    public JMultiToolbarFrame()
    {
        this.isEmpty = true;
        this.containerStack = new Vector<Container>();
        this.toolbarStack = new Vector<JToolBar>();
        this.currentContentPane = new JPanel(new BorderLayout());
        this.topContainer = super.getContentPane();
        this.topContainer.add(this.currentContentPane, BorderLayout.CENTER);
    }

    // ------------------------------------------------------------
    // Public instance methods
    // ------------------------------------------------------------

    /**
     * Returns whether the window is empty.
     * 
     * @return isEmpty
     */
    public boolean isEmpty()
    {
        return this.isEmpty;
    }

    /**
     * Sets whether the window is empty
     * 
     * @param empty Empty frame or not
     */
    public void setEmpty(final boolean empty)
    {
        this.isEmpty = empty;
    }

    /**
     * Overrides <TT>JFrame.getContentPane()</TT>. The pane returned should
     * always be "inside" the toolbars.
     * 
     * @return The content pane.
     */
    @Override
    public Container getContentPane()
    {
        return this.currentContentPane;
    }

    /**
     * Adds another toolbar to the specified side of the frame. Any attempt to
     * add a toolbar that has already been added is ignored.
     * 
     * @param bar The toolbar to add.
     * @param align One of <TT>SwingConstants.TOP</TT>,<TT>
     *            SwingConstants.BOTTOM</TT>,<TT>SwingConstants.LEFT</TT>,
     *            and <TT>SwingConstants.RIGHT</TT>.
     */
    public void addJToolBar(final JToolBar bar, final int align)
    {
        if (!this.toolbarStack.contains(bar))
        {
            String border;
            int orientation;
            switch (align)
            {
            case TOP:
                border = BorderLayout.NORTH;
                orientation = HORIZONTAL;
                break;
            case BOTTOM:
                border = BorderLayout.SOUTH;
                orientation = HORIZONTAL;
                break;
            case LEFT:
                border = BorderLayout.WEST;
                orientation = VERTICAL;
                break;
            case RIGHT:
                border = BorderLayout.EAST;
                orientation = VERTICAL;
                break;
            default:
                throw new IllegalArgumentException(
                        "Alignment argument passed to JMultiToolbarFrame.addJToolBar is not one of SwingConstants.TOP/BOTTOM/LEFT/RIGHT.");
            }

            /*
             * The current content pane is lifted from the top container and
             * another container, containing the new toolbar, is inserted
             * between them. The new container and new toolbar are added to the
             * respective stacks, and the new container becomes the new top
             * container. Note that the original content pane
             * (super.getContentPane()) does act as a top container, but never
             * contains a toolbar and is never added to the container stack. The
             * current content pane always stays on top.
             */
            this.topContainer.remove(this.currentContentPane);
            Container newContainer = new JPanel(new BorderLayout());
            newContainer.add(bar, border);
            newContainer.add(this.currentContentPane, BorderLayout.CENTER);
            this.topContainer.add(newContainer);
            this.topContainer = newContainer;
            this.containerStack.addElement(newContainer);
            bar.setOrientation(orientation);
            this.toolbarStack.addElement(bar);
        }
    }

    /**
     * Removes the specified toolbar. Any attempt to remove a toolbar that has
     * never been added is ignored.
     * 
     * @param bar Toolbar to remove
     */
    public void removeJToolBar(final JToolBar bar)
    {
        if (this.toolbarStack.contains(bar))
        {
            Container current;
            Container above;
            Container below;
            int position = this.toolbarStack.indexOf(bar);
            if (position != 0)
            {
                below = this.containerStack.elementAt(position - 1);
            } else
            {
                below = super.getContentPane();
            }

            if (position != (this.containerStack.size() - 1))
            {
                above = this.containerStack.elementAt(position + 1);
            } else
            {
                above = this.currentContentPane;
            }

            current = this.containerStack.elementAt(position);
            below.remove(current);
            current.remove(above);
            below.add(above, BorderLayout.CENTER);
        }
    }
}
