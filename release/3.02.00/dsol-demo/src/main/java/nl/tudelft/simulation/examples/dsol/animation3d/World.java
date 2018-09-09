package nl.tudelft.simulation.examples.dsol.animation3d;

import java.util.Enumeration;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;

import nl.tudelft.simulation.dsol.animation.D3.Renderable3D;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * World, a simple world for the ball model in 3D <br>
 * Copyright (c) 2003-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @version 1.0 10.05.2004 <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/royc/index.htm">Roy Chin </a>
 */
public class World extends Renderable3D
{

    /**
     * Construct the world.
     * @param staticLocation Static location
     * @param simulator Simulator
     */
    public World(final DirectedPoint staticLocation, final SimulatorInterface.TimeDouble simulator)
    {
        super(staticLocation, simulator);
    }

    /** {@inheritDoc} */
    @Override
    public void provideModel(final TransformGroup locationGroup)
    {
        this.setScale(0.1d);
        // ----------------
        // The shape itself
        Appearance app = new Appearance();
        // Create colors for the material
        Color3f ambientColor = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f diffuseColor = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f specularColor = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f emissiveColor = new Color3f(0.0f, 0.0f, 0.0f);
        // Define shininess
        float shininess = 10.0f;
        // Set material
        app.setMaterial(new Material(ambientColor, emissiveColor, diffuseColor, specularColor, shininess));
        // Create a ball
        // TODO: Node model = new Box(100f * (float) this.scale, 100f * (float) this.scale, 1f * (float) this.scale,
        // app);

        // Node model = new ColorCube(0.4);

        // ---------------
        // Put it together
        // TODO: locationGroup.addChild(model);
    }

    /** {@inheritDoc} */
    @Override
    protected void update(final Enumeration children)
    {
        // Do nothing
    }
}
