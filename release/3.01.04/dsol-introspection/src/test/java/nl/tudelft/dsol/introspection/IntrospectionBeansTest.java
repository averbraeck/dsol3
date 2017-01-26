package nl.tudelft.dsol.introspection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.dsol.introspection.beans.SubTestBean2;
import nl.tudelft.simulation.introspection.Property;
import nl.tudelft.simulation.introspection.beans.BeanIntrospector;

/**
 * A test program for the JavaBean introspection implementation.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang" >Niels Lang </a><a
 *         href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.1 Apr 15, 2004
 * @since 1.5
 */
public final class IntrospectionBeansTest
{
    /** the logger./ */
    private static Logger logger = LogManager.getLogger(IntrospectionBeansTest.class);

    /**
     * constructs a new PTestBeans.
     */
    private IntrospectionBeansTest()
    {
        super();
        // unreachable code;
    }

    /**
     * executes the PTestBeans
     * @param args the commandline arguments
     */
    public static void main(final String[] args)
    {
        Property[] props = (new BeanIntrospector()).getProperties(new SubTestBean2());
        for (int i = 0; i < props.length; i++)
        {
            logger.info("main", "Prop name: " + props[i].getName());
            logger.info("main", "Prop class: " + props[i].getType());
            logger.info("main", "Prop value: " + props[i].getValue());
        }
    }
}
