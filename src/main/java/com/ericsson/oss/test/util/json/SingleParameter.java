/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.test.util.json;

/**
 * The <code>SingleParameter</code> class is a representation of a single parameter.
 * 
 * <p>
 * This contains some static utility methods for some primitive JSON data structures that we plan on supporting. The primitive JSON data structures
 * include things like like Strings, Numbers, Booleans, and Null. We are currently supporting Integer, Long, Boolean, String, and Byte types.
 * 
 * @author eleejhn
 */
public class SingleParameter extends Parameter {
    public SingleParameter(final String name, final Object value) {
        super(name, value);
        setValue(value);
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Byte) {
            byte byteValue = (Byte) value;
            if (byteValue >= 0) {
                value = new Integer(byteValue);
            } else {
                value = new Integer(256 + byteValue);
            }
        } else {
            this.value = value;
        }
    }

    public static Integer decodeInteger(final Object obj) throws Exception {
        Integer intValue = null;
        if (obj instanceof SingleParameter) {
            SingleParameter param = (SingleParameter) obj;
            intValue = (Integer) param.getValue();
        } else {
            throw new IllegalArgumentException("Argument passed should be of type SingleParameter");
        }
        return intValue;
    }

    public static Long decodeLong(final Object obj) throws Exception {
        Long longValue = null;
        if (obj instanceof SingleParameter) {
            SingleParameter param = (SingleParameter) obj;
            longValue = (Long) param.getValue();
        } else {
            throw new IllegalArgumentException("Argument passed should be of type SingleParameter");
        }
        return longValue;
    }

    public static Boolean decodeBoolean(final Object obj) throws Exception {
        Boolean booleanValue = null;
        if (obj instanceof SingleParameter) {
            SingleParameter param = (SingleParameter) obj;
            booleanValue = (Boolean) param.getValue();
        } else {
            throw new IllegalArgumentException("Argument passed should be of type SingleParameter");
        }
        return booleanValue;
    }

    public static String decodeString(final Object obj) throws Exception {
        String stringValue = null;
        if (obj instanceof SingleParameter) {
            SingleParameter param = (SingleParameter) obj;
            stringValue = (String) param.getValue();
        } else {
            throw new IllegalArgumentException("Argument passed should be of type SingleParameter");
        }
        return stringValue;
    }

    public static Byte decodeByte(final Object obj) throws Exception {
        Byte byteValue = null;
        Integer intValue = decodeInteger(obj);
        if (intValue < 0 || intValue > 255) {
            throw new IllegalArgumentException("Invalid byte value " + intValue);
        }
        byteValue = new Byte((byte) (intValue.intValue()));
        return byteValue;
    }
}
