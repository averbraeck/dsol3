package nl.tudelft.simulation.dsol.interpreter;

/**
 * 
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Sep 7, 2014
 */
public class SubMethods
{
    /**
     * Subclass method.
     * @return number 10
     */
    protected int iSub10()
    {
        return 10;
    }

    /**
     * Subclass method.
     * @return number 10
     */
    protected int iOver5()
    {
        return 10;
    }

    /**
     * Subclass method.
     * @return number 3
     */
    protected int iPlus4()
    {
        return 3;
    }

    /**
     * Subclass method.
     * @param nul value = 0
     * @return number 1 + 2 + 3
     */
    protected int iPl123(int nul)
    {
        int i = nul;
        i += 1;
        i += 2;
        i += 3;
        return i;
    }
    
    /**
     * Subclass method.
     * @return String ABC
     */
    protected String sSubABC()
    {
        return "ABC";
    }

    /**
     * Subclass method.
     * @param abc value to add to the end
     * @return String DEF plus value in argument
     */
    protected String sPlusDEF(String abc)
    {
        return "DEF" + abc;
    }


}
