import PubSub from "pubsub-js"
import React, { Component } from "react"

class ReturnBox extends Component {

    state = {
        message: ""
    }

    componentDidMount(){
        PubSub.subscribe("response_data",(msg,data)=>{
            this.setState({
                message: data
            })
        })
    }

    render() {
        let {message} = this.state
        return (
            <div className="return-box">
                Redis Return: 
                <div className="return-message">
                    {message}
                </div>
            </div>
        )
    }
}

export default ReturnBox