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

import java.util.ArrayList;

import org.json.JSONArray;

public class ByteArrayParameter extends ArrayParameter {
    public ByteArrayParameter(final byte[] ba) {
        super("", byteArrayToArrayList(ba));
    }

    public ByteArrayParameter(final String name, final byte[] ba) {
        super(name, byteArrayToArrayList(ba));
    }

    @Override
    public void add(final Parameter param) {
        throw new IllegalStateException("Don't support adding a Parameter to this type.");
    }

    private static ArrayList<Parameter> byteArrayToArrayList(final byte[] ba) {
        final ArrayList<Parameter> arrayList = new ArrayList<Parameter>();
        if (ba != null) {
            for (int i = 0; i < ba.length; i++) {
                arrayList.add(new SingleParameter("" + i, byteToInt(ba[i])));
            }
        }
        return arrayList;
    }

    private static int byteToInt(final byte b) {
        int i = b;
        if (b < 0) {
            i = 256 + b;
        }
        return i;
    }

    private static byte intToByte(final int i) throws Exception {
        if (i < 0 || i > 255) {
            throw new IllegalArgumentException("Invalid byte value " + i);
        }
        return (byte) i;
    }

    JSONArray toJSONArray(final Message.EncodeMode encodeMode) {
        JSONArray jsonArray = null;
        if (values != null) {
            jsonArray = new JSONArray();
            for (int i = 0; i < values.size(); i++) {
                int intValue = (Integer) (values.get(i).getValue());
                jsonArray.put(intValue);
            }
        }
        return jsonArray;
    }

    public static byte[] decodeByteArray(Object obj) throws Exception {
        byte[] ba = null;
        if (obj instanceof ArrayParameter) {
            final ArrayParameter arrayParameter = (ArrayParameter) obj;
            final ArrayList<?> values = arrayParameter.getValues();
            ba = new byte[values.size()];
            for (int i = 0; i < values.size(); i++) {
                SingleParameter param = (SingleParameter) values.get(i);
                Object value = param.getValue();
                if (!(value instanceof Integer)) {
                    throw new IllegalArgumentException("Invalid type");
                }
                int intValue = ((Integer) value).intValue();
                ba[i] = intToByte(intValue);
            }
        }
        return ba;
    }
}
