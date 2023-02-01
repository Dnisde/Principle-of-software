import { Form, InputNumber, Table, Typography, message } from 'antd';
import React, { useState } from 'react';
import axios from 'axios';
import MD5 from "./md5_lib"
import { REDIS_URL, SALT, PASSWORD } from "../constants";

function InputTable (props) {
    const { user_num, max_rank, max_sum } = props;

    const originData = [];
    for (let i = 0; i < user_num; i++) {
        var obj = {
            key: i.toString(),
            user: 'Git' + i
        }
        for (let j = 0; j < user_num; j++) {
            obj['git' + j] = 0;
        }
        originData.push(obj);
    }
    console.log(originData)

    const EditableCell = ({
                              editing,
                              dataIndex,
                              title,
                              inputType,
                              record,
                              index,
                              children,
                              ...restProps
                          }) => {

        return (
            <td {...restProps}>
                {editing ? (
                    <Form.Item
                        name={dataIndex}
                        style={{
                            margin: 0,
                        }}
                        rules={[
                            {
                                required: true,
                                message: `Can't be Empty!`,
                            },
                        ]}
                    >
                        <InputNumber
                            min={0}
                            max={max_rank}
                            size="small"
                        />
                    </Form.Item>
                ) : (
                    children
                )}
            </td>
        );
    };

    const [form] = Form.useForm();
    const [data, setData] = useState(originData);
    const [editingKey, setEditingKey] = useState('');
    const isEditing = (record) => record.key === editingKey;

    const edit = (record) => {
        form.setFieldsValue({
            name: '',
            age: '',
            address: '',
            ...record,
        });
        setEditingKey(record.key);
    };

    const save = async (key) => {
        try {
            const row = await form.validateFields();
            const newData = [...data];
            const index = newData.findIndex((item) => key === item.key);
            if (index > -1) {
                const item = newData[index];
                newData.splice(index, 1, {
                    ...item,
                    ...row,
                });
                setData(newData);
                setEditingKey('');
            } else {
                newData.push(row);
                setData(newData);
                setEditingKey('');
            }
            let row_sum = 0;
            for (let g = 0; g < user_num; g++) {
                row_sum += newData[index]["git" + g];
            }
            console.log("row_sum=", row_sum)
            if (row_sum === max_sum) {
                message.success("Score saved succeed!");
            } else {
                message.warn("Row sum must be " + max_sum + "!");
            }
        } catch (errInfo) {
            console.log('Validate Failed:', errInfo);
        }
    };

    const columns = [];
    columns.push( {
        title: 'User',
        dataIndex: 'user',
        width: '3%',
        fixed: 'left'
        // render: (index) => {
        //     return (
        //         <h>Git{index}</h>
        //     )
        // }
    })

    for (let i = 0; i < user_num; i++) {
        columns.push({
            title: 'Git' + i,
            dataIndex: 'git' + i,
            width: '3%',
            editable: true,
        });
    }

    columns.push({
        title: 'Operation',
        dataIndex: 'operation',
        fixed: 'right',
        render: (_, record) => {
            const editable = isEditing(record);
            return editable ? (
                <span>
                    <Typography.Link
                        onClick={() => save(record.key)}
                        style={{
                            marginRight: 8,
                        }}
                        className="save-link"
                    >
                        Save
                    </Typography.Link>
                </span>
            ) : (
                <Typography.Link
                    disabled={editingKey !== ''}
                    onClick={() => edit(record)}
                >
                    Edit
                </Typography.Link>
            );
        },
    })


    const mergeColumns = columns.map((col) => {
        if (col.title === 'Users') {
            return col;
        }
        if (!col.editable) {
            return col;
        }
        return {
            ...col,
            onCell: (record) => ({
                record,
                inputType: 'number',
                dataIndex: col.dataIndex,
                title: col.title,
                editing: isEditing(record),
            }),
        };
    });

    const submit = async () => {
        const new_md5 = new MD5(SALT + PASSWORD);
        const hash = new_md5.md5;
        let submitFlag = 0;
        for (let index = 0; index < user_num; index++) {
            let url = REDIS_URL + '?salt=' + SALT + '&hash=' + hash + '&message=HMSET user:git' + index;
            for (let i = 0; i < user_num; i++) {
                const score = data[index]["git" + i];
                url += ' git' + i + ' ' + score + ' ';
            }
            const opt = {
                method: 'GET',
                url: url,
                headers: { 'content-type': 'application/json'}
            };
            await axios(opt)
                .then( response => {
                    console.log('Request sent to Redis: ', opt)
                    console.log(response.data)
                    if(response.status === 200) {
                        if (!response.data || response.data.length === 0) {
                            message.error('Submit Fail!');
                        }
                        else {
                            submitFlag++;
                        }

                    }
                })
                .catch( error => {
                    console.log(error.message);
                    message.error('Submit Fail!');
                })
        }
        console.log(submitFlag);
        if (submitFlag === user_num) {
            message.success('Submit Succeed!')
        }
    }

    return (
        <div>
            <div>
                <Form
                    form={form}
                    component={false}
                >
                    <Table
                        components={{
                            body: {
                                cell: EditableCell
                            }
                        }}
                        bordered
                        size="small"
                        dataSource={data}
                        columns={mergeColumns}
                        rowClassName="editable-row"
                    />
                </Form>
            </div>
            <div>
                <button
                    onClick={submit}
                    disabled={false}
                    id="submit-btn"
                    className="submit-btn">
                    Submit
                </button>
            </div>
        </div>
    );
};

export default InputTable;