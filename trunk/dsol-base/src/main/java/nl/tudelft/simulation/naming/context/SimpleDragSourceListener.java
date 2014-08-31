package nl.tudelft.simulation.naming.context;

import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

/**
 * A DragSourceListener listens to context objects selected for DndDrop operations.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 2004-03-24
 * @since 1.5
 */
public class SimpleDragSourceListener implements DragSourceListener
{
    /**
     * constructs a new SimpleDragSourceListener
     */
    public SimpleDragSourceListener()
    {
        super();
    }

    /** {@inheritDoc} */
    public void dragEnter(final DragSourceDragEvent dsde)
    {
        DragSourceContext context = dsde.getDragSourceContext();
        context.setCursor(DragSource.DefaultCopyDrop);
    }

    /** {@inheritDoc} */
    public void dragOver(final DragSourceDragEvent dsde)
    {
        DragSourceContext context = dsde.getDragSourceContext();
        context.setCursor(DragSource.DefaultCopyDrop);
    }

    /** {@inheritDoc} */
    public void dropActionChanged(final DragSourceDragEvent dsde)
    {
        DragSourceContext context = dsde.getDragSourceContext();
        context.setCursor(DragSource.DefaultCopyDrop);
    }

    /** {@inheritDoc} */
    public void dragExit(final DragSourceEvent dse)
    {
        DragSourceContext context = dse.getDragSourceContext();
        context.setCursor(DragSource.DefaultCopyDrop);
    }

    /** {@inheritDoc} */
    public void dragDropEnd(final DragSourceDropEvent dsde)
    {
        DragSourceContext context = dsde.getDragSourceContext();
        context.setCursor(DragSource.DefaultCopyDrop);
    }
}
