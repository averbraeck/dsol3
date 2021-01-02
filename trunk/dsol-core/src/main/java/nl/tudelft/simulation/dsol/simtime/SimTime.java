package nl.tudelft.simulation.dsol.simtime;

import java.io.Serializable;

/**
 * The abstract SimTime class that implements the majority of the operators on a SimTime.
 * <p>
 * Copyright (c) 2016-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type, e.g. Calendar for the absolute time to ensure type safety. This is the <i>absolute</i>
 *            number, e.g., a Calendar for a simulation time. For simple calendar types such as a double for simulation time,
 *            the internal (relative) and external (absolute) storage types are the same.
 * @param <R> the relative number type, e.g. Double for the internal storage type to ensure type safety. This is the
 *            <i>relative</i> number, so in case of a Calendar for a simulation time, the relative storage type is a relative
 *            time with a unit.
 * @param <T> the extended type itself to be able to implement a comparator, and to ease the use of extension return types.
 */
public abstract class SimTime<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> implements Serializable, Comparable<T>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * @param time A; initial time.
     */
    public SimTime(final A time)
    {
        set(time);
    }

    /**
     * add a relative time to this simtime.
     * @param relativeTime R; the time to add.
     */
    public abstract void add(R relativeTime);

    /**
     * subtract a simtime from this simtime.
     * @param relativeTime R; the simtime to subtract.
     */
    public abstract void subtract(R relativeTime);

    /**
     * add a number of simtimes.
     * @param absTime T; the absolute time to add the values to.
     * @param relativeTimes R...; the relative times to add.
     * @return the sum of the absolute time and the relative times.
     */
    @SuppressWarnings("unchecked")
    public final T sum(final T absTime, final R... relativeTimes)
    {
        T ret = absTime.copy();
        for (R relTime : relativeTimes)
        {
            ret.add(relTime);
        }
        return ret;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public int compareTo(final T simTime)
    {
        return get().compareTo(simTime.get());
    }

    /**
     * initialize a simtime to its logical 'zero' time.
     * @return the zero simTime, so e.g., time.copy().setZero() can be used.
     */
    public abstract T setZero();

    /**
     * @return a copy of the time.
     */
    public abstract T copy();

    /**
     * set the value of the SimTime.
     * @param absoluteTime A; the value to set the SimTime to.
     */
    public abstract void set(A absoluteTime);

    /**
     * @return the value of the SimTime.
     */
    public abstract A get();

    /**
     * The plus function makes a copy of the time, adds the relative time, and returns the result.
     * @param relativeTime R; the time to add.
     * @return a copy of the time with the relative time added.
     */
    public final T plus(final R relativeTime)
    {
        T ret = copy();
        ret.add(relativeTime);
        return ret;
    }

    /**
     * The minus function makes a copy of the time, subtracts the relative time, and returns the result.
     * @param relativeTime R; the time to subtract.
     * @return a copy of the time with the relative time subtracted.
     */
    public final T minus(final R relativeTime)
    {
        T ret = copy();
        ret.subtract(relativeTime);
        return ret;
    }

    /**
     * The minus function of two absolute times returns a relative time, which is the difference between the two absolute times.
     * @param simTime T; the time to subtract.
     * @return the relative time difference between this SimTime object and the provided absoluteTime argument.
     */
    public final R diff(final T simTime)
    {
        return diff(simTime.get());
    }

    /**
     * The minus function of two absolute times returns a relative time, which is the difference between the two absolute times.
     * Note that the function was called diff because the erasure of minus(A) is the same as the erasure of minus(R) in case of
     * SimTimeDOuble, SimTimeFloat and SimTimeLong.
     * @param absoluteTime A; the time to subtract.
     * @return the relative time based, which is the difference between the two absolute times.
     */
    public abstract R diff(A absoluteTime);

    /**
     * Return absolute zero.
     * @return absolute zero.
     */
    public abstract A getAbsoluteZero();

    /**
     * Return relative zero.
     * @return relative zero.
     */
    public abstract R getRelativeZero();

    /**
     * @param simTime T; the time to compare to
     * @return true if greater than simTime
     */
    public final boolean gt(final T simTime)
    {
        return this.compareTo(simTime) > 0;
    }

    /**
     * @param simTime T; the time to compare to
     * @return true if greater than or equal to simTime
     */
    public final boolean ge(final T simTime)
    {
        return this.compareTo(simTime) >= 0;
    }

    /**
     * @param simTime T; the time to compare to
     * @return true if less than simTime
     */
    public final boolean lt(final T simTime)
    {
        return this.compareTo(simTime) < 0;
    }

    /**
     * @param simTime T; the time to compare to
     * @return true if less than or equal to simTime
     */
    public final boolean le(final T simTime)
    {
        return this.compareTo(simTime) <= 0;
    }

    /**
     * @param simTime T; the time to compare to
     * @return true if contents equal to simTime's contents
     */
    public final boolean eq(final T simTime)
    {
        return this.compareTo(simTime) == 0;
    }

    /**
     * @param simTime T; the time to compare to
     * @return true if contents not equal to simTime's contents
     */
    public final boolean ne(final T simTime)
    {
        return this.compareTo(simTime) != 0;
    }

    /**
     * @return whether this simTime equals the zero SimTime.
     */
    public final boolean eq0()
    {
        return eq(this.copy().setZero());
    }

    /**
     * @return whether this simTime is not equal to the zero SimTime.
     */
    public final boolean ne0()
    {
        return ne(this.copy().setZero());
    }

    /**
     * @return whether this simTime is not equal to the zero SimTime.
     */
    public final boolean lt0()
    {
        return lt(this.copy().setZero());
    }

    /**
     * @return whether this simTime is not equal to the zero SimTime.
     */
    public final boolean le0()
    {
        return le(this.copy().setZero());
    }

    /**
     * @return whether this simTime is not equal to the zero SimTime.
     */
    public final boolean gt0()
    {
        return gt(this.copy().setZero());
    }

    /**
     * @return whether this simTime is not equal to the zero SimTime.
     */
    public final boolean ge0()
    {
        return ge(this.copy().setZero());
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "" + get();
    }
}
