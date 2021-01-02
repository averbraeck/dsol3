package nl.tudelft.dsol.introspection.beans;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.djutils.immutablecollections.ImmutableHashMap;
import org.djutils.immutablecollections.ImmutableHashSet;
import org.djutils.immutablecollections.ImmutableMap;
import org.djutils.immutablecollections.ImmutableSet;

/**
 * Test bean for testing introspection of simple and composite color and font properties.
 * <p>
 * (c) 2002-2019-2004 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a>.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang
 *         </a><a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class GUIBean
{
    /** the color to use. */
    private Color color = Color.YELLOW;

    /** the font to use. */
    private Font font = new Font("Arial", Font.BOLD, 11);

    /** the array. */
    private Color[] colorArray = new Color[] {Color.BLACK, Color.BLUE, Color.GREEN};

    /** A list. */
    private List<String> countryList = Arrays.asList("Netherlands", "Belgium", "Germany", "UK", "USA");

    /** An ImmutableSet. */
    private ImmutableSet<Double> immutableDoubleSet = new ImmutableHashSet<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 8.0, 16.0));

    /** A map. */
    private Map<Integer, String> numberMap = new TreeMap<>();

    /** An Immutable map. */
    private ImmutableMap<Double, Double> immutableMap;

    /** A list of maps of sets. */
    private List<Map<String, Set<Double>>> listMapSet = new ArrayList<>();

    /**
     * Construct the values.
     */
    public GUIBean()
    {
        this.numberMap.put(1, "one");
        this.numberMap.put(2, "two");
        this.numberMap.put(3, "three");
        this.numberMap.put(4, "four");
        this.numberMap.put(5, "five");
        this.numberMap.put(6, "six");
        this.numberMap.put(7, "seven");
        this.numberMap.put(8, "eight");
        this.numberMap.put(9, "nine");
        this.numberMap.put(10, "ten");

        Map<Double, Double> doubleMap = new HashMap<>();
        doubleMap.put(1.0, 1.0);
        doubleMap.put(2.0, 4.0);
        doubleMap.put(3.0, 9.0);
        doubleMap.put(4.0, 16.0);
        doubleMap.put(5.0, 25.0);
        this.immutableMap = new ImmutableHashMap<Double, Double>(doubleMap);

        Map<String, Set<Double>> map1 = new HashMap<>();
        this.listMapSet.add(map1);
        Set<Double> set11 = new HashSet<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0));
        map1.put("first", set11);
        Set<Double> set12 = new HashSet<>(Arrays.asList(11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0));
        map1.put("second", set12);

        Map<String, Set<Double>> map2 = new HashMap<>();
        this.listMapSet.add(map2);
        Set<Double> set21 = new HashSet<>(Arrays.asList(1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5));
        map2.put("first", set21);
        Set<Double> set22 = new HashSet<>(Arrays.asList(11.5, 12.5, 13.5, 14.5, 15.5, 16.5, 17.5));
        map2.put("second", set22);
    }

    /**
     * @return the Color
     */
    public Color getColor()
    {
        return this.color;
    }

    /**
     * @return the Font
     */
    public Font getFont()
    {
        return this.font;
    }

    /**
     * @return colorArray
     */
    public final Color[] getColorArray()
    {
        return this.colorArray;
    }

    /**
     * @return countryList
     */
    public final List<String> getCountryList()
    {
        return this.countryList;
    }

    /**
     * @return immutableDoubleSet
     */
    public final ImmutableSet<Double> getImmutableDoubleSet()
    {
        return this.immutableDoubleSet;
    }

    /**
     * @return numberMap
     */
    public final Map<Integer, String> getNumberMap()
    {
        return this.numberMap;
    }

    /**
     * @return immutableMap
     */
    public final ImmutableMap<Double, Double> getImmutableMap()
    {
        return this.immutableMap;
    }

    /**
     * @return listMapSet
     */
    public final List<Map<String, Set<Double>>> getListMapSet()
    {
        return this.listMapSet;
    }

    /**
     * @param color the color of the bean
     */
    public void setColor(final Color color)
    {
        this.color = color;
    }

    /**
     * sets the font
     * @param font the font
     */
    public void setFont(final Font font)
    {
        this.font = font;
    }

    /**
     * sets the color array.
     * @param colorArray the colorSet
     */
    public void setColorArray(final Color[] colorArray)
    {
        this.colorArray = colorArray;
    }

}
