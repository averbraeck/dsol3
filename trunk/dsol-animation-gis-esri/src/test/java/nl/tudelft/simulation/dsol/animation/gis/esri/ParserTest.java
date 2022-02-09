package nl.tudelft.simulation.dsol.animation.gis.esri;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import org.djutils.io.URLResource;
import org.junit.Test;

import nl.tudelft.simulation.dsol.animation.gis.GisMapInterface;
import nl.tudelft.simulation.dsol.animation.gis.MapUnits;
import nl.tudelft.simulation.dsol.animation.gis.esri.EsriFileXmlParser;

/**
 * ParserTest.java.
 * <p>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class ParserTest
{
    /**
     * Test the XML map parser.
     * @throws IOException on error
     */
    @Test
    public void testParser() throws IOException
    {
        URL url = URLResource.getResource("/resources/esri/tudelft.xml");
        GisMapInterface map = EsriFileXmlParser.parseMapFile(URLResource.getResource("/resources/esri/tudelft.xml"));
        assertEquals("tudelft", map.getName());
        assertEquals(MapUnits.DECIMAL_DEGREES, map.getUnits());
    }
}
