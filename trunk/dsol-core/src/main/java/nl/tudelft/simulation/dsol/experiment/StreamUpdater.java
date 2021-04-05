package nl.tudelft.simulation.dsol.experiment;

import java.util.Map;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The StreamUpdater interface describes how to update the seed values for the next replication.
 * <p>
 * Copyright (c) 2021-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface StreamUpdater
{
    /**
     * Update the seeds for the given replication number. The method should be fully reproducible, and based on the previous
     * seed values, possibly the String representation, and the replication number.
     * @param streams Map&lt;String, StreamIterface&gt;; the map of the streams for the replication
     * @param replicationnumber int; the replication number for which to set the seed values
     */
    void updateSeeds(Map<String, StreamInterface> streams, int replicationnumber);
}
