/*
 * @(#) ContextTransferable.java Oct 25, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.naming.context;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.naming.NamingException;

/**
 * The ContextTransferable class transfers keys in DropNDrag Operations.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version 1.2 2004-03-24
 * @since 1.5
 */
public class ContextTransferable implements Transferable
{
    /** the name under which the object can be found in the context */
    private String name = null;

    /**
     * constructs a new ContextTransferable
     * @param object the object to send
     * @throws NamingException whenever the object cannot be found in the context
     */
    public ContextTransferable(final Object object) throws NamingException
    {
        super();
        this.name = ContextUtil.resolveKey(object);
    }

    /**
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    public DataFlavor[] getTransferDataFlavors()
    {
        return new DataFlavor[]{DataFlavor.stringFlavor};
    }

    /**
     * @see java.awt.datatransfer.Transferable #isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    public boolean isDataFlavorSupported(final DataFlavor flavor)
    {
        if (flavor.equals(DataFlavor.stringFlavor))
        {
            return true;
        }
        return false;
    }

    /**
     * @see java.awt.datatransfer.Transferable #getTransferData(java.awt.datatransfer.DataFlavor)
     */
    public Object getTransferData(final DataFlavor flavor)
    {
        if (flavor.equals(DataFlavor.stringFlavor))
        {
            return this.name;
        }
        return null;
    }
}