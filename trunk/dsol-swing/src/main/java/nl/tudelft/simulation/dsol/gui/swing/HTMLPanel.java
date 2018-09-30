package nl.tudelft.simulation.dsol.gui.swing;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck </a>
 */
public class HTMLPanel extends JEditorPane
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Construct an HTML panel for the user interface.
     */
    public HTMLPanel()
    {
        super();
        super.setEditable(false);
        this.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
    }

    /**
     * Construct an HTML panel for the user interface.
     * @param page the URL of the page to display in the panel.
     * @throws IOException in case page cannot be loaded
     */
    public HTMLPanel(final URL page) throws IOException
    {
        this();
        this.setPage(page);
    }

    /** {@inheritDoc} */
    @Override
    public void setPage(final URL page) throws IOException
    {
        try
        {
            super.setPage(page);
        }
        catch (Exception e)
        {
            Logger.getLogger("nl.tudelft.simulation.dsol").warning(e.getMessage());
        }
    }

    /**
     * Method main.
     * @param args arguments for main
     * @throws Exception on error
     */
    public static void main(String[] args) throws Exception
    {
        if (args.length != 1)
        {
            System.out.println("Usage: java nl.tudelft.simulation.dsol.gui.HTMLPanel [url]");
            System.exit(0);
        }
        JFrame app = new JFrame("HTMLPanel, (c) 2003 Delft University of Technology");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setContentPane(new JScrollPane(new HTMLPanel(new URL(args[0]))));
        app.setVisible(true);
    }
}
