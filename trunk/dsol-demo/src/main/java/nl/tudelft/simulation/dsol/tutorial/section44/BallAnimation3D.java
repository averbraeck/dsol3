package nl.tudelft.simulation.dsol.tutorial.section44;

import java.awt.Color;
import java.util.Enumeration;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.animation.D3.Renderable3D;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * BallAnimation3D, animation of a ball in 3D <br>
 * (c) copyright 2002-2016 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 10.05.2004 <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/royc/index.htm">Roy Chin </a>
 */
public class BallAnimation3D extends Renderable3D
{
    /**
     * constructs a new BallAnimation3D.
     * @param source the source
     * @param simulator the simulator
     */
    public BallAnimation3D(final Locatable source, final SimulatorInterface.TimeDouble simulator)
    {
        super(source, simulator);
    }

    /**
     * provides the model for the animationObject.
     * @param locationGroup
     */
    @Override
    public void provideModel(final TransformGroup locationGroup)
    {
        this.setScale(0.1d);

        // ----------------
        // The shape itself
        Appearance app = new Appearance();

        // Create colors for the material
        Color3f ambientColor = new Color3f(Color.ORANGE);
        Color3f diffuseColor = new Color3f(Color.ORANGE);

        Color3f specularColor = new Color3f(Color.WHITE);
        Color3f emissiveColor = new Color3f(Color.orange);

        // Define shininess
        float shininess = 10.0f;
        
        // Set material
        app.setMaterial(new Material(ambientColor, emissiveColor, diffuseColor, specularColor, shininess));
        
        // Create a ball
        // TODO: Node model = new Sphere(5f * (float) this.scale, app);

        // Node model = new ColorCube(0.4);

        // ---------------
        // Put it together
        // TODO: locationGroup.addChild(model);
    }

    /**
     * updates the animation of this object.
     * @param children the children to update
     */
    @Override
    protected void update(final Enumeration children)
    {
        // Do nothing
    }
}
