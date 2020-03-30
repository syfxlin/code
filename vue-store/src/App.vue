<template>
  <div id="app">
    <img alt="Vue logo" src="./assets/logo.png" />
    <p></p>
    <button @click="delCount()">-</button>
    {{ count }}
    <button @click="addCount()">+</button>
  </div>
</template>

<script>
import { mapState, mapActions } from "./store";

export default {
  name: "App",
  computed: {
    ...mapState(["count"])
  },
  methods: {
    ...mapActions(["addCount", "delCount"])
  }
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
