package nl.tudelft.simulation.dsol.animation.D2;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version May 20, 2015
 */
public class BorderPanel extends JPanel
{
    /** */
    private static final long serialVersionUID = 20150520L;

    /**
     * Create a "wrapper" panel that gives access to the border layout fields. The animation will be placed in the
     * CENTER.
     */
    public BorderPanel()
    {
        super(new BorderLayout());
    }

    /**
     * Warning: do not touch the center.
     * @return the border panel that gives access to top, bottom, left and right panels.
     */
    public final JPanel getBorderPanel()
    {
        return this;
    }
}
