package nl.tudelft.simulation.dsol.animation;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;

import org.djutils.io.URLResource;

import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.language.d3.CartesianPoint;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * An Editable object is a simulation object that can be edited by the user. That means that the user is capable of
 * instantiating, moving, rotating, and editing the vertices that span up the shape of this object during the
 * simulation.
 * <p>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public abstract class Editable extends EventProducer implements Locatable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the static map of editables. */
    private static Map<Object, Object> editables = new HashMap<Object, Object>();

    // We read editables from a file called editable.properties
    // Editables read from this file will be made available to the user
    // for instantiation (e.g. in the Editor2D of the DSOL-GUI).
    static
    {
        try
        {
            Properties properties = new Properties();
            properties.load(URLResource.getResourceAsStream("/editable.properties"));
            Editable.editables.putAll(properties);
        }
        catch (Exception exception)
        {
            SimLogger.always().error(exception, "<clinit>");
        }
    }

    /** LOCATION_CHANGED_EVENT the LOCATION_CHANGED_EVENT. */
    public static final EventType LOCATION_CHANGED_EVENT = new EventType("LOCATION_CHANGED_EVENT");

    /**
     * the simulator to use.
     */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected SimulatorInterface<?, ?, ?> simulator = null;

    /**
     * the location of the editable.
     */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DirectedPoint location = null;

    /**
     * the location of the bounds.
     */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Bounds bounds = new BoundingSphere();

    /**
     * the vertices.
     */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected CartesianPoint[] vertices = new CartesianPoint[0];

    /**
     * returns the editables as a list of name=class.
     * @return the map
     */
    public static Map<Object, Object> listEditables()
    {
        return Editable.editables;
    }

    /**
     * constructs a new Editable.
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator to schedule on
     * @param location DirectedPoint; the initial location
     */
    public Editable(final SimulatorInterface<?, ?, ?> simulator, final DirectedPoint location)
    {
        super();
        this.simulator = simulator;
        this.location = location;
    }

    /**
     * @return the vertices of the CartesianPoint.
     */
    public CartesianPoint[] getVertices()
    {
        return this.vertices;
    }

    /**
     * sets the bounds of this editable.
     * @param bounds Bounds; the new bounds of this editable.
     */
    public void setBounds(final Bounds bounds)
    {
        this.bounds = bounds;
    }

    /**
     * sets the location of this editable.
     * @param location DirectedPoint; the new location of this editable
     */
    public void setLocation(final DirectedPoint location)
    {
        this.location = location;
    }

    /**
     * sets the vertices of this editable.
     * @param vertices CartesianPoint[]; the new vertices.
     */
    public void setVertices(final CartesianPoint[] vertices)
    {
        this.vertices = vertices;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds getBounds()
    {
        return this.bounds;
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint getLocation()
    {
        return this.location;
    }
}
