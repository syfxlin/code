/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day9;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JSON 工具类
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 5:03
 */
public class Json extends ObjectMapper {

    private static final long serialVersionUID = -4218242599617125307L;

    private Json() {
        super();
    }

    public static Json make() {
        return Inner.INSTANCE;
    }

    public static Json getInstance() {
        return Inner.INSTANCE;
    }

    private static class Inner {

        private static final Json INSTANCE = new Json();
    }

    public static ObjectNode parseObject(final String json) {
        try {
            return make().readValue(json, ObjectNode.class);
        } catch (final JsonProcessingException e) {
            return null;
        }
    }

    public static ArrayNode parseArray(final String json) {
        try {
            return make().readValue(json, ArrayNode.class);
        } catch (final JsonProcessingException e) {
            return null;
        }
    }

    public static JsonNode parse(final String json) {
        try {
            return make().readTree(json);
        } catch (final JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T parse(final String json, final Class<T> clazz) {
        try {
            return make().readValue(json, clazz);
        } catch (final JsonProcessingException e) {
            return null;
        }
    }

    public static String stringify(final Object object) {
        try {
            return make().writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            return null;
        }
    }

    public static String stringify(final JsonNode node) {
        return node.toString();
    }

    public static ObjectNode createObject() {
        return make().createObjectNode();
    }

    public static ArrayNode createArray() {
        return make().createArrayNode();
    }

    public static JsonNode convertToNode(final Object object) {
        return make().valueToTree(object);
    }

    public static <T> T convertToObject(
        final JsonNode node,
        final Class<T> type
    ) {
        try {
            return make().treeToValue(node, type);
        } catch (final JsonProcessingException e) {
            return null;
        }
    }
}
