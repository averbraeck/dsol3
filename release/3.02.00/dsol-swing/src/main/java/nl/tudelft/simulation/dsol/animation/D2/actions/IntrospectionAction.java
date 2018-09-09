package nl.tudelft.simulation.dsol.animation.D2.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import nl.tudelft.simulation.introspection.gui.IntroSpectionDialog;

/**
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:37:48 $
 * @since 1.5
 */
public class IntrospectionAction extends AbstractAction
{
    /** */
    private static final long serialVersionUID = 20140909L;

    /** the target to introspect */
    private Object target = null;

    /**
     * constructs a new IntrospectionAction
     * @param target the target to introspect
     */
    public IntrospectionAction(final Object target)
    {
        super(target.toString());
        this.target = target;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        new IntroSpectionDialog(this.target);
    }
}