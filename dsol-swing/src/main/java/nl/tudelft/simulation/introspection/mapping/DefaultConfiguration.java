package nl.tudelft.simulation.introspection.mapping;

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;

import nl.tudelft.simulation.introspection.gui.ExpandButton;

/**
 * A default implementation of the {see CellPresentationConfiguration} interface. Editors and renders are provided for
 * the JComponent, Color and Font classes. Furthermore, a special editor is provided for the ExpandButton class, to
 * implement the pop-up behaviour of the {see nl.tudelft.simulation.introspection.gui.ExpandButton}.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 */
public class DefaultConfiguration implements CellPresentationConfiguration
{
    /** the defaultConfiguation. */
    private static DefaultConfiguration defaultConfig;
    static
    {
        defaultConfig = new DefaultConfiguration();
        defaultConfig.addRenderer(JComponent.class, SwingCellRenderer.class);
        defaultConfig.addRenderer(Object.class, MyDefaultRenderer.class);
        defaultConfig.addRenderer(Object[].class, ArrayRenderer.class);
        defaultConfig.addRenderer(Collection.class, CollectionRenderer.class);
        defaultConfig.addRenderer(Color.class, MyColorRenderer.class);
        defaultConfig.addEditor(Color.class, MyColorEditor.class);
        defaultConfig.addEditor(JComponent.class, SwingCellEditor.class);
        defaultConfig.addEditor(Object.class, MyDefaultEditor.class);
        defaultConfig.addEditor(ExpandButton.class, ExpandButtonEditor.class);
    }

    /** the renderers. */
    private Set<Class<?>[]> renderers = new HashSet<Class<?>[]>();

    /** the editors. */
    private Set<Class<?>[]> editors = new HashSet<Class<?>[]>();

    /**
     * @return Returns the defaultConfiguration
     */
    public static CellPresentationConfiguration getDefaultConfiguration()
    {
        return defaultConfig;
    }

    /**
     * adds a renderer to the configuration
     * @param cellType the cellType
     * @param renderingClass the renderingClass
     */
    protected synchronized void addRenderer(final Class<?> cellType, final Class<?> renderingClass)
    {
        this.renderers.add(new Class[]{cellType, renderingClass});
    }

    /**
     * adds an editingClass to a cellType
     * @param cellType the cellType
     * @param editingClass an editingClass
     */
    protected void addEditor(final Class<?> cellType, final Class<?> editingClass)
    {
        this.editors.add(new Class[]{cellType, editingClass});
    }

    /** {@inheritDoc} */
    @Override
    public Class<?>[][] getRenderers()
    {
        return this.renderers.toArray(new Class[0][0]);
    }

    /** {@inheritDoc} */
    @Override
    public Class<?>[][] getEditors()
    {
        return this.editors.toArray(new Class[0][0]);
    }
}
