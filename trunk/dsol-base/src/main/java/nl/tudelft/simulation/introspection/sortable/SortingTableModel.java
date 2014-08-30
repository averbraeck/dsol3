/*
 * @(#) SortingTableModel.java April 15, 2004 Copyright (c) 2002-2005-2004 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */

package nl.tudelft.simulation.introspection.sortable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import nl.tudelft.simulation.logger.Logger;

/**
 * The SortingTableModel.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang </a><a
 *         href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.1 Apr 15, 2004
 * @since 1.5
 */
public class SortingTableModel implements TableModel, Sortable
{
    /** the listeners */
    protected Map<ProxyListener, TableModelListener> proxyListeners = new HashMap<ProxyListener, TableModelListener>(5);

    /** the source */
    protected TableModel source;

    /** the index */
    private List<Object> index = Collections.synchronizedList(new ArrayList<Object>());

    /** expandedIndex */
    protected Integer[] expandedIndex;

    /** the definitions */
    private List<Definition> definitions = Collections.synchronizedList(new ArrayList<Definition>());

    /**
     * constructs a new SortingTableModel
     * @param source the sorce
     */
    public SortingTableModel(final TableModel source)
    {
        this.source = source;
    }

    /**
     * The ProxyListener
     */
    private class ProxyListener implements TableModelListener
    {
        /**
         * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
         */
        public void tableChanged(final TableModelEvent e)
        {
            sort();
            (SortingTableModel.this.proxyListeners.get(this)).tableChanged(e);
        }
    }

    /**
     * builds the index
     */
    private synchronized void buildIndex()
    {
        initIndex();
        for (Definition definition : this.definitions)
        {
            this.index = sortList(this.index, definition.isAcendingSort(), definition.getFieldID());
        }
        this.expandIndex();
    }

    /**
     * expands the index
     */
    private void expandIndex()
    {
        List<Object> expandedList = new ArrayList<Object>();
        for (Object current : this.index)
        {
            if (current instanceof List)
            {
                expandedList.addAll((List<?>) current);
            }
            else
            {
                expandedList.add(current);
            }
        }
        this.expandedIndex = expandedList.toArray(new Integer[0]);
    }

    /**
     * initializes the index
     */
    private void initIndex()
    {
        this.index.clear();
        List<Integer> entry = new ArrayList<Integer>();
        for (int i = 0; i < this.getRowCount(); i++)
        {
            entry.add(i);
        }
        this.index.add(entry);
    }

    /**
     * @param unsorted the list
     * @param ascending is it ascending
     * @param column the column
     * @return the sortedList
     */
    private List<Object> sortList(final List<Object> unsorted, final boolean ascending, final int column)
    {
        List<Object> result = new ArrayList<Object>(unsorted.size());
        synchronized (unsorted)
        {
            for (Object current : unsorted)
            {
                if (current instanceof Integer)
                {
                    result.add(current);
                }
                else
                {
                    List<?> currentList = (List<?>) current;
                    result.addAll(sortSubList(currentList, ascending, column));
                }
            }
        }
        return result;
    }

    /**
     * @param unsorted the list
     * @param ascending is it ascending
     * @param column the column
     * @return the sortedList
     */
    @SuppressWarnings("unchecked")
    private List<Object> sortSubList(final List<?> unsorted, final boolean ascending, final int column)
    {
        List<Object> result = new ArrayList<Object>(unsorted.size());
        synchronized (unsorted)
        {
            for (int i = 0; i < unsorted.size(); i++)
            {
                Integer unsortedEntry = (Integer) unsorted.get(i);
                Object current = this.source.getValueAt(unsortedEntry.intValue(), column);
                boolean allocated = false;
                for (int y = 0; (y < result.size() && !allocated); y++)
                {
                    boolean inList = false;
                    Object resultValue = result.get(y);
                    if (resultValue instanceof List)
                    {
                        inList = true;
                        resultValue =
                                this.source.getValueAt(((Integer) ((List<?>) resultValue).get(0)).intValue(), column);
                    }
                    else
                    {
                        resultValue = this.source.getValueAt(((Integer) resultValue).intValue(), column);
                    }
                    if (current instanceof Comparable && resultValue instanceof Comparable)
                    {
                        try
                        {
                            int comparisson = ((Comparable) current).compareTo(resultValue);
                            if (comparisson == 0)
                            {
                                if (inList)
                                {
                                    ((List<Object>) result.get(y)).add(unsortedEntry);
                                }
                                else
                                {
                                    List valueList = new ArrayList(2);
                                    valueList.add(result.get(y));
                                    valueList.add(unsortedEntry);
                                    result.remove(y);
                                    result.add(y, valueList);
                                }
                                allocated = true;
                            }
                            if (ascending && comparisson < 0 || !ascending && comparisson > 0)
                            {
                                result.add(y, unsortedEntry);
                                allocated = true;
                            }
                        }
                        catch (ClassCastException exception)
                        {
                            Logger.info(this, "sortSubList", " Could not compare " + current + " and " + resultValue
                                    + ": " + exception);
                        }
                    }
                }
                if (!allocated)
                {
                    result.add(unsortedEntry);
                }
            }
        }
        return result;
    }

    /**
     * @see nl.tudelft.simulation.introspection.sortable.Sortable#getDefinitions()
     */
    public Definition[] getDefinitions()
    {
        return this.definitions.toArray(new Definition[0]);
    }

    /**
     * @see nl.tudelft.simulation.introspection.sortable.Sortable #setDefinitions(Definition[])
     */
    public void setDefinitions(final Definition[] definitions)
    {
        this.definitions.clear();
        this.definitions.addAll(Arrays.asList(definitions));
    }

    /**
     * @see nl.tudelft.simulation.introspection.sortable.Sortable#sort()
     */
    public void sort()
    {
        buildIndex();
    }

    /**
     * @see javax.swing.table.TableModel#addTableModelListener(TableModelListener)
     */
    public void addTableModelListener(final TableModelListener l)
    {
        ProxyListener proxy = new ProxyListener();
        this.proxyListeners.put(proxy, l);
        this.source.addTableModelListener(proxy);
    }

    /**
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class<?> getColumnClass(final int columnIndex)
    {
        return this.source.getColumnClass(columnIndex);
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return this.source.getColumnCount();
    }

    /**
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(final int columnIndex)
    {
        return this.source.getColumnName(columnIndex);
    }

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
        return this.source.getRowCount();
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(final int rowIndex, final int columnIndex)
    {
        if (this.expandedIndex == null)
        {
            buildIndex();
        }
        if (rowIndex > this.expandedIndex.length)
        {
            Logger.warning(this, "getValueAt", " could not retrieve row " + rowIndex
                    + " from sorted list. Returning default instead.");
            return this.source.getValueAt(rowIndex, columnIndex);
        }
        return this.source.getValueAt(this.expandedIndex[rowIndex].intValue(), columnIndex);
    }

    /**
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(final int rowIndex, final int columnIndex)
    {
        return this.source.isCellEditable(this.expandedIndex[rowIndex].intValue(), columnIndex);
    }

    /**
     * @see javax.swing.table.TableModel#removeTableModelListener(TableModelListener)
     */
    public void removeTableModelListener(final TableModelListener l)
    {
        ProxyListener proxy = (ProxyListener) this.proxyListeners.get(l);
        this.source.removeTableModelListener(proxy);
        this.proxyListeners.remove(proxy);
    }

    /**
     * @see javax.swing.table.TableModel#setValueAt(Object, int, int)
     */
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex)
    {
        if (rowIndex > this.expandedIndex.length)
        {
            Logger.warning(this, "setValueAt", " could not retrieve row " + rowIndex
                    + " from sorted list. Ignoring 'setValue' command.");
            return;
        }
        this.source.setValueAt(aValue, this.expandedIndex[rowIndex].intValue(), columnIndex);
        this.buildIndex();
        if (this.source instanceof DefaultTableModel)
        {
            ((DefaultTableModel) this.source).fireTableDataChanged();
        }
    }
}