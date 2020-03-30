import React, { useContext } from "react";
import logo from "./logo.svg";
import "./App.css";
import { StoreContext } from "./store/StoreProvider";

function App() {
  const store = useContext(StoreContext);
  const { count } = store.state;
  const { addCount, delCount } = store.actions;
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <button onClick={() => delCount()}>-</button>
        {count}
        <button onClick={() => addCount()}>+</button>
      </header>
    </div>
  );
}

export default App;
