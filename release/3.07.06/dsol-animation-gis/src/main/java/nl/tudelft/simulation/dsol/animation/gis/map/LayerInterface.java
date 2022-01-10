package nl.tudelft.simulation.dsol.animation.gis.map;

import java.awt.Color;

import nl.tudelft.simulation.dsol.animation.gis.io.DataSourceInterface;

/**
 * This interface defines the layer of the map.
 * <p>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface LayerInterface extends java.io.Serializable
{
    /**
     * Return the fill color for the layer.
     * @return Color; the rgb(a) fill color for the layer
     */
    Color getFillColor();

    /**
     * Set the fill color for the layer.
     * @param fillColor Color; the rgb(a) fill color for the layer
     */
    void setFillColor(Color fillColor);

    /**
     * Return the outline (line)  color for the layer.
     * @return Color; the rgb(a) outline (line) color for the layer
     */
    Color getOutlineColor();

    /**
     * Set the outline (line) color for the layer.
     * @param outlineColor Color; the rgb(a) outline (line) color for the layer
     */
    void setOutlineColor(Color outlineColor);

    /**
     * Return the layer name.
     * @return String; layer name
     */
    String getName();

    /**
     * Set the layer name.
     * @param name String; layer name
     */
    void setName(String name);

    /**
     * Return the data source, which contains the location of an ESRI shape datasource for now.
     * @return DataSourceInterface the data source, contains the location of an ESRI shape datasource for now
     */
    DataSourceInterface getDataSource();

    /**
     * Set the data source, which contains the location of an ESRI shape datasource for now.
     * @param dataSource DataSourceInterface; the data source, contains the location of an ESRI shape datasource for now
     */
    void setDataSource(DataSourceInterface dataSource);

    /**
     * Return the minimum scale at which this layer has to be drawn. FIXME: how do we define scale?
     * @return int; the minimum scale at which this layer has to be drawn
     */
    int getMinScale();

    /**
     * Set the minimum scale at which this layer has to be drawn.
     * @param minscale int; the minimum scale at which this layer has to be drawn
     */
    void setMinScale(int minscale);

    /**
     * Return the maximum scale at which this layer has to be drawn. FIXME: how do we define scale?
     * @return int; the maximum scale at which this layer has to be drawn
     */
    int getMaxScale();

    /**
     * Set the maximum scale at which this layer has to be drawn.
     * @param maxScale int; the maximum scale at which this layer has to be drawn
     */
    void setMaxScale(int maxScale);

    /**
     * Return the display status of the layer (displayed or not).
     * @return boolean; the display status of the layer (displayed or not)
     */
    boolean isDisplay();

    /**
     * Set the display status of the layer (displayed or not).
     * @param status boolean; the display status of the layer (displayed or not)
     */
    void setDisplay(boolean status);

    /**
     * Return the status for the transformation: should the transform be used for this layer or not?
     * @return boolean; the status for the transformation: should the transform be used for this layer or not?
     */
    boolean isTransform();

    /**
     * Set the status for the transformation: should the transform be used for this layer or not?
     * @param transform boolean; the status for the transformation: should the transform be used for this layer or not?
     */
    void setTransform(boolean transform);
}
