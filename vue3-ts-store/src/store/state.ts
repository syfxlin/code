import { reactive } from "vue";

const state = reactive({
  count: 1,
  sub: {
    count: 2
  },
  arr: ["arr1", "arr2"]
});

export type State = typeof state;
export default state;
