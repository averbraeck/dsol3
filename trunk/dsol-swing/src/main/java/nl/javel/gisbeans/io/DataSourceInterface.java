/*
 * DataSourceInterface.java
 * 
 * Created on April 17, 2002, 6:54 PM Last edited on October 11, 2002
 */
package nl.javel.gisbeans.io;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import nl.javel.gisbeans.geom.GisObject;
import nl.javel.gisbeans.geom.SerializableRectangle2D;

/**
 * @author <a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a> <br>
 *         <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.2
 * @version 1.0
 */
public interface DataSourceInterface extends java.io.Serializable
{
    /**
     * returns the columnNames of the attribute data
     * @return String[]
     */
    String[] getColumnNames();

    /**
     * returns the attribute data
     * @return the attributes
     * @throws IOException on IOException
     */
    String[][] getAttributes() throws IOException;

    /**
     * returns the URL of the datasource
     * @return URL the URL of the file
     */
    URL getDataSource();

    /**
     * returns the number of shapes of the particular datasource
     * @return int the number of shapes
     * @throws IOException possible file IO or database connection failures.
     */
    int getNumShapes() throws IOException;

    /**
     * returns a GisObject
     * @param index the number of the shape to be returned
     * @return GisObject returns a <code>nl.javel.gisbeans.geom.GisObject</code>
     * @throws IndexOutOfBoundsException whenever index>numShapes
     * @throws IOException on IOFailure
     */
    GisObject getShape(int index) throws IOException, IndexOutOfBoundsException;

    /**
     * returns all the shapes of the particular datasource
     * @return List the resulting ArrayList of <code>nl.javel.gisbeans.geom.GisObject</code>
     * @throws IOException on IOFailure
     */
    List getShapes() throws IOException;

    /**
     * returns the shapes of the particular datasource in a particular extent
     * @param rectangle the extent of the box (in geo-coordinates)
     * @return List the resulting ArrayList of <code>nl.javel.gisbeans.geom.GisObject</code>
     * @throws IOException on IOFailure
     */
    List getShapes(SerializableRectangle2D rectangle) throws IOException;

    /**
     * returns the shapes based on a particular value of the attributes
     * @param attribute the value of the attribute
     * @param columnName the columnName
     * @return List the resulting ArrayList of <code>nl.javel.gisbeans.geom.GisObject</code>
     * @throws IOException on IOFailure
     */
    List getShapes(String attribute, String columnName) throws IOException;

    /**
     * @return returns the type of this dataSouce
     * @throws IOException on IOFailure
     */
    int getType() throws IOException;
}