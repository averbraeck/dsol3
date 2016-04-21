package nl.tudelft.simulation.dsol;

/**
 * Interface for an identifiable class, which can return an id. Preferably the id is unique in a certain context.
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Apr 21, 2016
 * @param <T> the type of Id that is returned by an Identifiable, e.g. String
 */
public interface Identifiable<T>
{
    /**
     * @return the Id, which is preferably unique in a certain context
     */
    T getId();
    
    /**
     * String interface for an identifiable class.
     * <p />
     * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
     * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
     * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @version Apr 21, 2016
     */
    public interface String extends Identifiable<String>
    {
        @Override
        String getId();
    }

    /**
     * Long interface for an identifiable class.
     * <p />
     * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
     * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
     * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @version Apr 21, 2016
     */
    public interface Long extends Identifiable<Long>
    {
        @Override
        Long getId();
    }

}
