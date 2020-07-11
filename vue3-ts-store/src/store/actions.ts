import state from "@/store/state";

const actions = {
  addCount() {
    state.count++;
  },
  sub: {
    addSubCount() {
      state.sub.count++;
    }
  }
};

export type Actions = typeof actions;
export default actions;
