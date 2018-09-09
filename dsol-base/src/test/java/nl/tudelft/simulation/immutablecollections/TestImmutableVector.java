package nl.tudelft.simulation.immutablecollections;

import java.util.Vector;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * TestImmutable.java. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information
 * <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The source code and
 * binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class TestImmutableVector
{

    @Test
    public final void testVector()
    {
        Vector<Integer> intVector = new Vector(Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        Vector<Integer> vector = new Vector<Integer>(intVector);
        testIntVector(vector, new ImmutableVector<Integer>(vector, Immutable.WRAP), Immutable.WRAP);
        vector = new Vector<Integer>(intVector);
        testIntVector(vector, new ImmutableVector<Integer>(vector, Immutable.COPY), Immutable.COPY);
        vector = new Vector<Integer>(intVector);
        testIntVector(vector, new ImmutableVector<Integer>(vector), Immutable.COPY);
        vector = new Vector<Integer>(intVector);
        ImmutableVector<Integer> ial = new ImmutableVector<Integer>(vector);
        testIntVector(vector, new ImmutableVector<Integer>(ial), Immutable.COPY);
        
        vector = new Vector<Integer>(intVector);
        Set<Integer> intSet = new HashSet<>(Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        testIntVector(vector, new ImmutableVector<Integer>(intSet), Immutable.COPY);
    }

    private void testIntVector(final Vector<Integer> vector, final ImmutableVector<Integer> imVector, final Immutable copyOrWrap)
    {
        Assert.assertTrue(vector.size() == 10);
        Assert.assertTrue(imVector.size() == 10);
        for (int i = 0; i < 10; i++)
            Assert.assertTrue(imVector.get(i) == vector.get(i));
        Assert.assertFalse(imVector.isEmpty());
        Assert.assertTrue(imVector.contains(5));
        Assert.assertFalse(imVector.contains(15));
        if (copyOrWrap == Immutable.COPY)
        {
            Assert.assertTrue(imVector.isCopy());
            Assert.assertTrue(imVector.toList().equals(vector));
            Assert.assertFalse(imVector.toList() == vector);
        }
        else
        {
            Assert.assertTrue(imVector.isWrap());
            Assert.assertTrue(imVector.toList().equals(vector));
            Assert.assertFalse(imVector.toList() == vector); // this WRAP method returns a NEW list
        }
        
        Vector<Integer> to = imVector.toVector();
        Assert.assertTrue(vector.equals(to));
        
        Integer[] arr = (Integer[]) imVector.toArray(new Integer[]{});
        Integer[] sar = (Integer[]) vector.toArray(new Integer[]{});
        Assert.assertArrayEquals(arr, sar);
        
        // modify the underlying data structure
        vector.add(11);
        if (copyOrWrap == Immutable.COPY)
            Assert.assertTrue(imVector.size() == 10);
        else
            Assert.assertTrue(imVector.size() == 11);
    }
}
