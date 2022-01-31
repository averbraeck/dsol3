package nl.tudelft.simulation.dsol.animation.gis.io.osm;

import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import nl.tudelft.simulation.dsol.animation.gis.GisObject;
import nl.tudelft.simulation.dsol.animation.gis.SerializablePath;
import nl.tudelft.simulation.dsol.animation.gis.transform.CoordinateTransform;

/**
 * OsmLayerSink.java.
 * <p>
 * Copyright (c) 2021-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class OsmLayerSink implements Sink
{
    /** the ways in the OSM file. */
    private Map<Long, Way> ways = new HashMap<Long, Way>();

    /** the nodes in the OSM file. */
    private Map<Long, Node> nodes = new HashMap<Long, Node>();

    /** the key - value pairs to read. There can be multiple values per key, or '*' for all. */
    private final Map<String, Set<String>> featuresToRead;

    /** the list of shapes we have constructed from the OSM file. */
    private final List<GisObject> shapes;

    /** an optional transformation of the lat/lon (or other) coordinates. */
    private final CoordinateTransform coordinateTransform;

    /**
     * @param featuresToRead the key - value pairs to read. There can be multiple values per key, or '*' for all
     * @param shapes the list of shapes we have constructed from the OSM file
     * @param coordinateTransform CoordinateTransform; the transformation of (x, y) coordinates to (x', y') coordinates.
     */
    public OsmLayerSink(final Map<String, Set<String>> featuresToRead, final List<GisObject> shapes,
            final CoordinateTransform coordinateTransform)
    {
        this.featuresToRead = featuresToRead;
        this.shapes = shapes;
        this.coordinateTransform = coordinateTransform;
    }

    /** {@inheritDoc} */
    @Override
    public void process(final EntityContainer entityContainer)
    {
        Entity entity = entityContainer.getEntity();

        if (entity instanceof Node)
        {
            this.nodes.put(entity.getId(), (Node) entity);
            Iterator<Tag> tagIterator = entity.getTags().iterator();
            while (tagIterator.hasNext())
            {
                Tag nodeTag = tagIterator.next();
                String key = nodeTag.getKey();
                String value = nodeTag.getValue();
            }
        }

        else if (entity instanceof Way)
        {
            this.ways.put(entity.getId(), (Way) entity);
        }

        else if (entity instanceof Relation)
        {
            Iterator<Tag> tagint = entity.getTags().iterator();
            while (tagint.hasNext())
            {
                Tag route = tagint.next();
            }
        }

    }

    /** {@inheritDoc} */
    @Override
    public void initialize(final Map<String, Object> metaData)
    {
        // nothing to do right now.
    }

    /** {@inheritDoc} */
    @Override
    public void complete()
    {
        for (Way way : this.ways.values())
        {
            List<WayNode> wayNodes = way.getWayNodes();
            SerializablePath path = new SerializablePath(Path2D.WIND_NON_ZERO, wayNodes.size());
            boolean start = false;
            for (WayNode wayNode : wayNodes)
            {
                float[] coordinate;
                if (wayNode.getNodeId() != 0)
                {
                    Node node = this.nodes.get(wayNode.getNodeId());
                    coordinate = this.coordinateTransform.floatTransform(node.getLongitude(), node.getLatitude());
                }
                else
                {
                    coordinate = this.coordinateTransform.floatTransform(wayNode.getLongitude(), wayNode.getLatitude());
                }
                if (!start)
                {
                    path.moveTo(coordinate[0], coordinate[1]); 
                    start = true;
                }
                path.lineTo(coordinate[0], coordinate[1]); 
            }
            String[] att = new String[0];
            this.shapes.add(new GisObject(path, att));
        }
        System.out.println("Ways added: " + this.shapes.size());
    }

    /** {@inheritDoc} */
    @Override
    public void close()
    {
        // nothing to do right now.
    }

}
