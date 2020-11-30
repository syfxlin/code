package me.ixk.days.day9;

import static me.ixk.days.day9.DataUtil.caseGet;
import static me.ixk.days.day9.DataUtil.dataGet;
import static me.ixk.days.day9.DataUtil.dataSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/11/30 下午 5:20
 */
class Day9Test {

    @Test
    void jsonNodeDataSet() {
        final ObjectNode objectNode = Json.createObject();
        objectNode.set("sub", Json.createArray());
        dataSet(objectNode, "key", TextNode.valueOf("value"));
        dataSet(objectNode, "sub.0", TextNode.valueOf("array0"));
        dataSet(objectNode, "sub.2", TextNode.valueOf("array2"));
        dataSet(objectNode, "sub1.sub2", TextNode.valueOf("sub3"));
        dataSet(objectNode, "sub.3.data", TextNode.valueOf("data"));
        assertEquals(
            "{\"sub\":[\"array0\",null,\"array2\",{\"data\":\"data\"}],\"key\":\"value\",\"sub1\":{\"sub2\":\"sub3\"}}",
            Json.stringify(objectNode)
        );
    }

    @Test
    void jsonNodeDataGet() {
        final ObjectNode objectNode = Json.parseObject(
            "{\"sub\":[\"array0\",null,\"array2\",{\"data\":\"data\"}],\"key\":\"value\",\"sub1\":{\"sub2\":\"sub3\"}}"
        );
        assertEquals("value", dataGet(objectNode, "key").asText());
        assertEquals("array0", dataGet(objectNode, "sub.0").asText());
        assertEquals("array2", dataGet(objectNode, "sub.2").asText());
        assertEquals("sub3", dataGet(objectNode, "sub1.sub2").asText());
        assertEquals("data", dataGet(objectNode, "sub.3.data").asText());
        assertEquals(
            "value",
            dataGet(objectNode, "sub.10", TextNode.valueOf("value")).asText()
        );
    }

    @Test
    void objectDataSet() {
        final Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("sub", new ArrayList<>());
        dataSet(map, "key", "value");
        dataSet(map, "sub.0", "array0");
        dataSet(map, "sub.2", "array2");
        dataSet(map, "sub1.sub2", "sub3");
        dataSet(map, "sub.3.data", "data");
        assertEquals(
            "{\"sub\":[\"array0\",null,\"array2\",{\"data\":\"data\"}],\"sub1\":{\"sub2\":\"sub3\"},\"key\":\"value\"}",
            Json.stringify(map)
        );
    }

    @Test
    void objectDataGet() {
        final Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("sub", new ArrayList<>());
        dataSet(map, "key", "value");
        dataSet(map, "sub.0", "array0");
        dataSet(map, "sub.2", "array2");
        dataSet(map, "sub1.sub2", "sub3");
        dataSet(map, "sub.3.data", "data");

        assertEquals("value", dataGet(map, "key"));
        assertEquals("array0", dataGet(map, "sub.0"));
        assertEquals("array2", dataGet(map, "sub.2"));
        assertEquals("sub3", dataGet(map, "sub1.sub2"));
        assertEquals("data", dataGet(map, "sub.3.data"));
        assertEquals("value", dataGet(map, "sub.10", "value"));
    }

    @Test
    void testCaseGet() {
        // caseGet
        assertNotNull(
            caseGet(
                "caseGet",
                name -> {
                    if (name.equals("case-get")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "caseGet",
                name -> {
                    if (name.equals("case_get")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "caseGet",
                name -> {
                    if (name.equals("CASE_GET")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "caseGet",
                name -> {
                    if (name.equals("CaseGet")) {
                        return "";
                    }
                    return null;
                }
            )
        );

        // case-get
        assertNotNull(
            caseGet(
                "case-get",
                name -> {
                    if (name.equals("caseGet")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "case-get",
                name -> {
                    if (name.equals("case_get")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "case-get",
                name -> {
                    if (name.equals("CASE_GET")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "case-get",
                name -> {
                    if (name.equals("CaseGet")) {
                        return "";
                    }
                    return null;
                }
            )
        );

        // case_get
        assertNotNull(
            caseGet(
                "case_get",
                name -> {
                    if (name.equals("caseGet")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "case_get",
                name -> {
                    if (name.equals("case-get")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "case_get",
                name -> {
                    if (name.equals("CASE_GET")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "case_get",
                name -> {
                    if (name.equals("CaseGet")) {
                        return "";
                    }
                    return null;
                }
            )
        );

        // CASE_GET
        assertNotNull(
            caseGet(
                "CASE_GET",
                name -> {
                    if (name.equals("caseGet")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "CASE_GET",
                name -> {
                    if (name.equals("case_get")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "CASE_GET",
                name -> {
                    if (name.equals("case-get")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "CASE_GET",
                name -> {
                    if (name.equals("CaseGet")) {
                        return "";
                    }
                    return null;
                }
            )
        );

        // CaseGet
        assertNotNull(
            caseGet(
                "CaseGet",
                name -> {
                    if (name.equals("caseGet")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "CaseGet",
                name -> {
                    if (name.equals("case_get")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "CaseGet",
                name -> {
                    if (name.equals("CASE_GET")) {
                        return "";
                    }
                    return null;
                }
            )
        );
        assertNotNull(
            caseGet(
                "CaseGet",
                name -> {
                    if (name.equals("case-get")) {
                        return "";
                    }
                    return null;
                }
            )
        );
    }
}
