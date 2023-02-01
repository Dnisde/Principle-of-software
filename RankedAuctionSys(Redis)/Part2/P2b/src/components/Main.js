import InputTable from "./Table"
import ParamBox from './ParamBox'

import React, {useState} from 'react'
import {Routes, Route, HashRouter} from "react-router-dom";

function Main() {

    const [user_num, setUserNum] = useState(0);
    const [max_rank, setMaxRank] = useState(0);
    const [max_sum, setMaxSum] = useState(0);

    const handleUserNum = (value) => {
        setUserNum(value);
    }
    const handleMaxRank = (value) => {
        setMaxRank(value);
    }
    const handleMaxSum = (value) => {
        setMaxSum(value);
    }

    return (
        <HashRouter>
            <Routes>
                <Route path="/" element={<ParamBox handleUserNum={handleUserNum} handleMaxRank={handleMaxRank} handleMaxSum={handleMaxSum} />}/>
                <Route path="/show" element={<ParamBox handleUserNum={handleUserNum} handleMaxRank={handleMaxRank} handleMaxSum={handleMaxSum} />}/>
                <Route path="/input" element={<InputTable user_num={user_num} max_rank={max_rank} max_sum={max_sum}/>}/>
            </Routes>
        </HashRouter>
    )
}

export default Main