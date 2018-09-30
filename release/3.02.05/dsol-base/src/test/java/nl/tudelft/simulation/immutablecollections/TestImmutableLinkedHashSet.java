package nl.tudelft.simulation.immutablecollections;

import java.util.Arrays;
import java.util.LinkedHashSet;
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
public class TestImmutableLinkedHashSet
{

    @Test
    public final void testLinkedHashSet()
    {
        Set<Integer> intSet = new LinkedHashSet<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        Set<Integer> set = new LinkedHashSet<Integer>(intSet);
        testIntSet(set, new ImmutableLinkedHashSet<Integer>(set, Immutable.WRAP), Immutable.WRAP);
        set = new LinkedHashSet<Integer>(intSet);
        testIntSet(set, new ImmutableLinkedHashSet<Integer>(set, Immutable.COPY), Immutable.COPY);
        set = new LinkedHashSet<Integer>(intSet);
        testIntSet(set, new ImmutableLinkedHashSet<Integer>(set), Immutable.COPY);
        set = new LinkedHashSet<Integer>(intSet);
        ImmutableLinkedHashSet<Integer> ihs = new ImmutableLinkedHashSet<Integer>(set);
        testIntSet(set, new ImmutableLinkedHashSet<Integer>(ihs), Immutable.COPY);

        set = new LinkedHashSet<Integer>(intSet);
        List<Integer> il = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        testIntSet(set, new ImmutableLinkedHashSet<Integer>(il), Immutable.COPY);
    }

    private void testIntSet(final Set<Integer> set, final ImmutableSet<Integer> imSet, final Immutable copyOrWrap)
    {
        Assert.assertTrue(set.size() == 10);
        Assert.assertTrue(imSet.size() == 10);
        for (int i = 0; i < 10; i++)
            Assert.assertTrue(imSet.contains(i + 1));
        Assert.assertFalse(imSet.isEmpty());
        Assert.assertFalse(imSet.contains(15));
        if (copyOrWrap == Immutable.COPY)
        {
            Assert.assertTrue(imSet.isCopy());
            Assert.assertTrue(imSet.toSet().equals(set));
            Assert.assertFalse(imSet.toSet() == set);
        }
        else
        {
            Assert.assertTrue(imSet.isWrap());
            Assert.assertTrue(imSet.toSet().equals(set));
            Assert.assertFalse(imSet.toSet() == set); // this WRAP method returns a NEW list
        }

        Set<Integer> to = imSet.toSet();
        Assert.assertTrue(set.equals(to));

        Integer[] arr = (Integer[]) imSet.toArray(new Integer[]{});
        Integer[] sar = (Integer[]) set.toArray(new Integer[]{});
        Assert.assertArrayEquals(arr, sar);

        // modify the underlying data structure
        set.add(11);
        if (copyOrWrap == Immutable.COPY)
            Assert.assertTrue(imSet.size() == 10);
        else
            Assert.assertTrue(imSet.size() == 11);
    }
}
