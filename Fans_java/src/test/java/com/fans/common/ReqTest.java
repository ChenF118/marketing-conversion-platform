package com.fans.common;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReqTest {

    @Test
    void parsesBracketParameterIntoNestedMap() {
        Map<String, Object> root = new LinkedHashMap<>();
        Req.putKey(root, "condition[group]", new String[]{"3"});
        Req.putKey(root, "condition[words]", new String[]{"abc"});

        @SuppressWarnings("unchecked")
        Map<String, Object> condition = (Map<String, Object>) root.get("condition");
        assertEquals("3", condition.get("group"));
        assertEquals("abc", condition.get("words"));
    }

    @Test
    void parsesArrayParameter() {
        Map<String, Object> root = new LinkedHashMap<>();
        Req.putKey(root, "arr[]", new String[]{"1", "2"});
        Req.putKey(root, "arr[]", new String[]{"3"});

        @SuppressWarnings("unchecked")
        List<Object> arr = (List<Object>) root.get("arr");
        assertEquals(List.of("1", "2", "3"), arr);
    }

    @Test
    void numericConversionAndTruthiness() {
        assertEquals(12, Req.toInteger("12"));
        assertEquals(12, Req.toInteger(12.0));
        assertEquals(null, Req.toInteger(""));
        assertEquals(null, Req.toInteger("abc"));

        assertTrue(Req.truthy((Object) "x"));
        assertTrue(Req.truthy((Object) 1));
        assertFalse(Req.truthy((Object) ""));
        assertFalse(Req.truthy((Object) "0"));
        assertFalse(Req.truthy((Object) 0));
        assertFalse(Req.truthy((Object) null));
    }
}
