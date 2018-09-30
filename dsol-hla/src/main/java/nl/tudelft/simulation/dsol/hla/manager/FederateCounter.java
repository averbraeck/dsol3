/*
 * Created on Mar 2, 2005 TODO To change the template for this generated file go
 * to Window - Preferences - Java - Code Style - Code Templates
 */
package nl.tudelft.simulation.dsol.hla.manager;

/**
 * @author peter TODO To change the template for this generated type comment go to Window - Preferences - Java - Code
 *         Style - Code Templates
 */
public class FederateCounter implements PopulateReadinessController
{
    /** the number of to be discovered federates. */
    private int goal = 0;

    /** the number of discovered federates. */
    private int discovered = 0;

    /**
     * constructs a new FederateCounter.
     */
    public FederateCounter(final int goal)
    {
        super();
        this.goal = goal;
    }

    /*
     * (non-Javadoc)
     * @see nl.tudelft.simulation.dsol.hla.manager.PopulateReadinessController#discoverObjectInstance(int, int,
     * java.lang.String)
     */
    public boolean discoverObjectInstance(int theObject, int theObjectClass, String objectName)
    {
        this.discovered++;
        if (this.goal == this.discovered)
        {
            return true;
        }
        return false;
    }
}
