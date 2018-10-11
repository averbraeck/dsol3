package nl.tudelft.simulation.introspection.gui;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 */
public class DefaultModelManager implements ModelManager
{
    /** the class of the defaultTableModel. */
    protected Class<?> defaultObjectTableModel = ObjectTableModel.class;

    /** the class of the defaultCollectionTableModel. */
    protected Class<?> defaultCollectionObjectTableModel = CollectionTableModel.class;

    /**
     * Bean Constructor for the model manager.
     */
    public DefaultModelManager()
    {
        this(ObjectTableModel.class, CollectionTableModel.class);
    }

    /**
     * Constructor for the model manager.
     * @param objectModelClass Class&lt;?&gt;; the objectModelClass
     * @param collectionModelClass Class&lt;?&gt;; the collectionModelClass
     */
    public DefaultModelManager(final Class<?> objectModelClass, final Class<?> collectionModelClass)
    {
        this.defaultObjectTableModel = objectModelClass;
        this.defaultCollectionObjectTableModel = collectionModelClass;
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getDefaultCollectionObjectTableModel()
    {
        return this.defaultCollectionObjectTableModel;
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getDefaultObjectTableModel()
    {
        return this.defaultObjectTableModel;
    }

    /** {@inheritDoc} */
    @Override
    public void setDefaultCollectionObjectTableModel(final Class<?> defaultCollectionObjectTableModel)
    {
        this.defaultCollectionObjectTableModel = defaultCollectionObjectTableModel;
    }

    /** {@inheritDoc} */
    @Override
    public void setDefaultObjectTableModel(final Class<?> defaultObjectTableModel)
    {
        this.defaultObjectTableModel = defaultObjectTableModel;
    }
}
