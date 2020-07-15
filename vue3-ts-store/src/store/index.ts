import { computed, reactive, ref } from "vue";
import { WritableComputedRef } from "@vue/reactivity";

type stateGetterFn<R, S = any> = (state: S) => R;
type stateWritableFn<R, S = any> = {
  get: (state: S, ctx?: any) => R;
  set: (state: S, value: R) => void;
};
type actionFn<R, A> = (actions: A) => R;
type stateSetterFn<S = any> = (state: S) => void;

const stores = ref<{ [key: string]: any }>({});

export function createState<S extends object>(state: S) {
  return reactive(state);
}

export function createStore<S extends object = any, A extends object = any>(
  state: S,
  actions: A,
  name = "default"
) {
  const store = { state, actions };
  stores.value[name] = store;
  return store;
}

export function useStore(name = "default") {
  if (stores.value[name] === undefined) {
    throw new Error("Store not yet registered");
  }
  const store = stores.value[name];
  return { state: computed(store.state), actions: store.actions };
}

export function useState<R, S = any>(
  key: string | stateGetterFn<R, S> | stateWritableFn<R, S> | null = null,
  name = "default"
): WritableComputedRef<R> {
  if (stores.value[name] === undefined) {
    throw new Error("Store not yet registered");
  }
  const state = stores.value[name].state;
  if (key === null) {
    return computed(() => (state as any) as R);
  }
  if (typeof key === "function") {
    return computed(() => key(state));
  }
  if (typeof key === "string") {
    let result = state as any;
    const keys = key.split(".");
    const lastKey = keys.pop() as string;
    for (const k of keys) {
      result = result[k];
      if (result === null || result === undefined) {
        throw Error(`key is error [${key}]`);
      }
    }
    return computed({
      get: ctx => result[lastKey],
      set: v => (result[lastKey] = v)
    });
  }
  return computed({
    get: ctx => key.get(state, ctx),
    set: v => key.set(state, v)
  });
}

export function useAction<R, A = any>(
  key: string | actionFn<R, A> | null = null,
  name = "default"
): R {
  if (stores.value[name] === undefined) {
    throw new Error("Store not yet registered");
  }
  const actions = stores.value[name].actions;
  if (key === null) {
    return (actions as any) as R;
  }
  if (typeof key === "string") {
    let result = actions as any;
    const keys = key.split(".");
    for (const k of keys) {
      result = result[k];
      if (result === null || result === undefined) {
        throw Error(`key is error [${key}]`);
      }
    }
    return result;
  }
  return key(actions);
}

export function setState<S>(setter: stateSetterFn<S>, name = "default") {
  setter(useState<S, S>(null, name).value);
}
