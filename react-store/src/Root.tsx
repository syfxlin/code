import React from "react";
import StoreProvider from "./store/StoreProvider";
import App from "./App";

export default function Root() {
  return (
    <StoreProvider>
      <App />
    </StoreProvider>
  );
}
