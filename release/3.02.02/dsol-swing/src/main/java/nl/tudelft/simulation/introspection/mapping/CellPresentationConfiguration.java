package nl.tudelft.simulation.introspection.mapping;

/**
 * An interface defining the services of a table presentation configuration. It is used to initialize tables with
 * renderers and editors for different cell types. Renderer and editor classes are not checked for type-safety.
 * Normally, they should be assignable from {@link javax.swing.table.TableCellRenderer}.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 */
public interface CellPresentationConfiguration
{
    /**
     * Returns all the cell-renderer combinations available in this configuration
     * @return A double class array of cardinality M-2. Tuple [i][j] defines M cell class - render class combinations,
     *         with 'i' identifying the row. 'j=0' identifies the cell class, 'j=1' identifies the renderer class.
     */
    Class<?>[][] getRenderers();

    /**
     * Returns all the cell-editor combinations available in this configuration
     * @return A double class array of cardinality M-2. Tuple [i][j] defines M cell class - editor class combinations,
     *         with 'i' identifying the row. 'j=0' identifies the cell class, 'j=1' identifies the editor class.
     */
    Class<?>[][] getEditors();
}
