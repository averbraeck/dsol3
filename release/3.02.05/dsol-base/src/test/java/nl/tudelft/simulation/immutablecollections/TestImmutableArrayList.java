package nl.tudelft.simulation.immutablecollections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * TestImmutable.java. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information
 * <a href="https://simulation.tudelft.nl/" target="_blank"> www.simulation.tudelft.nl</a>. The source code and
 * binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 */
public class TestImmutableArrayList
{

    @Test
    public final void testArrayList()
    {
        List<Integer> intList = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        List<Integer> list = new ArrayList<Integer>(intList);
        testIntList(list, new ImmutableArrayList<Integer>(list, Immutable.WRAP), Immutable.WRAP);
        list = new ArrayList<Integer>(intList);
        testIntList(list, new ImmutableArrayList<Integer>(list, Immutable.COPY), Immutable.COPY);
        list = new ArrayList<Integer>(intList);
        testIntList(list, new ImmutableArrayList<Integer>(list), Immutable.COPY);
        list = new ArrayList<Integer>(intList);
        ImmutableArrayList<Integer> ial = new ImmutableArrayList<Integer>(list);
        testIntList(list, new ImmutableArrayList<Integer>(ial), Immutable.COPY);
        
        list = new ArrayList<Integer>(intList);
        Set<Integer> intSet = new HashSet<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        testIntList(list, new ImmutableArrayList<Integer>(intSet), Immutable.COPY);
    }

    private void testIntList(final List<Integer> list, final ImmutableList<Integer> imList, final Immutable copyOrWrap)
    {
        Assert.assertTrue(list.size() == 10);
        Assert.assertTrue(imList.size() == 10);
        for (int i = 0; i < 10; i++)
            Assert.assertTrue(imList.get(i) == list.get(i));
        Assert.assertFalse(imList.isEmpty());
        Assert.assertTrue(imList.contains(5));
        Assert.assertFalse(imList.contains(15));
        if (copyOrWrap == Immutable.COPY)
        {
            Assert.assertTrue(imList.isCopy());
            Assert.assertTrue(imList.toList().equals(list));
            Assert.assertFalse(imList.toList() == list);
        }
        else
        {
            Assert.assertTrue(imList.isWrap());
            Assert.assertTrue(imList.toList().equals(list));
            Assert.assertFalse(imList.toList() == list); // this WRAP method returns a NEW list
        }
        
        List<Integer> to = imList.toList();
        Assert.assertTrue(list.equals(to));
        
        Integer[] arr = (Integer[]) imList.toArray(new Integer[]{});
        Integer[] sar = (Integer[]) list.toArray(new Integer[]{});
        Assert.assertArrayEquals(arr, sar);
        
        // modify the underlying data structure
        list.add(11);
        if (copyOrWrap == Immutable.COPY)
            Assert.assertTrue(imList.size() == 10);
        else
            Assert.assertTrue(imList.size() == 11);
    }
}
