package nl.tudelft.simulation.dsol.model.inputparameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.djunits.unit.LengthUnit;
import org.djunits.value.vdouble.scalar.Length;
import org.junit.Test;

/**
 * InputParameterTest.java. <br>
 * <br>
 * Copyright (c) 2003-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class InputParameterTest
{

    /** test boolean input parameter. */
    @Test
    public final void testInputParameterBoolean()
    {
        InputParameterBoolean ip = new InputParameterBoolean("bool", "boolean", "boolean value", true, 1.0);
        assertEquals("key should be 'bool'", "bool", ip.getKey());
        assertEquals("short name should be 'boolean'", "boolean", ip.getShortName());
        assertEquals("description should be 'boolean value'", "boolean value", ip.getDescription());
        assertTrue("Default value should be 'true'", ip.getDefaultValue());
        assertTrue("InputParameter should have calculated value 'true'", ip.getCalculatedValue());
        assertTrue("InputParameter should have value 'true'", ip.getValue());
        try
        {
            ip.setBooleanValue(false);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        assertTrue("Default value should be 'true'", ip.getDefaultValue());
        assertTrue("InputParameter should have calculated value 'false'", !ip.getCalculatedValue());
        assertTrue("InputParameter should have value 'false'", !ip.getValue());
    }

    /**
     * test double input parameter.
     * @throws InputParameterException o setDoubleValue that did not work
     */
    @Test
    public final void testInputParameterDouble() throws InputParameterException
    {
        InputParameterDouble ip = new InputParameterDouble("d", "double", "double value", 12.34, 2.0);
        assertEquals("key should be 'd'", "d", ip.getKey());
        assertEquals("short name should be 'double'", "double", ip.getShortName());
        assertEquals("description should be 'double value'", "double value", ip.getDescription());
        assertEquals("Default value should be '12.34'", 12.34, ip.getDefaultValue(), 0.001);
        assertEquals("InputParameter should have calculated value '12.34'", 12.34, ip.getCalculatedValue(), 0.001);
        assertEquals("InputParameter should have value '12.34'", 12.34, ip.getValue(), 0.001);
        try
        {
            ip.setDoubleValue(23.45);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        assertEquals("Default value should be '12.34'", 12.34, ip.getDefaultValue(), 0.001);
        assertEquals("InputParameter should have calculated value '23.45'", 23.45, ip.getCalculatedValue(), 0.001);

        InputParameterDouble ipmmff =
                new InputParameterDouble("d", "double", "double value", 12.34, 0.0, 20.0, false, false, "%6.2f", 3.0);
        try
        {
            ipmmff.setDoubleValue(0.0);
            fail("Set value to 0.0 should have thrown exception");
        }
        catch (Exception exception)
        {
            // ok
        }
        try
        {
            ipmmff.setDoubleValue(20.0);
            fail("Set value to 20.0 should have thrown exception");
        }
        catch (Exception exception)
        {
            // ok
        }
        try
        {
            ipmmff.setDoubleValue(Double.NaN);
            fail("Set value to NaN should have thrown exception");
        }
        catch (Exception exception)
        {
            // ok
        }
        try
        {
            ipmmff.setDoubleValue(Double.POSITIVE_INFINITY);
            fail("Set value to Inf should have thrown exception");
        }
        catch (Exception exception)
        {
            // ok
        }
        try
        {
            ipmmff.setDoubleValue(50.0);
            fail("Set value to 50 should have thrown exception");
        }
        catch (Exception exception)
        {
            // ok
        }
        ipmmff.setDoubleValue(0.0001);
        ipmmff.setDoubleValue(19.999);
    }

    /**
     * test double input parameter.
     * @throws InputParameterException o setDoubleValue that did not work
     */
    @Test
    public final void testInputParameterDoubleScalar() throws InputParameterException
    {
        Length len = new Length(12.34, LengthUnit.CENTIMETER);
        InputParameterDoubleScalar<LengthUnit, Length> ip =
                new InputParameterDoubleScalar<>("d", "double", "double value", len, 2.0);
        assertEquals("key should be 'd'", "d", ip.getKey());
        assertEquals("short name should be 'double'", "double", ip.getShortName());
        assertEquals("description should be 'double value'", "double value", ip.getDescription());
        assertEquals("Default value should be '12.34 cm'", len, ip.getDefaultTypedValue());
        ip.setCalculatedValue();
        assertEquals("InputParameter should have value '12.34 cm'", len, ip.getCalculatedValue());
        assertEquals(12.34, ip.getDoubleParameter().getValue().doubleValue(), 0.001);
        assertEquals(0.1234, ip.getCalculatedValue().si, 0.0001);
        try
        {
            ip.getDoubleParameter().setDoubleValue(23.45);
            ip.getUnitParameter().setMapValue(LengthUnit.KILOMETER);
            ip.setCalculatedValue();
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
        assertEquals("Default value should be '12.34 cm'", len, ip.getDefaultTypedValue());
        assertEquals("InputParameter should have calculated value '23.45 km'", new Length(23.45, LengthUnit.KILOMETER).si,
                ip.getCalculatedValue().si, 0.001);

        InputParameterDoubleScalar<LengthUnit, Length> ipmmff =
                new InputParameterDoubleScalar<>("d", "double", "double value", len, 0.0, 20.0, false, false, "%6.2f", 3.0);
        try
        {
            ipmmff.getDoubleParameter().setDoubleValue(0.0);
            ipmmff.getUnitParameter().setMapValue(LengthUnit.HECTOMETER);
            ipmmff.setCalculatedValue();
            fail("Set value to 0.0 hm should have thrown exception");
        }
        catch (Exception exception)
        {
            // ok
        }
        try
        {
            ipmmff.getDoubleParameter().setDoubleValue(0.0);
            ipmmff.getUnitParameter().setMapValue(LengthUnit.METER);
            ipmmff.setCalculatedValue();
            fail("Set value to 20.0 m should have thrown exception");
        }
        catch (Exception exception)
        {
            // ok
        }
        try
        {
            ipmmff.getDoubleParameter().setDoubleValue(Double.NaN);
            ipmmff.getUnitParameter().setMapValue(LengthUnit.METER);
            ipmmff.setCalculatedValue();
            fail("Set value to NaN m should have thrown exception");
        }
        catch (Exception exception)
        {
            // ok
        }
        try
        {
            ipmmff.getDoubleParameter().setDoubleValue(Double.POSITIVE_INFINITY);
            ipmmff.getUnitParameter().setMapValue(LengthUnit.METER);
            ipmmff.setCalculatedValue();
            fail("Set value to Inf m should have thrown exception");
        }
        catch (Exception exception)
        {
            // ok
        }
        try
        {
            ipmmff.getDoubleParameter().setDoubleValue(50.0);
            ipmmff.getUnitParameter().setMapValue(LengthUnit.METER);
            ipmmff.setCalculatedValue();
            fail("Set value to 50 m should have thrown exception");
        }
        catch (Exception exception)
        {
            // ok
        }
        try
        {
            ipmmff.getDoubleParameter().setDoubleValue(1.0);
            ipmmff.getUnitParameter().setMapValue(LengthUnit.KILOMETER);
            ipmmff.setCalculatedValue();
            fail("Set value to 1.0 km should have thrown exception");
        }
        catch (Exception exception)
        {
            // ok
        }
        
        ipmmff.getDoubleParameter().setDoubleValue(1.0);
        ipmmff.getUnitParameter().setMapValue(LengthUnit.METER);
        ipmmff.setCalculatedValue();

        ipmmff.getDoubleParameter().setDoubleValue(5000.0);
        ipmmff.getUnitParameter().setMapValue(LengthUnit.MILLIMETER);
        ipmmff.setCalculatedValue();

    }

}
