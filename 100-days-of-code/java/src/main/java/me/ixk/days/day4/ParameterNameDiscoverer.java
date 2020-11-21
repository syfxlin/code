/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day4;

import cn.hutool.core.lang.SimpleCache;
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
    protected static final SimpleCache<Executable, String[]> PARAMETER_CACHE = new SimpleCache<>();

    public static String[] getMethodParamNames(Method method) {
        return getParameterNames(method);
    }

    public static String[] getConstructorParamNames(
        Constructor<?> constructor
    ) {
        return getParameterNames(constructor);
    }

    /**
     * 读取参数名称
     * <p>
     * 首先通过反射参数名称，当
     *
     * @param method 方法
     *
     * @return 参数名称
     */
    public static String[] getParameterNames(final Executable method) {
        String[] paramNames = getParameterNamesByReflection(method);
        if (paramNames == null) {
            paramNames = getParameterNamesByAsm(method);
        }
        return paramNames;
    }

    /**
     * 通过反射获取
     * <p>
     * 通过反射获取需要加上 <code>-parameters</code> 的编译参数，同时需要 JDK 8 及以上才支持。
     * <p>
     * 如果没有这个编译参数或者在 JDK 8 以下的版本通过反射获取到的只会是 <code>arg0</code> <code>arg1</code>
     * 等名称，并不是实际的名称。
     *
     * @param method 方法
     *
     * @return 参数名称
     */
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

    /**
     * 通过 ASM 字节码操纵工具获取参数名称
     * <p>
     * 除了通过添加编译参数的方式，还可以通过 ASM 的方式来获取参数名称。 Spring 就是这样处理的，当检测到无法通过反射获取参数名称的时候就采用
     * ASM 的方式来读取。
     * <p>
     * 在 Maven 管理的项目中会自动加上 <code>-g</code> 编译参数，生成的字节码就包含了
     * <code>LocalVariableTable</code> 这个表，通过读取该表我们就可以实际的获取到参数的名称。
     *
     * @param method 方法
     *
     * @return 参数名称
     */
    public static String[] getParameterNamesByAsm(final Executable method) {
        String[] cache = PARAMETER_CACHE.get(method);
        if (cache != null) {
            return cache;
        }
        final String[] paramNames = new String[method.getParameterCount()];
        final String className = method.getDeclaringClass().getName();
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
