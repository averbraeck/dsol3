package nl.tudelft.simulation.introspection.mapping;

/**
 * An interface defining the services of a table presentation configuration. It is used to initialize tables with
 * renderers and editors for different cell types. Renderer and editor classes are not checked for type-safety.
 * Normally, they should be assignable from {@link javax.swing.table.TableCellRenderer} and
 * @author (c) 2003 <a href="http://www.tudelft.nl">Delft University of Technology </a>, Delft, the Netherlands <br>
 *         <a href="http://www.tbm.tudelft.nl">Faculty of Technology, Policy and Management </a> <br>
 *         <a href="http://www.sk.tbm.tudelft.nl">Department of System Engineering </a> <br>
 *         Main researcher : <a href="http://www.tbm.tudelft.nl/webstaf/alexandv/">Dr. Ir. A. Verbraeck <a/><br>
 *         Assistant researchers <a href="https://www.linkedin.com/in/peterhmjacobs">Ir. P.H.M. Jacobs </a> and <a
 *         href="http://www.tbm.tudelft.nl/webstaf/nielsl">Ir. N.A. Lang </a>
 */
public interface CellPresentationConfiguration
{
    /**
     * Returns all the cell-renderer combinations available in this configuration
     * @return A double class array of cardinality M-2. Tuple [i][j] defines M cell class - render class combinations,
     *         with 'i' identifying the row. 'j=0' identifies the cell class, 'j=1' identifies the renderer class.
     */
    Class[][] getRenderers();

    /**
     * Returns all the cell-editor combinations available in this configuration
     * @return A double class array of cardinality M-2. Tuple [i][j] defines M cell class - editor class combinations,
     *         with 'i' identifying the row. 'j=0' identifies the cell class, 'j=1' identifies the editor class.
     */
    Class[][] getEditors();
}
