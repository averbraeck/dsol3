package nl.tudelft.simulation.dsol.animation.gis.map;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

import org.djutils.immutablecollections.ImmutableList;
import org.djutils.immutablecollections.ImmutableMap;

import nl.tudelft.simulation.dsol.animation.gis.GraphicsException;
import nl.tudelft.simulation.dsol.animation.gis.SerializableRectangle2D;

/**
 * This interface defines the map.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface MapInterface extends Serializable
{
    /** MINX contant. */
    byte MINX = 0;

    /** MINY contant. */
    byte MINY = 1;

    /** MAXX contant. */
    byte MAXX = 2;

    /** MAXY contant. */
    byte MAXY = 3;

    /** FEET contant. */
    byte FEET = 0;

    /** INCHES contant. */
    byte INCHES = 1;

    /** KILOMETERS contant. */
    byte KILOMETERS = 2;

    /** METERS contant. */
    byte METERS = 3;

    /** MILES contant. */
    byte MILES = 4;

    /** DD contant. */
    byte DD = 5;

    /** TEXT. */
    byte TEXT = 0;

    /** ANGLEDEg. */
    byte ANGLEDEG = 1;

    /** ANGLERAd. */
    byte ANGLERAD = 2;

    /** IMAGe. */
    byte IMAGE = 3;

    /** AIRPHOTO. */
    byte AIRPHOTO = 4;

    /** POLYGON. */
    byte POLYGON = 0;

    /** POINT. */
    byte POINT = 1;

    /** LINe. */
    byte LINE = 2;

    /** FEET_TO_METER. */
    double FEET_TO_METER = 0.3048;

    /** INCH_TO_METER. */
    double INCH_TO_METER = 0.0254;

    /** KILOMETER_TO_METER. */
    double KILOMETER_TO_METER = 1000;

    /** MILES_TO_METER. */
    double MILES_TO_METER = 1609.34;

    /** DD_TO_METER. */
    double DD_TO_METER = 111119;

    /** CENTIMETER_PER_INCH. */
    double CENTIMETER_PER_INCH = 2.54;

    /**
     * draws the map on a graphics object.
     * @param graphics Graphics2D; the graphics object
     * @return Graphics2D
     * @throws GraphicsException on drawing failure
     * @throws RemoteException on network failure
     */
    Graphics2D drawMap(Graphics2D graphics) throws GraphicsException, RemoteException;

    /**
     * Getter for property extent.
     * @return the extent of the map
     * @throws RemoteException on network exception
     */
    SerializableRectangle2D getExtent() throws RemoteException;

    /**
     * Getter for property image.
     * @return ImageInterface the value of property image.
     * @throws RemoteException on network exception
     */
    MapImageInterface getImage() throws RemoteException;

    /**
     * Getter for the map of layer names to property layers.
     * @return List the value of property layers.
     * @throws RemoteException on network exception
     */
    ImmutableMap<String, LayerInterface> getLayerMap() throws RemoteException;

    /**
     * Getter for all the property layers.
     * @return List the value of property layers.
     * @throws RemoteException on network exception
     */
    ImmutableList<LayerInterface> getAllLayers() throws RemoteException;

    /**
     * Getter for all the visible property layers.
     * @return List the value of property layers.
     * @throws RemoteException on network exception
     */
    ImmutableList<LayerInterface> getVisibleLayers() throws RemoteException;

    /**
     * Return whether the map has not been changed, and reset the same parameter to true.
     * @return whether the map has not been changed, and reset the same parameter to true
     * @throws RemoteException on network exception
     */
    boolean isSame() throws RemoteException;

    /**
     * Getter for property name.
     * @return String the value of property extent.
     * @throws RemoteException on network exception
     */
    String getName() throws RemoteException;

    /**
     * returns the scale of the map.
     * @return double the scale of the map in its units
     * @throws RemoteException on network exception
     */
    double getScale() throws RemoteException;

    /**
     * returns the scale of the Image.
     * @return double the unitPerPixel
     * @throws RemoteException on network exception
     */
    double getUnitImageRatio() throws RemoteException;

    /**
     * Getter for property units.
     * @return int the value of property units.
     * @throws RemoteException on network exception
     */
    int getUnits() throws RemoteException;

    /**
     * Setter for property extent.
     * @param extent Rectangle2D; New value of property extent.
     * @throws RemoteException on network exception
     */
    void setExtent(Rectangle2D extent) throws RemoteException;

    /**
     * Setter for property image.
     * @param image ImageInterface; New value of property image.
     * @throws RemoteException on network exception
     */
    void setImage(MapImageInterface image) throws RemoteException;

    /**
     * Setter for property layers.
     * @param layers List&lt;LayerInterface&gt;; New value of property layers.
     * @throws RemoteException on network exception
     */
    void setLayers(List<LayerInterface> layers) throws RemoteException;

    /**
     * Setter for property layers.
     * @param index int; Index value of layer
     * @param layer LayerInterface; New value of property layers.
     * @throws RemoteException on network exception
     */
    void setLayer(int index, LayerInterface layer) throws RemoteException;

    /**
     * Setter for property layers.
     * @param layer LayerInterface; New value of property layers.
     * @throws RemoteException on network exception
     */
    void addLayer(LayerInterface layer) throws RemoteException;

    /**
     * Hide a layer.
     * @param layer LayerInterface; the layer to hide
     * @throws RemoteException on network exception
     */
    void hideLayer(LayerInterface layer) throws RemoteException;

    /**
     * Show a layer.
     * @param layer LayerInterface; the layer to show
     * @throws RemoteException on network exception
     */
    void showLayer(LayerInterface layer) throws RemoteException;

    /**
     * Hide a layer.
     * @param layerName String; the name of the layer to hide
     * @throws RemoteException on network exception
     */
    void hideLayer(String layerName) throws RemoteException;

    /**
     * Show a layer.
     * @param layerName String; the name of the layer to show
     * @throws RemoteException on network exception
     */
    void showLayer(String layerName) throws RemoteException;

    /**
     * Setter for property name.
     * @param name String; New value of property name.
     * @throws RemoteException on network exception
     */
    void setName(String name) throws RemoteException;

    /**
     * Setter for property units.
     * @param units int; New value of property units.
     * @throws RemoteException on network exception
     */
    void setUnits(int units) throws RemoteException;

    /**
     * zooms the map with a particular factor.
     * @param zoomFactor double; (0=1)
     * @throws RemoteException on network exception
     */
    void zoom(double zoomFactor) throws RemoteException;

    /**
     * zooms the map based on a given position in the image.
     * @param pixelPosition Point2D; the position in the image
     * @param zoomFactor double; the zoomFactor (0=1)
     * @throws RemoteException on network exception
     */
    void zoomPoint(Point2D pixelPosition, double zoomFactor) throws RemoteException;

    /**
     * zooms the map based on a given rectangle.
     * @param rectangle SerializableRectangle2D; a rectangle in the map (image units)
     * @throws RemoteException on network exception
     */
    void zoomRectangle(SerializableRectangle2D rectangle) throws RemoteException;

    /**
     * return whether background is drawn or not.
     * @return drawBackground
     */
    boolean isDrawBackground();

    /**
     * set whether background is drawn or not.
     * @param drawBackground boolean; set drawBackground
     */
    void setDrawBackground(boolean drawBackground);

}
