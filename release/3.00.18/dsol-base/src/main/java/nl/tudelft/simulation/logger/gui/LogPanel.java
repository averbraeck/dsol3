package nl.tudelft.simulation.logger.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.logger.formatters.StyledTextFormatter;
import nl.tudelft.simulation.logger.handlers.EventLogHandler;
import nl.tudelft.simulation.logger.handlers.MemoryHandler;

/**
 * A LogPanel <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:39:18 $
 * @author Peter Jacobs, Niels Lang, Alexander Verbraeck
 */
public class LogPanel extends JPanel implements EventListenerInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the maximum number of records to show. */
    private int bufferSize = 10000;

    /** the logger to display */
    private Logger logger = null;

    /** the handler for this panel. */
    private EventLogHandler handler = null;

    /** defines whether we clean automatically */
    private JCheckBox autoCheck = null;

    /** the textPane. */
    private JTextPane textPane = new JTextPane();

    /** counts the row number. */
    private int row = 0;

    /**
     * constructs a new LogPanel.
     * @param logger the logger to show
     */
    public LogPanel(final Logger logger)
    {
        super();
        this.logger = logger;
        this.initializePanel();
        this.initializeLogger();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void finalize()
    {
        if (this.handler != null)
        {
            Handler[] handlers = this.logger.getHandlers();
            if (handlers.length == 1)
            {
                this.logger.addHandler(new MemoryHandler());
            }
            this.logger.removeHandler(this.handler);
        }
    }

    /**
     * gets bufferSize
     * @return Returns the bufferSize.
     */
    public int getBufferSize()
    {
        return this.bufferSize;
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        if (!event.getType().equals(EventLogHandler.LOG_RECORD_PRODUCED_EVENT))
        {
            return;
        }
        try
        {
            synchronized (this.textPane.getDocument())
            {
                this.row++;
                Record[] records = this.constructMessage((String) event.getContent());
                for (int i = records.length - 1; i > -1; i--)
                {
                    this.textPane.getDocument().insertString(0, records[i].getMessage(), records[i].getStyle());
                }
                this.textPane.getDocument().insertString(0, "(" + this.row + ")  ", records[0].getStyle());
                if (this.autoCheck.isSelected() && this.textPane.getDocument().getLength() > this.bufferSize)
                {
                    this.textPane.getDocument().remove(this.bufferSize,
                            this.textPane.getDocument().getLength() - this.bufferSize);
                }
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * sets the bufferSize
     * @param bufferSize The bufferSize to set.
     */
    public void setBufferSize(final int bufferSize)
    {
        this.bufferSize = bufferSize;
    }

    /** *************** PRIVATE METHODS ************************* */

    /**
     * creates a clearPanel
     * @return the clearPanel
     */
    private JPanel createClearPanel()
    {
        JPanel result = new JPanel();

        JPanel bufferSizePanel = new JPanel();
        bufferSizePanel.setLayout(new BoxLayout(bufferSizePanel, BoxLayout.X_AXIS));
        bufferSizePanel.setBorder(BorderFactory.createTitledBorder("LogPanel settings"));
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new DocumentCleaner(this.textPane.getDocument()));
        this.autoCheck = new JCheckBox("auto", true);
        this.autoCheck.setPreferredSize(new Dimension(75, 1));
        JTextField clearText = new JTextField("" + this.bufferSize);
        clearText.setEditable(false);
        clearText.setPreferredSize(new Dimension(80, 20));
        JButton updateSize = new JButton("Change max buffer-size");
        updateSize.addActionListener(new ChangeBufferSizeListener(this, clearText));
        bufferSizePanel.add(clearButton);
        bufferSizePanel.add(this.autoCheck);
        bufferSizePanel.add(clearText);
        bufferSizePanel.add(updateSize);
        result.add(bufferSizePanel, BorderLayout.CENTER);
        return result;
    }

    /**
     * returns the message as an array of
     * @param styledMessage the message
     * @return Record[] the record
     */
    private Record[] constructMessage(final String styledMessage)
    {
        String[] records = styledMessage.split(StyledTextFormatter.SEPARATOR);
        Record[] result = new Record[records.length];
        for (int i = 0; i < records.length; i++)
        {
            result[i] = this.constructRecord(records[i]);
        }
        return result;
    }

    /**
     * constructs a styledRecord
     * @param styledRecord the styled message
     * @return Record the record
     */
    private Record constructRecord(final String styledRecord)
    {
        if (styledRecord.startsWith("<" + StyledTextFormatter.STYLE_DEFAULT + ">")
                && styledRecord.endsWith("</" + StyledTextFormatter.STYLE_DEFAULT + ">"))
        {
            return new Record(this.constructMessage(StyledTextFormatter.STYLE_DEFAULT, styledRecord),
                    this.textPane.getStyle(StyledTextFormatter.STYLE_DEFAULT));
        }
        if (styledRecord.startsWith("<" + StyledTextFormatter.STYLE_FINE + ">")
                && styledRecord.endsWith("</" + StyledTextFormatter.STYLE_FINE + ">"))
        {
            return new Record(this.constructMessage(StyledTextFormatter.STYLE_FINE, styledRecord),
                    this.textPane.getStyle(StyledTextFormatter.STYLE_FINE));
        }
        if (styledRecord.startsWith("<" + StyledTextFormatter.STYLE_ORIGIN + ">")
                && styledRecord.endsWith("</" + StyledTextFormatter.STYLE_ORIGIN + ">"))
        {
            return new Record(this.constructMessage(StyledTextFormatter.STYLE_ORIGIN, styledRecord),
                    this.textPane.getStyle(StyledTextFormatter.STYLE_ORIGIN));
        }
        if (styledRecord.startsWith("<" + StyledTextFormatter.STYLE_SOURCE + ">")
                && styledRecord.endsWith("</" + StyledTextFormatter.STYLE_SOURCE + ">"))
        {
            return new Record(this.constructMessage(StyledTextFormatter.STYLE_SOURCE, styledRecord),
                    this.textPane.getStyle(StyledTextFormatter.STYLE_SOURCE));
        }
        if (styledRecord.startsWith("<" + StyledTextFormatter.STYLE_WARNING + ">")
                && styledRecord.endsWith("</" + StyledTextFormatter.STYLE_WARNING + ">"))
        {
            return new Record(this.constructMessage(StyledTextFormatter.STYLE_WARNING, styledRecord),
                    this.textPane.getStyle(StyledTextFormatter.STYLE_WARNING));
        }
        return null;
    }

    /**
     * regenerates the message from the record
     * @param tag the used tag
     * @param message the message
     * @return String the message
     */
    private String constructMessage(final String tag, final String message)
    {
        return message.substring(new String("<" + tag + ">").length(),
                message.length() - new String("</" + tag + ">").length());
    }

    /**
     * initializes the logger
     */
    private void initializeLogger()
    {
        this.handler = new EventLogHandler();
        this.handler.setFormatter(new StyledTextFormatter(true));
        this.handler.addListener(this, EventLogHandler.LOG_RECORD_PRODUCED_EVENT);
        this.logger.addHandler(this.handler);
        Handler[] handlers = this.logger.getHandlers();
        for (int i = 0; i < handlers.length; i++)
        {
            if (handlers[i] instanceof MemoryHandler)
            {
                ((MemoryHandler) handlers[i]).push(this.handler);
                this.logger.removeHandler(handlers[i]);
            }
        }
    }

    /**
     * initializes the panel
     */
    private void initializePanel()
    {
        this.textPane.setEditable(false);
        this.setOpaque(true);
        this.setPreferredSize(new Dimension(500, 500));
        this.setLayout(new BorderLayout());

        this.add(new JScrollPane(this.textPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

        JComboBox levelChooser =
                new JComboBox(new Level[]{Level.ALL, Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG, Level.FINE,
                        Level.FINER, Level.FINEST, Level.OFF});
        levelChooser.setSelectedItem(this.logger.getLevel());
        levelChooser.addActionListener(new MyLevelChooser(this.logger, levelChooser));
        this.add(levelChooser, BorderLayout.NORTH);
        this.add(this.createClearPanel(), BorderLayout.SOUTH);
        this.initStyles();
    }

    /**
     * initializes the styles
     */
    private void initStyles()
    {
        Style defaultStyle = this.textPane.addStyle(StyledTextFormatter.STYLE_DEFAULT, null);

        Style sourceStyle = this.textPane.addStyle(StyledTextFormatter.STYLE_SOURCE, defaultStyle);
        StyleConstants.setForeground(sourceStyle, Color.BLUE);

        Style warningStyle = this.textPane.addStyle(StyledTextFormatter.STYLE_WARNING, defaultStyle);
        StyleConstants.setForeground(warningStyle, Color.RED);

        Style fineStyle = this.textPane.addStyle(StyledTextFormatter.STYLE_FINE, defaultStyle);
        StyleConstants.setForeground(fineStyle, Color.GREEN);

        Style originStyle = this.textPane.addStyle(StyledTextFormatter.STYLE_ORIGIN, defaultStyle);
        StyleConstants.setForeground(originStyle, Color.GRAY);
    }

    /**
     * The ChangeMaxRecordListener
     */
    private class MyLevelChooser implements ActionListener
    {
        /** the owner. */
        private Logger myLogger = null;

        /** the owner. */
        private JComboBox owner = null;

        /**
         * constructs a new logPanel.
         * @param logger the logger
         * @param owner the owner
         */
        public MyLevelChooser(final Logger logger, final JComboBox owner)
        {
            this.myLogger = logger;
            this.owner = owner;
        }

        /** {@inheritDoc} */
        @Override
        public void actionPerformed(final ActionEvent actionEvent)
        {
            if (actionEvent.getSource() != null)
            {
                this.myLogger.setLevel((Level) this.owner.getSelectedItem());
            }
        }
    }

    /**
     * The ChangeBufferSizeListener
     */
    private class ChangeBufferSizeListener implements ActionListener
    {
        /** the owner. */
        private LogPanel owner = null;

        /** the textField. */
        private JTextField textField = null;

        /**
         * constructs a new logPanel.
         * @param logPanel the logPanel
         * @param textField the textField
         */
        public ChangeBufferSizeListener(final LogPanel logPanel, final JTextField textField)
        {
            this.owner = logPanel;
            this.textField = textField;
        }

        /** {@inheritDoc} */
        @Override
        public void actionPerformed(final ActionEvent actionEvent)
        {
            if (actionEvent == null)
            {
                nl.tudelft.simulation.logger.Logger.warning(this, "actionPerformed", "actionEvent=null");
            }
            String result = JOptionPane.showInputDialog("Enter desired buffersize (integer)");
            if (result != null)
            {
                try
                {
                    int value = (int) Math.round(Double.parseDouble(result));
                    if (value <= 0.0)
                    {
                        throw new IllegalArgumentException();
                    }
                    this.owner.setBufferSize(value);
                    this.textField.setText(value + "");
                }
                catch (Exception exception)
                {
                    try
                    {
                        JOptionPane.showMessageDialog(null, "Invalid input: " + result
                                + ". value should be long and larger than 0");
                    }
                    catch (Exception remoteException)
                    {
                        nl.tudelft.simulation.logger.Logger.warning(this, "actionPerformed", remoteException);
                    }
                }
            }
        }
    }

    /**
     * A DocumentCleaner
     */
    private class DocumentCleaner implements ActionListener
    {
        /** the document to clean. */
        private Document document = null;

        /**
         * creates a new DocumentCleanner
         * @param document the document
         */
        public DocumentCleaner(final Document document)
        {
            this.document = document;
        }

        /** {@inheritDoc} */
        @Override
        public void actionPerformed(final ActionEvent actionEvent)
        {
            if (actionEvent == null)
            {
                nl.tudelft.simulation.logger.Logger.warning(this, "actionPerformed", "actionEvent=null");
            }
            synchronized (this.document)
            {
                try
                {
                    this.document.remove(0, this.document.getLength());
                }
                catch (BadLocationException exception)
                {
                    nl.tudelft.simulation.logger.Logger.warning(this, "actionPerformed", exception);
                }
            }
        }
    }

    /**
     * defines a record
     */
    private class Record
    {
        /** the message. */
        private String message;

        /** the style. */
        private Style style;

        /**
         * constructs a new Record.
         * @param message the message
         * @param style the style
         */
        public Record(final String message, final Style style)
        {
            this.message = message;
            this.style = style;
        }

        /**
         * gets the message
         * @return String message
         */
        public String getMessage()
        {
            return this.message;
        }

        /**
         * gets the style
         * @return style
         */
        public Style getStyle()
        {
            return this.style;
        }
    }
}
