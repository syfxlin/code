package me.ixk.days.day33.ioc;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Supplier;
import me.ixk.days.day33.exceptions.ContainerException;
import me.ixk.days.day33.ioc.context.Context;
import me.ixk.days.day33.ioc.context.ScopeType;
import me.ixk.days.day33.ioc.factory.FactoryBean;
import me.ixk.days.day33.ioc.processor.BeanAfterProcessor;
import me.ixk.days.day33.ioc.processor.BeanBeforeProcessor;
import me.ixk.days.day33.utils.ClassUtils;
import me.ixk.days.day33.utils.Convert;
import me.ixk.days.day33.utils.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 容器
 *
 * @author Otstar Lin
 * @date 2020/12/23 下午 10:37
 */
public class Container {

    private static final Logger log = LoggerFactory.getLogger(Container.class);

    private static final String FACTORY_BEAN_PREFIX = "&_";
    private static final String ATTRIBUTE_PREFIX = "$_";
    /**
     * 参数注入器
     */
    private final Deque<ParameterInjector> parameterInjectors = new ConcurrentLinkedDeque<>();

    /**
     * 实例注入器
     */
    private final Deque<InstanceInjector> instanceInjectors = new ConcurrentLinkedDeque<>();

    /**
     * 前置处理器，在初始化后进行
     */
    private final Deque<BeanBeforeProcessor> beanBeforeProcessors = new ConcurrentLinkedDeque<>();
    /**
     * 后置处理,在删除前进行
     */
    private final Deque<BeanAfterProcessor> beanAfterProcessors = new ConcurrentLinkedDeque<>();

    /**
     * Contexts，存储实例的空间
     */
    private final Map<Class<? extends Context>, Context> contexts = Collections.synchronizedMap(
        new LinkedHashMap<>(5)
    );

    /**
     * Bindings key：Bean 名称
     */
    private final Map<String, Binding> bindings = new ConcurrentHashMap<>(256);

    /**
     * Bindings 类型索引 key：类型，value：Bean 名称
     */
    private final Map<Class<?>, List<String>> bindingNamesByType = new ConcurrentHashMap<>(
        265
    );

    /**
     * 别名
     */
    private final Map<String, String> aliases = new ConcurrentHashMap<>(256);

    /**
     * 注入的临时变量
     */
    private final ThreadLocal<DataBinder> dataBinder = new InheritableThreadLocal<>();

    public Container() {
        this.dataBinder.set(
                new DefaultDataBinder(this, new ConcurrentHashMap<>())
            );

        log.info("Container created");
    }

    /**
     * 销毁方法
     */
    public void destroy() {
        while (this.contexts.values().iterator().hasNext()) {
            final Context context = this.contexts.values().iterator().next();
            this.removeContext(context);
        }
        log.info("Container destroyed");
    }

    public Map<String, Binding> getBindings() {
        return bindings;
    }

    public Map<Class<?>, List<String>> getBindingNamesByType() {
        return bindingNamesByType;
    }

    public Map<String, String> getAliases() {
        return aliases;
    }

    /* ===================== Context ===================== */

    public void registerContext(final Context context) {
        final Class<? extends Context> contextType = context.getClass();
        if (log.isDebugEnabled()) {
            log.debug(
                "Container registered context: {}",
                contextType.getName()
            );
        }
        this.contexts.put(contextType, context);
    }

    public void removeContext(final Class<? extends Context> contextType) {
        final Context context = this.contexts.get(contextType);
        this.removeContext(context);
    }

    public void removeContext(final Context context) {
        final Class<? extends Context> contextType = context.getClass();
        if (log.isDebugEnabled()) {
            log.debug("Container remove context: {}", contextType.getName());
        }
        if (context.isCreated()) {
            for (final Entry<String, Binding> entry : this.bindings.entrySet()) {
                if (context.matchesScope(entry.getValue().getScope())) {
                    this.doRemove(entry.getKey());
                }
            }
        }
        this.contexts.remove(contextType);
    }

    public void registerContexts(final List<Context> contexts) {
        for (final Context context : contexts) {
            this.registerContext(context);
        }
    }

    public Context getContextByScope(final String scopeType) {
        for (final Context context : this.contexts.values()) {
            if (context.matchesScope(scopeType)) {
                return context;
            }
        }
        return null;
    }

    public Context getContext(final Class<? extends Context> contextType) {
        return this.contexts.get(contextType);
    }

    /* ===================== Binding ===================== */

    public Binding newBinding(
        final String name,
        final Class<?> instanceType,
        final String scopeType
    ) {
        return new Binding(
            this.getContextByScope(scopeType),
            name,
            instanceType,
            scopeType
        );
    }

    public Binding newBinding(
        final String name,
        final Object instance,
        final String scopeType
    ) {
        return new Binding(
            this.getContextByScope(scopeType),
            name,
            instance,
            scopeType
        );
    }

    public Binding newBinding(
        final String name,
        final FactoryBean<?> factoryBean,
        final String scopeType
    ) {
        return new Binding(
            this.getContextByScope(scopeType),
            name,
            factoryBean,
            scopeType
        );
    }

    public boolean has(final String name) {
        return this.get(name) != null;
    }

    public Binding get(final String name) {
        return this.bindings.get(this.getCanonicalName(name));
    }

    public Binding getOrDefaultBinding(
        final String name,
        final Class<?> instanceType
    ) {
        Binding binding = name == null ? null : this.get(name);
        if (binding == null) {
            binding = this.get(this.getNameByType(instanceType));
        }
        if (binding == null) {
            binding =
                this.newBinding(
                        name,
                        instanceType,
                        ScopeType.PROTOTYPE.asString()
                    );
        }
        return binding;
    }

    public void set(String name, final Binding binding) {
        if (log.isDebugEnabled()) {
            log.debug("Container set binding: {}", name);
        }
        name = this.getCanonicalName(name);
        this.bindings.put(name, binding);
        Class<?> clazz = binding.getType();
        while (clazz != null && clazz != Object.class) {
            this.addType(name, clazz);
            for (final Class<?> in : clazz.getInterfaces()) {
                this.addType(name, in);
            }
            clazz = clazz.getSuperclass();
        }
    }

    private void validHas(final String name, final String message) {
        if (this.has(name)) {
            throw new IllegalStateException(String.format(message, name));
        }
    }

    public void removeBinding(String name) {
        if (log.isDebugEnabled()) {
            log.debug("Container remove binding: {}", name);
        }
        name = this.getCanonicalName(name);
        final Binding binding = this.bindings.remove(name);
        Class<?> clazz = binding.getType();
        while (clazz != Object.class) {
            this.addType(name, clazz);
            for (final Class<?> in : clazz.getInterfaces()) {
                this.removeType(name, in);
            }
            clazz = clazz.getSuperclass();
        }
    }

    protected void addType(final String name, final Class<?> type) {
        this.bindingNamesByType.compute(
                type,
                (t, o) -> {
                    if (o != null) {
                        o.add(name);
                        return o;
                    } else {
                        final List<String> list = new ArrayList<>();
                        list.add(name);
                        return list;
                    }
                }
            );
    }

    protected void removeType(final String name, final Class<?> type) {
        final List<String> list = this.bindingNamesByType.get(type);
        if (list != null) {
            list.remove(name);
        }
    }

    protected String getNameByType(final Class<?> type) {
        // 先查找 bindingNamesByType 里是否有类型
        final List<String> list = this.bindingNamesByType.get(type);
        if (list == null || list.isEmpty()) {
            // 未找到或空则使用短类名作为名称
            return this.typeToName(type);
        }
        // 否则取第一个返回
        return list.get(0);
    }

    protected String typeToName(final Class<?> type) {
        return StrUtil.toCamelCase(type.getSimpleName());
    }

    /* ===================== Alias ===================== */

    private String getCanonicalName(final String name) {
        final String resolve = this.getAlias(name);
        if (resolve == null) {
            return name;
        }
        return resolve;
    }

    public void setAlias(final String alias, final String name) {
        if (alias == null || alias.equals(name)) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Container add alias: {} => {}", alias, name);
        }
        this.validHas(alias, "Alias [%s] has contains");
        this.aliases.put(alias, name);
    }

    public void removeAlias(final String alias) {
        if (log.isDebugEnabled()) {
            log.debug("Container remove alias: {}", alias);
        }
        this.aliases.remove(alias);
    }

    public boolean hasAlias(final String alias) {
        return this.getAlias(alias) != null;
    }

    public String getAlias(final String alias) {
        return this.aliases.get(alias);
    }

    /*======================  Attribute  ==================*/

    public void removeAttribute(final String name) {
        for (final Context context : this.contexts.values()) {
            if (context.has(ATTRIBUTE_PREFIX + name)) {
                context.remove(ATTRIBUTE_PREFIX + name);
                return;
            }
        }
    }

    public Object getAttribute(final String name) {
        for (final Context context : this.contexts.values()) {
            if (context.has(ATTRIBUTE_PREFIX + name)) {
                return context.get(ATTRIBUTE_PREFIX + name);
            }
        }
        return null;
    }

    public boolean hasAttribute(final String name) {
        return this.getAttribute(name) != null;
    }

    public void setAttribute(
        final String name,
        final Object attribute,
        final String scopeType
    ) {
        this.getContextByScope(scopeType)
            .set(ATTRIBUTE_PREFIX + name, attribute);
    }

    public void removeAttribute(final String name, final String scopeType) {
        this.getContextByScope(scopeType).remove(ATTRIBUTE_PREFIX + name);
    }

    public Object getAttribute(final String name, final String scopeType) {
        return this.getContextByScope(scopeType).get(ATTRIBUTE_PREFIX + name);
    }

    public boolean hasAttribute(final String name, final String scopeType) {
        return this.getAttribute(name, scopeType) != null;
    }

    /* ===================== Process ===================== */

    protected Object processInstanceInjector(
        final Binding binding,
        Object instance
    ) {
        final Class<?> instanceClass = ClassUtils.getUserClass(instance);
        final InstanceContext context = new InstanceContext(
            binding,
            instanceClass
        );
        for (final InstanceInjector injector : this.instanceInjectors) {
            instance =
                injector.process(
                    this,
                    instance,
                    context,
                    this.dataBinder.get()
                );
        }
        return instance;
    }

    protected Object[] processParameterInjector(
        final Binding binding,
        Executable method
    ) {
        Object[] dependencies = new Object[method.getParameterCount()];
        method = ClassUtils.getUserMethod(method);
        final ParameterContext context = new ParameterContext(binding, method);
        for (final ParameterInjector injector : this.parameterInjectors) {
            dependencies =
                injector.process(
                    this,
                    dependencies,
                    context,
                    this.dataBinder.get()
                );
        }
        return dependencies;
    }

    protected Object processBeanBefore(final Binding binding, Object instance) {
        final Class<?> instanceClass = ClassUtils.getUserClass(instance);
        final InstanceContext context = new InstanceContext(
            binding,
            instanceClass
        );
        for (final BeanBeforeProcessor processor : this.beanBeforeProcessors) {
            instance = processor.process(this, instance, context);
        }
        return instance;
    }

    protected void processBeanAfter(
        final Binding binding,
        final Object instance
    ) {
        final Class<?> instanceClass = ClassUtils.getUserClass(instance);
        final InstanceContext context = new InstanceContext(
            binding,
            instanceClass
        );
        for (final BeanAfterProcessor processor : this.beanAfterProcessors) {
            processor.process(this, instance, context);
        }
    }

    /* ===================== doBind ===================== */

    protected Binding doBind(final String name, final Binding binding) {
        if (log.isDebugEnabled()) {
            log.debug("Container bind: {} - {}", binding.getScope(), name);
        }
        this.set(name, binding);
        return binding;
    }

    protected Binding doBind(
        final String name,
        final FactoryBean<?> factoryBean,
        final String scopeType
    ) {
        this.validHas(name, "Target [%s] has been bind");
        final Binding binding = this.newBinding(name, factoryBean, scopeType);
        return this.doBind(name, binding);
    }

    protected Binding doBind(
        final String name,
        final Class<?> instanceType,
        final String scopeType
    ) {
        this.validHas(name, "Target [%s] has been bind");
        final Binding binding = this.newBinding(name, instanceType, scopeType);
        return this.doBind(name, binding);
    }

    protected Binding doBind(
        final String name,
        final Object instance,
        final String scopeType
    ) {
        this.validHas(name, "Target [%s] has been bind");
        final Binding binding = this.newBinding(name, instance, scopeType);
        return this.doBind(name, binding);
    }

    /* ===================== doBuild ===================== */

    protected Object doBuild(final Binding binding) {
        final Class<?> instanceType = binding.getType();
        if (instanceType == null) {
            return null;
        }
        // 排除 JDK 自带类的 doBuild
        if (ClassUtils.isSkipBuildType(instanceType)) {
            return ClassUtil.getDefaultValue(instanceType);
        }
        if (log.isDebugEnabled()) {
            log.debug("Container build: {}", instanceType);
        }
        final Constructor<?>[] constructors = ReflectUtils.sortConstructors(
            instanceType.getDeclaredConstructors()
        );
        Object instance;
        final List<Exception> errors = new ArrayList<>();
        for (final Constructor<?> constructor : constructors) {
            constructor.setAccessible(true);
            final Object[] dependencies =
                this.processParameterInjector(binding, constructor);
            try {
                instance = constructor.newInstance(dependencies);
            } catch (final Exception e) {
                errors.add(e);
                continue;
            }
            instance = this.processInstanceInjector(binding, instance);
            instance = this.processBeanBefore(binding, instance);
            // TODO: aop
            // if (this.aspectMatches(instanceType)) {
            //     instance =
            //         ProxyCreator.createAop(
            //             this.make(AspectManager.class),
            //             instance,
            //             instanceType,
            //             instanceType.getInterfaces(),
            //             constructor.getParameterTypes(),
            //             dependencies
            //         );
            // }
            if (instance != null) {
                return instance;
            }
        }
        log.error(
            "Build instance failed, use default value, Type: {}",
            instanceType
        );
        for (final Exception error : errors) {
            log.error("Build instance failed error", error);
        }
        return ClassUtil.getDefaultValue(instanceType);
    }

    /* ===================== doMake ===================== */

    protected <T> T doMake(
        final String instanceName,
        final Class<T> returnType
    ) {
        if (log.isDebugEnabled()) {
            log.debug("Container make: {} - {}", instanceName, returnType);
        }
        final Binding binding =
            this.getOrDefaultBinding(instanceName, returnType);
        Object instance = binding.getSource();
        if (instance != null) {
            return Convert.convert(returnType, instance);
        }
        try {
            FactoryBean<?> factoryBean = binding.getFactoryBean();
            if (factoryBean == null) {
                factoryBean =
                    new FactoryBean<>() {
                        @Override
                        public Object getObject() throws Exception {
                            return doBuild(binding);
                        }

                        @Override
                        public Class<?> getObjectType() {
                            return binding.getType();
                        }
                    };
            }
            instance = factoryBean.getObject();
        } catch (final Throwable e) {
            throw new ContainerException("Instance make failed", e);
        }
        final T returnInstance = Convert.convert(returnType, instance);
        if (binding.isShared()) {
            binding.setSource(returnInstance);
        }
        return returnInstance;
    }

    /* ===================== doRemove ===================== */

    protected void doRemove(final String name) {
        if (log.isDebugEnabled()) {
            log.debug("Container remove: {}", name);
        }
        final Binding binding = this.get(name);
        if (binding.isCreated()) {
            this.processBeanAfter(binding, binding.getSource());
        }
        this.removeBinding(name);
    }

    /* ===================== doCall =============== */

    protected <T> T doCall(
        final Object instance,
        final Method method,
        final Class<T> returnType
    ) {
        if (log.isDebugEnabled()) {
            log.debug("Container call method: {} - {}", method, returnType);
        }
        final Object[] dependencies =
            this.processParameterInjector(null, method);
        return Convert.convert(
            returnType,
            ReflectUtil.invoke(instance, method, dependencies)
        );
    }

    protected <T> T doCall(
        final Object instance,
        final String methodName,
        final Class<T> returnType
    ) {
        final Method[] methods = Arrays
            .stream(instance.getClass().getMethods())
            .filter(m -> m.getName().equals(methodName))
            .toArray(Method[]::new);
        if (methods.length == 0) {
            throw new NullPointerException(
                "The specified method was not found"
            );
        } else if (methods.length > 1) {
            throw new IllegalCallerException(
                "The called method cannot be overloaded"
            );
        }
        return this.doCall(instance, methods[0], returnType);
    }

    protected <T> T doCall(
        final String name,
        final String methodName,
        final Class<T> returnType
    ) {
        return this.doCall(
                this.make(name, Object.class),
                methodName,
                returnType
            );
    }

    protected <T> T doCall(
        final Class<?> type,
        final String methodName,
        final Class<T> returnType
    ) {
        return this.doCall(this.make(type), methodName, returnType);
    }

    /* ===================== bind ===================== */

    // name, factory
    // name, type
    @SuppressWarnings("unchecked")
    public Binding bind(
        final String name,
        final Class<?> type,
        final String scopeType
    ) {
        if (FactoryBean.class.isAssignableFrom(type)) {
            return this.bind(
                    name,
                    this.make(
                            FACTORY_BEAN_PREFIX + name,
                            (Class<? extends FactoryBean<?>>) type
                        ),
                    scopeType
                );
        } else {
            return this.doBind(name, type, scopeType);
        }
    }

    public Binding bind(
        final String name,
        final FactoryBean<?> factoryBean,
        final String scopeType
    ) {
        return this.doBind(name, factoryBean, scopeType);
    }

    public Binding bind(final String name, final Class<?> type) {
        return this.bind(name, type, ScopeType.SINGLETON.asString());
    }

    public Binding bind(final String name, final FactoryBean<?> factoryBean) {
        return this.bind(name, factoryBean, ScopeType.SINGLETON.asString());
    }

    // name, instance

    public Binding instance(
        final String name,
        final Object instance,
        final String scopeType
    ) {
        return this.doBind(name, instance, scopeType);
    }

    public Binding instance(final String name, final Object instance) {
        return this.instance(name, instance, ScopeType.SINGLETON.asString());
    }

    // factory
    // type
    @SuppressWarnings("unchecked")
    public Binding bind(final Class<?> type, final String scopeType) {
        if (FactoryBean.class.isAssignableFrom(type)) {
            final FactoryBean<?> factoryBean =
                this.make((Class<? extends FactoryBean<?>>) type);
            return this.bind(
                    this.typeToName(factoryBean.getObjectType()),
                    factoryBean,
                    scopeType
                );
        } else {
            return this.bind(this.typeToName(type), type, scopeType);
        }
    }

    public Binding bind(final Class<?> type) {
        return this.bind(type, ScopeType.SINGLETON.asString());
    }

    public Binding bind(final FactoryBean<?> factoryBean, String scopeType) {
        return this.bind(
                this.typeToName(factoryBean.getObjectType()),
                factoryBean,
                scopeType
            );
    }

    public Binding bind(final FactoryBean<?> factoryBean) {
        return this.bind(factoryBean, ScopeType.SINGLETON.asString());
    }

    // instance

    public Binding instance(final Object instance, final String scopeType) {
        return this.instance(
                this.typeToName(instance.getClass()),
                instance,
                scopeType
            );
    }

    public Binding instance(final Object instance) {
        return this.instance(instance, ScopeType.SINGLETON.asString());
    }

    /* ===================== make ===================== */

    public <T> T make(final Class<T> returnType) {
        return this.make(this.getNameByType(returnType), returnType);
    }

    public <T> T make(final String name, final Class<T> returnType) {
        return this.doMake(name, returnType);
    }

    public <T> T make(final Class<T> returnType, final DataBinder dataBinder) {
        return this.withAndReset(() -> this.make(returnType), dataBinder);
    }

    public <T> T make(
        final String name,
        final Class<T> returnType,
        final DataBinder dataBinder
    ) {
        return this.withAndReset(() -> this.make(name, returnType), dataBinder);
    }

    /* ====================== remove ======================= */

    public void remove(final String name) {
        this.doRemove(name);
    }

    /* ====================== call ======================= */

    @SuppressWarnings("unchecked")
    public <T> T call(final Object instance, final Method method) {
        return (T) this.call(instance, method, method.getReturnType());
    }

    public <T> T call(
        final Object instance,
        final Method method,
        final Class<T> returnType
    ) {
        return this.doCall(instance, method, returnType);
    }

    public <T> T call(
        final Object instance,
        final String methodName,
        final Class<T> returnType
    ) {
        return this.doCall(instance, methodName, returnType);
    }

    public <T> T call(
        final String name,
        final String methodName,
        final Class<T> returnType
    ) {
        return this.doCall(name, methodName, returnType);
    }

    public <T> T call(
        final Class<?> type,
        final String methodName,
        final Class<T> returnType
    ) {
        return this.doCall(type, methodName, returnType);
    }

    public <T> T call(final Method method) {
        return this.call(this.make(method.getDeclaringClass()), method);
    }

    public <T> T call(
        final Object instance,
        final Method method,
        final DataBinder dataBinder
    ) {
        return this.withAndReset(() -> this.call(instance, method), dataBinder);
    }

    public <T> T call(
        final Object instance,
        final Method method,
        final Class<T> returnType,
        final DataBinder dataBinder
    ) {
        return this.withAndReset(
                () -> this.call(instance, method, returnType),
                dataBinder
            );
    }

    public <T> T call(
        final Object instance,
        final String methodName,
        final Class<T> returnType,
        final DataBinder dataBinder
    ) {
        return this.withAndReset(
                () -> this.call(instance, methodName, returnType),
                dataBinder
            );
    }

    public <T> T call(
        final String name,
        final String methodName,
        final Class<T> returnType,
        final DataBinder dataBinder
    ) {
        return this.withAndReset(
                () -> this.call(name, methodName, returnType),
                dataBinder
            );
    }

    public <T> T call(
        final Class<?> type,
        final String methodName,
        final Class<T> returnType,
        final DataBinder dataBinder
    ) {
        return this.withAndReset(
                () -> this.call(type, methodName, returnType),
                dataBinder
            );
    }

    public <T> T call(final Method method, final DataBinder dataBinder) {
        return this.withAndReset(() -> this.call(method), dataBinder);
    }

    /* ===================================================== */

    public DataBinder getDataBinder() {
        return this.dataBinder.get();
    }

    public Container with(final Map<String, Object> args) {
        return this.with(null, args);
    }

    public Container with(final String prefix, final Map<String, Object> args) {
        this.dataBinder.set(new DefaultDataBinder(this, args));
        return this;
    }

    public Container resetWith() {
        this.dataBinder.set(
                new DefaultDataBinder(this, new ConcurrentHashMap<>(256))
            );
        return this;
    }

    public <T> T withAndReset(
        final Supplier<T> callback,
        final DataBinder dataBinder
    ) {
        final DataBinder reset = this.dataBinder.get();
        this.dataBinder.set(dataBinder);
        final T result = callback.get();
        this.dataBinder.set(reset);
        return result;
    }

    public Map<Class<? extends Context>, Context> getContexts() {
        return contexts;
    }

    public Container addFirstInstanceInjector(final InstanceInjector injector) {
        if (log.isDebugEnabled()) {
            log.debug("Container add instance injector: {}", injector);
        }
        this.instanceInjectors.addFirst(injector);
        return this;
    }

    public Container addInstanceInjector(final InstanceInjector injector) {
        if (log.isDebugEnabled()) {
            log.debug("Container add instance injector: {}", injector);
        }
        this.instanceInjectors.addLast(injector);
        return this;
    }

    public Container removeInstanceInjector(final InstanceInjector injector) {
        if (log.isDebugEnabled()) {
            log.debug("Container remove instance injector: {}", injector);
        }
        this.instanceInjectors.remove(injector);
        return this;
    }

    public Deque<InstanceInjector> getInstanceInjectors() {
        return instanceInjectors;
    }

    public Container addFirstParameterInjector(
        final ParameterInjector injector
    ) {
        if (log.isDebugEnabled()) {
            log.debug("Container add parameter injector: {}", injector);
        }
        this.parameterInjectors.addFirst(injector);
        return this;
    }

    public Container addParameterInjector(final ParameterInjector injector) {
        if (log.isDebugEnabled()) {
            log.debug("Container add parameter injector: {}", injector);
        }
        this.parameterInjectors.addLast(injector);
        return this;
    }

    public Container removeParameterInjector(final ParameterInjector injector) {
        if (log.isDebugEnabled()) {
            log.debug("Container remove parameter injector: {}", injector);
        }
        this.parameterInjectors.remove(injector);
        return this;
    }

    public Deque<ParameterInjector> getParameterInjectors() {
        return parameterInjectors;
    }

    public Container addFirstBeanBeforeProcessor(
        final BeanBeforeProcessor processor
    ) {
        if (log.isDebugEnabled()) {
            log.debug("Container add bean before processor: {}", processor);
        }
        this.beanBeforeProcessors.addFirst(processor);
        return this;
    }

    public Container addBeanBeforeProcessor(
        final BeanBeforeProcessor processor
    ) {
        if (log.isDebugEnabled()) {
            log.debug("Container add bean before processor: {}", processor);
        }
        this.beanBeforeProcessors.addLast(processor);
        return this;
    }

    public Container removeBeanBeforeProcessor(
        final BeanBeforeProcessor processor
    ) {
        if (log.isDebugEnabled()) {
            log.debug("Container remove bean before processor: {}", processor);
        }
        this.beanBeforeProcessors.remove(processor);
        return this;
    }

    public Deque<BeanBeforeProcessor> getBeanBeforeProcessors() {
        return beanBeforeProcessors;
    }

    public Container addFirstBeanAfterProcessor(
        final BeanAfterProcessor processor
    ) {
        if (log.isDebugEnabled()) {
            log.debug("Container add bean after processor: {}", processor);
        }
        this.beanAfterProcessors.addFirst(processor);
        return this;
    }

    public Container addBeanAfterProcessor(final BeanAfterProcessor processor) {
        if (log.isDebugEnabled()) {
            log.debug("Container add bean after processor: {}", processor);
        }
        this.beanAfterProcessors.addLast(processor);
        return this;
    }

    public Container removeBeanAfterProcessor(
        final BeanAfterProcessor processor
    ) {
        if (log.isDebugEnabled()) {
            log.debug("Container remove bean after processor: {}", processor);
        }
        this.beanAfterProcessors.remove(processor);
        return this;
    }

    public Deque<BeanAfterProcessor> getBeanAfterProcessors() {
        return beanAfterProcessors;
    }
}
