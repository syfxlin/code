<template>
  <div id="app">
    <img alt="Vue logo" src="./assets/logo.png" />
    <div>
      {{ count }}
      <button @click="addCount">+</button>
    </div>
    <div>
      Sub get: {{ subCount }}
      <button @click="addSubCount">+</button>
    </div>
    <div>Array get: {{ arr1 }}</div>
    <div>Function get: {{ functionGet }}</div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import {
  createState,
  createStore,
  setState,
  useAction,
  useState
} from "@/store";

export default defineComponent({
  name: "App",
  setup() {
    const state = createState({
      count: 1,
      sub: {
        count: 2
      },
      arr: ["arr1", "arr2"]
    });
    const actions = {
      addCount() {
        state.count++;
      },
      sub: {
        addSubCount() {
          setState<typeof state>(s => s.sub.count++);
        }
      }
    };
    createStore(state, actions);
    const count = useState<number>("count");
    const addCount = useAction<Function>("addCount");
    const subCount = useState<number>("sub.count");
    const addSubCount = useAction("sub.addSubCount");
    const arr1 = useState<string>("arr.0");
    const functionGet = useState<number, typeof state>(s => s.count);
    return { count, addCount, subCount, addSubCount, arr1, functionGet };
  }
});
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
