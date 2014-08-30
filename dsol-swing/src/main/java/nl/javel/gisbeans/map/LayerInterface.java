/*
 * Image.java
 * 
 * Created on April 17, 2002, 7:23 PM Last edited on October 11, 2002
 */
package nl.javel.gisbeans.map;

import java.awt.Color;
import java.util.List;

import nl.javel.gisbeans.io.DataSourceInterface;

/**
 * This interface defines the layer of the map
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.0
 * @version 1.0
 */
public interface LayerInterface extends java.io.Serializable
{
    /**
     * sets the symbols for this layer
     * @param symbols the symbols
     */
    public void setSymbols(List symbols);

    /**
     * returns the symbols used in this layer
     * @return List the symbol list
     */
    public List getSymbols();

    /**
     * Getter for property attributes
     * @return List the value of property attributes.
     */
    public List getAttributes();

    /**
     * Getter for property attribute
     * @param index
     * @return AttributeInterface.
     */
    public AttributeInterface getAttribute(int index);

    /**
     * Returns the color of the layer.
     * @return Color.
     */
    public Color getColor();

    /**
     * sets the color of the layer
     * @param color the rgb-color
     */
    public void setColor(Color color);

    /**
     * gets the outline color
     * @return Color the rgb-color
     */
    public Color getOutlineColor();

    /**
     * sets the outlineColor of the layer
     * @param outlineColor the rgb-color
     */
    public void setOutlineColor(Color outlineColor);

    /**
     * Getter for property name.
     * @return String of property name.
     */
    public String getName();

    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name);

    /**
     * Getter for property dataSource.
     * @return DataSourceInterface the value of property dataSource.
     */
    public DataSourceInterface getDataSource();

    /**
     * Setter for property attributes
     * @param attributes
     */
    public void setAttributes(List attributes);

    /**
     * Setter for property dataSource.
     * @param dataSource New value of property dataSource.
     */
    public void setDataSource(DataSourceInterface dataSource);

    /**
     * Getter for property minscale.
     * @return int the value of property minscale.
     */
    public int getMinScale();

    /**
     * Setter for property minscale.
     * @param minscale New value of property minscale.
     */
    public void setMinScale(int minscale);

    /**
     * Getter for property maxScale.
     * @return int the value of property maxScale.
     */
    public int getMaxScale();

    /**
     * Setter for property maxScale.
     * @param maxScale New value of property maxScale.
     */
    public void setMaxScale(int maxScale);

    /**
     * Getter for property status
     * @return bollean the value of property status
     */
    public boolean isStatus();

    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(boolean status);

    /**
     * Getter for property transform.
     * @return boolean the value of property transform.
     */
    public boolean isTransform();

    /**
     * Setter for property transform.
     * @param transform New value of property transform.
     */
    public void setTransform(boolean transform);
}
