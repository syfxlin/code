import Vue from "vue";

export const state = Vue.observable({
  count: 1
});

export const actions = {
  addCount() {
    // 由于已经使用了Vue.observable来初始化State,所以这时的State已经拥有响应式，所以可以直接对其进行修改
    state.count++;
  },
  delCount() {
    state.count--;
  }
};
