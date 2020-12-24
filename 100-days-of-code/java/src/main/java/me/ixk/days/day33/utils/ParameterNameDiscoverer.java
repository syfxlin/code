/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.utils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * 参数名称获取器
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 5:13
 */
public class ParameterNameDiscoverer {

    protected static final SoftCache<Executable, String[]> PARAMETER_CACHE = new SoftCache<>();

    public static String[] getMethodParamNames(Method method) {
        return getParameterNames(method);
    }

    public static String[] getConstructorParamNames(
        Constructor<?> constructor
    ) {
        return getParameterNames(constructor);
    }

    public static String[] getParameterNames(final Executable method) {
        String[] paramNames = getParameterNamesByReflection(method);
        if (paramNames == null) {
            paramNames = getParameterNamesByAsm(method);
        }
        return paramNames;
    }

    public static String[] getParameterNamesByReflection(
        final Executable method
    ) {
        String[] cache = PARAMETER_CACHE.get(method);
        if (cache != null) {
            return cache;
        }
        String[] paramNames = new String[method.getParameterCount()];
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            if (!param.isNamePresent()) {
                return null;
            }
            paramNames[i] = param.getName();
        }
        PARAMETER_CACHE.put(method, paramNames);
        return paramNames;
    }

    public static String[] getParameterNamesByAsm(final Executable method) {
        String[] cache = PARAMETER_CACHE.get(method);
        if (cache != null) {
            return cache;
        }
        final String[] paramNames = new String[method.getParameterCount()];
        final String className = ClassUtils
            .getUserClass(method.getDeclaringClass())
            .getName();
        final ClassWriter classWriter = new ClassWriter(
            ClassWriter.COMPUTE_MAXS
        );
        ClassReader classReader;
        try {
            classReader = new ClassReader(className);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        classReader.accept(
            new ClassVisitor(Opcodes.ASM8, classWriter) {
                @Override
                public MethodVisitor visitMethod(
                    final int access,
                    final String name,
                    final String desc,
                    final String signature,
                    final String[] exceptions
                ) {
                    final Type[] args = Type.getArgumentTypes(desc);
                    final String equalName = method instanceof Constructor
                        ? method.getDeclaringClass().getName()
                        : name;
                    if (
                        !equalName.equals(method.getName()) ||
                        !sameType(args, method.getParameterTypes())
                    ) {
                        return super.visitMethod(
                            access,
                            name,
                            desc,
                            signature,
                            exceptions
                        );
                    }
                    MethodVisitor methodVisitor = cv.visitMethod(
                        access,
                        name,
                        desc,
                        signature,
                        exceptions
                    );
                    return new MethodVisitor(Opcodes.ASM8, methodVisitor) {
                        @Override
                        public void visitLocalVariable(
                            String name,
                            String desc,
                            String signature,
                            Label start,
                            Label end,
                            int index
                        ) {
                            int i = index - 1;
                            // 如果是静态方法，则第一就是参数
                            // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
                            if (Modifier.isStatic(method.getModifiers())) {
                                i = index;
                            }
                            if (i >= 0 && i < paramNames.length) {
                                paramNames[i] = name;
                            }
                            super.visitLocalVariable(
                                name,
                                desc,
                                signature,
                                start,
                                end,
                                index
                            );
                        }
                    };
                }
            },
            0
        );
        PARAMETER_CACHE.put(method, paramNames);
        return paramNames;
    }

    private static boolean sameType(Type[] types, Class<?>[] classes) {
        if (types.length != classes.length) {
            return false;
        }

        for (int i = 0; i < types.length; i++) {
            if (!Type.getType(classes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }
}
