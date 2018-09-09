package nl.tudelft.simulation.introspection.gui;

import nl.tudelft.simulation.introspection.mapping.CellPresentationConfiguration;

/**
 * Allows discovery of a cell presentation configuration {see
 * nl.tudelft.simulation.introspection.mapping.CellPresentationConfiguration}.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public interface ICellPresentationConfigProvider
{
    /**
     * @return the cellPresentationConfiguration
     */
    public CellPresentationConfiguration getCellPresentationConfiguration();
}
