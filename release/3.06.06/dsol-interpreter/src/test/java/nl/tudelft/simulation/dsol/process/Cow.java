package nl.tudelft.simulation.dsol.process;

import java.io.Serializable;

import nl.tudelft.simulation.dsol.interpreter.process.InterpretableProcess;

/**
 * The specifies
 * <p>
 * copyright (c) 2004-2021 <a href="https://simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="https://simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @since 1.5
 */
class Cow extends InterpretableProcess
{
    /**
     * constructs a new Cow.
     */
    public Cow()
    {
        super();
    }

    /**
     * moos
     */
    public void moo()
    {
        System.out.println("moo");
    }

    /**
     * executes the process
     */
    public void process()
    {
        this.moo();
        this.suspendProcess();
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return "Cow";
    }
}
