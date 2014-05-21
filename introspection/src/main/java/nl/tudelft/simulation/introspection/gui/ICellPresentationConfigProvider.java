/*
 * Created on Aug 26, 2004 @ Erasmus University Rotterdam Copyright (c) Delft
 * University of Technology
 */
package nl.tudelft.simulation.introspection.gui;

import nl.tudelft.simulation.introspection.mapping.CellPresentationConfiguration;

/**
 * Allows discovery of a cell presentation configuration {see
 * nl.tudelft.simulation.introspection.mapping.CellPresentationConfiguration}.
 * 
 * @author <a
 *         href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels
 *         Lang </a><a
 * @since 1.5
 */
public interface ICellPresentationConfigProvider
{
    /**
     * @return the cellPresentationConfiguration
     */
    public CellPresentationConfiguration getCellPresentationConfiguration();
}
