/*
 * MapInterface.java
 * 
 * Created on April 17, 2002, 7:23 PM Last edited on October 12, 2002
 */
package nl.javel.gisbeans.map;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.util.List;

import nl.javel.gisbeans.geom.SerializableRectangle2D;
import nl.tudelft.simulation.immutablecollections.ImmutableList;
import nl.tudelft.simulation.immutablecollections.ImmutableMap;

/**
 * This interface defines the map.
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.0
 */
public interface MapInterface extends java.io.Serializable
{
    /** MINX contant. */
    public static final byte MINX = 0;

    /** MINY contant. */
    public static final byte MINY = 1;

    /** MAXX contant. */
    public static final byte MAXX = 2;

    /** MAXY contant. */
    public static final byte MAXY = 3;

    /** FEET contant. */
    public static final byte FEET = 0;

    /** INCHES contant. */
    public static final byte INCHES = 1;

    /** KILOMETERS contant. */
    public static final byte KILOMETERS = 2;

    /** METERS contant. */
    public static final byte METERS = 3;

    /** MILES contant. */
    public static final byte MILES = 4;

    /** DD contant. */
    public static final byte DD = 5;

    /** UL */
    public static final byte UL = 0;

    /** Uc. */
    public static final byte UC = 1;

    /** UR */
    public static final byte UR = 2;

    /** CL */
    public static final byte CL = 3;

    /** Cc. */
    public static final byte CC = 4;

    /** CR */
    public static final byte CR = 5;

    /** LL */
    public static final byte LL = 6;

    /** Lc. */
    public static final byte LC = 7;

    /** LR */
    public static final byte LR = 8;

    /** TEXT */
    public static final byte TEXT = 0;

    /** ANGLEDEg. */
    public static final byte ANGLEDEG = 1;

    /** ANGLERAd. */
    public static final byte ANGLERAD = 2;

    /** IMAGe. */
    public static final byte IMAGE = 3;

    /** AIRPHOTO */
    public static final byte AIRPHOTO = 4;

    /** POLYGON */
    public static final byte POLYGON = 0;

    /** POINT */
    public static final byte POINT = 1;

    /** LINe. */
    public static final byte LINE = 2;

    /** FEET_TO_METER */
    public static final double FEET_TO_METER = 0.3048;

    /** INCH_TO_METER */
    public static final double INCH_TO_METER = 0.0254;

    /** KILOMETER_TO_METER */
    public static final double KILOMETER_TO_METER = 1000;

    /** MILES_TO_METER */
    public static final double MILES_TO_METER = 1609.34;

    /** DD_TO_METER */
    public static final double DD_TO_METER = 111119;

    /** CENTIMETER_PER_INCH */
    public static final double CENTIMETER_PER_INCH = 2.54;

    /**
     * draws the legend on a graphics object
     * @return Graphics2D
     * @param graphics Graphics2D; the graphics object
     * @throws GraphicsException on drawing failure
     * @throws RemoteException on network failure
     */
    public Graphics2D drawLegend(Graphics2D graphics) throws GraphicsException, RemoteException;

    /**
     * draws the map on a graphics object
     * @param graphics Graphics2D; the graphics object
     * @return Graphics2D
     * @throws GraphicsException on drawing failure
     * @throws RemoteException on network failure
     */
    public Graphics2D drawMap(Graphics2D graphics) throws GraphicsException, RemoteException;

    /**
     * draws the reference map on a graphics object
     * @param graphics Graphics2D; the graphics object
     * @return Graphics2D
     * @throws GraphicsException on drawing failure
     * @throws RemoteException on network failure
     */
    public Graphics2D drawReferenceMap(Graphics2D graphics) throws GraphicsException, RemoteException;

    /**
     * draws the scalebar on a graphics object
     * @param graphics Graphics2D; the graphics object
     * @return Graphics2D
     * @throws GraphicsException on drawing failure
     * @throws RemoteException on network failure
     */
    public Graphics2D drawScalebar(Graphics2D graphics) throws GraphicsException, RemoteException;

    /**
     * Getter for property extent
     * @return the extent of the map
     * @throws RemoteException on network exception
     */
    public SerializableRectangle2D getExtent() throws RemoteException;

    /**
     * Getter for property image
     * @return ImageInterface the value of property image.
     * @throws RemoteException on network exception
     */
    public ImageInterface getImage() throws RemoteException;

    /**
     * Getter for the map of layer names to property layers
     * @return List the value of property layers.
     * @throws RemoteException on network exception
     */
    public ImmutableMap<String, LayerInterface> getLayerMap() throws RemoteException;

    /**
     * Getter for all the property layers
     * @return List the value of property layers.
     * @throws RemoteException on network exception
     */
    public ImmutableList<LayerInterface> getAllLayers() throws RemoteException;

    /**
     * Getter for all the visible property layers
     * @return List the value of property layers.
     * @throws RemoteException on network exception
     */
    public ImmutableList<LayerInterface> getVisibleLayers() throws RemoteException;

    /**
     * Return whether the map has not been changed, and reset the same parameter to true.
     * @return whether the map has not been changed, and reset the same parameter to true
     * @throws RemoteException on network exception
     */
    public boolean isSame() throws RemoteException;

    /**
     * Getter for property name
     * @return String the value of property extent.
     * @throws RemoteException on network exception
     */
    public String getName() throws RemoteException;

    /**
     * Getter for property referenceMap
     * @return ReferenceMap the value of property referenceMap.
     * @throws RemoteException on network exception
     */
    public ReferenceMapInterface getReferenceMap() throws RemoteException;

    /**
     * returns the sclale of the map
     * @return double the scale of the map in its units
     * @throws RemoteException on network exception
     */
    public double getScale() throws RemoteException;

    /**
     * returns the scale of the Image
     * @return double the unitPerPixel
     * @throws RemoteException on network exception
     */
    public double getUnitImageRatio() throws RemoteException;

    /**
     * Getter for property units
     * @return int the value of property units.
     * @throws RemoteException on network exception
     */
    public int getUnits() throws RemoteException;

    /**
     * Setter for property extent.
     * @param extent Rectangle2D; New value of property extent.
     * @throws RemoteException on network exception
     */
    public void setExtent(Rectangle2D extent) throws RemoteException;

    /**
     * Setter for property image.
     * @param image ImageInterface; New value of property image.
     * @throws RemoteException on network exception
     */
    public void setImage(ImageInterface image) throws RemoteException;

    /**
     * Setter for property layers.
     * @param layers List&lt;LayerInterface&gt;; New value of property layers.
     * @throws RemoteException on network exception
     */
    public void setLayers(List<LayerInterface> layers) throws RemoteException;

    /**
     * Setter for property layers.
     * @param index int; Index value of layer
     * @param layer LayerInterface; New value of property layers.
     * @throws RemoteException on network exception
     */
    public void setLayer(int index, LayerInterface layer) throws RemoteException;

    /**
     * Setter for property layers.
     * @param layer LayerInterface; New value of property layers.
     * @throws RemoteException on network exception
     */
    public void addLayer(LayerInterface layer) throws RemoteException;

    /**
     * Hide a layer.
     * @param layer LayerInterface; the layer to hide
     * @throws RemoteException on network exception
     */
    public void hideLayer(LayerInterface layer) throws RemoteException;

    /**
     * Show a layer.
     * @param layer LayerInterface; the layer to show
     * @throws RemoteException on network exception
     */
    public void showLayer(LayerInterface layer) throws RemoteException;

    /**
     * Hide a layer.
     * @param layerName String; the name of the layer to hide
     * @throws RemoteException on network exception
     */
    public void hideLayer(String layerName) throws RemoteException;

    /**
     * Show a layer.
     * @param layerName String; the name of the layer to show
     * @throws RemoteException on network exception
     */
    public void showLayer(String layerName) throws RemoteException;

    /**
     * Setter for property name.
     * @param name String; New value of property name.
     * @throws RemoteException on network exception
     */
    public void setName(String name) throws RemoteException;

    /**
     * Setter for property referenceMap.
     * @param referenceMap ReferenceMapInterface; New value of property referenceMap.
     * @throws RemoteException on network exception
     */
    public void setReferenceMap(ReferenceMapInterface referenceMap) throws RemoteException;

    /**
     * Setter for property units.
     * @param units int; New value of property units.
     * @throws RemoteException on network exception
     */
    public void setUnits(int units) throws RemoteException;

    /**
     * zooms the map with a prticular factor
     * @param zoomFactor double; (0=1)
     * @throws RemoteException on network exception
     */
    public void zoom(double zoomFactor) throws RemoteException;

    /**
     * zooms the map based on a given position in the image
     * @param pixelPosition Point2D; the position in the image
     * @param zoomFactor double; the zoomFactor (0=1)
     * @throws RemoteException on network exception
     */
    public void zoomPoint(Point2D pixelPosition, double zoomFactor) throws RemoteException;

    /**
     * zooms the map based on a given rectangle
     * @param rectangle SerializableRectangle2D; a rectangle in the map (image units)
     * @throws RemoteException on network exception
     */
    public void zoomRectangle(SerializableRectangle2D rectangle) throws RemoteException;

    /**
     * return whether background is drawn or not.
     * @return drawBackground
     */
    public boolean isDrawBackground();

    /**
     * set whether background is drawn or not.
     * @param drawBackground boolean; set drawBackground
     */
    public void setDrawBackground(final boolean drawBackground);

}
