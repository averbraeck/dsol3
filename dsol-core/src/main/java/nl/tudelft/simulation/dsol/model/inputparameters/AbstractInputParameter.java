package nl.tudelft.simulation.dsol.model.inputparameters;

/**
 * Abstract input parameter.
 * <p>
 * Copyright (c) 2013-2018 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="http://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * <p>
 * $LastChangedDate: 2016-05-28 11:33:31 +0200 (Sat, 28 May 2016) $, @version $Revision: 2051 $, by $Author: averbraeck $,
 * initial version 18 dec. 2014 <br>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @param <T> type of the input parameter
 */
public abstract class AbstractInputParameter<T> implements InputParameter<T>
{
    /** */
    private static final long serialVersionUID = 20150000L;

    /** Key of this input parameter. */
    private final String key;

    /** The shortName of the input parameter. */
    private String shortName;

    /** The description of the input parameter. */
    private String description;

    /** the default value. */
    private T defaultValue;

    /** the current value. */
    private T value;

    /** Determines sorting order when properties are displayed to the user. */
    private final double displayPriority;

    /** The input parameter is read-only. */
    private boolean readOnly = false;

    /** Parent of this AbstractInputParameter. */
    private InputParameterMap parent = null;

    /**
     * Construct a new AbstractInputParameter.
     * @param key String; unique (within this input parameter tree) name of the new AbstractInputParameter
     * @param shortName String; concise description of the input parameter
     * @param description String; long description of the input parameter (may use HTML markup)
     * @param defaultValue T; the default value of this input parameter
     * @param displayPriority double; sorting order when properties are displayed to the user
     */
    public AbstractInputParameter(final String key, final String shortName, final String description, final T defaultValue,
            final double displayPriority)
    {
        this.key = key;
        this.shortName = shortName;
        this.description = description;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.displayPriority = displayPriority;
    }

    /** {@inheritDoc} */
    @Override
    public final String getKey()
    {
        return this.key;
    }

    /** {@inheritDoc} */
    @Override
    public final String getExtendedKey()
    {
        return (getParent() == null) ? getKey() : getParent().getExtendedKey() + "." + getKey();
    }

    /** {@inheritDoc} */
    @Override
    public final T getValue()
    {
        return this.value;
    }

    /**
     * Change the value of the input parameter. This method is protected and final, so classes that extend this class must use
     * their own method to set the value (e.g., setDoubleValue(...)), which has to call, in turn, super.setValue(...) to make
     * the actual setting of the value happen. In case the setValue(...) method would be non-final and public, it would be too
     * easy to forget to call super.setValue(...). 
     * @param newValue T; the new value for the input parameter
     * @throws InputParameterException when this InputParameter is read-only, or newValue is not valid
     */
    protected final void setValue(final T newValue) throws InputParameterException
    {
        if (isReadOnly())
        {
            throw new InputParameterException("The InputParameter with key " + getExtendedKey() + " is read-only");
        }
        this.value = newValue;
    }

    /** {@inheritDoc} */
    @Override
    public T getDefaultValue()
    {
        return this.defaultValue;
    }

    /** {@inheritDoc} */
    @Override
    public void setDefaultValue(final T newValue) throws InputParameterException
    {
        this.defaultValue = newValue;
    }

    /** {@inheritDoc} */
    @Override
    public final void setReadOnly(final boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    /** {@inheritDoc} */
    @Override
    public final double getDisplayPriority()
    {
        return this.displayPriority;
    }

    /** {@inheritDoc} */
    @Override
    public final String getShortName()
    {
        return this.shortName;
    }

    /** {@inheritDoc} */
    @Override
    public final String getDescription()
    {
        return this.description;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isReadOnly()
    {
        return this.readOnly;
    }

    /**
     * Set the parent of this AbstractInputParameter.
     * @param newParent InputParameterMap; the new parent of this AbstractInputParameter
     */
    protected final void setParent(final InputParameterMap newParent)
    {
        this.parent = newParent;
    }

    /** {@inheritDoc} */
    @Override
    public final InputParameterMap getParent()
    {
        return this.parent;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return getKey() + "[" + getShortName() + "] = " + getValue();
    }

    /** {@inheritDoc} */
    @Override
    public AbstractInputParameter<?> clone() throws CloneNotSupportedException
    {
        return (AbstractInputParameter<?>) super.clone();
    }

}
