package nl.tudelft.simulation.dsol.gui.swing;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * <br>
 * Copyright (c) 2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * The MEDLABS project (Modeling Epidemic Disease with Large-scale Agent-Based Simulation) is aimed at providing policy
 * analysis tools to predict and help contain the spread of epidemics. It makes use of the DSOL simulation engine and
 * the agent-based modeling formalism. See for project information <a href="http://www.simulation.tudelft.nl/">
 * www.simulation.tudelft.nl</a>. The project is a co-operation between TU Delft, Systems Engineering and Simulation
 * Department (Netherlands) and NUDT, Simulation Engineering Department (China). This software is licensed under the BSD
 * license. See license.txt in the main project.
 * @version May 4, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck </a>
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
     * @param args
     * @throws Exception
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
