/**
 * Experimental design in DSOL.
 * <p>
 * Most DSOL users find the use of the Experiment, Replication, Treatment, and ExperimentalFrame in relation to the Model and
 * the Simulator extremely difficult to use and control. The publish/subscribe mechanisms to move to a next replication or event
 * are difficult to understand, but very helpful in case different experiments or replications run on different computers. As
 * most users don't do this, the question was how to simplify the use of the experimental framework.
 * <p>
 * So, what's the idea? When executing a simulation, you execute a set of Experiments on a model, using a certain simulator.
 * Different experiments will have different parameter settings for the model. Each experiment can have a number of
 * replications. Each replication has different seeds for the random number generators used, but the specified parameters are
 * the same for all replications within an experiment. An experiment has a certain runtime, warm-up time, time units, and the
 * like for execution. These are also all the same for all replications within one experiment.
 * <p>
 * The central object is therefore the <b>Experiment</b>. This is the object that links to Replications, Model, and Simulator.
 * The Replication object is an object that indicates the current replication that is being carried out. The Simulator knows its
 * Replication, and the Model now knows its simulator (change in DSOLModel). The Replication is now initialized with its
 * Experiment and not anymore with its Treatment. Treatment and Experiment are combined into Experiment. The list of
 * Replications has also been moved to the Experiment where it belongs. This makes the structure more consistent. The set of
 * Experiments to execute was called an ExperimentalFrame, but this term will be reserved for the experimental frame cf. Zeigler
 * et al. (2000) in the DSOL-DEVS formalism. he new same is ExperimentSet, because it is just a set of experiments to carry out.
 * Static helper methods have been added to help to construct an ExperimentSet with one experiment with multiple replications
 * quickly. This means that an experiment xml-file is no longer necessary -- the experiment can also easily be created with a
 * few lines of code now.
 * <p>
 * These changes mean that most models that are not extending from DSOLApplication might need a few small changes in the
 * creation and access of the Experiment, Replication, and ExperimentSet. Furthermore, all Models should now implement the
 * getSimulator() method.
 * <p>
 * The experiment xsd-file has been changed in relation to this, and the replications have been moved to the experiment instead
 * of being part of the treatment. The individual replication elements need to be surrounded by a replications element. This
 * means that experiment xml-files need to be updated as well!
 * <p>
 * Something that really caused problems when carrying out an experiment was that when building a model, there was no experiment
 * defined.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 */
package nl.tudelft.simulation.dsol.experiment;
