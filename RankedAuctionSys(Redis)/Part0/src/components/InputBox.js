import React from "react";
import { Form, Input, Button, message } from 'antd';
import axios from 'axios';
import PubSub from "pubsub-js"
// import md5 from "./md5"
import MD5 from "./Md5_lib"
import { REDIS_URL } from "../constants";

const formItemLayout = {
    labelCol: {
        xs: { span: 24 },
        sm: { span: 8 },
    },
    wrapperCol: {
        xs: { span: 24 },
        sm: { span: 20 },
    },
};
const tailFormItemLayout = {
    wrapperCol: {
        xs: {
            span: 16,
            offset: 0,
        },
        sm: {
            span: 20,
            offset: 8,
        },
    },
};

function InputBox() {
    const [form] = Form.useForm();

    const onFinish = values => {
        console.log('Received values of form: ', values);
        const { salt, password, command } = values;
        var md5 = new MD5(salt + password);
        const hash = md5.md5;
        const opt = {
            method: 'GET',
            url: REDIS_URL + '?salt=' + salt + '&hash=' + hash + '&message=' + command,
            headers: { 'content-type': 'application/json'}
        };
        axios(opt)
            .then( response => {
                console.log('Request sent to Redis: ', opt)
                console.log(response.data)
                PubSub.publish('response_data',response.data)
                if(response.status === 200) {
                    if (!response.data || response.data.length === 0) {
                        message.error('Password Incorrect, Please Try Again');
                    }
                    else{
                        message.success('Message Send Succeed');
                    }
                    
                }
                return response.data
            })
            .catch( error => {
                console.log('Network error, message send failed: ', error.message);
                message.error('Message Send Failed: ' + error.message);
            })

    };

    return (
        <Form
            {...formItemLayout}
            form={form}
            name="InputBox"
            onFinish={onFinish}
            className="input-box"
        >
            <Form.Item
                name="salt"
                label="Salt"
                rules={[
                    {
                        required: true,
                        message: 'Please input your salt',
                    },
                ]}
                
            >
                <Input placeholder="Any Sequences of Characters" />
            </Form.Item>

            <Form.Item
                name="password"
                label="Password"
                rules={[
                    {
                        required: true,
                        message: 'Please input your password',
                    },
                ]}
            >
                <Input.Password placeholder="Default: A8D6E93C2D204" />
            </Form.Item>

            <Form.Item
                name="command"
                label="Message"
                rules={[
                    {
                        required: true,
                        message: 'Please input your message',
                    },
                ]}
            >
                <Input placeholder="Example: GET server:name" />
            </Form.Item>

            <Form.Item {...tailFormItemLayout}>
                <Button type="primary" htmlType="submit" className="send-btn">
                    Send Message
                </Button>
            </Form.Item>
        </Form>
    );
}

export default InputBox;