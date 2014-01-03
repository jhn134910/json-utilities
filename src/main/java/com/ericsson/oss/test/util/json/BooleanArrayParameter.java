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

/**
 * 
 * @author eleejhn
 */
public class BooleanArrayParameter extends ArrayParameter {
    public BooleanArrayParameter(final boolean[] ba) {
        super("", booleanArrayToArrayList(ba));
    }

    public BooleanArrayParameter(final String name, final boolean[] ba) {
        super(name, booleanArrayToArrayList(ba));
    }

    @Override
    public void add(final Parameter param) {
        throw new IllegalStateException("Don't support adding a Parameter to this type.");
    }

    private static ArrayList<Parameter> booleanArrayToArrayList(final boolean[] ba) {
        final ArrayList<Parameter> arrayList = new ArrayList<Parameter>();
        if (ba != null) {
            for (int i = 0; i < ba.length; i++) {
                arrayList.add(new SingleParameter("" + i, ba[i]));
            }
        }
        return arrayList;
    }

    JSONArray toJSONArray(final Message.EncodeMode encodeMode) {
        JSONArray jsonArray = null;
        if (values != null) {
            jsonArray = new JSONArray();
            for (int i = 0; i < values.size(); i++) {
                boolean b = (Boolean) (values.get(i).getValue());
                jsonArray.put(b);
            }
        }
        return jsonArray;
    }
}
