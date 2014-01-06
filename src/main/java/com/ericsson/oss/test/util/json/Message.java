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
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The <code>Message</code> class represents a JSON pay-load of primitive and structured types (e.g. <code>JSONObject</code> and
 * <code>JSONArray</code> types).
 * 
 * <p>
 * It encapsulates a consistent JSON encoding and decoding functionality, ensuring that its clients don't have to embark on this handling. It also
 * provides a uniform printing of encoded JSON data structures to its clients.
 * 
 * @author eleejhn
 */
public class Message {
    public static final int INDENT_FACTOR = 4;
    private String name;
    private volatile Parameter[] parameters;

    public enum EncodeMode {
        COMPACT, VERBOSE
    }

    public Message(final Parameter... parameters) {
        this.name = null;
        this.parameters = parameters;
    }

    public Message(final String name, final ArrayParameter parameters) {
        this.name = name;
        this.parameters = new Parameter[] { parameters };
    }

    /**
     * Decode a JSON message
     * 
     * @param msgText
     */
    public Message(final String msgText) throws JSONException {
        new Decoder().decode(msgText, this);
    }

    public String getName() {
        return name;
    }

    public Parameter[] getNativeParameters() {
        return parameters;
    }

    public ArrayParameter getParameters() {
        return (ArrayParameter) parameters[0];
    }

    @SuppressWarnings("unchecked")
    public <T extends Parameter> T getParameter(final String paramName) {
        T parameter = null;
        for (Parameter param : parameters) {
            if (param.getName().equals(paramName)) {
                parameter = (T) param;
            }
        }
        if (parameter == null) {
            throw new IllegalArgumentException("The parameter name " + paramName + " is not found.");
        }
        return parameter;
    }

    /**
     * Encode a message in JSON
     * 
     * @return String
     */
    public String toJSONString(final EncodeMode encodeMode) throws Exception {
        return new Encoder().encode(name, parameters, encodeMode);
    }

    /**
     * Encode a message in pretty JSON
     * 
     * @return String
     */
    public String toJSONStringPretty(final EncodeMode encodeMode) throws JSONException {
        String msgText = new Encoder().encode(name, parameters, encodeMode);
        JSONObject jsonObject = new JSONObject(msgText);
        return jsonObject.toString(INDENT_FACTOR);
    }

    class Encoder {
        String encode(final String name, final Parameter[] parameters, final Message.EncodeMode encodeMode) throws JSONException {
            String encodedMessage = null;
            if (parameters != null && name != null) {
                final JSONObject message = new JSONObject();
                message.put(name, ((ArrayParameter) parameters[0]).toJSONArray(encodeMode));
                encodedMessage = message.toString();
            }
            if (parameters != null && name == null) {
                final JSONObject msg = new JSONObject();
                for (int i = 0; i < parameters.length; i++) {
                    Parameter param = parameters[i];
                    if (param instanceof ArrayParameter) {
                        msg.put(param.getName(), ((ArrayParameter) param).toJSONArray(encodeMode));
                    } else {
                        msg.put(param.getName(), param.getValue());
                    }
                }
                encodedMessage = msg.toString();
            }
            return encodedMessage;
        }
    }

    class Decoder {
        void decode(final String msgText, final Message message) throws JSONException {
            final JSONObject jsonObject = new JSONObject(msgText);
            final JSONArray names = jsonObject.names();

            if (names.length() == 1) {
                final String firstName = (String) (names.get(0));
                final Object firstObj = jsonObject.get(firstName);

                if (firstObj instanceof JSONArray) {
                    final JSONArray params = jsonObject.getJSONArray(firstName);
                    message.name = firstName;
                    message.parameters = new Parameter[] { decodeParams(params) };
                } else {
                    message.parameters = decodeParams(jsonObject);
                }
            } else {
                message.parameters = decodeParams(jsonObject);
            }
        }

        private Parameter[] decodeParams(final JSONObject jsonObject) throws JSONException {
            final Iterator<?> keys = jsonObject.keys();
            final ArrayList<Parameter> paramArray = new ArrayList<Parameter>();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Parameter param = decodeParam(jsonObject.get(key));
                param.setName(key);
                paramArray.add(param);
            }
            final Parameter[] params = {};
            return paramArray.toArray(params);
        }

        private ArrayParameter decodeParams(final JSONArray params) throws JSONException {
            final ArrayParameter arrayParameter = new ArrayParameter("");
            for (int i = 0; i < params.length(); i++) {
                arrayParameter.add(decodeParam(params.get(i)));
            }
            return arrayParameter;
        }

        private Parameter decodeParam(Object obj) throws JSONException {
            Parameter param = null;
            final String paramName = "";
            if (obj instanceof JSONObject) {
                JSONObject jsonParam = (JSONObject) obj;
                GroupedParameter groupedParam = new GroupedParameter("");
                groupedParam.setValues(decodeParams(jsonParam));
                param = groupedParam;
            } else if (obj instanceof JSONArray) {
                JSONArray params = (JSONArray) obj;
                param = decodeParams(params);
            } else {
                if (obj instanceof String) {
                    String value = (String) obj;
                    param = new SingleParameter("", value);
                } else if (obj instanceof Integer) {
                    Integer value = (Integer) obj;
                    param = new SingleParameter("", value);
                } else if (obj instanceof Long) {
                    Long value = (Long) obj;
                    param = new SingleParameter("", value);
                } else if (obj instanceof Boolean) {
                    Boolean value = (Boolean) obj;
                    param = new SingleParameter("", value);
                }
            }

            if (param != null) {
                param.setName(paramName);
            }
            return param;
        }
    }
}
