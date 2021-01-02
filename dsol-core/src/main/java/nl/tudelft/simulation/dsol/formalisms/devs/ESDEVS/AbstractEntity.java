package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.io.Serializable;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * AbstractEntity class. The AbstractEntity takes care of modeling components without behaviour but with state within coupled
 * models.
 * <p>
 * Copyright (c) 2009-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a><br>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public class AbstractEntity<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends AbstractDEVSModel<A, R, T>
{
    /** the default serial version UId. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for an abstract entity: we have to indicate the simulator for reporting purposes, and the parent model we are
     * part of. A parent model of null means that we are the top model.
     * @param modelName String; the name of this component
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; the simulator for this model.
     * @param parentModel CoupledModel&lt;A,R,T&gt;; the parent model we are part of (can be null for highest level model).
     */
    public AbstractEntity(final String modelName, final DEVSSimulatorInterface<A, R, T> simulator,
            final CoupledModel<A, R, T> parentModel)
    {
        super(modelName, simulator, parentModel);
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this.modelName;
    }

    @Override
    public void printModel(final String header)
    {
        // TODO implement printmodel
    }

}
