package nl.tudelft.simulation.naming.context;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.naming.NamingException;

/**
 * The ContextTransferable class transfers keys in DropNDrag Operations.
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 2004-03-24
 * @since 1.5
 */
public class ContextTransferable implements Transferable
{
    /** the name under which the object can be found in the context. */
    private String name = null;

    /**
     * constructs a new ContextTransferable.
     * @param object the object to send
     * @throws NamingException whenever the object cannot be found in the context
     */
    public ContextTransferable(final Object object) throws NamingException
    {
        super();
        this.name = ContextUtil.resolveKey(object);
    }

    /** {@inheritDoc} */
    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return new DataFlavor[]{DataFlavor.stringFlavor};
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
