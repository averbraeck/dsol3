package nl.tudelft.simulation.language.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

/**
 * JMultiToolbarFrame with a stack of ToolBars.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="mailto:royc@tbm.tudelft.nl">Roy Chin </a>
 */
public class JMultiToolbarFrame extends JFrame implements SwingConstants
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * The pane you can fiddle with -- to which content can be added, of which the layout can be changed, and so on. It's
     * returned by <code>getContentPane()</code>.
     */
    protected Container currentContentPane;

    /**
     * A stack of all the toolbar containers -- those "outside" of <code>currentContentPane</code>. The indices match those of
     * <code>toolbarStack</code>.
     */
    protected Vector<Container> containerStack;

    /**
     * A stack of all the toolbars. The indices match those of <code>
     * containerStack</code>.
     */
    protected Vector<JToolBar> toolbarStack;

    /**
     * The container of <code>currentContentPane</code>.
     */
    protected Container topContainer;

    /**
     * Whether the window is empty (i.e., has only an unedited, blank 'untitled' document in it).
     */
    protected boolean isEmpty;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Returns a new <code>JMultiToolbarFrame</code>.
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
     * @return isEmpty
     */
    public boolean isEmpty()
    {
        return this.isEmpty;
    }

    /**
     * Sets whether the window is empty
     * @param empty boolean; Empty frame or not
     */
    public void setEmpty(final boolean empty)
    {
        this.isEmpty = empty;
    }

    /**
     * Overrides <code>JFrame.getContentPane()</code>. The pane returned should always be "inside" the toolbars.
     * @return The content pane.
     */
    @Override
    public Container getContentPane()
    {
        return this.currentContentPane;
    }

    /**
     * Adds another toolbar to the specified side of the frame. Any attempt to add a toolbar that has already been added is
     * ignored.
     * @param bar JToolBar; The toolbar to add.
     * @param align int; One of &lt;TT&gt;SwingConstants.TOP&lt;/TT&gt;,&lt;TT&gt;SwingConstants.BOTTOM&lt;/TT&gt;,
     *            &lt;TT&gt;SwingConstants.LEFT&lt;/TT&gt;, or <code>SwingConstants.RIGHT</code>.
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
             * The current content pane is lifted from the top container and another container, containing the new toolbar, is
             * inserted between them. The new container and new toolbar are added to the respective stacks, and the new
             * container becomes the new top container. Note that the original content pane (super.getContentPane()) does act as
             * a top container, but never contains a toolbar and is never added to the container stack. The current content pane
             * always stays on top.
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
     * Removes the specified toolbar. Any attempt to remove a toolbar that has never been added is ignored.
     * @param bar JToolBar; Toolbar to remove
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
            }
            else
            {
                below = super.getContentPane();
            }

            if (position != (this.containerStack.size() - 1))
            {
                above = this.containerStack.elementAt(position + 1);
            }
            else
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
