package nl.tudelft.simulation.naming.context.tree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.naming.NamingException;

/**
 * The ContextTransferable class transfers keys in drag-and-drop (DnD) operations.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class ContextTransferable implements Transferable
{
    /** the name under which the object can be found in the context. */
    private String name = null;

    /**
     * constructs a new ContextTransferable.
     * @param object Object; the object to send
     * @throws NamingException whenever the object cannot be found in the context
     */
    public ContextTransferable(final Object object) throws NamingException
    {
        super();
        // TODO this.name = InitialEventContext.instantiate().resolveKey(object);
    }

    /** {@inheritDoc} */
    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return new DataFlavor[] {DataFlavor.stringFlavor};
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDataFlavorSupported(final DataFlavor flavor)
    {
        if (flavor.equals(DataFlavor.stringFlavor))
        {
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public Object getTransferData(final DataFlavor flavor)
    {
        if (flavor.equals(DataFlavor.stringFlavor))
        {
            return this.name;
        }
        return null;
    }
}
