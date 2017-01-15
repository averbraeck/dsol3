package nl.tudelft.simulation.dsol.animation.D2.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import nl.tudelft.simulation.dsol.animation.D2.GridPanel;

/**
 * @author peter
 */
public class ShowGridAction extends AbstractAction
{
    /** */
    private static final long serialVersionUID = 20140909L;

    /** target of the gridpanel */
    private GridPanel target = null;

    /**
     * constructs a new AddRowAction
     * @param target the target
     */
    public ShowGridAction(final GridPanel target)
    {
        super("ShowGrid");
        this.target = target;
        this.setEnabled(true);
    }

    /**
     * @see java.awt.event.ActionListener #actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent actionEvent)
    {
        this.target.showGrid(!this.target.isShowGrid());
        this.target.requestFocus();
    }
}