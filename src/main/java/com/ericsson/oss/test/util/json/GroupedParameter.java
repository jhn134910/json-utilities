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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The <code>GroupedParameter</code> represents grouped <code>SingleParameter</code> types.
 * 
 * <p>
 * The <code>GroupedParameter</code> would typically make up a <code>JSONObject</code> JSON structured type, within the JSON <code>Message</code>.
 * This class is used for populating <code>JSONObject</code> based on <code>SingleParameter</code> types.
 * 
 * @author eleejhn
 */
public class GroupedParameter extends SingleParameter {
    protected ArrayList<Parameter> values;

    public GroupedParameter(final String name) {
        super(name, null);
        values = new ArrayList<Parameter>();
    }

    public GroupedParameter(final String name, final ArrayList<Parameter> values) {
        super(name, null);
        this.values = new ArrayList<Parameter>(values);
    }

    public ArrayList<Parameter> getValues() {
        return new ArrayList<Parameter>(values);
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

    public void setValues(Parameter[] params) {
        for (int i = 0; i < params.length; i++) {
            values.add(params[i]);
        }
    }

    public Object getValue() {
        Object objectToReturn = null;
        try {
            objectToReturn = (Object) toJSONObject(Message.EncodeMode.VERBOSE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objectToReturn;
    }

    JSONObject toJSONObject(final Message.EncodeMode encodeMode) throws JSONException {
        JSONObject objectGroup = null;
        if (values != null) {
            objectGroup = new JSONObject();
            for (int i = 0; i < values.size(); i++) {
                Parameter param = values.get(i);
                if (param instanceof SingleParameter) {
                    objectGroup.put(param.getName(), param.getValue());
                } else if (param instanceof ArrayParameter) {
                    ArrayParameter arrayParam = (ArrayParameter) param;
                    objectGroup.put(param.getName(), arrayParam.toJSONArray(encodeMode));
                }
            }
        }
        return objectGroup;
    }
}
