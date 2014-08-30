package nl.tudelft.simulation.introspection.mapping;

import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;

import nl.tudelft.simulation.introspection.gui.ExpandButton;

/**
 * A default implementation of the {see CellPresentationConfiguration} interface. Editors and renders are provided for
 * the JComponent, Color and Font classes. Furthermore, a special editor is provided for the ExpandButton class, to
 * implement the pop-up behaviour of the {see nl.tudelft.simulation.introspection.gui.ExpandButton}.
 * @author (c) 2003 <a href="http://www.tudelft.nl">Delft University of Technology </a>, Delft, the Netherlands <br>
 *         <a href="http://www.tbm.tudelft.nl">Faculty of Technology, Policy and Management </a> <br>
 *         <a href="http://www.sk.tbm.tudelft.nl">Department of System Engineering </a> <br>
 *         Main researcher : <a href="http://www.tbm.tudelft.nl/averbraeck">Dr. Ir. A. Verbraeck <a/><br>
 *         Assistant researchers <a href="https://www.linkedin.com/in/peterhmjacobs">Ir. P.H.M. Jacobs </a> and <a
 *         href="http://www.tbm.tudelft.nl/webstaf/nielsl">Ir. N.A. Lang </a>
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
    private Set<Class[]> renderers = new HashSet<Class[]>();

    /** the editors. */
    private Set<Class[]> editors = new HashSet<Class[]>();

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
    protected synchronized void addRenderer(final Class cellType, final Class renderingClass)
    {
        this.renderers.add(new Class[]{cellType, renderingClass});
    }

    /**
     * adds an editingClass to a cellType
     * @param cellType the cellType
     * @param editingClass an editingClass
     */
    protected void addEditor(final Class cellType, final Class editingClass)
    {
        this.editors.add(new Class[]{cellType, editingClass});
    }

    /** {@inheritDoc} */
    public Class[][] getRenderers()
    {
        return this.renderers.toArray(new Class[0][0]);
    }

    /** {@inheritDoc} */
    public Class[][] getEditors()
    {
        return this.editors.toArray(new Class[0][0]);
    }
}
