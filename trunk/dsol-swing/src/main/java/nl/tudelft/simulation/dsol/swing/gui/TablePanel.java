package nl.tudelft.simulation.dsol.swing.gui;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

/**
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck </a>
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
     * @param columns int; number of columns
     * @param rows int; number of rows
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
     * @param container Component; the component to set
     * @param row int; the row number
     * @param column int; the column number
     */
    public void setCell(Component container, int column, int row)
    {
        this.add(container, "" + column + "," + row + ",C,C");
    }

    /**
     * Method refractor.
     * @param value int; the number of cells to be used
     * @return double[] the double factors corresponding to 1/value
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
     * @param args String[]; arguments
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
