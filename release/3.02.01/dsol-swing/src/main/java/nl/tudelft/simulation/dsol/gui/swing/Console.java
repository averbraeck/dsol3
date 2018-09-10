package nl.tudelft.simulation.dsol.gui.swing;

import java.awt.Color;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JTextArea;

/**
 * <br>
 * Copyright (c) 2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * The MEDLABS project (Modeling Epidemic Disease with Large-scale Agent-Based Simulation) is aimed at providing policy
 * analysis tools to predict and help contain the spread of epidemics. It makes use of the DSOL simulation engine and
 * the agent-based modeling formalism. See for project information <a href="https://simulation.tudelft.nl/">
 * www.simulation.tudelft.nl</a>. The project is a co-operation between TU Delft, Systems Engineering and Simulation
 * Department (Netherlands) and NUDT, Simulation Engineering Department (China). This software is licensed under the BSD
 * license. See license.txt in the main project.
 * @version May 4, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck </a>
 */
public class Console extends JTextArea
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    protected Handler logHandler;

    /**
     * Constructor for Console.
     */
    public Console()
    {
        super();
        this.setEditable(false);
        this.setForeground(Color.RED);
        this.logHandler = new LogHandler(this);
    }

    /**
     * Method addLogger.
     * @param logger the logger to add
     */
    public void addLogger(Logger logger)
    {
        Handler[] handlers = logger.getHandlers();
        for (int i = 0; i < handlers.length; i++)
        {
            logger.removeHandler(handlers[i]);
        }
        logger.addHandler(this.getHandler());
    }

    /**
     * Method getHandler.
     * @return Handler
     */
    public Handler getHandler()
    {
        return this.logHandler;
    }

    /**
     * <br>
     * Copyright (c) 2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
     * reserved. The MEDLABS project (Modeling Epidemic Disease with Large-scale Agent-Based Simulation) is aimed at
     * providing policy analysis tools to predict and help contain the spread of epidemics. It makes use of the DSOL
     * simulation engine and the agent-based modeling formalism. See for project information <a
     * href="https://simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>. The project is a co-operation between TU
     * Delft, Systems Engineering and Simulation Department (Netherlands) and NUDT, Simulation Engineering Department
     * (China). This software is licensed under the BSD license. See license.txt in the main project.
     * @version May 4, 2014 <br>
     * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck </a>
     */
    private class LogHandler extends Handler
    {
        /** */
        private JTextArea textArea;

        /**
         * Method LogHandler.
         * @param textArea
         */
        public LogHandler(JTextArea textArea)
        {
            this.textArea = textArea;
        }

        /** {@inheritDoc} */
        @Override
        public void publish(LogRecord record)
        {
            if (record.getThrown() != null)
            {
                this.textArea.append("-" + record.getLevel() + "  " + record.getLoggerName() + "  "
                        + record.getThrown() + "  " + record.getMessage() + " \n");
            }
            else
            {
                this.textArea.append("-" + record.getLevel() + "  " + record.getLoggerName() + "  "
                        + record.getMessage() + " \n");
            }
        }

        /** {@inheritDoc} */
        @Override
        public void close()
        {
            //
        }

        /** {@inheritDoc} */
        @Override
        public void flush()
        {
            //
        }
    }
}
