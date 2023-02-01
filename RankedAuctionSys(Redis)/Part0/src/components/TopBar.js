import React from 'react';
import logo from "../assets/images/logo.svg";

function TopBar() {
    return (
        <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
            <span className="App-title">redis</span>
        </header>
    );
}

export default TopBar;