package nl.tudelft.simulation.xml.language;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;

import org.jdom2.Element;

/**
 * <br>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version Jun 25, 2004 <br>
 * @author <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public final class ColorParser
{
    /** the default mapfile. */
    public static final URL COLORFILE_SCHEMA = ColorParser.class.getResource("/xsd/color.xsd");

    /**
     * constructs a new ColorParser.
     */
    private ColorParser()
    {
        super();
        // unreachable code
    }

    /**
     * parses a xml-element representing a Color
     * @param element The j-dom element
     * @return Color the color
     * @throws IOException on failure
     */
    public static Color parseColor(final Element element) throws IOException
    {
        try
        {
            int r = new Integer(element.getAttributeValue("R")).intValue();
            int g = new Integer(element.getAttributeValue("G")).intValue();
            int b = new Integer(element.getAttributeValue("B")).intValue();
            return new Color(r, g, b);
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }
}
