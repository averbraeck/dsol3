package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * AbstractEntity class. The AbstractEntity takes care of modeling components without behaviour but with state within
 * coupled models.
 * <p>
 * Copyright (c) 2002-2009 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
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
public class AbstractEntity extends AbstractDEVSModel
{
    /** the default serial version UId. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for an abstract entity: we have to indicate the simulator for reporting purposes, and the parent
     * model we are part of. A parent model of null means that we are the top model.
     * @param modelName the name of this component
     * @param simulator the simulator for this model.
     * @param parentModel the parent model we are part of (can be null for highest level model).
     */
    public AbstractEntity(final String modelName, final DEVSSimulatorInterface.TimeDouble simulator,
            final CoupledModel parentModel)
    {
        super(modelName, simulator, parentModel);
    }

    @Override
    public void printModel(final String header)
    {
        // todo
    }

}
