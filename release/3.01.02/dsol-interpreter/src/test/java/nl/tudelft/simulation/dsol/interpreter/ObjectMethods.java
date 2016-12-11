package nl.tudelft.simulation.dsol.interpreter;

/**
 * A number of long methods to test the interpreted bytecode for longs.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Aug 31, 2014
 */
public class ObjectMethods
{
    /** value to use in the test */
    private Object value;

    /**
     * @param value
     */
    public ObjectMethods(Object value)
    {
        super();
        this.value = value;
    }

    /**
     * @return value
     */
    public final Object get()
    {
        return this.value;
    }

    /**
     * @param value set value
     */
    public final void set(Object value)
    {
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ObjectMethods other = (ObjectMethods) obj;
        if (this.value == null)
        {
            if (other.value != null)
                return false;
        }
        else if (!this.value.equals(other.value))
            return false;
        return true;
    }

    /**
     * @param obj
     * @return hashcode equal or not
     */
    public boolean equalsHC(Object obj)
    {
        if (obj == null)
            return false;
        return this.hashCode() == obj.hashCode();
    }

    /**
     * Concat 5 objects.
     * @param s1
     * @param s2
     * @param s3
     * @param s4
     * @param s5
     * @return concatenation of the toString of the objects.
     */
    public static String concat5(Object s1, Object s2, Object s3, Object s4, Object s5)
    {
        return s1.toString() + s2.toString() + s3.toString() + s4.toString() + s5.toString();
    }

    /**
     * Concat N objects.
     * @param objects
     * @return the concatenation of the toString of the objects.
     */
    public static String concatN(Object... objects)
    {
        String ret = "";
        for (Object s : objects)
        {
            ret += s.toString();
        }
        return ret;
    }
    
    /**
     * @return class info from toString().
     */
    public String getC()
    {
        String ret = "";
        Class<?> c = getClass();
        String n = c.getName();
        ret += n;
        ret += "@";
        int hc = hashCode();
        String hcs = Integer.toHexString(hc);
        ret += hcs;
        return ret;
        // return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
