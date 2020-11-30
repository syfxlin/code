/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day9;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 静态 Helper 工具类
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 9:09
 */
public class DataUtil {

    public static final String UNDERLINE = "_";
    public static final String DASHED = "-";

    public static JsonNode dataGet(JsonNode target, String key) {
        return dataGet(target, key, (JsonNode) null);
    }

    public static JsonNode dataGet(
        JsonNode target,
        String key,
        JsonNode defaultValue
    ) {
        String[] keys = key.split("\\.");
        return dataGet(target, keys, defaultValue);
    }

    public static JsonNode dataGet(JsonNode target, String[] keys) {
        return dataGet(target, keys, (JsonNode) null);
    }

    public static JsonNode dataGet(
        JsonNode target,
        String[] keys,
        JsonNode defaultValue
    ) {
        if (keys == null) {
            return target;
        }
        for (int i = 0; i < keys.length; i++) {
            if ("*".equals(keys[i])) {
                ArrayNode array = Json.createArray();
                String[] subKeys = Arrays.copyOfRange(keys, i + 1, keys.length);
                if (target.isObject()) {
                    ObjectNode object = (ObjectNode) target;
                    for (
                        Iterator<String> it = object.fieldNames();
                        it.hasNext();
                    ) {
                        String itemKey = it.next();
                        JsonNode element = dataGet(
                            object.get(itemKey),
                            subKeys,
                            null,
                            JsonNode.class
                        );
                        if (element != null) {
                            array.add(element);
                        }
                    }
                } else if (target.isArray()) {
                    ArrayNode array1 = (ArrayNode) target;
                    for (JsonNode item : array1) {
                        JsonNode element = dataGet(
                            item,
                            subKeys,
                            null,
                            JsonNode.class
                        );
                        if (element != null) {
                            array.add(element);
                        }
                    }
                }
                return array;
            } else if (target.isArray()) {
                ArrayNode array = (ArrayNode) target;
                int index = Integer.parseInt(keys[i]);
                if (array.size() <= index) {
                    target = null;
                    break;
                }
                target = array.get(index);
            } else if (target.isObject()) {
                ObjectNode object = (ObjectNode) target;
                if (!object.has(keys[i])) {
                    target = null;
                    break;
                }
                target = object.get(keys[i]);
            } else {
                target = null;
                break;
            }
        }
        if (target == null || target.isNull()) {
            return defaultValue;
        }
        return target;
    }

    public static Object dataGet(Object target, String key) {
        return dataGet(target, key, null);
    }

    public static Object dataGet(
        Object target,
        String key,
        Object defaultValue
    ) {
        String[] keys = key.split("\\.");
        return dataGet(target, keys, defaultValue, Object.class);
    }

    public static Object dataGet(Object target, String[] keys) {
        return dataGet(target, keys, null, Object.class);
    }

    public static <T> T dataGet(
        Object target,
        String key,
        Class<T> returnType
    ) {
        return dataGet(target, key, null, returnType);
    }

    public static <T> T dataGet(
        Object target,
        String key,
        T defaultValue,
        Class<T> returnType
    ) {
        String[] keys = key.split("\\.");
        return dataGet(target, keys, defaultValue, returnType);
    }

    public static <T> T dataGet(
        Object target,
        String[] keys,
        Class<T> returnType
    ) {
        return dataGet(target, keys, null, returnType);
    }

    @SuppressWarnings("unchecked")
    public static <T> T dataGet(
        Object target,
        String[] keys,
        T defaultValue,
        Class<T> returnType
    ) {
        if (keys == null) {
            return Convert.convert(returnType, target);
        }
        for (int i = 0; i < keys.length; i++) {
            if ("*".equals(keys[i])) {
                List<Object> array = new ArrayList<>();
                String[] subKeys = Arrays.copyOfRange(keys, i + 1, keys.length);
                if (target instanceof Map) {
                    Map<String, Object> object = (Map<String, Object>) target;
                    for (String itemKey : object.keySet()) {
                        Object element = dataGet(
                            object.get(itemKey),
                            subKeys,
                            null,
                            returnType
                        );
                        if (element != null) {
                            array.add(element);
                        }
                    }
                } else if (target instanceof List) {
                    List<Object> array1 = (List<Object>) target;
                    for (Object item : array1) {
                        Object element = dataGet(
                            item,
                            subKeys,
                            null,
                            returnType
                        );
                        if (element != null) {
                            array.add(element);
                        }
                    }
                }
                return Convert.convert(returnType, array);
            } else if (target instanceof List) {
                List<Object> array = (List<Object>) target;
                int index = Integer.parseInt(keys[i]);
                if (array.size() <= index) {
                    target = null;
                    break;
                }
                target = array.get(index);
            } else if (target instanceof Map) {
                Map<String, Object> object = (Map<String, Object>) target;
                if (!object.containsKey(keys[i])) {
                    target = null;
                    break;
                }
                target = object.get(keys[i]);
            } else {
                target = null;
                break;
            }
        }
        if (target == null) {
            return defaultValue;
        }
        return Convert.convert(returnType, target);
    }

    public static void dataSet(JsonNode target, String key, JsonNode value) {
        String[] keys = key.split("\\.");
        dataSet(target, keys, value);
    }

    public static void dataSet(JsonNode target, String[] keys, JsonNode value) {
        if (target == null) {
            return;
        }
        for (int i = 0; i < keys.length; i++) {
            if (i != keys.length - 1) {
                if (target.isObject()) {
                    ObjectNode node = (ObjectNode) target;
                    target = node.get(keys[i]);
                    if (target == null) {
                        target = Json.createObject();
                        node.set(keys[i], target);
                    }
                } else if (target.isArray()) {
                    ArrayNode node = (ArrayNode) target;
                    int index = Integer.parseInt(keys[i]);
                    target = node.get(index);
                    if (target == null) {
                        target = Json.createObject();
                        while (node.size() < index) {
                            node.addNull();
                        }
                        node.insert(index, target);
                    }
                } else {
                    throw new ClassCastException(
                        "Can not set value to ValueNode"
                    );
                }
            } else {
                if (target.isObject()) {
                    ObjectNode object = (ObjectNode) target;
                    object.set(keys[i], value);
                } else if (target.isArray()) {
                    ArrayNode array = (ArrayNode) target;
                    int index = Integer.parseInt(keys[i]);
                    while (array.size() < index) {
                        array.addNull();
                    }
                    array.insert(index, value);
                } else {
                    throw new ClassCastException(
                        "Can not set value to ValueNode"
                    );
                }
            }
        }
    }

    public static void dataSet(Object target, String key, Object value) {
        String[] keys = key.split("\\.");
        dataSet(target, keys, value);
    }

    @SuppressWarnings("unchecked")
    public static void dataSet(Object target, String[] keys, Object value) {
        if (target == null) {
            return;
        }
        for (int i = 0; i < keys.length; i++) {
            if (i != keys.length - 1) {
                if (target instanceof List) {
                    List<Object> list = (List<Object>) target;
                    int index = Integer.parseInt(keys[i]);
                    if (list.size() > index) {
                        target = list.get(index);
                    } else {
                        target = new ConcurrentHashMap<String, Object>();
                        while (list.size() < index) {
                            list.add(null);
                        }
                        list.add(index, target);
                    }
                } else if (target instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) target;
                    target = map.get(keys[i]);
                    if (target == null) {
                        target = new ConcurrentHashMap<>();
                        map.put(keys[i], target);
                    }
                } else {
                    throw new ClassCastException(
                        "Can not set value to " + target.getClass().getName()
                    );
                }
            } else {
                if (target instanceof List) {
                    List<Object> list = (List<Object>) target;
                    int index = Integer.parseInt(keys[i]);
                    while (list.size() < index) {
                        list.add(null);
                    }
                    list.add(index, value);
                } else if (target instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) target;
                    map.put(keys[i], value);
                } else {
                    throw new ClassCastException(
                        "Can not set value to " + target.getClass().getName()
                    );
                }
            }
        }
    }

    public static <T> T caseGet(String name, Function<String, T> fun) {
        return caseGet(
            name,
            fun,
            new char[] { '_', '-', 'c', 'S', 'C' },
            Objects::nonNull
        );
    }

    public static <T> T caseGet(
        String name,
        Function<String, T> fun,
        Predicate<T> predicate
    ) {
        return caseGet(
            name,
            fun,
            new char[] { '_', '-', 'c', 'S', 'C' },
            predicate
        );
    }

    public static <T> T caseGet(
        String name,
        Function<String, T> fun,
        char[] splits,
        Predicate<T> predicate
    ) {
        T target = fun.apply(name);
        if (!predicate.test(target)) {
            name = toCamelCase(name);
            for (char split : splits) {
                String rename;
                if (split == 'S') {
                    rename = StrUtil.toSymbolCase(name, '_').toUpperCase();
                } else if (split == 'c') {
                    rename = name;
                } else if (split == 'C') {
                    final String camelCase = toCamelCase(name);
                    rename =
                        (char) (camelCase.charAt(0) - 32) +
                        camelCase.substring(1);
                } else {
                    rename = StrUtil.toSymbolCase(name, split);
                }
                target = fun.apply(rename);
                if (target == NullNode.getInstance()) {
                    target = null;
                }
                if (predicate.test(target)) {
                    break;
                }
            }
        }
        return target;
    }

    public static String toCamelCase(CharSequence name) {
        if (null == name) {
            return null;
        }
        String name2 = name.toString();
        if (name2.contains(UNDERLINE) || name2.contains(DASHED)) {
            // case-case, case_case, CASE_CASE
            final StringBuilder sb = new StringBuilder(name2.length());
            boolean upperCase = false;
            for (int i = 0; i < name2.length(); i++) {
                char c = name2.charAt(i);

                if (c == '_' || c == '-') {
                    upperCase = true;
                } else if (upperCase) {
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
            return sb.toString();
        }
        if (!name2.isEmpty() && Character.isUpperCase(name2.charAt(0))) {
            // CaseCase
            return Character.toLowerCase(name2.charAt(0)) + name2.substring(1);
        } else {
            return name2;
        }
    }
}
