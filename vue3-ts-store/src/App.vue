<template>
  <div id="app">
    <img alt="Vue logo" src="./assets/logo.png" />
    <div>
      {{ count }}
      <button @click="addCount">+</button>
    </div>
    <div>
      {{ subCount }}
      <button @click="addSubCount">+</button>
    </div>
    <div>{{ arr1 }}</div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useAction, useState } from "@/store";

export default defineComponent({
  name: "App",
  setup() {
    const count = useState<number>("count");
    const addCount = useAction<Function>("addCount");
    const subCount = useState<number>("sub.count");
    const addSubCount = useAction("sub.addSubCount");
    const arr1 = useState<string>("arr.0");
    return { count, addCount, subCount, addSubCount, arr1 };
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
