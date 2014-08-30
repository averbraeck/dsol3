package nl.tudelft.simulation.dsol.gui.swing;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
public class TablePanel extends JPanel
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    private int rows;

    /** */
    private int columns;

    /**
     * Constructor for TablePanel.
     * @param columns
     * @param rows
     */
    public TablePanel(int columns, int rows)
    {
        super();
        this.rows = rows;
        this.columns = columns;
        double[][] tableDefinition = {this.refractor(this.columns), this.refractor(this.rows)};
        TableLayout layout = new TableLayout(tableDefinition);
        this.setLayout(layout);
    }

    /**
     * Method setCell.
     * @param container
     * @param row
     * @param column
     */
    public void setCell(Component container, int column, int row)
    {
        this.add(container, "" + column + "," + row + ",C,C");
    }

    /**
     * Method refractor.
     * @param value
     * @return double[]
     */
    private double[] refractor(int value)
    {
        double[] result = new double[value];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = 1.0 / value;
            if (result[i] == 1.0)
                result[i] = TableLayoutConstants.FILL;
        }
        return result;
    }

    /**
     * tests the TablePanel
     * @param args
     */
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.out.println("Usage: java nl.tudelft.simulation.dsol.gui.TablePanel [column] [row]");
            System.exit(0);
        }
        int columns = new Integer(args[0]).intValue();
        int rows = new Integer(args[1]).intValue();
        JFrame app = new JFrame();
        TablePanel content = new TablePanel(columns, rows);
        for (int i = 0; i < columns; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                content.setCell(new JTextField("x=" + i + " y=" + j), i, j);
            }
        }
        app.setContentPane(content);
        app.setVisible(true);
    }
}
