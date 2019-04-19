package nl.tudelft.simulation.naming.context;

import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

/**
 * A DnDSourceListener listens to context objects selected for Drag-and-Drop operations.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SimpleDnDSourceListener implements DragSourceListener
{
    /**
     * constructs a new SimpleDragSourceListener
     */
    public SimpleDnDSourceListener()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void dragEnter(final DragSourceDragEvent dsde)
    {
        DragSourceContext context = dsde.getDragSourceContext();
        context.setCursor(DragSource.DefaultCopyDrop);
    }

    /** {@inheritDoc} */
    @Override
    public void dragOver(final DragSourceDragEvent dsde)
    {
        DragSourceContext context = dsde.getDragSourceContext();
        context.setCursor(DragSource.DefaultCopyDrop);
    }

    /** {@inheritDoc} */
    @Override
    public void dropActionChanged(final DragSourceDragEvent dsde)
    {
        DragSourceContext context = dsde.getDragSourceContext();
        context.setCursor(DragSource.DefaultCopyDrop);
    }

    /** {@inheritDoc} */
    @Override
    public void dragExit(final DragSourceEvent dse)
    {
        DragSourceContext context = dse.getDragSourceContext();
        context.setCursor(DragSource.DefaultCopyDrop);
    }

    /** {@inheritDoc} */
    @Override
    public void dragDropEnd(final DragSourceDropEvent dsde)
    {
        DragSourceContext context = dsde.getDragSourceContext();
        context.setCursor(DragSource.DefaultCopyDrop);
    }
}
