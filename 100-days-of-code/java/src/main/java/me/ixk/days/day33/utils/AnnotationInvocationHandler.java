/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.utils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * InvocationHandler for dynamic proxy implementation of Annotation.
 *
 * @author Josh Bloch
 * @since 1.5
 */
class AnnotationInvocationHandler implements InvocationHandler, Serializable {

    private static final long serialVersionUID = 6182022883658399397L;
    private final Class<? extends Annotation> type;
    private final Map<String, Object> memberValues;
    private transient volatile Method[] memberMethods;

    AnnotationInvocationHandler(
        final Class<? extends Annotation> type,
        final Map<String, Object> memberValues
    ) {
        final Class<?>[] superInterfaces = type.getInterfaces();
        if (
            !type.isAnnotation() ||
            superInterfaces.length != 1 ||
            superInterfaces[0] != Annotation.class
        ) {
            throw new AnnotationFormatError(
                "Attempt to create proxy for a non-annotation type."
            );
        }
        this.type = type;
        this.memberValues = memberValues;
    }

    /**
     * Translates a member value (in "dynamic proxy return form") into a
     * string.
     */
    private static String memberValueToString(final Object value) {
        final Class<?> type = value.getClass();
        if (!type.isArray()) {
            // primitive value, string, class, enum const, or annotation
            if (type == Class.class) {
                return toSourceString((Class<?>) value);
            } else if (type == String.class) {
                return toSourceString((String) value);
            }
            if (type == Character.class) {
                return toSourceString((char) value);
            } else if (type == Double.class) {
                return toSourceString((double) value);
            } else if (type == Float.class) {
                return toSourceString((float) value);
            } else if (type == Long.class) {
                return toSourceString((long) value);
            } else if (type == Byte.class) {
                return toSourceString((byte) value);
            } else {
                return value.toString();
            }
        } else {
            final Stream<String> stringStream;
            if (type == byte[].class) {
                stringStream = convert((byte[]) value);
            } else if (type == char[].class) {
                stringStream = convert((char[]) value);
            } else if (type == double[].class) {
                stringStream =
                    DoubleStream
                        .of((double[]) value)
                        .mapToObj(AnnotationInvocationHandler::toSourceString);
            } else if (type == float[].class) {
                stringStream = convert((float[]) value);
            } else if (type == int[].class) {
                stringStream =
                    IntStream.of((int[]) value).mapToObj(String::valueOf);
            } else if (type == long[].class) {
                stringStream =
                    LongStream
                        .of((long[]) value)
                        .mapToObj(AnnotationInvocationHandler::toSourceString);
            } else if (type == short[].class) {
                stringStream = convert((short[]) value);
            } else if (type == boolean[].class) {
                stringStream = convert((boolean[]) value);
            } else if (type == Class[].class) {
                stringStream =
                    Arrays
                        .stream((Class<?>[]) value)
                        .map(AnnotationInvocationHandler::toSourceString);
            } else if (type == String[].class) {
                stringStream =
                    Arrays
                        .stream((String[]) value)
                        .map(AnnotationInvocationHandler::toSourceString);
            } else {
                stringStream =
                    Arrays.stream((Object[]) value).map(Objects::toString);
            }

            return stringStreamToString(stringStream);
        }
    }

    /**
     * Translates a Class value to a form suitable for use in the string
     * representation of an annotation.
     */
    private static String toSourceString(final Class<?> clazz) {
        Class<?> finalComponent = clazz;
        final StringBuilder arrayBrackets = new StringBuilder();

        while (finalComponent.isArray()) {
            finalComponent = finalComponent.getComponentType();
            arrayBrackets.append("[]");
        }

        return finalComponent.getName() + arrayBrackets.toString() + ".class";
    }

    private static String toSourceString(final float f) {
        if (Float.isFinite(f)) {
            return Float.toString(f) + "f";
        } else {
            if (Float.isInfinite(f)) {
                return (f < 0.0f) ? "-1.0f/0.0f" : "1.0f/0.0f";
            } else {
                return "0.0f/0.0f";
            }
        }
    }

    private static String toSourceString(final double d) {
        if (Double.isFinite(d)) {
            return Double.toString(d);
        } else {
            if (Double.isInfinite(d)) {
                return (d < 0.0f) ? "-1.0/0.0" : "1.0/0.0";
            } else {
                return "0.0/0.0";
            }
        }
    }

    private static String toSourceString(final char c) {
        final StringBuilder sb = new StringBuilder(4);
        sb.append('\'');
        sb.append(quote(c));
        return sb.append('\'').toString();
    }

    /**
     * Escapes a character if it has an escape sequence or is non-printable
     * ASCII.  Leaves non-ASCII characters alone.
     */
    private static String quote(final char ch) {
        switch (ch) {
            case '\b':
                return "\\b";
            case '\f':
                return "\\f";
            case '\n':
                return "\\n";
            case '\r':
                return "\\r";
            case '\t':
                return "\\t";
            case '\'':
                return "\\'";
            case '\"':
                return "\\\"";
            case '\\':
                return "\\\\";
            default:
                return (isPrintableAscii(ch))
                    ? String.valueOf(ch)
                    : String.format("\\u%04x", (int) ch);
        }
    }

    /**
     * Is a character printable ASCII?
     */
    private static boolean isPrintableAscii(final char ch) {
        return ch >= ' ' && ch <= '~';
    }

    private static String toSourceString(final byte b) {
        return String.format("(byte)0x%02x", b);
    }

    private static String toSourceString(final long ell) {
        return String.valueOf(ell) + "L";
    }

    /**
     * Return a string suitable for use in the string representation of an
     * annotation.
     */
    private static String toSourceString(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            sb.append(quote(s.charAt(i)));
        }
        sb.append('"');
        return sb.toString();
    }

    private static Stream<String> convert(final byte[] values) {
        final List<String> list = new ArrayList<>(values.length);
        for (final byte b : values) {
            list.add(toSourceString(b));
        }
        return list.stream();
    }

    private static Stream<String> convert(final char[] values) {
        final List<String> list = new ArrayList<>(values.length);
        for (final char c : values) {
            list.add(toSourceString(c));
        }
        return list.stream();
    }

    private static Stream<String> convert(final float[] values) {
        final List<String> list = new ArrayList<>(values.length);
        for (final float f : values) {
            list.add(toSourceString(f));
        }
        return list.stream();
    }

    private static Stream<String> convert(final short[] values) {
        final List<String> list = new ArrayList<>(values.length);
        for (final short s : values) {
            list.add(Short.toString(s));
        }
        return list.stream();
    }

    private static Stream<String> convert(final boolean[] values) {
        final List<String> list = new ArrayList<>(values.length);
        for (final boolean b : values) {
            list.add(Boolean.toString(b));
        }
        return list.stream();
    }

    private static String stringStreamToString(final Stream<String> stream) {
        return stream.collect(Collectors.joining(", ", "{", "}"));
    }

    /**
     * Returns true iff the two member values in "dynamic proxy return form" are
     * equal using the appropriate equality function depending on the member
     * type.  The two values will be of the same type unless one of the
     * containing annotations is ill-formed.  If one of the containing
     * annotations is ill-formed, this method will return false unless the two
     * members are identical object references.
     */
    private static boolean memberValueEquals(final Object v1, final Object v2) {
        final Class<?> type = v1.getClass();

        // Check for primitive, string, class, enum const, annotation,
        // or ExceptionProxy
        if (!type.isArray()) {
            return v1.equals(v2);
        }

        // Check for array of string, class, enum const, annotation,
        // or ExceptionProxy
        if (v1 instanceof Object[] && v2 instanceof Object[]) {
            return Arrays.equals((Object[]) v1, (Object[]) v2);
        }

        // Check for ill formed annotation(s)
        if (v2.getClass() != type) {
            return false;
        }

        // Deal with array of primitives
        if (type == byte[].class) {
            return Arrays.equals((byte[]) v1, (byte[]) v2);
        }
        if (type == char[].class) {
            return Arrays.equals((char[]) v1, (char[]) v2);
        }
        if (type == double[].class) {
            return Arrays.equals((double[]) v1, (double[]) v2);
        }
        if (type == float[].class) {
            return Arrays.equals((float[]) v1, (float[]) v2);
        }
        if (type == int[].class) {
            return Arrays.equals((int[]) v1, (int[]) v2);
        }
        if (type == long[].class) {
            return Arrays.equals((long[]) v1, (long[]) v2);
        }
        if (type == short[].class) {
            return Arrays.equals((short[]) v1, (short[]) v2);
        }
        assert type == boolean[].class;
        return Arrays.equals((boolean[]) v1, (boolean[]) v2);
    }

    /**
     * Computes hashCode of a member value (in "dynamic proxy return form")
     */
    private static int memberValueHashCode(final Object value) {
        final Class<?> type = value.getClass();
        if (!type.isArray()) {
            return value.hashCode();
        }

        if (type == byte[].class) {
            return Arrays.hashCode((byte[]) value);
        }
        if (type == char[].class) {
            return Arrays.hashCode((char[]) value);
        }
        if (type == double[].class) {
            return Arrays.hashCode((double[]) value);
        }
        if (type == float[].class) {
            return Arrays.hashCode((float[]) value);
        }
        if (type == int[].class) {
            return Arrays.hashCode((int[]) value);
        }
        if (type == long[].class) {
            return Arrays.hashCode((long[]) value);
        }
        if (type == short[].class) {
            return Arrays.hashCode((short[]) value);
        }
        if (type == boolean[].class) {
            return Arrays.hashCode((boolean[]) value);
        }
        return Arrays.hashCode((Object[]) value);
    }

    @Override
    public Object invoke(
        final Object proxy,
        final Method method,
        final Object[] args
    ) {
        final String member = method.getName();
        final int parameterCount = method.getParameterCount();

        // Handle Object and Annotation methods
        if (
            parameterCount == 1 &&
            "equals".equals(member) &&
            method.getParameterTypes()[0] == Object.class
        ) {
            return equalsImpl(proxy, args[0]);
        }
        if (parameterCount != 0) {
            throw new AssertionError(
                "Too many parameters for an annotation method"
            );
        }

        if ("toString".equals(member)) {
            return toStringImpl();
        } else if ("hashCode".equals(member)) {
            return hashCodeImpl();
        } else if ("annotationType".equals(member)) {
            return type;
        }

        // Handle annotation member accessors
        Object result = memberValues.get(member);

        if (result == null) {
            throw new IncompleteAnnotationException(type, member);
        }

        if (result.getClass().isArray() && Array.getLength(result) != 0) {
            result = cloneArray(result);
        }

        return result;
    }

    /**
     * This method, which clones its array argument, would not be necessary if
     * Cloneable had a public clone method.
     */
    private Object cloneArray(final Object array) {
        final Class<?> type = array.getClass();

        if (type == byte[].class) {
            final byte[] byteArray = (byte[]) array;
            return byteArray.clone();
        }
        if (type == char[].class) {
            final char[] charArray = (char[]) array;
            return charArray.clone();
        }
        if (type == double[].class) {
            final double[] doubleArray = (double[]) array;
            return doubleArray.clone();
        }
        if (type == float[].class) {
            final float[] floatArray = (float[]) array;
            return floatArray.clone();
        }
        if (type == int[].class) {
            final int[] intArray = (int[]) array;
            return intArray.clone();
        }
        if (type == long[].class) {
            final long[] longArray = (long[]) array;
            return longArray.clone();
        }
        if (type == short[].class) {
            final short[] shortArray = (short[]) array;
            return shortArray.clone();
        }
        if (type == boolean[].class) {
            final boolean[] booleanArray = (boolean[]) array;
            return booleanArray.clone();
        }

        final Object[] objectArray = (Object[]) array;
        return objectArray.clone();
    }

    /**
     * Implementation of dynamicProxy.toString()
     */
    private String toStringImpl() {
        final StringBuilder result = new StringBuilder(128);
        result.append('@');
        result.append(type.getName());
        result.append('(');
        boolean firstMember = true;
        final Set<Map.Entry<String, Object>> entries = memberValues.entrySet();
        boolean loneValue = entries.size() == 1;
        for (final Map.Entry<String, Object> e : entries) {
            if (firstMember) {
                firstMember = false;
            } else {
                result.append(", ");
            }

            final String key = e.getKey();
            if (!loneValue || !"value".equals(key)) {
                result.append(key);
                result.append('=');
            }
            loneValue = false;
            result.append(memberValueToString(e.getValue()));
        }
        result.append(')');
        return result.toString();
    }

    /**
     * Implementation of dynamicProxy.equals(Object o)
     */
    private Boolean equalsImpl(final Object proxy, final Object o) {
        if (o == proxy) {
            return true;
        }

        if (!type.isInstance(o)) {
            return false;
        }
        for (final Method memberMethod : getMemberMethods()) {
            final String member = memberMethod.getName();
            final Object ourValue = memberValues.get(member);
            Object hisValue = null;
            final AnnotationInvocationHandler hisHandler = asOneOfUs(o);
            if (hisHandler != null) {
                hisValue = hisHandler.memberValues.get(member);
            } else {
                try {
                    hisValue = memberMethod.invoke(o);
                } catch (final InvocationTargetException e) {
                    return false;
                } catch (final IllegalAccessException e) {
                    throw new AssertionError(e);
                }
            }
            if (!memberValueEquals(ourValue, hisValue)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns an object's invocation handler if that object is a dynamic proxy
     * with a handler of type AnnotationInvocationHandler. Returns null
     * otherwise.
     */
    private AnnotationInvocationHandler asOneOfUs(final Object o) {
        if (Proxy.isProxyClass(o.getClass())) {
            final InvocationHandler handler = Proxy.getInvocationHandler(o);
            if (handler instanceof AnnotationInvocationHandler) {
                return (AnnotationInvocationHandler) handler;
            }
        }
        return null;
    }

    /**
     * Returns the member methods for our annotation type.  These are obtained
     * lazily and cached, as they're expensive to obtain and we only need them
     * if our equals method is invoked (which should be rare).
     */
    private Method[] getMemberMethods() {
        Method[] value = memberMethods;
        if (value == null) {
            value = computeMemberMethods();
            memberMethods = value;
        }
        return value;
    }

    private Method[] computeMemberMethods() {
        return AccessController.doPrivileged(
            new PrivilegedAction<Method[]>() {
                @Override
                public Method[] run() {
                    final Method[] methods = type.getDeclaredMethods();
                    validateAnnotationMethods(methods);
                    AccessibleObject.setAccessible(methods, true);
                    return methods;
                }
            }
        );
    }

    /**
     * Validates that a method is structurally appropriate for an annotation
     * type. As of Java SE 8, annotation types cannot contain static methods and
     * the declared methods of an annotation type must take zero arguments and
     * there are restrictions on the return type.
     */
    private void validateAnnotationMethods(final Method[] memberMethods) {
        /*
         * Specification citations below are from JLS
         * 9.6.1. Annotation Type Elements
         */
        boolean valid = true;
        for (final Method method : memberMethods) {
            /*
             * "By virtue of the AnnotationTypeElementDeclaration
             * production, a method declaration in an annotation type
             * declaration cannot have formal parameters, type
             * parameters, or a throws clause.
             *
             * "By virtue of the AnnotationTypeElementModifier
             * production, a method declaration in an annotation type
             * declaration cannot be default or static."
             */
            if (
                method.getModifiers() !=
                (Modifier.PUBLIC | Modifier.ABSTRACT) ||
                method.isDefault() ||
                method.getParameterCount() != 0 ||
                method.getExceptionTypes().length != 0
            ) {
                valid = false;
                break;
            }

            /*
             * "It is a compile-time error if the return type of a
             * method declared in an annotation type is not one of the
             * following: a primitive type, String, Class, any
             * parameterized invocation of Class, an enum type
             * (section 8.9), an annotation type, or an array type
             * (chapter 10) whose element type is one of the preceding
             * types."
             */
            Class<?> returnType = method.getReturnType();
            if (returnType.isArray()) {
                returnType = returnType.getComponentType();
                if (returnType.isArray()) { // Only single dimensional arrays
                    valid = false;
                    break;
                }
            }

            if (
                !(
                    (returnType.isPrimitive() && returnType != void.class) ||
                    returnType == String.class ||
                    returnType == Class.class ||
                    returnType.isEnum() ||
                    returnType.isAnnotation()
                )
            ) {
                valid = false;
                break;
            }

            /*
             * "It is a compile-time error if any method declared in an
             * annotation type has a signature that is
             * override-equivalent to that of any public or protected
             * method declared in class Object or in the interface
             * java.lang.annotation.Annotation."
             *
             * The methods in Object or Annotation meeting the other
             * criteria (no arguments, contrained return type, etc.)
             * above are:
             *
             * String toString()
             * int hashCode()
             * Class<? extends Annotation> annotationType()
             */
            final String methodName = method.getName();
            if (
                ("toString".equals(methodName) && returnType == String.class) ||
                ("hashCode".equals(methodName) && returnType == int.class) ||
                (
                    "annotationType".equals(methodName) &&
                    returnType == Class.class
                )
            ) {
                valid = false;
                break;
            }
        }
        if (!valid) {
            throw new AnnotationFormatError(
                "Malformed method on an annotation type"
            );
        }
    }

    /**
     * Implementation of dynamicProxy.hashCode()
     */
    private int hashCodeImpl() {
        int result = 0;
        for (final Map.Entry<String, Object> e : memberValues.entrySet()) {
            result +=
                (127 * e.getKey().hashCode()) ^
                memberValueHashCode(e.getValue());
        }
        return result;
    }
}
