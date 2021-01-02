package nl.tudelft.simulation.examples.dsol.animation3d;

import java.util.Enumeration;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.swing.animation.D3.Renderable3D;

/**
 * BallAnimation3D, animation of a ball in 3D.
 * <p>
 * Copyright (c) 2003-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/royc/index.htm">Roy Chin </a>
 */
public class BallAnimation3D extends Renderable3D
{

    /**
     * constructs a new BallAnimation3D.
     * @param source Locatable; the source
     * @param simulator SimulatorInterface.TimeDouble; the simulator
     */
    public BallAnimation3D(final Locatable source, final SimulatorInterface.TimeDouble simulator)
    {
        super(source, simulator);
    }

    /*-
    @Override
    public void provideModel(final TransformGroup locationGroup)
    {
        this.setScale(0.1d);

        // ----------------
        // The shape itself
        Appearance app = new Appearance();
        // Create colors for the material
        Color3f ambientColor = new Color3f(1.0f, 0.0f, 0.0f);
        Color3f diffuseColor = new Color3f(1.0f, 0.0f, 0.0f);
        Color3f specularColor = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f emissiveColor = new Color3f(0.0f, 0.0f, 0.0f);
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
*/
    
    @Override
    protected void update(final Enumeration children)
    {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void update()
    {
    }
}
