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

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ericsson.oss.test.util.json.Message.EncodeMode;

/**
 * 
 * @author eleejhn
 */
public class MessageTest {
    private static boolean verbose = false;
    static final String staticStandardMsgText = "{\"local\":{\"uri\":\"tel:535\",\"na\":3},\"remote\":{\"uri\":\"tel:0405222721\",\"na\":3,\"si\":1,\"pi\":0,\"np\":0,\"ni\":0},\"protocol\":{\"host\":\"p10-tss100-unit2\",\"name\":\"SIP\",\"version\":\"2.0\",\"sip\":{\"invite\":\"sip:535@194.42.60.111 SIP/2.0\",\"callId\":\"770159606@194.42.60.78\",\"via\":\"SIP/2.0/UDP 194.42.60.78:5060\",\"contact\":\"<sip:0405222721@194.42.60.78>\",\"sdp\":{\"v\":0,\"o\":\"- 1489790089 231315048 IN IP4 194.42.60.78\",\"s\":\"-\",\"c\":\"IN IP4 194.42.60.79\",\"m\":[\"audio 48132 RTP/AVP 8 101\",\"video 48130 RTP/AVP 34\"],\"a\":[\"rtpmap:8 PCMA/8000\",\"rtpmap:101 telephone-event/8000\",\"fmtp:101 0-15\",\"sendonly\",\"rtpmap:34 H263/90000\",\"fmtp:34 QCIF=2\",\"sendonly\"]},\"from\":\"0405222721\",\"to\":\"535\"}},\"ericsson\":{\"video\":\"videoh263\",\"unitnumber\":1},\"host\":\"p10-tss100-unit2\"}";
    static JSONObject staticJSONObject;

    static {
        try {
            staticJSONObject = new JSONObject(staticStandardMsgText);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @BeforeClass
    public static void setUp() {
        verbose = true;
    }

    @Test
    public void testArrayParameter_get() {
        final ArrayParameter arrayParam = new ArrayParameter("arrayParam");
        final SingleParameter ap0 = new SingleParameter("arrayEntry0", "avalue0");
        final SingleParameter ap1 = new SingleParameter("arrayEntry1", "avalue1");
        final SingleParameter ap2 = new SingleParameter("arrayEntry2", "avalue2");

        arrayParam.add(ap0);
        arrayParam.add(ap1);
        arrayParam.add(ap2);

        assertEquals(ap0, arrayParam.get(ap0.getName()));
        assertEquals(ap1, arrayParam.get(ap1.getName()));
        assertEquals(ap2, arrayParam.get(ap2.getName()));
    }

    @Test
    public void testMessage_getParameter() {
        final ArrayParameter arrayParam = new ArrayParameter("arrayParam");
        arrayParam.add(new SingleParameter("arrayEntry0", "avalue0"));
        arrayParam.add(new SingleParameter("arrayEntry1", "avalue1"));
        arrayParam.add(new SingleParameter("arrayEntry2", "avalue2"));

        final GroupedParameter groupedParam = new GroupedParameter("groupedParam");
        groupedParam.add(new SingleParameter("groupEntry0", "gvalue0"));
        groupedParam.add(new SingleParameter("groupEntry1", "gvalue1"));
        groupedParam.add(new SingleParameter("groupEntry2", "gvalue2"));

        final Message message = new Message(arrayParam, groupedParam);
        final GroupedParameter gotGroupedParam = message.getParameter("groupedParam");
        assertEquals(groupedParam, gotGroupedParam);
    }

    @Test
    public void testBasic() throws Exception {
        final ArrayParameter params = new ArrayParameter("");
        params.add(new SingleParameter("param0", "value0"));
        params.add(new SingleParameter("param1", 42));
        params.add(new SingleParameter("param2", true));

        final ArrayParameter param3 = new ArrayParameter("param3");
        param3.add(new SingleParameter("param3-1", "value3-1"));
        param3.add(new SingleParameter("param3-2", "value3-2"));
        params.add(param3);

        final ArrayParameter param4 = new ArrayParameter("param4");
        param4.add(new SingleParameter("param4-1", "value4-1"));
        params.add(param4);

        final ArrayParameter param5 = new ArrayParameter("param5");
        param5.add(new SingleParameter("0", true));
        param5.add(new SingleParameter("1", false));
        param5.add(new SingleParameter("2", true));
        param5.add(new SingleParameter("3", false));
        params.add(param5);

        final ArrayParameter param6 = new ArrayParameter("param6");
        param6.add(new SingleParameter("0", (byte) 4));
        param6.add(new SingleParameter("0", (byte) 5));
        param6.add(new SingleParameter("0", (byte) 6));
        params.add(param6);

        params.add(new ByteArrayParameter("param7", new byte[] { 7, 8, 9 }));
        params.add(new BooleanArrayParameter("param8", new boolean[] { false, true }));
        params.add(new IntegerArrayParameter("param9", new int[] { 41, 42, 43 }));
        params.add(new SingleParameter("param10", "value10"));

        final Message message = new Message("json-msg", params);
        showMessage(message);
        Assert.assertEquals("json-msg", message.getName());

        // Encode the message to COMPACT
        final String compactMsgText = message.toJSONString(Message.EncodeMode.COMPACT);

        // Decode the passed compact String
        final Message message1 = new Message(compactMsgText);
        showMessage(message1);
        assertEquals(message, message1);

        // Re-Encode the message
        final String compactMsgText1 = message1.toJSONString(Message.EncodeMode.COMPACT);
        Assert.assertEquals(compactMsgText1, compactMsgText);
    }

    @Test
    public void testMessage_fromJSONObjectWithOnlyOneField() throws Exception {
        final String json = "{\"String\":\"strValue\",\"Object\":{\"objChildString\":\"objChildStrValue\"}}";
        final Message message = new Message(json);
        final String json2 = message.toJSONString(EncodeMode.VERBOSE);
        Assert.assertEquals(json, json2);
    }

    @Test
    public void testMessage_fromJSONArray() throws Exception {
        final String json = "{\"AVPS\":[\"263\",\"UTF8String\",\"1234787711756\",\"293\",\"DiamIdent\",\"peer5.ericsson.com\",\"283\",\"DiamIdent\",\"ericsson.com\",\"296\",\"DiamIdent\",\"ericsson.com\",\"264\",\"DiamIdent\",\"cgw-1-tocs.ericsson.com\",\"258\",\"Unsigned32\",\"4\",\"416\",\"Enumerated\",\"INITIAL_REQUEST\",\"415\",\"Unsigned32\",\"0\",\"456\",\"Grouped\",[\"431\",\"Grouped\",[\"420\",\"Unsigned32\",\"180\"]],\"268\",\"Unsigned32\",\"2001\"]}";
        final Message message = new Message(json);
        final String json2 = message.toJSONString(EncodeMode.COMPACT);
        Assert.assertEquals(json, json2);
    }

    @Test
    public void _testEncodeDecodeStandardJSON() throws Exception {
        final JSONObject obj1 = new JSONObject(staticStandardMsgText);
        final String formattedString1 = obj1.toString(4);
        if (isVerbose())
            System.out.println(formattedString1);

        final JSONObject obj2 = new JSONObject(obj1.toString());
        final String formattedString2 = obj2.toString(4);
        if (isVerbose())
            System.out.println(formattedString2);

        // Following assert may fail because can't predict order of JSON parameters
        Assert.assertEquals(formattedString1, formattedString2);
    }

    // --------------------------------------------------------------

    protected void assertEquals(Message xmessage, Message message) {
        assertEquals(xmessage.getParameters(), message.getParameters());
    }

    protected void assertEquals(ArrayParameter xparams, Object obj) {
        ArrayParameter params = (ArrayParameter) obj;
        assertEquals(xparams.getValues(), params.getValues());
    }

    protected void assertEquals(ArrayList<?> xlist, ArrayList<?> list) {
        Assert.assertEquals(xlist.size(), list.size());
        for (int i = 0; i < xlist.size(); i++) {
            Object obj = xlist.get(i);
            if (obj instanceof ArrayParameter) {
                assertEquals((ArrayParameter) obj, list.get(i));
            } else if (obj instanceof GroupedParameter) {
                assertEquals((GroupedParameter) obj, list.get(i));
            } else if (obj instanceof SingleParameter) {
                assertEquals((SingleParameter) obj, list.get(i));
            }
        }
    }

    protected void assertEquals(SingleParameter xparam, Object obj) {
        SingleParameter param = (SingleParameter) obj;
        Object xvalue = xparam.getValue();
        if (xvalue instanceof String) {
            Assert.assertEquals((String) xvalue, (String) param.getValue());
        } else if (xvalue instanceof Integer) {
            Assert.assertEquals((Integer) xvalue, (Integer) param.getValue());
        } else if (xvalue instanceof Boolean) {
            Assert.assertEquals((Boolean) xvalue, (Boolean) param.getValue());
        }
    }

    protected void assertEquals(GroupedParameter exp, Object obj) {
        GroupedParameter got = (GroupedParameter) obj;
        Assert.assertNotNull(got);
        Assert.assertEquals(exp.getName(), got.getName());
        Assert.assertEquals(exp.getValues(), got.getValues());
    }

    private void showMessage(Message message) throws Exception {
        if (isVerbose()) {
            showMessageCompact(message);
            showMessagePrettyCompact(message);
            showMessageVerbose(message);
            showMessagePrettyVerbose(message);
        }
    }

    private void showMessageCompact(Message message) throws Exception {
        String compactMsgText = message.toJSONString(Message.EncodeMode.COMPACT);
        System.out.println("Compact, length = " + compactMsgText.length());
        System.out.println("    " + compactMsgText);
    }

    private void showMessageVerbose(Message message) throws Exception {
        String verboseMsgText = message.toJSONString(Message.EncodeMode.VERBOSE);
        System.out.println("Verbose, length = " + verboseMsgText.length());
        System.out.println("    " + verboseMsgText);
    }

    private void showMessagePrettyCompact(Message message) throws Exception {
        System.out.println("Pretty - compact");
        System.out.println("    " + message.toJSONStringPretty(Message.EncodeMode.COMPACT));
    }

    private void showMessagePrettyVerbose(Message message) throws Exception {
        System.out.println("Pretty - verbose");
        System.out.println("    " + message.toJSONStringPretty(Message.EncodeMode.VERBOSE));
    }

    private boolean isVerbose() {
        return verbose;
    }
}
