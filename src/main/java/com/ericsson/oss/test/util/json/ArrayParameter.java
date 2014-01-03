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
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The <code>ArrayParameter</code> represents an array of <code>Parameter</code> types.
 * 
 * <p>
 * The <code>ArrayParameter</code> would typically make up a <code>JSONArray</code> JSON structured type, within the JSON <code>Message</code>. This
 * class is used for populating <code>JSONArray</code> based on <code>Parameter</code> types.
 * 
 * @author eleejhn
 */
public class ArrayParameter extends Parameter {
    protected ArrayList<Parameter> values;

    public ArrayParameter(final String name) {
        super(name, null);
        values = new ArrayList<Parameter>();
    }

    public ArrayParameter(final String name, final ArrayList<Parameter> values) {
        super(name, null);
        this.values = new ArrayList<Parameter>(values);
    }

    public ArrayList<Parameter> getValues() {
        return values;
    }

    public void add(final Parameter param) {
        values.add(param);
    }

    public Parameter get(final String name) {
        Parameter parameterToReturn = null;
        for (final Parameter param : values) {
            if (param.getName().equals(name)) {
                parameterToReturn = param;
            }
        }
        if (parameterToReturn == null) {
            throw new IllegalArgumentException("The parameter " + name + " is not found");
        }
        return parameterToReturn;
    }

    JSONArray toJSONArray(final Message.EncodeMode encodeMode) throws JSONException {
        JSONArray jsonArray = null;
        if (values != null) {
            jsonArray = new JSONArray();
            for (int i = 0; i < values.size(); i++) {
                Parameter param = values.get(i);
                if (encodeMode == Message.EncodeMode.VERBOSE) {
                    if (param instanceof SingleParameter) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(param.getName(), param.getValue());
                        jsonArray.put(jsonObject);
                    } else if (param instanceof ArrayParameter) {
                        ArrayParameter arrayParam = (ArrayParameter) param;
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(param.getName(), arrayParam.toJSONArray(encodeMode));
                        jsonArray.put(jsonObject);
                    }
                } else {
                    if (param instanceof SingleParameter) {
                        jsonArray.put(param.getValue());
                    } else if (param instanceof ArrayParameter) {
                        ArrayParameter arrayParam = (ArrayParameter) param;
                        jsonArray.put(arrayParam.toJSONArray(encodeMode));
                    }
                }
            }
        }
        return jsonArray;
    }
}
