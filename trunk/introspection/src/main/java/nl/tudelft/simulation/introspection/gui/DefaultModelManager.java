/*
 * Created on May 8, 2004
 */
package nl.tudelft.simulation.introspection.gui;

/**
 * <br>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">
 * www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @version May 29, 2004 <br>
 * @author <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander
 *         Verbraeck </a>
 */
/**
 * A simple implementation.
 * 
 * @author Niels Lang
 */
public class DefaultModelManager implements ModelManager
{
    /** the class of the defaultTableModel */
    protected Class defaultObjectTableModel = ObjectTableModel.class;

    /** the class of the defaultCollectionTableModel */
    protected Class defaultCollectionObjectTableModel = CollectionTableModel.class;

    /**
     * Constructor
     */
    public DefaultModelManager()
    {
        this(ObjectTableModel.class, CollectionTableModel.class);
    }

    /**
     * Constructor
     * 
     * @param objectModelClass the objectModelClass
     * @param collectionModelClass the collectionModelClass
     */
    public DefaultModelManager(final Class objectModelClass,
            final Class collectionModelClass)
    {
        this.defaultObjectTableModel = objectModelClass;
        this.defaultCollectionObjectTableModel = collectionModelClass;
    }

    /**
     * @see nl.tudelft.simulation.introspection.gui.ModelManager
     *      #getDefaultCollectionObjectTableModel()
     */
    public Class getDefaultCollectionObjectTableModel()
    {
        return this.defaultCollectionObjectTableModel;
    }

    /**
     * @see nl.tudelft.simulation.introspection.gui.ModelManager
     *      #getDefaultObjectTableModel()
     */
    public Class getDefaultObjectTableModel()
    {
        return this.defaultObjectTableModel;
    }

    /**
     * @see nl.tudelft.simulation.introspection.gui.ModelManager
     *      #setDefaultCollectionObjectTableModel(java.lang.Class)
     */
    public void setDefaultCollectionObjectTableModel(
            final Class defaultCollectionObjectTableModel)
    {
        this.defaultCollectionObjectTableModel = defaultCollectionObjectTableModel;
    }

    /**
     * @see nl.tudelft.simulation.introspection.gui.ModelManager
     *      #setDefaultObjectTableModel(java.lang.Class)
     */
    public void setDefaultObjectTableModel(final Class defaultObjectTableModel)
    {
        this.defaultObjectTableModel = defaultObjectTableModel;
    }
}