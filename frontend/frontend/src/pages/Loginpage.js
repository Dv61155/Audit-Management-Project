import React, { useEffect } from "react";
import {Button, Card, Col, Container, Form, InputGroup, Row} from "react-bootstrap";
import {Navigate, useNavigate} from "react-router-dom";
import axios from "axios";
import {APIRoutes} from "../api/api";
import {toast} from "react-toastify";

import {UitSignout} from '@iconscout/react-unicons-thinline'

export default function Loginpage() {
    const [data, setData] = React.useState({
        username: "",
        password: "",
    });
    const [errorData, setErrorData] = React.useState({
        username: "",
        password: "",
    });
    const [validated, setValidated] = React.useState(false);
   // let {username, password} = formData;

    const navigate = useNavigate();
    
    useEffect(()=>{

    },[errorData])

    async function fetchData(props) {
        const body = JSON.stringify({
            username: data.username,
            password: data.password
        });
       
        try {
            const response = await axios.post(
                `${APIRoutes.AuthorizeModule}/authenticate`,
                body,{
                    headers: {
                    'Content-Type': 'application/json'
                    }
                }
            );
            console.log(response);
            if (response.status && response.status === 200) {
                if (localStorage.getItem("access") != null) {
                    localStorage.removeItem("access");
                }
                localStorage.setItem("access", response.data);
                localStorage.setItem("isLoggedIn", true);

                if (localStorage.getItem("isLoggedIn") === "true") {
                    navigate("/");
                }
            }
            //  const response={
            //     username: "rohit12",
            //     password: "rohit12",
            //}
            // localStorage.removeItem("access");
            // localStorage.setItem("access", response);
            // localStorage.setItem("isLoggedIn", true);
            // if (localStorage.getItem("isLoggedIn") === "true") {
            //     navigate("/");
            // }
        } catch (e) {
            console.log("Catch Block")
            console.log(e.response)
            alert(e.response.data);
            const {response} = e

            // switch (response.status) {
            //     case 404:
            //         toast.error(response.data.message, {
            //             position: "top-right",
            //             autoClose: 3000,
            //             hideProgressBar: false,
            //             closeOnClick: true,
            //             pauseOnHover: true,
            //             draggable: true,
            //             progress: undefined,
            //         });
            //         break;
            //     case 400:
            //         console.log(response.data.errors)
            //         for (let error in response.data.errors) {
            //             console.log(response.data.errors[error][0]);
            //             toast.error(response.data.errors[error][0], {
            //                 position: "top-right",
            //                 autoClose: 3000,
            //                 hideProgressBar: false,
            //                 closeOnClick: true,
            //                 pauseOnHover: true,
            //                 draggable: true,
            //                 progress: undefined,
            //             });
            //         }
            // }

        }
    }

    const validateForm = () => {
        let errorStatus = true;
        if (!data.username.length) {
            setErrorData(prevState => ({...prevState, username: "Username is empty"}));
            errorStatus = false;
            setValidated(true);
        }else if (data.username.length <4) {
            setErrorData(prevState => ({...prevState, username: "Username length must be 5 length"}));
            errorStatus = false;
            setValidated(true);
        } else {
            setErrorData(prevState => ({...prevState, username: ""}))
            errorStatus = true
        }
        if (!data.password.length) {
            setErrorData(prevState => ({...prevState, password: "Password is empty"}));
            errorStatus = false;
            setValidated(true);
        } else if (data.password.length <4) {
            setErrorData(prevState => ({...prevState, password: "Password length must be 5 length"}));
            errorStatus = false;
            setValidated(true);
        } else {
            setErrorData(prevState => ({...prevState, password: ""}))
            errorStatus = true;
        }
        return errorStatus
    }

    const handelSubmit = (e) => {
        e.preventDefault();

        const form = e.currentTarget;
        console.log(form.checkValidity());
        if (validateForm()) {
            fetchData();
        }

    };

    if (localStorage.getItem("isLoggedIn") === "true") {
        console.log(localStorage.getItem("isLoggedIn"));
        return <Navigate to="/"/>;
    }

    return (
        <>
            <main className="loginpage">
                <div className="auth-fluid">
                    <div className="auth-fluid-form-box">
                        <div className="align-items-center d-flex h-100">

                            <Card.Body>
                                <h1 className="text-dark mt-0">Login</h1>
                                <p className="text-muted mb-4">Enter your username and password to access
                                    portal.</p>
                                <Form onSubmit={(e) => handelSubmit(e)}>
                                    <Form.Group className="mb-3">
                                        <Form.Label htmlFor="username">
                                            Username
                                        </Form.Label>
                                        <InputGroup>
                                            <Form.Control
                                                type={"text"}
                                                name="username"
                                                id="username"
                                                value={data.username}
                                                onChange={(e) => {
                                                    setData({
                                                        ...data,
                                                        [e.target.name]: e.target.value,
                                                    });
                                                    validateForm();
                                                 }
                                                }
                                                isInvalid={errorData.username}
                                            />
                                            {errorData.username && (
                                                <Form.Control.Feedback type="invalid">
                                                    {errorData.username}
                                                </Form.Control.Feedback>
                                            )}
                                        </InputGroup>
                                    </Form.Group>
                                    <Form.Group className="mb-3">
                                        <Form.Label htmlFor="password">
                                            Password
                                        </Form.Label>
                                        <Form.Control
                                            type={"password"}
                                            name="password"
                                            id="password"
                                            value={data.password}
                                            onChange={(e) => {

                                                setData({
                                                    ...data,
                                                    [e.target.name]: e.target.value,
                                                });
                                                validateForm();
                                            }
                                            }
                                            isInvalid={data.password.length<=4?errorData.password:null}
                                        />
                                        {errorData.password && (
                                            <Form.Control.Feedback type={"invalid"}>
                                                {errorData.password}
                                            </Form.Control.Feedback>
                                        )}

                                    </Form.Group>

                                    <Form.Group>
    
                                        <Button
                                            type={"submit"}
                                            disabled={(data.username.length<5||data.password.length<5)?true:false}
                                            className="btn btn-primary">
                                            <i className="uit uit-signin"></i>
                                            Login
                                        </Button>
                                    </Form.Group>
                                </Form>
                            </Card.Body>


                        </div>
                    </div>
                    <div className="d-flex auth-fluid-right  align-items-center justify-content-center text-center">
                        
                    </div>
                </div>
            </main>
        </>
    )
}
