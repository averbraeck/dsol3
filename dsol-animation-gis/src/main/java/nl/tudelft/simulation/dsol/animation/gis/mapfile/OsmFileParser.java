package nl.tudelft.simulation.dsol.animation.gis.mapfile;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.djutils.draw.bounds.Bounds2d;

import nl.tudelft.simulation.dsol.animation.gis.io.osm.OsmFileReader;
import nl.tudelft.simulation.dsol.animation.gis.map.GisMap;
import nl.tudelft.simulation.dsol.animation.gis.map.GisMapInterface;
import nl.tudelft.simulation.dsol.animation.gis.map.Layer;
import nl.tudelft.simulation.dsol.animation.gis.map.LayerInterface;
import nl.tudelft.simulation.dsol.animation.gis.map.MapImage;
import nl.tudelft.simulation.dsol.animation.gis.map.MapImageInterface;
import nl.tudelft.simulation.dsol.animation.gis.transform.CoordinateTransform;

/**
 * This class parses an XML-mapfile that contains and constructs appropriate map objects.
 * <p>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * <p>
 * The dsol-animation-gis project is based on the gisbeans project that has been part of DSOL since 2002, originally by Peter
 * Jacobs and Paul Jacobs.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class OsmFileParser
{
    /** Utility class, no constructor. */
    private OsmFileParser()
    {
        // Utility class
    }

    /**
     * parses a Mapfile URL to a mapFile.
     * @param url URL; the mapfile url.
     * @return MapInterface the parsed mapfile.
     * @throws IOException on failure
     */
    public static GisMapInterface parseMapFile(final URL url) throws IOException
    {
        return parseMapFile(url, new CoordinateTransform.NoTransform());
    }

    /**
     * parses a Mapfile URL to a mapFile. <br>
     * TODO: add parameter for features to read
     * @param url URL; the mapfile url.
     * @param coordinateTransform CoordinateTransform; the transformation of (x, y) coordinates to (x', y') coordinates.
     * @return MapInterface the parsed mapfile.
     * @throws IOException on failure
     */
    public static GisMapInterface parseMapFile(final URL url, final CoordinateTransform coordinateTransform) throws IOException
    {
        GisMapInterface map = new GisMap();

        // map.name
        map.setName("OSM map"); // TODO: as parameter?

        // map.extent
        map.setExtent(new Bounds2d(4.1, 4.5, 52.0, 52.12)); // TODO

        // map.image
        MapImageInterface mapImage = new MapImage();
        mapImage.setSize(new Dimension(800, 400));
        mapImage.setBackgroundColor(Color.WHITE);
        map.setImage(mapImage);

        // map.layer
        for (int i = 0; i < 1; i++) // TODO: get from feature map
        {
            LayerInterface layer = new Layer();
            map.addLayer(layer);
            layer.setName("Layer 1"); // TODO: get from feature map
            layer.setDataSource(new OsmFileReader(url, coordinateTransform, new HashMap<>())); // TODO make feature map
            layer.setOutlineColor(Color.DARK_GRAY);
            layer.setFillColor(Color.YELLOW);
            layer.setDisplay(true);
            layer.setTransform(true);
            layer.setMinScale(10); // TODO: huh?
            layer.setMaxScale(-10);
        }
        
        map.setDrawBackground(false);

        return map;
    }

}
