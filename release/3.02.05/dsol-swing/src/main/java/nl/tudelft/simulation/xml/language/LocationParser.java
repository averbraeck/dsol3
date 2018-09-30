package nl.tudelft.simulation.xml.language;

import java.io.IOException;
import java.net.URL;

import org.jdom2.Element;

import nl.tudelft.simulation.language.d3.DirectedPoint;

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
public final class LocationParser
{
    /** the default schema file. */
    public static final URL LOCATIONFILE_SCHEMA = LocationParser.class.getResource("/xsd/location.xsd");

    /**
     * constructs a new LocationParser.
     */
    private LocationParser()
    {
        super();
        // unreachable code
    }

    /**
     * parses a xml-element representing a DirectedPoint
     * @param element The j-dom element
     * @return DirectedPoint of element
     * @throws IOException on IOfailure
     */
    public static DirectedPoint parseLocation(final Element element) throws IOException
    {
        try
        {
            double x = new Double(element.getAttributeValue("x")).doubleValue();
            double y = new Double(element.getAttributeValue("y")).doubleValue();
            double z = 0.0;
            if (element.getAttributeValue("z") != null)
            {
                z = new Double(element.getAttributeValue("z")).doubleValue();
            }
            double phi = 0.0;
            if (element.getAttributeValue("phi") != null)
            {
                phi = new Double(element.getAttributeValue("phi")).doubleValue();
            }
            double rho = 0.0;
            if (element.getAttributeValue("rho") != null)
            {
                rho = new Double(element.getAttributeValue("rho")).doubleValue();
            }
            double theta = 0.0;
            if (element.getAttributeValue("theta") != null)
            {
                theta = new Double(element.getAttributeValue("theta")).doubleValue();
            }
            return new DirectedPoint(x, y, z, phi, rho, theta);
        }
        catch (Exception exception)
        {
            throw new IOException("element: " + element + "\nattributes: " + element.getAttributes() + "\nchaildren: "
                    + element.getChildren() + "\nmessage: " + exception.getMessage());
        }
    }
}
