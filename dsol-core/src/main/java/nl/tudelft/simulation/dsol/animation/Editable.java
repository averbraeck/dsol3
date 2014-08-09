/*
 * @(#) Editable.java Aug 3, 2004 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.animation;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;

import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.language.d3.CartesianPoint;
import nl.tudelft.simulation.language.d3.DirectedPoint;
import nl.tudelft.simulation.language.io.URLResource;
import nl.tudelft.simulation.logger.Logger;

/**
 * An Editable object is a simulation object that can be edited by the user. That means that the user is capable of
 * instantiating, moving, rotating, and editing the vertices that span up the shape of this object during the
 * simulation.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:45 $
 * @since 1.5
 */
public abstract class Editable extends EventProducer implements LocatableInterface
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** the static map of editables */
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
            Logger.severe(Editable.class, "<clinit>", exception);
        }
    }

    /** LOCATION_CHANGED_EVENT the LOCATION_CHANGED_EVENT */
    public static final EventType LOCATION_CHANGED_EVENT = new EventType("LOCATION_CHANGED_EVENT");

    /**
     * the simulator to use
     */
    protected SimulatorInterface simulator = null;

    /**
     * the location of the editable
     */
    protected DirectedPoint location = null;

    /**
     * the location of the bounds
     */
    protected Bounds bounds = new BoundingSphere();

    /**
     * the vertices
     */
    protected CartesianPoint[] vertices = new CartesianPoint[0];

    /**
     * returns the editables as a list of name=class
     * @return the map
     */
    public static Map<Object, Object> listEditables()
    {
        return Editable.editables;
    }

    /**
     * constructs a new Editable
     * @param simulator the simulator to schedule on
     * @param location the initial location
     */
    public Editable(final SimulatorInterface simulator, final DirectedPoint location)
    {
        super();
        this.simulator = simulator;
        this.location = location;
    }

    /**
     * @return the vertices of the CartesianPoint
     */
    public CartesianPoint[] getVertices()
    {
        return this.vertices;
    }

    /**
     * sets the bounds of this editable
     * @param bounds the new bounds of this editable.
     */
    public void setBounds(final Bounds bounds)
    {
        this.bounds = bounds;
    }

    /**
     * sets the location of this editable.
     * @param location the new location of this editable
     */
    public void setLocation(final DirectedPoint location)
    {
        this.location = location;
    }

    /**
     * sets the vertices of this editable
     * @param vertices the new vertices.
     */
    public void setVertices(final CartesianPoint[] vertices)
    {
        this.vertices = vertices;
    }

    /**
     * @see nl.tudelft.simulation.dsol.animation.LocatableInterface#getBounds()
     */
    public Bounds getBounds()
    {
        return this.bounds;
    }

    /**
     * @see nl.tudelft.simulation.dsol.animation.LocatableInterface#getLocation()
     */
    public DirectedPoint getLocation()
    {
        return this.location;
    }
}