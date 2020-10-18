package nl.tudelft.simulation.dsol.animation.gis.map;

import java.awt.Color;
import java.util.List;

import nl.tudelft.simulation.dsol.animation.gis.io.DataSourceInterface;

/**
 * This interface defines the layer of the map.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface LayerInterface extends java.io.Serializable
{
    /**
     * Getter for property attributes.
     * @return List the value of property attributes.
     */
    List<? extends AttributeInterface> getAttributes();

    /**
     * Getter for property attribute.
     * @param index int; the index
     * @return AttributeInterface.
     */
    AttributeInterface getAttribute(int index);

    /**
     * Returns the color of the layer.
     * @return Color.
     */
    Color getColor();

    /**
     * sets the color of the layer.
     * @param color Color; the rgb-color
     */
    void setColor(Color color);

    /**
     * gets the outline color.
     * @return Color the rgb-color
     */
    Color getOutlineColor();

    /**
     * sets the outlineColor of the layer.
     * @param outlineColor Color; the rgb-color
     */
    void setOutlineColor(Color outlineColor);

    /**
     * Getter for property name.
     * @return String of property name.
     */
    String getName();

    /**
     * Setter for property name.
     * @param name String; New value of property name.
     */
    void setName(String name);

    /**
     * Getter for property dataSource.
     * @return DataSourceInterface the value of property dataSource.
     */
    DataSourceInterface getDataSource();

    /**
     * Setter for property attributes.
     * @param attributes List; the attributes to set
     */
    void setAttributes(List<? extends AttributeInterface> attributes);

    /**
     * Setter for property dataSource.
     * @param dataSource DataSourceInterface; New value of property dataSource.
     */
    void setDataSource(DataSourceInterface dataSource);

    /**
     * Getter for property minscale.
     * @return int the value of property minscale.
     */
    int getMinScale();

    /**
     * Setter for property minscale.
     * @param minscale int; New value of property minscale.
     */
    void setMinScale(int minscale);

    /**
     * Getter for property maxScale.
     * @return int the value of property maxScale.
     */
    int getMaxScale();

    /**
     * Setter for property maxScale.
     * @param maxScale int; New value of property maxScale.
     */
    void setMaxScale(int maxScale);

    /**
     * Getter for property status.
     * @return bollean the value of property status
     */
    boolean isStatus();

    /**
     * Setter for property status.
     * @param status boolean; New value of property status.
     */
    void setStatus(boolean status);

    /**
     * Getter for property transform.
     * @return boolean the value of property transform.
     */
    boolean isTransform();

    /**
     * Setter for property transform.
     * @param transform boolean; New value of property transform.
     */
    void setTransform(boolean transform);
}
