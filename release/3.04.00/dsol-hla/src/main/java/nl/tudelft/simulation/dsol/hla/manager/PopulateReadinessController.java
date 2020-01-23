/*
 * Created on Mar 2, 2005 TODO To change the template for this generated file go
 * to Window - Preferences - Java - Code Style - Code Templates
 */
package nl.tudelft.simulation.dsol.hla.manager;

/**
 * @author peter TODO To change the template for this generated type comment go to Window - Preferences - Java - Code
 *         Style - Code Templates
 */
public interface PopulateReadinessController
{
    /**
     * discovers a new object instance and checks whether the manager is ready to synchronize on population
     * @param arg0 the new objectInstance discovered
     * @param arg1 the new objectclasshandle discovered
     * @param arg2 the name of the new instance
     * @return whether we are finished and ready to populate
     */
    public boolean discoverObjectInstance(int theObject, int theObjectClass, String objectName);
}
