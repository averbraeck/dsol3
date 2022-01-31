package nl.tudelft.simulation.dsol.animation.gis;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

import org.djutils.draw.bounds.Bounds2d;

import nl.tudelft.simulation.dsol.animation.gis.GisObject;

/**
 * DataSourceInterface is the connector between the reader of a GIS file and the display in the DSOL animation. Note that the
 * data source can be a 'live' data source, with updates between queries for the shapes! Often, the data source will be static
 * and cache the shapes.
 * <p>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface DataSourceInterface extends Serializable
{
    /**
     * Return the key names of the attribute data. The attribute values are stored in the GisObject together with the shape.
     * @return String[]; the key names of the attribute data
     */
    String[] getAttributeKeyNames();

    /**
     * Return the URL of the data source. Note that the data source can be a 'live' data source, with updates between queries
     * for the shapes! Often, the data source will be static and cache the shapes.
     * @return URL; the URL of the data source
     */
    URL getDataSource();

    /**
     * Return the number of shapes in the data source at this moment.
     * @return int; the number of shapes in the data source
     * @throws IOException on file IO or database connection failure
     */
    int getNumShapes() throws IOException;

    /**
     * Return a GisObject.
     * @param index int; the number of the shape to be returned
     * @return GisObject returns a <code>nl.tudelft.simulation.dsol.animation.gis.GisObject</code>
     * @throws IndexOutOfBoundsException whenever index &gt; numShapes or index &lt; 0
     * @throws IOException on file IO or database connection failure
     */
    GisObject getShape(int index) throws IOException, IndexOutOfBoundsException;

    /**
     * Return all the shapes of the particular data source.
     * @return List the resulting ArrayList of <code>nl.tudelft.simulation.dsol.animation.gis.GisObject</code>
     * @throws IOException on file IO or database connection failure
     */
    List<GisObject> getShapes() throws IOException;

    /**
     * Return the shapes of the particular data source in a particular extent.
     * @param rectangle Bounds2d; the extent of the box (in geo-coordinates)
     * @return List the resulting ArrayList of <code>nl.tudelft.simulation.dsol.animation.gis.GisObject</code>
     * @throws IOException on file IO or database connection failure
     */
    List<GisObject> getShapes(Bounds2d rectangle) throws IOException;

    /**
     * Return the shapes based on a particular value of the attributes.
     * @param attribute String; the value of the attribute
     * @param columnName String; the columnName
     * @return List the resulting ArrayList of <code>nl.tudelft.simulation.dsol.animation.gis.GisObject</code>
     * @throws IOException on file IO or database connection failure
     */
    List<GisObject> getShapes(String attribute, String columnName) throws IOException;

    /**
     * Return the type of this dataSouce.
     * @return int the type of this dataSouce.
     * @throws IOException on file IO or database connection failure
     */
    int getType() throws IOException;
}
