package nl.tudelft.simulation.dsol.swing.animation.D3;

import java.util.Enumeration;

import org.djutils.draw.Transform3d;
import org.djutils.draw.point.DirectedPoint3d;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.animation.StaticLocation3d;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * Renderable3D, a 3d renderable.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="mailto:royc@tbm.tudelft.nl">Roy Chin </a>
 */
public abstract class Renderable3D /* extends BranchGroup */ implements Renderable3DInterface
{
    /** */
    private static final long serialVersionUID = 20200109L;

    /** the source of this animatableObject. */
    protected Locatable source = null;

    /** the simulator. */
    protected SimulatorInterface simulator = null;

    /** Rotation group. */
    // protected TransformGroup locationGroup = null;

    /** Type of renderable (world, static or simulated. */
    protected int type = Renderable3DInterface.DYNAMIC_OBJECT;

    /**
     * Scale: temporary solution This scale is used to scale translations and can also be used to scale the model (shape). This
     * is a temporary solution as we would actually like to scale the entire content branch, but this is not possible yet
     */
    protected double scale = 1.0d;

    /** translation Used in update; put here to prevent garbage. */
    private Transform3d translate = new Transform3d();

    /** combined rotation angle Used in update; put here to prevent garbage. */
    private Transform3d rotate = new Transform3d();

    /** yaw angle Used in update; put here to prevent garbage. */
    private Transform3d yaw = new Transform3d();

    /** pitch angle Used in update; put here to prevent garbage. */
    private Transform3d pitch = new Transform3d();

    /** roll angle Used in update; put here to prevent garbage. */
    private Transform3d roll = new Transform3d();

    /**
     * @param simulator SimulatorInterface; SimulatorInterface
     */
    public Renderable3D(final SimulatorInterface simulator)
    {
        if (!Locatable.class.isAssignableFrom(this.getClass()))
        {
            throw new IllegalArgumentException("this class should implement Locatable interface");
        }
        this.source = (Locatable) this;
        this.simulator = simulator;
        this.initialize();
    }

    /**
     * @param staticLocation DirectedPoint3d; Point3d
     * @param simulator SimulatorInterface; SimulatorInterface
     */
    public Renderable3D(final DirectedPoint3d staticLocation, final SimulatorInterface simulator)
    {
        super();
        this.source = new StaticLocation3d(staticLocation, null);
        this.simulator = simulator;
        this.type = Renderable3DInterface.STATIC_OBJECT;
        this.initialize();
    }

    /**
     * @param staticLocation DirectedPoint3d; Point3d
     * @param branchGroup BranchGroup; A branchGroup containing (a part of) the model
     * @param simulator SimulatorInterface; SimulatorInterface
     */
    /*-
    public Renderable3D(final DirectedPoint3d staticLocation, final BranchGroup branchGroup, final SimulatorInterface simulator)
    {
        super();
        this.source = new StaticLocation(staticLocation, null);
        this.simulator = simulator;
        this.type = Renderable3DInterface.STATIC_OBJECT;
        this.initializeTransformGroups();
        this.provideModel(this.locationGroup);
        this.addChild(this.locationGroup);
        this.addChild(branchGroup);
        this.update();
        try
        {
            ContextInterface context = ContextUtil.lookupOrCreateSubContext(simulator.getReplication().getContext(), "/animation/3D");
            context.bindObject(this);
        }
        catch (NamingException | RemoteException exception)
        {
            this.simulator.getLogger().always().warn(exception, "<init>");
        }
    }
    */

    /**
     * @param staticLocation Point2D; Point2D
     * @param simulator SimulatorInterface; SimulatorInterface
     */
    /*-
    public Renderable3D(final Point2D staticLocation, final SimulatorInterface simulator)
    {
        this(new DirectedPoint3d(staticLocation), simulator);
    }
    */

    /**
     * @param source Locatable; LocatableInterface
     * @param simulator SimulatorInterface; SimulatorInterface
     */
    public Renderable3D(final Locatable source, final SimulatorInterface simulator)
    {
        super();
        this.source = source;
        this.simulator = simulator;
        this.initialize();
    }

    /**
     * Initialize.
     */
    private void initialize()
    {
        //
    }

    /*-
        // Note: the order of the lines below does matter!
        // AnimationFrame3D wants to draw as soon as it has
        // got the object from the context, so transforms
        // better be initialized.
        //
        // Note2: RunControl.notify() must create the subcontext:
        // "/animation3d"
        this.initializeTransformGroups();
        this.provideModel(this.locationGroup);
        this.addChild(this.locationGroup);
        this.update();
        try
        {
            ContextInterface context = ContextUtil.lookupOrCreateSubContext(simulator.getReplication().getContext(), "/animation/3D");
            context.bindObject(this);
        }
        catch (NamingException | RemoteException exception)
        {
            this.simulator.getLogger().always().warn(exception, "<init>");
        }
    }
    */

    /**
     * Initialize TransformGroups.
     */
    /*-
    private void initializeTransformGroups()
    {
        this.setCapability(Group.ALLOW_CHILDREN_READ);
        this.setCapability(Group.ALLOW_CHILDREN_WRITE);
        this.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        this.setCapability(BranchGroup.ALLOW_DETACH);
        this.setCapability(Node.ENABLE_PICK_REPORTING);
        // Determine if this branch group is pickable
        if (this.type == Renderable3DInterface.DYNAMIC_OBJECT)
        {
            this.setPickable(true);
        }
        else
        {
            this.setPickable(false);
        }
    
        this.locationGroup = new TransformGroup();
    
        this.locationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        this.locationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.locationGroup.setCapability(Group.ALLOW_CHILDREN_READ);
        this.locationGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);
        this.locationGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        if (this.type == Renderable3DInterface.DYNAMIC_OBJECT)
        {
            this.locationGroup.setCapabilityIsFrequent(TransformGroup.ALLOW_TRANSFORM_READ);
            this.locationGroup.setCapabilityIsFrequent(TransformGroup.ALLOW_TRANSFORM_READ);
        }
    }
    */

    /**
     * Provide the 3D model and add it to the locationGroup
     * @param locationGroup TransformGroup; The location of the object
     */
    // protected abstract void provideModel(final TransformGroup locationGroup);

    /**
     * updates the renderable
     */
    /*-
    public void update()
    {
        try
        {
            this.update(this.getAllChildren());
            DirectedPoint3d location = this.getSource().getLocation();
            this.translate.set(new Vector3d(location.x * this.scale, location.y * this.scale, location.z * this.scale));
    
            // Hope the order of the rotations is correct ..
            this.yaw.setRotation(new AxisAngle4d(0, 0, 1, location.getDirX()));
            this.pitch.setRotation(new AxisAngle4d(0, 1, 0, location.getDirZ()));
            this.roll.setRotation(new AxisAngle4d(1, 0, 0, location.getDirY()));
    
            // first translate then rotate
            this.rotate.set(this.translate);
            this.rotate.mul(this.yaw);
            this.rotate.mul(this.pitch);
            this.rotate.mul(this.roll);
    
            this.locationGroup.setTransform(this.rotate);
        }
        catch (RemoteException exception)
        {
            this.rotate.set(new Vector3d(0, 0, 0));
            this.simulator.getLogger().always().warn(exception, "update");
        }
    }
    */

    /**
     * Method update.
     * @param children Enumeration; the children to update.
     */
    protected abstract void update(final Enumeration children);

    /**
     * @return LocatableInterface
     */
    public Locatable getSource()
    {
        return this.source;
    }

    /**
     * returns the type
     * @return the returned type.
     */
    public int getType()
    {
        return this.type;
    }

    /**
     * @return scale
     */
    public double getScale()
    {
        return this.scale;
    }

    /**
     * @param scale double; Set the scale of the coordinates
     */
    public void setScale(final double scale)
    {
        this.scale = scale;
    }
}
