import state, { State } from "@/store/state";
import actions, { Actions } from "@/store/actions";
import { WritableComputedRef } from "@vue/reactivity";
import { computed } from "vue";

type useStateGetterFn = <R>(state: State) => R;
type useStateWritableFn<R> = {
  get: (state: State, ctx?: any) => R;
  set: (value: R) => void;
};
type useActionFn = (actions: Actions) => any;

export const useStore = (): { state: State; actions: Actions } => {
  return { state, actions };
};

export function useState<R>(
  key: string | useStateGetterFn | useStateWritableFn<R> | null = null
): WritableComputedRef<R> {
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
    set: v => key.set(v)
  });
}

export function useAction<A>(key: string | useActionFn | null = null): A {
  if (key === null) {
    return (actions as any) as A;
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
