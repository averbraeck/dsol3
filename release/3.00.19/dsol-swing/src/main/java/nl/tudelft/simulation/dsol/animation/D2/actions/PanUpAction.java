package nl.tudelft.simulation.dsol.animation.D2.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import nl.tudelft.simulation.dsol.animation.D2.GridPanel;
import nl.tudelft.simulation.language.io.URLResource;

/**
 * The PanLeftAction.java (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of
 * Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:37:48 $
 * @author <a href="mailto:nlang@fbk.eur.nl">Niels Lang </a>, <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class PanUpAction extends AbstractAction
{
    /** */
    private static final long serialVersionUID = 20140909L;

    /** target of the gridpanel */
    private GridPanel target = null;

    /**
     * constructs a new ZoomIn
     * @param target the target
     */
    public PanUpAction(final GridPanel target)
    {
        super("PanUp");
        this.target = target;
        this.putValue(Action.SMALL_ICON,
                new ImageIcon(URLResource.getResource("/toolbarButtonGraphics/navigation/Up16.gif")));
        this.setEnabled(true);
    }

    /**
     * @see java.awt.event.ActionListener #actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent actionEvent)
    {
        this.target.pan(GridPanel.UP, 0.1);
        this.target.requestFocus();
    }
}