package nl.tudelft.simulation.dsol.formalisms.process;

import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.interpreter.operations.custom.InterpreterOracleInterface;

/**
 * The specifies
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a>
 * <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2007/01/07 04:55:53 $
 * @since 1.5
 */
public class DSOLInterpreterOracle implements InterpreterOracleInterface
{
    /**
     * constructs a new DSOLInterpreterOracle
     */
    public DSOLInterpreterOracle()
    {
        super();
    }

    /**
     * should this method be interpreted?
     * @param method whether this method should be interpreted
     * @return <code>
     *       if (ProcessInterface.class.isAssignableFrom(method.getDeclaringClass()))
     {
     return true;
     }
     </code>
     */
    public boolean shouldBeInterpreted(final Method method)
    {
        if (method.getDeclaringClass().equals(Process.class) && method.getName().equals("resume"))
        {
            return false;
        }
        if (Process.class.isAssignableFrom(method.getDeclaringClass()))
        {
            return true;
        }
        return false;
    }
}
