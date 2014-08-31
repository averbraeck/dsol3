package nl.tudelft.simulation.logger.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.util.EventProducingMap;
import nl.tudelft.simulation.logger.Logger;

/**
 * A LoggerSelectFrame <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:39:18 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,, <a href="mailto:nlang@fbk.eur.nl">Niels Lang </a>
 */
public class LoggerSelectFrame extends JFrame implements EventListenerInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the loggerChooser. */
    private JComboBox loggerChooser;

    /** the openButton. */
    private JButton openButton = new JButton("Open");

    /**
     * constructs a new LoggerSelectFrame.
     */
    public LoggerSelectFrame()
    {
        super("Logger selection Frame");
        this.initialize();
    }

    /**
     * initializes the frame
     */
    private void initialize()
    {
        JPanel contentPane = new JPanel(new BorderLayout());
        this.loggerChooser = new JComboBox(Logger.getLoggerNames());
        contentPane.setPreferredSize(new Dimension(300, (int) this.loggerChooser.getPreferredSize().getHeight()));
        Logger.LOGGERS.addListener(this, EventProducingMap.OBJECT_ADDED_EVENT);
        Logger.LOGGERS.addListener(this, EventProducingMap.OBJECT_REMOVED_EVENT);
        this.openButton.addActionListener(new MyActionListener(this.loggerChooser));
        contentPane.add(this.loggerChooser, BorderLayout.CENTER);
        contentPane.add(this.openButton, BorderLayout.EAST);
        this.setContentPane(contentPane);
        pack();
        setVisible(true);
    }

    /** {@inheritDoc} */
    public synchronized void notify(final EventInterface event)
    {
        if (event.getType().equals(EventProducingMap.OBJECT_ADDED_EVENT)
                || event.getType().equals(EventProducingMap.OBJECT_REMOVED_EVENT))
        {
            String[] names = Logger.getLoggerNames();
            this.loggerChooser.removeAllItems();
            for (int i = 0; i < names.length; i++)
            {
                this.loggerChooser.addItem(names[i]);
            }
            this.repaint();
        }
    }

    /**
     * A MyActionListener
     */
    private class MyActionListener implements ActionListener
    {
        /** the loggerChooser. */
        private JComboBox myLoggerChooser = null;

        /**
         * creates a new MyActionListener
         * @param loggerChooser the chooser
         */
        public MyActionListener(final JComboBox loggerChooser)
        {
            this.myLoggerChooser = loggerChooser;
        }

        /** {@inheritDoc} */
        public void actionPerformed(final ActionEvent actionEvent)
        {
            if (this.myLoggerChooser.getSelectedItem() != null)
            {
                String logger = (String) this.myLoggerChooser.getSelectedItem();
                new LoggerFrame(java.util.logging.Logger.getLogger(logger));
            }
            else
            {
                Logger.warning(this, "actionPerformed", actionEvent.getActionCommand() + " on empty logger");
            }
        }
    }
}
