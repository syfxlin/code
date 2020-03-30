import StoreProvider from "./StoreProvider";

export type State = {
  count: number;
};

export const initialState: State = {
  count: 1
};

export type Actions = ThisType<StoreProvider> & {
  addCount: () => void;
  delCount: () => void;
};

export const initialActions: Actions = {
  addCount() {
    this.setState({ ...this.state, count: this.state.count + 1 });
  },
  delCount() {
    this.setState({ ...this.state, count: this.state.count - 1 });
  }
};
