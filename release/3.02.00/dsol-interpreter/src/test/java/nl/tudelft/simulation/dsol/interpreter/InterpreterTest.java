
package nl.tudelft.simulation.dsol.interpreter;

import java.rmi.RemoteException;

/**
 * The InterpreterTest.
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Mar 31, 2004
 * @since 1.5
 */
public class InterpreterTest
{
    /**
     * constructs a new InterpreterTest.
     */
    private InterpreterTest()
    {
        super();
        // unreachable code
    }

    /**
     * we do method c
     */
    public void doC()
    {
        try
        {
            throw new RuntimeException("exception 1");
        }
        catch (RuntimeException runtimeException)
        {
            System.out.println(runtimeException.getMessage());
        }
        System.out.println("We succesfully passed the test");
        try
        {
            throw new RuntimeException("exception 2");
        }
        catch (RuntimeException runtimeException)
        {
            System.out.println(runtimeException.getMessage());
        }
    }

    /**
     * we do method b
     * @throws RemoteException to trigger a remoteException
     */
    public void doB() throws RemoteException
    {
        System.out.println("Peter");
        throw new RemoteException("A remoteException");
    }

    /**
     * we do method a
     */
    public void doA()
    {
        try
        {
            try
            {
                throw new RuntimeException("hoi");
            }
            catch (Exception e)
            {
                this.doB();
            }
        }
        catch (RemoteException r1)
        {
            System.out.println("R1");
            throw new IllegalStateException();
        }
    }

    /**
     * executes the application
     * @param args the commandline arguments
     */
    public static void main(final String[] args)
    {
        try
        {
            Interpreter.invoke(new InterpreterTest(), "doC", null, null);
            Interpreter.invoke(new InterpreterTest(), "doA", null, null);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}