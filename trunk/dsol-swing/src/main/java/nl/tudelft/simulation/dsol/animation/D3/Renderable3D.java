package nl.tudelft.simulation.dsol.animation.D3;

import java.awt.geom.Point2D;
import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Vector3d;

import nl.tudelft.simulation.dsol.animation.LocatableInterface;
import nl.tudelft.simulation.dsol.animation.StaticLocation;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;
import nl.tudelft.simulation.logger.Logger;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * Renderable3D, a 3d renderable <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:37:27 $
 * @author <a href="mailto:royc@tbm.tudelft.nl">Roy Chin </a>
 */
public abstract class Renderable3D extends BranchGroup implements Renderable3DInterface
{
    /**
     * the source of this animatableObject
     */
    protected LocatableInterface source = null;

    /**
     * the simulator
     */
    protected SimulatorInterface simulator = null;

    /**
     * Rotation group
     */
    protected TransformGroup locationGroup = null;

    /**
     * Type of renderable (world, static or simulated
     */
    protected int type = Renderable3DInterface.DYNAMIC_OBJECT;

    /**
     * Scale: temporary solution This scale is used to scale translations and can also be used to scale the model
     * (shape). This is a temporary solution as we would actually like to scale the entire content branch, but this is
     * not possible yet
     */
    protected double scale = 1.0d;

    /**
     * translation Used in update; put here to prevent garbage
     */
    private Transform3D translate = new Transform3D();

    /**
     * combined rotation angle Used in update; put here to prevent garbage
     */
    private Transform3D rotate = new Transform3D();

    /**
     * yaw angle Used in update; put here to prevent garbage
     */
    private Transform3D yaw = new Transform3D();

    /**
     * pitch angle Used in update; put here to prevent garbage
     */
    private Transform3D pitch = new Transform3D();

    /**
     * roll angle Used in update; put here to prevent garbage
     */
    private Transform3D roll = new Transform3D();

    /**
     * @param simulator SimulatorInterface
     */
    public Renderable3D(final SimulatorInterface simulator)
    {
        if (!LocatableInterface.class.isAssignableFrom(this.getClass()))
        {
            throw new IllegalArgumentException("this class should implement Locatable interface");
        }
        this.source = (LocatableInterface) this;
        this.simulator = simulator;
        this.initialize();
    }

    /**
     * @param staticLocation Point3d
     * @param simulator SimulatorInterface
     */
    public Renderable3D(final DirectedPoint staticLocation, final SimulatorInterface simulator)
    {
        super();
        this.source = new StaticLocation(staticLocation, null);
        this.simulator = simulator;
        this.type = Renderable3DInterface.STATIC_OBJECT;
        this.initialize();
    }

    /**
     * @param staticLocation Point3d
     * @param branchGroup A branchGroup containing (a part of) the model
     * @param simulator SimulatorInterface
     */
    public Renderable3D(final DirectedPoint staticLocation, final BranchGroup branchGroup,
            final SimulatorInterface simulator)
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
            Context context = ContextUtil.lookup(this.simulator.getReplication().getContext(), "/animation/3");
            ContextUtil.bind(context, this);
        }
        catch (RemoteException | NamingException exception)
        {
            Logger.warning(this, "<init>", exception);
        }
    }

    /**
     * @param staticLocation Point2D
     * @param simulator SimulatorInterface
     */
    public Renderable3D(final Point2D staticLocation, final SimulatorInterface simulator)
    {
        this(new DirectedPoint(staticLocation), simulator);
    }

    /**
     * @param source LocatableInterface
     * @param simulator SimulatorInterface
     */
    public Renderable3D(final LocatableInterface source, final SimulatorInterface simulator)
    {
        super();
        this.source = source;
        this.simulator = simulator;
        this.initialize();
    }

    /**
     * Initialize
     */
    private void initialize()
    {
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
            Context context = ContextUtil.lookup(this.simulator.getReplication().getContext(), "/animation/3");
            ContextUtil.bind(context, this);
        }
        catch (RemoteException | NamingException exception)
        {
            Logger.warning(this, "<init>", exception);
        }
    }

    /**
     * Initialize TransformGroups
     */
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

    /**
     * Provide the 3D model and add it to the locationGroup
     * @param locationGroup The location of the object
     */
    protected abstract void provideModel(final TransformGroup locationGroup);

    /**
     * updates the renderable
     */
    public void update()
    {
        try
        {
            this.update(this.getAllChildren());
            DirectedPoint location = this.getSource().getLocation();
            this.translate.set(new Vector3d(location.x * this.scale, location.y * this.scale, location.z * this.scale));

            // Hope the order of the rotations is correct ..
            this.yaw.setRotation(new AxisAngle4d(0, 0, 1, location.getRotX()));
            this.pitch.setRotation(new AxisAngle4d(0, 1, 0, location.getRotZ()));
            this.roll.setRotation(new AxisAngle4d(1, 0, 0, location.getRotY()));

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
            Logger.warning(this, "update", exception);
        }
    }

    /**
     * Method update.
     * @param children the children to update.
     */
    protected abstract void update(final Enumeration children);

    /**
     * @return LocatableInterface
     */
    public LocatableInterface getSource()
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
     * @param scale Set the scale of the coordinates
     */
    public void setScale(final double scale)
    {
        this.scale = scale;
    }
}
