package nl.tudelft.simulation.dsol.process;

import nl.tudelft.simulation.dsol.interpreter.process.Process;

/**
 * The specifies
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:13 $
 * @since 1.5
 */
class Dog extends Process
{
    /**
     * constructs a new Cow.
     */
    public Dog()
    {
        super();
    }

    /**
     */
    public void bark()
    {
        System.out.println("bark");
        this.suspend();
    }

    /**
     * executes the process
     */
    public void process()
    {
        this.bark();
    }
}
