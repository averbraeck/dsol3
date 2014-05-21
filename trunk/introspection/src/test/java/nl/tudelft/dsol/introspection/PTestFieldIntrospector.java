package nl.tudelft.dsol.introspection;

import nl.tudelft.dsol.introspection.beans.SubTestBean2;
import nl.tudelft.simulation.introspection.Property;
import nl.tudelft.simulation.introspection.fields.FieldIntrospector;
import nl.tudelft.simulation.logger.Logger;

/**
 * A test program for the field introspection implementation.
 * @author (c) 2003 <a href="http://www.tudelft.nl">Delft University of Technology </a>, Delft, the Netherlands <br>
 *         <a href="http://www.tbm.tudelft.nl">Faculty of Technology, Policy and Management </a> <br>
 *         <a href="http://www.sk.tbm.tudelft.nl">Department of System Engineering </a> <br>
 *         Main researcher : <a href="http://www.tbm.tudelft.nl/webstaf/alexandv/">Dr. Ir. A. Verbraeck <a/><br>
 *         Assistant researchers <a href="http://www.peter-jacobs.com">Ir. P.H.M. Jacobs </a> and <a
 *         href="http://www.tbm.tudelft.nl/webstaf/nielsl">Ir. N.A. Lang </a>
 */
public final class PTestFieldIntrospector
{
    /**
     * constructs a new PTestFieldIntrospector
     */
    private PTestFieldIntrospector()
    {
        super();
        // unreachable code
    }

    /**
     * executes the PTestFieldIntrospector
     * @param args the commandline arguments
     */
    public static void main(final String[] args)
    {
        try
        {
            Logger.info(PTestFieldIntrospector.class, "main", "Running field introspector test");
            Property[] props = (new FieldIntrospector()).getProperties(new SubTestBean2());
            for (int i = 0; i < props.length; i++)
            {
                Logger.info(PTestFieldIntrospector.class, "main", "Prop name: " + props[i].getName());
                Logger.info(PTestFieldIntrospector.class, "main", "Prop class: " + props[i].getType());
                Logger.info(PTestFieldIntrospector.class, "main", "Prop value: " + props[i].getValue());
                Logger.info(PTestFieldIntrospector.class, "main", "Setting Possible? ");
                props[i].setValue("TEST");
                Logger.info(PTestFieldIntrospector.class, "main",
                        "If so, 'TEST' should be retrieved: " + props[i].getValue());
            }
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }
}