package nl.tudelft.simulation.dsol.web.animation.peer;

import java.awt.Insets;
import java.awt.peer.ContainerPeer;

/**
 * HTMLContainer.java. <br>
 * <br>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class HTMLContainer extends HTMLComponent implements ContainerPeer
{

    /**
     * 
     */
    public HTMLContainer()
    {
    }

    /** {@inheritDoc} */
    @Override
    public Insets getInsets()
    {
        return new Insets(0, 0, 1080, 1920);
    }

    /** {@inheritDoc} */
    @Override
    public void beginValidate()
    {
    }

    /** {@inheritDoc} */
    @Override
    public void endValidate()
    {
    }

    /** {@inheritDoc} */
    @Override
    public void beginLayout()
    {
    }

    /** {@inheritDoc} */
    @Override
    public void endLayout()
    {
    }

}