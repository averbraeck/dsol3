package nl.tudelft.simulation.dsol.animation.graph;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * DoubleList is a random access list of double values to which values can only be appended. It stores the values as primitives
 * rather than as wrapping Objects. For the rest, it behaves like a regular list, with the exception of the absence of the
 * add(i, value) method and the remove methods.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DoubleAppendList implements Iterable<Double>, Serializable
{
    /** */
    private static final long serialVersionUID = 20210103L;

    /** the chunk size. */
    private static final int CHUNK_SIZE = 256;

    /** the number of bits of the chunk size. */
    private static final int CHUNK_BITS = 8;

    /** the mask for the rightmost CHUNK_BITS bits. */
    private static final int CHUNK_MASK = CHUNK_SIZE - 1;

    /** the backing list with chunks. */
    private final List<double[]> storage = new ArrayList<>();

    /** the current chunk, already stored in the List. */
    private double[] currentChunk;

    /** the total number of elements. */
    private int numElements;

    /** the current in-chunk counter that indicates the next value to be written. */
    private int inChunkNext;

    /**
     * Initialize the DoubleAppendList.
     */
    public DoubleAppendList()
    {
        this.numElements = 0;
        this.inChunkNext = 0;
        this.currentChunk = new double[CHUNK_SIZE];
        this.storage.add(this.currentChunk);
    }

    /**
     * Add a value to the list.
     * @param value double; the value to add
     */
    public void add(final double value)
    {
        if (this.inChunkNext == CHUNK_SIZE)
        {
            this.inChunkNext = 0;
            this.currentChunk = new double[CHUNK_SIZE];
            this.storage.add(this.currentChunk);
        }
        this.currentChunk[this.inChunkNext] = value;
        this.inChunkNext++;
        this.numElements++;
    }

    /**
     * Return the number of elements.
     * @return int; the number of elements
     */
    public int size()
    {
        return this.numElements;
    }

    /**
     * Return the value at a position.
     * @param i int; the position
     * @return double; the value at the position
     * @throws IndexOutOfBoundsException when i &lt; 0 or i &gt; number of values
     */
    public double get(final int i)
    {
        if (i < 0 || i >= this.numElements)
        {
            throw new IndexOutOfBoundsException("DoubleAppendList.get(i) -- i out of bounds.");
        }
        return this.storage.get(i >> CHUNK_BITS)[i & CHUNK_MASK];
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Double> iterator()
    {
        return new DoubleAppendIterator(this);
    }

    /**
     * An iterator for the DoubleAppendList.
     */
    class DoubleAppendIterator implements Iterator<Double>
    {
        /** the list to iterate over. */
        private final DoubleAppendList list;

        /** the counter. */
        private int counter;

        /**
         * Make an iterator for the DoubleAppendList.
         * @param list DoubleAppendList; the list to iterate over
         */
        DoubleAppendIterator(final DoubleAppendList list)
        {
            this.list = list;
            this.counter = 0;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext()
        {
            return this.counter < this.list.size();
        }

        /** {@inheritDoc} */
        @Override
        public Double next()
        {
            return this.list.get(this.counter++);
        }
    }

    /**
     * @param args not used
     */
    public static void main(final String[] args)
    {
        int num = 10_000_000;
        long t = System.currentTimeMillis();
        DoubleAppendList list = new DoubleAppendList();
        for (int i = 0; i < num; i++)
        {
            list.add(i);
        }
        for (int i = 0; i < num; i++)
        {
            if (i != list.get(i))
            {
                System.err.println("get-error at " + i);
            }
        }
        int count = 0;
        for (double d : list)
        {
            if (count != d)
            {
                System.err.println("iterate-error at " + count);
            }
            count++;
        }
        System.out.println("msec DoubleArrayList = " + (System.currentTimeMillis() - t));
        System.out.println("size DoubleArrayList = " + sizeOf(list));

        t = System.currentTimeMillis();
        ArrayList<Double> alist = new ArrayList<>();
        for (int i = 0; i < num; i++)
        {
            alist.add((double) i);
        }
        for (int i = 0; i < num; i++)
        {
            if (i != alist.get(i))
            {
                System.err.println("get-error at " + i);
            }
        }
        count = 0;
        for (double d : alist)
        {
            if (count != d)
            {
                System.err.println("iterate-error at " + count);
            }
            count++;
        }
        System.out.println("msec       ArrayList = " + (System.currentTimeMillis() - t));
        System.out.println("size       ArrayList = " + sizeOf(alist));
    }

    /**
     * Calculate the size of a complete (serializable) object, including its dependencies.
     * @param object the object to check
     * @return the size in bytes
     */
    private static long sizeOf(final Serializable object)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();
            return baos.size();
        }
        catch (IOException ioe)
        {
            return -1;
        }
    }
}
