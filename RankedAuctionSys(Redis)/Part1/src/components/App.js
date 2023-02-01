import React from "react";
import TopBar from "./TopBar";
import Main from "./Main";
import Footer from "./Footer";

import "../styles/App.css";

function App() {
    return (
        <div className="App">
            <TopBar />
            <Main />
            <Footer />
        </div>
    );
}

export default App;