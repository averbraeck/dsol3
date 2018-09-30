package nl.tudelft.simulation.xml.language;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;

import org.jdom2.Element;

/**
 * <br>
 * (c) 2002-2018-2004 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a>.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
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
