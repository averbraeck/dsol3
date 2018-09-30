package nl.tudelft.simulation.language.filters;

/**
 * The composite filter combines two filters.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/">https://simulation.tudelft.nl</a>. The
 * DSOL project is distributed under a three-clause BSD-style license, which can be found at <a href=
 * "https://simulation.tudelft.nl/dsol/3.0/license.html">https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang" >Niels Lang </a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class CompositeFilter extends AbstractFilter
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the AND operator. */
    public static final short AND = 0;

    /** the OR operator. */
    public static final short OR = 1;

    /** the operator of the composite filter. */
    private short operator = -1;

    /** the filters to compose. */
    private FilterInterface[] filters = new FilterInterface[2];

    /**
     * constructs a new CompositeFilter.
     * @param filter1 the first filter
     * @param filter2 the second filter
     * @param operator the operator (AND or OR)
     */
    public CompositeFilter(final FilterInterface filter1, final FilterInterface filter2, final short operator)
    {
        super();
        if (operator < 0 || operator > 1)
        {
            throw new IllegalArgumentException("unknown operator");
        }
        this.filters[0] = filter1;
        this.filters[1] = filter2;
        this.operator = operator;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean filter(final Object entry)
    {
        if (this.operator == CompositeFilter.AND)
        {
            return this.filters[0].accept(entry) && this.filters[1].accept(entry);
        }
        return this.filters[0].accept(entry) || this.filters[1].accept(entry);
    }

    /**
     * Converts the operator of this filter into a human readable string.
     * @return the operator in human readable string
     */
    protected String operatorToString()
    {
        if (this.operator == AND)
        {
            return "AND";
        }
        return "OR";
    }

    /** {@inheritDoc} */
    @Override
    public String getCriterion()
    {
        return "composed[" + this.filters[0].getCriterion() + ";" + operatorToString() + ";"
                + this.filters[1].getCriterion() + "]";
    }
}
