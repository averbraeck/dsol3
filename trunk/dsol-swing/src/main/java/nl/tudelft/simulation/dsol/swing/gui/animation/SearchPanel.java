package nl.tudelft.simulation.dsol.swing.gui.animation;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.swing.gui.appearance.AppearanceControl;
import nl.tudelft.simulation.dsol.swing.gui.util.Icons;

/**
 * The search panel. Code based on OpenTrafficSim project component with the same purpose.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class SearchPanel extends JPanel implements ActionListener, FocusListener, DocumentListener
{
    /** ... */
    private static final long serialVersionUID = 20200127L;

    /** The animation panel. */
    private final DSOLAnimationTab dsolAnimationPanel;

    /** The type-of-object-to-search-for selector. */
    private final JComboBox<ObjectKind<?>> typeToSearch;

    /** Id of the object to search for. */
    private final JTextField idTextField;

    /** Track object check box. */
    private final JCheckBox trackObject;

    /**
     * Construct a new search panel.
     * @param dsolAnimationPanel DSOLAnimationPanel; the animation panel
     */
    public SearchPanel(final DSOLAnimationTab dsolAnimationPanel)
    {
        this.dsolAnimationPanel = dsolAnimationPanel;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(new JLabel("    ")); // insert some white space in the GUI
        this.add(new JLabel(Icons.loadIcon("/Search.png")));
        ObjectKind<?>[] objectKinds = new ObjectKind[] { new ObjectKind<Locatable>("Person") // XXX: was: Person 
        {
            @Override
            Locatable searchNetwork(final String idString) // XXX: was: Person
            {
                try
                {
//                    int id = Integer.parseInt(idString);
//                    if (dsolAnimationPanel.getModel().getPersonMap().containsKey(id))
//                    {
//                        return dsolAnimationPanel.getModel().getPersonMap().get(id);
//                    }
                }
                catch (NumberFormatException nfe)
                {
                    return null;
                }
                return null;
            }
        },
                // could be expanded with ObjectKind Bus, Metro, Tram, ...
        };
        this.typeToSearch = new JComboBox<ObjectKind<?>>(objectKinds);
        this.add(this.typeToSearch);

        /** Text field with appearance control. */
        class AppearanceControlTextField extends JTextField implements AppearanceControl
        {
            /** */
            private static final long serialVersionUID = 20180207L;

            /** {@inheritDoc} */
            @Override
            public boolean isForeground()
            {
                return false;
            }

            /** {@inheritDoc} */
            @Override
            public boolean isBackground()
            {
                return false;
            }

            /** {@inheritDoc} */
            @Override
            public String toString()
            {
                return "AppearanceControlLabel []";
            }
        }

        this.idTextField = new AppearanceControlTextField();
        this.idTextField.setPreferredSize(new Dimension(100, 0));
        this.add(this.idTextField);
        this.trackObject = new JCheckBox("track");
        this.add(this.trackObject);
        this.trackObject.setActionCommand("Tracking status changed");
        this.idTextField.setActionCommand("Id changed");
        this.typeToSearch.setActionCommand("Type changed");
        this.trackObject.addActionListener(this);
        this.idTextField.addActionListener(this);
        this.typeToSearch.addActionListener(this);
        this.idTextField.addFocusListener(this);
        this.idTextField.getDocument().addDocumentListener(this);
    }

    /**
     * Update all values at once.
     * @param objectKey String; key of the object type to search
     * @param personId String; id of object to search
     * @param track boolean; if true; track continuously; if false; center on it, but do not track
     */
    public void selectAndTrackObject(final String objectKey, final int personId, final boolean track)
    {
        for (int index = this.typeToSearch.getItemCount(); --index >= 0;)
        {
            if (this.typeToSearch.getItemAt(index).getKey().equals(objectKey))
            {
                this.typeToSearch.setSelectedIndex(index);
            }
        }
        this.trackObject.setSelected(track);
        this.idTextField.setText("" + personId);
        actionPerformed(null);
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        // TODO: turn autopan on for search
//        this.dsolAnimationPanel.setAutoPan(this.idTextField.getText(), (ObjectKind<?>) this.typeToSearch.getSelectedItem(),
//                this.trackObject.isSelected());
    }

    /** {@inheritDoc} */
    @Override
    public final void focusGained(final FocusEvent e)
    {
        actionPerformed(null);
    }

    /** {@inheritDoc} */
    @Override
    public final void focusLost(final FocusEvent e)
    {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public final void insertUpdate(final DocumentEvent e)
    {
        actionPerformed(null);
    }

    /** {@inheritDoc} */
    @Override
    public final void removeUpdate(final DocumentEvent e)
    {
        actionPerformed(null);
    }

    /** {@inheritDoc} */
    @Override
    public final void changedUpdate(final DocumentEvent e)
    {
        actionPerformed(null);
    }

    /**
     * Entries in the typeToSearch JComboBox of the search panel.
     * @param <T> Type of object identified by key
     */
    public abstract static class ObjectKind<T extends Locatable>
    {
        /** The key of this ObjectKind. */
        private final String key;

        /**
         * Construct a new ObjectKind (entry in the combo box).
         * @param key String; the key of the new ObjectKind
         */
        ObjectKind(final String key)
        {
            this.key = key;
        }

        /**
         * Retrieve the key.
         * @return String; the key
         */
        public Object getKey()
        {
            return this.key;
        }

        /**
         * Lookup an object of type T in an network.
         * @param id String; id of the object to return
         * @return T; the object in the network of the correct type and matching id, or null if no matching object was found.
         */
        abstract T searchNetwork(String id);

        /**
         * Produce the text that will appear in the combo box. This method should be overridden to implement localization.
         */
        @Override
        public String toString()
        {
            return this.key;
        }
    }
}
