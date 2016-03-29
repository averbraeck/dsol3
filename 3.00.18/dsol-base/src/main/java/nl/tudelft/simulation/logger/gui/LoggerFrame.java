package nl.tudelft.simulation.logger.gui;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The LoggerFrame <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:39:18 $
 * @author Peter Jacobs, Niels Lang, Alexander Verbraeck
 */
public class LoggerFrame extends JFrame
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the Logger. */
    private Logger logger = null;

    /**
     * constructs a new LoggerFrame.
     * @param logger the logger to see
     */
    public LoggerFrame(final Logger logger)
    {
        super("Logger: " + logger.getName());
        this.logger = logger;
        this.initialize();
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
     * initializes the Loggerframe
     */
    private void initialize()
    {
        LogPanel logPanel = new LogPanel(this.logger);
        this.setContentPane(logPanel);
    }

    /** {@inheritDoc} */
    @Override
    public void dispose()
    {
        LogPanel content = (LogPanel) this.getContentPane();
        content.finalize();
    }
}
