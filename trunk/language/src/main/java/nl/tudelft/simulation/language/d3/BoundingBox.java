/*
 * @(#) BoundingBox.java Jun 17, 2004 Copyright (c) 2002-2005 Delft University of Technology Jaffalaan 5, 2628
 * BX Delft, the Netherlands. All rights reserved. This software is proprietary information of Delft
 * University of Technology 
 */
package nl.tudelft.simulation.language.d3;

import java.io.Serializable;

import javax.media.j3d.Bounds;
import javax.vecmath.Point3d;

/**
 * A bounding box.
 * <p>
 * Copyright (c) 2002-2009 Delft University of Technology, Jaffalaan 5, 2628 BX
 * Delft, the Netherlands. All rights reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/">
 * www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is"
 * and any express or implied warranties, including, but not limited to, the
 * implied warranties of merchantability and fitness for a particular purpose
 * are disclaimed. In no event shall the copyright holder or contributors be
 * liable for any direct, indirect, incidental, special, exemplary, or
 * consequential damages (including, but not limited to, procurement of
 * substitute goods or services; loss of use, data, or profits; or business
 * interruption) however caused and on any theory of liability, whether in
 * contract, strict liability, or tort (including negligence or otherwise)
 * arising in any way out of the use of this software, even if advised of the
 * possibility of such damage.
 * 
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2009/10/21 07:32:42 $
 * @since 1.5
 */
public class BoundingBox extends javax.media.j3d.BoundingBox implements Serializable
{
    /** the default serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * constructs a new BoundingBox.
     */
    public BoundingBox()
    {
        super();
    }

    /**
     * constructs a new BoundingBox around [0;0;0].
     * 
     * @param deltaX the deltaX
     * @param deltaY the deltaY
     * @param deltaZ the deltaZ
     */
    public BoundingBox(final double deltaX, final double deltaY,
            final double deltaZ)
    {
        super(new Point3d(-0.5 * deltaX, -0.5 * deltaY, -0.5 * deltaZ),
                new Point3d(0.5 * deltaX, 0.5 * deltaY, 0.5 * deltaZ));
        this.normalize();
    }

    /**
     * constructs a new BoundingBox.
     * 
     * @param arg0 the boundaries
     */
    public BoundingBox(final Bounds arg0)
    {
        super(arg0);
        this.normalize();
    }

    /**
     * constructs a new BoundingBox.
     * 
     * @param arg0 the boundaries
     */
    public BoundingBox(final Bounds[] arg0)
    {
        super(arg0);
        this.normalize();
    }

    /**
     * constructs a new BoundingBox.
     * 
     * @param arg0 the boundaries
     * @param arg1 the point
     */
    public BoundingBox(final Point3d arg0, final Point3d arg1)
    {
        super(arg0, arg1);
        this.normalize();
    }

    /**
     * normalizes the boundingBox.
     */
    public final void normalize()
    {
        Point3d p1 = new Point3d();
        Point3d p2 = new Point3d();
        this.getLower(p1);
        this.getUpper(p2);
        this.setLower(new Point3d(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y),
                Math.min(p1.z, p2.z)));
        this.setUpper(new Point3d(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y),
                Math.max(p1.z, p2.z)));
    }
}
