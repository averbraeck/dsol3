package nl.tudelft.simulation.dsol.swing.gui.animation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.TimedEvent;
import org.djutils.exceptions.Throw;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.swing.animation.D2.AnimationPanel;
import nl.tudelft.simulation.dsol.swing.gui.util.Icons;
import nl.tudelft.simulation.language.DSOLException;

/**
 * Animation panel with various controls. Code based on OpenTrafficSim project and Meslabs project component with the same
 * purpose.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class DSOLAnimationTab extends JPanel implements ActionListener, EventListenerInterface
{
    /** */
    private static final long serialVersionUID = 20150617L;

    /** the simulator. */
    private final SimulatorInterface<?, ?, ?> simulator;

    /** The animation panel on tab position 0. */
    private final /* XXX: AutoPan */ AnimationPanel animationPanel;

    /** Border panel in which the animation is shown. */
    private final JPanel borderPanel;

    /** Toggle panel with which animation features can be shown/hidden. */
    private final JPanel togglePanel;

    /** Demo panel. */
    private JPanel demoPanel = null;

    /** Map of toggle names to toggle animation classes. */
    private Map<String, Class<? extends Locatable>> toggleLocatableMap = new LinkedHashMap<>();

    /** Set of animation classes to toggle buttons. */
    private Map<Class<? extends Locatable>, JToggleButton> toggleButtons = new LinkedHashMap<>();

    /** The coordinates of the cursor. */
    private final JLabel coordinateField;

    /** The person count field. */
    // XXX: private final JLabel objectCountField;

    /** The person count. */
    // XXX: private int objectCount = 0;

    /** The animation buttons. */
    private final ArrayList<JButton> buttons = new ArrayList<>();

    /** The formatter for the world coordinates. */
    private static final NumberFormat FORMATTER = NumberFormat.getInstance();

    /** Has the window close handler been registered? */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected boolean closeHandlerRegistered = false;

    /** Indicate the window has been closed and the timer thread can stop. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected boolean windowExited = false;

    /** XXX: Id of object to auto pan to. */
    // private String autoPanId = null;

    /** XXX: Type of object to auto pan to. */
    // private SearchPanel.ObjectKind<?> autoPanKind = null;

    /** XXX: The search panel. */
    // private final SearchPanel searchPanel;

    /** XXX: Track auto pan object continuously? */
    // private boolean autoPanTrack = false;

    /** XXX: Track auto on the next paintComponent operation; then copy state from autoPanTrack. */
    // private boolean autoPanOnNextPaintComponent = false;

    /** Initialize the formatter. */
    static
    {
        FORMATTER.setMaximumFractionDigits(3);
    }

    /**
     * Construct a panel for the animation of a DSOLModel.
     * @param homeExtent Bounds2d; initial extent of the animation
     * @param simulator SimulatorInterface; the simulator
     * @throws RemoteException when notification of the animation panel fails
     * @throws DSOLException when simulator does not implement the AnimatorInterface
     */
    @SuppressWarnings("unchecked")
    public DSOLAnimationTab(final Bounds2d homeExtent, final SimulatorInterface<?, ?, ?> simulator)
            throws RemoteException, DSOLException
    {
        super();
        this.simulator = simulator;
        // Add the animation panel as a tab.
        this.animationPanel = new /* AutoPan */ AnimationPanel(homeExtent, simulator);
        this.animationPanel.showGrid(true);
        this.borderPanel = new JPanel(new BorderLayout());
        this.borderPanel.add(this.animationPanel, BorderLayout.CENTER);

        // Include the TogglePanel WEST of the animation.
        this.togglePanel = new JPanel();
        this.togglePanel.setLayout(new BoxLayout(this.togglePanel, BoxLayout.Y_AXIS));
        this.borderPanel.add(this.togglePanel, BorderLayout.WEST);

        // add the buttons for home, zoom all, grid, and mouse coordinates
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        this.borderPanel.add(buttonPanel, BorderLayout.NORTH);
        buttonPanel.add(makeButton("allButton", "/Expand.png", "ZoomAll", "Zoom whole network", true));
        buttonPanel.add(makeButton("homeButton", "/Home.png", "Home", "Zoom to original extent", true));
        buttonPanel.add(makeButton("gridButton", "/Grid.png", "Grid", "Toggle grid on/off", true));
        buttonPanel.add(new JLabel("   "));

        // add info labels next to buttons
        JPanel infoTextPanel = new JPanel();
        buttonPanel.add(infoTextPanel);
        infoTextPanel.setMinimumSize(new Dimension(250, 35));
        infoTextPanel.setPreferredSize(new Dimension(250, 35));
        infoTextPanel.setLayout(new BoxLayout(infoTextPanel, BoxLayout.Y_AXIS));
        this.coordinateField = new JLabel("Mouse: ");
        this.coordinateField.setMinimumSize(new Dimension(250, 10));
        this.coordinateField.setPreferredSize(new Dimension(250, 10));
        infoTextPanel.add(this.coordinateField);

        setLayout(new BorderLayout());
        add(this.borderPanel, BorderLayout.CENTER);

        /*-
        // XXX: gtu fields
        JPanel personPanel = new JPanel();
        personPanel.setAlignmentX(0.0f);
        personPanel.setLayout(new BoxLayout(personPanel, BoxLayout.X_AXIS));
        personPanel.setMinimumSize(new Dimension(250, 10));
        personPanel.setPreferredSize(new Dimension(250, 10));
        infoTextPanel.add(personPanel);
        
        // XXX: person counter
        this.personCountField = new JLabel("0 persons");
        this.personCount = getModel().getPersonMap().size();
        personPanel.add(this.personCountField);
        setGtuCountText();
        */

        // XXX: search panel
        // this.searchPanel = new SearchPanel(dsolAnimationPanel);
        // this.add(this.searchPanel, BorderLayout.SOUTH);

        // Tell the animation to build the list of animation objects.
        this.animationPanel.notify(new TimedEvent(Replication.START_REPLICATION_EVENT, simulator.getSourceId(), null,
                simulator.getSimulatorTime()));

        // switch off the X and Y coordinates in a tooltip.
        this.animationPanel.setShowToolTip(false);

        // run the update task for the mouse coordinate panel
        new UpdateTimer().start();
    }

    // /**
    // * Provide access to the search panel.
    // * @return SearchPanel; the search panel
    // */
    // public SearchPanel getSearchPanel()
    // {
    // return this.searchPanel;
    // }
    //
    // /**
    // * Change auto pan target.
    // * @param newAutoPanId String; id of object to track (or
    // * @param newAutoPanKind String; kind of object to track
    // * @param newAutoPanTrack boolean; if true; tracking is continuously; if false; tracking is once
    // */
    // public void setAutoPan(final String newAutoPanId, final SearchPanel.ObjectKind<?> newAutoPanKind,
    // final boolean newAutoPanTrack)
    // {
    // this.autoPanId = newAutoPanId;
    // this.autoPanKind = newAutoPanKind;
    // this.autoPanTrack = newAutoPanTrack;
    // this.autoPanOnNextPaintComponent = true;
    // // System.out.println("AutoPan id=" + newAutoPanId + ", kind=" + newAutoPanKind + ", track=" + newAutoPanTrack);
    // if (null != this.autoPanId && this.autoPanId.length() > 0 && null != this.autoPanKind)
    // {
    // DSOLAnimationTab.this.animationPanel.repaint();
    // }
    // }

    /**
     * Create a button.
     * @param name String; name of the button
     * @param iconPath String; path to the resource
     * @param actionCommand String; the action command
     * @param toolTipText String; the hint to show when the mouse hovers over the button
     * @param enabled boolean; true if the new button must initially be enable; false if it must initially be disabled
     * @return JButton
     */
    private JButton makeButton(final String name, final String iconPath, final String actionCommand, final String toolTipText,
            final boolean enabled)
    {
        JButton result = new JButton(Icons.loadIcon(iconPath));
        result.setPreferredSize(new Dimension(34, 32));
        result.setName(name);
        result.setEnabled(enabled);
        result.setActionCommand(actionCommand);
        result.setToolTipText(toolTipText);
        result.addActionListener(this);
        this.buttons.add(result);
        return result;
    }

    /**
     * Add a button for toggling an animatable class on or off. Button icons for which 'idButton' is true will be placed to the
     * right of the previous button, which should be the corresponding button without the id. An example is an icon for
     * showing/hiding the class 'Lane' followed by the button to show/hide the Lane ids.
     * @param name String; the name of the button
     * @param locatableClass Class&lt;? extends Locatable&gt;; the class for which the button holds (e.g., Person.class)
     * @param iconPath String; the path to the 24x24 icon to display
     * @param toolTipText String; the tool tip text to show when hovering over the button
     * @param initiallyVisible boolean; whether the class is initially shown or not
     * @param idButton boolean; id button that needs to be placed next to the previous button
     */
    public void addToggleAnimationButtonIcon(final String name, final Class<? extends Locatable> locatableClass,
            final String iconPath, final String toolTipText, final boolean initiallyVisible, final boolean idButton)
    {
        JToggleButton button;
        Icon icon = Icons.loadIcon(iconPath);
        Icon unIcon = Icons.loadGrayscaleIcon(iconPath);
        button = new JCheckBox();
        button.setSelectedIcon(icon);
        button.setIcon(unIcon);
        button.setPreferredSize(new Dimension(32, 28));
        button.setName(name);
        button.setEnabled(true);
        button.setSelected(initiallyVisible);
        button.setActionCommand(name);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);

        // place an Id button to the right of the corresponding content button
        if (idButton && this.togglePanel.getComponentCount() > 0)
        {
            JPanel lastToggleBox = (JPanel) this.togglePanel.getComponent(this.togglePanel.getComponentCount() - 1);
            lastToggleBox.add(button);
        }
        else
        {
            JPanel toggleBox = new JPanel();
            toggleBox.setLayout(new BoxLayout(toggleBox, BoxLayout.X_AXIS));
            toggleBox.add(button);
            this.togglePanel.add(toggleBox);
            toggleBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        if (initiallyVisible)
        {
            this.animationPanel.showClass(locatableClass);
        }
        else
        {
            this.animationPanel.hideClass(locatableClass);
        }
        this.toggleLocatableMap.put(name, locatableClass);
        this.toggleButtons.put(locatableClass, button);
    }

    /**
     * Add a button for toggling an animatable class on or off.
     * @param name String; the name of the button
     * @param locatableClass Class&lt;? extends Locatable&gt;; the class for which the button holds (e.g., Person.class)
     * @param toolTipText String; the tool tip text to show when hovering over the button
     * @param initiallyVisible boolean; whether the class is initially shown or not
     */
    public void addToggleAnimationButtonText(final String name, final Class<? extends Locatable> locatableClass,
            final String toolTipText, final boolean initiallyVisible)
    {
        JToggleButton button;
        button = new JCheckBox(name);
        button.setName(name);
        button.setEnabled(true);
        button.setSelected(initiallyVisible);
        button.setActionCommand(name);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);

        JPanel toggleBox = new JPanel();
        toggleBox.setLayout(new BoxLayout(toggleBox, BoxLayout.X_AXIS));
        toggleBox.add(button);
        this.togglePanel.add(toggleBox);
        toggleBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (initiallyVisible)
        {
            this.animationPanel.showClass(locatableClass);
        }
        else
        {
            this.animationPanel.hideClass(locatableClass);
        }
        this.toggleLocatableMap.put(name, locatableClass);
        this.toggleButtons.put(locatableClass, button);
    }

    /**
     * Add a text to explain animatable classes.
     * @param text String; the text to show
     */
    public void addToggleText(final String text)
    {
        JPanel textBox = new JPanel();
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.X_AXIS));
        textBox.add(new JLabel(text));
        this.togglePanel.add(textBox);
        textBox.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final ActionEvent actionEvent)
    {
        String actionCommand = actionEvent.getActionCommand();
        try
        {
            if (actionCommand.equals("Home"))
            {
                this.animationPanel.home();
            }
            if (actionCommand.equals("ZoomAll"))
            {
                this.animationPanel.zoomAll();
            }
            if (actionCommand.equals("Grid"))
            {
                this.animationPanel.showGrid(!this.animationPanel.isShowGrid());
            }

            if (this.toggleLocatableMap.containsKey(actionCommand))
            {
                Class<? extends Locatable> locatableClass = this.toggleLocatableMap.get(actionCommand);
                this.animationPanel.toggleClass(locatableClass);
                this.togglePanel.repaint();
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Easy access to the AnimationPanel.
     * @return AnimationPanel
     */
    public final AnimationPanel getAnimationPanel()
    {
        return this.animationPanel;
    }

    /**
     * @return togglePanel
     */
    public final JPanel getTogglePanel()
    {
        return this.togglePanel;
    }

    /**
     * Creates a demo panel within the animation area.
     * @param position String; any string from BorderLayout indicating the position of the demo panel, except CENTER.
     * @throws IllegalStateException if the panel was already created
     */
    public void createDemoPanel(final DemoPanelPosition position)
    {
        Throw.when(this.demoPanel != null, IllegalStateException.class,
                "Attempt to create demo panel, but it's already created");
        Throw.whenNull(position, "Position may not be null.");
        Container parent = this.animationPanel.getParent();
        parent.remove(this.animationPanel);

        JPanel splitPanel = new JPanel(new BorderLayout());
        parent.add(splitPanel);
        splitPanel.add(this.animationPanel, BorderLayout.CENTER);

        this.demoPanel = new JPanel();
        this.demoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        splitPanel.add(this.demoPanel, position.getBorderLayoutPosition());
    }

    /**
     * Return a panel for on-screen demo controls. The panel is create on first call.
     * @return JPanel; panel
     */
    public JPanel getDemoPanel()
    {
        if (this.demoPanel == null)
        {
            createDemoPanel(DemoPanelPosition.RIGHT);
            // this.demoPanel = new JPanel();
            // this.demoPanel.setLayout(new BoxLayout(this.demoPanel, BoxLayout.Y_AXIS));
            // this.demoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            // this.demoPanel.setPreferredSize(new Dimension(300, 300));
            // getAnimationPanel().getParent().add(this.demoPanel, BorderLayout.EAST);
            this.demoPanel.addContainerListener(new ContainerListener()
            {
                @Override
                public void componentAdded(final ContainerEvent e)
                {
                    try
                    {
                        // setAppearance(getAppearance());
                    }
                    catch (NullPointerException exception)
                    {
                        //
                    }
                }

                @Override
                public void componentRemoved(final ContainerEvent e)
                {
                    //
                }
            });
        }
        return this.demoPanel;
    }

    /**
     * Update the checkmark related to a programmatically changed animation state.
     * @param locatableClass Class&lt;? extends Locatable&gt;; class to show the checkmark for
     */
    public void updateAnimationClassCheckBox(final Class<? extends Locatable> locatableClass)
    {
        JToggleButton button = this.toggleButtons.get(locatableClass);
        if (button == null)
        {
            return;
        }
        button.setSelected(getAnimationPanel().isShowClass(locatableClass));
    }

    /**
     * Display the latest world coordinate based on the mouse position on the screen.
     */
    protected final void updateWorldCoordinate()
    {
        String worldPoint = "(x=" + FORMATTER.format(this.animationPanel.getWorldCoordinate().getX()) + " ; y="
                + FORMATTER.format(this.animationPanel.getWorldCoordinate().getY()) + ")";
        this.coordinateField.setText("Mouse: " + worldPoint);
        int requiredWidth = this.coordinateField.getGraphics().getFontMetrics().stringWidth(this.coordinateField.getText());
        if (this.coordinateField.getPreferredSize().width < requiredWidth)
        {
            Dimension requiredSize = new Dimension(requiredWidth, this.coordinateField.getPreferredSize().height);
            this.coordinateField.setPreferredSize(requiredSize);
            this.coordinateField.setMinimumSize(requiredSize);
            Container parent = this.coordinateField.getParent();
            parent.setPreferredSize(requiredSize);
            parent.setMinimumSize(requiredSize);
            parent.revalidate();
        }
        this.coordinateField.repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event) throws RemoteException
    {
        /*-
        // XXX: add the correct notifications
        if (event.getType().equals(Network.Person_ADD_EVENT))
        {
            this.personCount++;
            setGtuCountText();
        }
        else if (event.getType().equals(Network.Person_REMOVE_EVENT))
        {
            this.personCount--;
            setGtuCountText();
        }
        */
    }

    // /**
    // * Updates the text of the Person counter.
    // */
    // private void setGtuCountText()
    // {
    // this.personCountField.setText(this.personCount + " persons");
    // }

    /**
     * UpdateTimer class to update the coordinate on the screen.
     */
    protected class UpdateTimer extends Thread
    {
        /** {@inheritDoc} */
        @Override
        public final void run()
        {
            while (!DSOLAnimationTab.this.windowExited)
            {
                if (DSOLAnimationTab.this.isShowing())
                {
                    DSOLAnimationTab.this.updateWorldCoordinate();
                }
                try
                {
                    Thread.sleep(50); // 20 times per second
                }
                catch (InterruptedException exception)
                {
                    // do nothing
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public final String toString()
        {
            return "UpdateTimer thread for AnimationPanel";
        }

    }

    /**
     * Animation panel that adds autopan functionality. Code based on OpenTrafficSim project component with the same purpose.
     * <p>
     * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
     */
    // private class AutoPanAnimationPanel extends AnimationPanel
    // {
    // /** */
    // private static final long serialVersionUID = 20180430L;
    //
    // /** Last Person that was followed. */
    // private Person lastPerson;
    //
    // /**
    // * Constructor.
    // * @param extent Rectangle2D; home extent
    // * @param size Dimension; size
    // * @param simulator SimulatorInterface&lt;?, ?, ?&gt;; simulator
    // * @throws RemoteException on remote animation error
    // * @throws DSOLException when simulator does not implement AnimatorInterface
    // */
    // AutoPanAnimationPanel(final Rectangle2D extent, final Dimension size, final SimulatorInterface<?, ?, ?> simulator)
    // throws RemoteException, DSOLException
    // {
    // super(extent, size, simulator);
    // MouseListener[] listeners = getMouseListeners();
    // for (MouseListener listener : listeners)
    // {
    // removeMouseListener(listener);
    // }
    // this.addMouseListener(new MouseAdapter()
    // {
    // /** {@inheritDoc} */
    // @Override
    // public void mouseClicked(final MouseEvent e)
    // {
    // if (e.isControlDown())
    // {
    // Person person = getSelectedPerson(e.getPoint());
    // if (person != null)
    // {
    // getControlPanel().getSearchPanel().selectAndTrackObject("Person", person.getId(), true);
    // e.consume(); // sadly doesn't work to prevent a pop up
    // }
    // }
    // e.consume();
    // }
    // });
    // for (MouseListener listener : listeners)
    // {
    // addMouseListener(listener);
    // }
    // // mouse wheel
    // MouseWheelListener[] wheelListeners = getMouseWheelListeners();
    // for (MouseWheelListener wheelListener : wheelListeners)
    // {
    // removeMouseWheelListener(wheelListener);
    // }
    // this.addMouseWheelListener(new InputListener(this)
    // {
    // /** {@inheritDoc} */
    // @Override
    // public void mouseWheelMoved(final MouseWheelEvent e)
    // {
    // super.mouseWheelMoved(e);
    // }
    // });
    // }
    //
    // /**
    // * returns the list of selected objects at a certain mousePoint.
    // * @param mousePoint Point2D; the mousePoint
    // * @return the selected objects
    // */
    // protected Person getSelectedPerson(final Point2D mousePoint)
    // {
    // List<Person> targets = new ArrayList<>();
    // Point2D point = Renderable2DInterface.Util.getWorldCoordinates(mousePoint,
    // DSOLAnimationPanel.this.animationPanel.getExtent(), DSOLAnimationPanel.this.animationPanel.getSize());
    // for (Renderable2DInterface<?> renderable : DSOLAnimationPanel.this.animationPanel.getElements())
    // {
    // if (DSOLAnimationPanel.this.animationPanel.isShowElement(renderable) && renderable.contains(point,
    // DSOLAnimationPanel.this.animationPanel.getExtent(), DSOLAnimationPanel.this.animationPanel.getSize()))
    // {
    // if (renderable.getSource() instanceof Person)
    // {
    // targets.add((Person) renderable.getSource());
    // }
    // }
    // }
    // if (targets.size() == 1)
    // {
    // return targets.get(0);
    // }
    // return null;
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public void paintComponent(final Graphics g)
    // {
    // final DSOLSearchPanel.ObjectKind<?> panKind = DSOLAnimationPanel.this.autoPanKind;
    // final String panId = DSOLAnimationPanel.this.autoPanId;
    // final boolean doPan = DSOLAnimationPanel.this.autoPanOnNextPaintComponent;
    // DSOLAnimationPanel.this.autoPanOnNextPaintComponent = DSOLAnimationPanel.this.autoPanTrack;
    // if (doPan && panKind != null && panId != null)
    // {
    // Locatable locatable = panKind.searchNetwork(panId);
    // if (null != locatable)
    // {
    // try
    // {
    // DirectedPoint3d point = locatable.getLocation();
    // if (point != null) // Center extent around point
    // {
    // double w = this.extent.getWidth();
    // double h = this.extent.getHeight();
    // this.extent = new Rectangle2D.Double(point.getX() - w / 2, point.getY() - h / 2, w, h);
    // }
    // }
    // catch (RemoteException exception)
    // {
    // getSimulator().getLogger().always().warn("Caught RemoteException trying to locate {} with id {}.",
    // panKind, panId);
    // return;
    // }
    // }
    // }
    // super.paintComponent(g);
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public String toString()
    // {
    // return "AutoAnimationPanel [lastPerson=" + this.lastPerson + "]";
    // }
    // }

    /**
     * Enum for demo panel position. Each value contains a field representing the position correlating to the
     * {@code BorderLayout} class.
     */
    public enum DemoPanelPosition
    {
        /** Top. */
        TOP("First"),

        /** Bottom. */
        BOTTOM("Last"),

        /** Left. */
        LEFT("Before"),

        /** Right. */
        RIGHT("After");

        /** Value used in {@code BorderLayout}. */
        private final String direction;

        /**
         * @param direction String; value used in {@code BorderLayout}
         */
        DemoPanelPosition(final String direction)
        {
            this.direction = direction;
        }

        /**
         * @return direction String; value used in {@code BorderLayout}
         */
        public String getBorderLayoutPosition()
        {
            return this.direction;
        }
    }

}
