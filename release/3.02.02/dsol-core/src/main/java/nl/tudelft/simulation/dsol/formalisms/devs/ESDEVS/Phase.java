package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.io.Serializable;

/**
 * Phase class. The Phase is used in phase explicit DEVS models. Phases partition the state space into regions, where
 * events are used to transfer the state from one phase to another phase. An example is a machine that can be in three
 * different phases: idle, active, or failed. There are explicit events to transfer the model from one phase to another.
 * A model or component is always in exactly one phase, and a transition diagram can be used to depict the transitions
 * between phases.
 * <p>
 * Copyright (c) 2002-2018  Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="https://simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Oct 17, 2009 <br>
 * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a><br>
 */
public class Phase implements Serializable
{
    /** the default serial version UId. */
    private static final long serialVersionUID = 1L;

    /** the identifier of the phase. */
    private final String name;

    /**
     * The lifetime of the phase, which is the time the component or model is expected to spend in the current phase.
     * This value could be changed by the model behaviour. When lifeTime is POSITIVE_INFINITY, the phase is passive,
     * because we cannot go to another phase, unless there is an external event. When it has a proper double value, the
     * phase is active and we know when the model is expected to move to another phase.
     */
    private double lifeTime = Double.POSITIVE_INFINITY;

    /**
     * The constructor of a new phase.
     * @param name the identifier of the phase
     */
    public Phase(final String name)
    {
        this.name = new String(name);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        String result;
        if (this.lifeTime == Double.POSITIVE_INFINITY)
        {
            result = " [ Passive phase] ";
        }
        else
        {
            result = " [ Active  phase] ";
        }
        return this.name + result;
    }

    /**
     * Sets the lifetime of the phase.
     * @param lifeTime the lifetime of the phase
     */
    public final void setLifeTime(final double lifeTime)
    {
        this.lifeTime = lifeTime;
    }

    /**
     * @return the lifetime of the phase.
     */
    public final double getLifeTime()
    {
        return this.lifeTime;
    }

    /**
     * @return name; the identifier of the phase
     */
    public final String getName()
    {
        return this.name;
    }
}