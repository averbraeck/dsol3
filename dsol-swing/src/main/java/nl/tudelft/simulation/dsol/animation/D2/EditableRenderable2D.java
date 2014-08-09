/*
 * @(#) EditableRenderable2D.java Aug 3, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.animation.D2;

import nl.tudelft.simulation.dsol.animation.Editable;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * EditableRenderable2D is an abstract class that implements EditableRenderable2DInterface. This class can be extended
 * by classes that animate editable simulation objects.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:37:20 $
 * @since 1.5
 */
public abstract class EditableRenderable2D extends Renderable2D implements EditableRenderable2DInterface
{

    /**
     * the source of this renderable
     */
    protected Editable source = null;

    /**
     * constructs a new EditableRenderable2D
     * @param source the source and target
     * @param simulator the simulator
     */
    public EditableRenderable2D(final Editable source, final SimulatorInterface simulator)
    {
        super(source, simulator);
        this.source = source;
    }

    /**
     * @see nl.tudelft.simulation.dsol.animation.D2.EditableRenderable2DInterface#isClosedShape()
     */
    public boolean isClosedShape()
    {
        return true;
    }

    /**
     * @see nl.tudelft.simulation.dsol.animation.D2.EditableRenderable2DInterface#allowMove()
     */
    public boolean allowMove()
    {
        return true;
    }

    /**
     * @see nl.tudelft.simulation.dsol.animation.D2.EditableRenderable2DInterface#allowRotate()
     */
    public boolean allowRotate()
    {
        return true;
    }

    /**
     * @see nl.tudelft.simulation.dsol.animation.D2.EditableRenderable2DInterface#allowScale()
     */
    public boolean allowScale()
    {
        return true;
    }

    /**
     * @see nl.tudelft.simulation.dsol.animation.D2.EditableRenderable2DInterface#allowEditPoints()
     */
    public boolean allowEditPoints()
    {
        return true;
    }

    /**
     * @see nl.tudelft.simulation.dsol.animation.D2.EditableRenderable2DInterface#allowDelete()
     */
    public boolean allowDelete()
    {
        return true;
    }

    /**
     * @see nl.tudelft.simulation.dsol.animation.D2.EditableRenderable2DInterface#allowAddOrDeletePoints()
     */
    public boolean allowAddOrDeletePoints()
    {
        return true;
    }

    /**
     * @see nl.tudelft.simulation.dsol.animation.D2.EditableRenderable2DInterface#getMaxNumberOfPoints()
     */
    public int getMaxNumberOfPoints()
    {
        return Integer.MAX_VALUE;
    }

    /**
     * @see nl.tudelft.simulation.dsol.animation.D2.EditableRenderable2DInterface#getMinNumberOfPoints()
     */
    public int getMinNumberOfPoints()
    {
        return 1;
    }
}