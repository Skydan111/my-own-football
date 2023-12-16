import {
    Button,
    Flex,
    FormLabel,
    Heading,
    Input,
    Stack,
    Image, Box, Alert, AlertIcon, Link,
} from '@chakra-ui/react';
import {Formik, Form, useField} from "formik";
import * as Yup from 'yup';
import {useAuth} from "../context/AuthContext.jsx";
import {errorNotification} from "../../services/notification.js";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";

// eslint-disable-next-line react/prop-types
const MyTextInput = ({label, ...props}) => {

    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

const LoginForm = () => {

    const { login } = useAuth();
    const navigate = useNavigate();

    return (
        <Formik
            validateOnMount={true}
            validationSchema={
                Yup.object({
                    username: Yup.string()
                        .email("Must be valid email")
                        .required("Email is required"),
                    password: Yup.string()
                        .min(8, "Password cannot be less then 8 characters")
                        .required("Password is required")
                })
            }
            initialValues={{username: '', password: ''}}
            onSubmit={(values, {setSubmitting}) => {
                setSubmitting(true);
                login(values).then(res => {
                    // TODO: navigate to dashboard
                    navigate("/dashboard");
                }).catch(err => {
                    errorNotification(err.code, err.response.data.message)
                }).finally(() => {
                    setSubmitting(false);
                })
            }}
        >

            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack spacing={5}>
                        <MyTextInput
                            label={"Email"}
                            name={"username"}
                            type={"email"}
                            placeholder={"povorozniuk@gmail.com"}
                        />
                        <MyTextInput
                            label={"Password"}
                            name={"password"}
                            type={"password"}
                            placeholder={"type your password"}
                        />

                        <Button
                            type={"submit"}
                            disabled={!isValid || isSubmitting}
                        >
                            Login
                        </Button>
                    </Stack>
                </Form>
            )}

        </Formik>
    )
}

const Login = () => {

    const { appUser } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (appUser) {
            navigate("/dashboard");
        }
    })

    return (
        <Stack minH={'100vh'} direction={{base: 'column', md: 'row'}}>
            <Flex p={8} flex={1} alignItems={'center'} justifyContent={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>

                    <Image
                        src={'https://user-images.githubusercontent.com/119408158/282424639-9d753b02-be1f-4818-9b68-54e6bba2e690.png'}
                        width={"150px"}
                        height={"190px"}
                        alt={"UPL logo"}
                        alignSelf={"center"}
                    />
                    <Heading fontSize={'2xl'} mb={15} alignSelf={"center"}>Sign in to your account</Heading>
                    <LoginForm/>
                    <Link color={"blue.500"} href={"/signup"}>
                        Dont have an account? Sign up now.
                    </Link>
                </Stack>
            </Flex>
            <Flex flex={1}>
                <Image
                    alt={'Login Image'}
                    objectFit={'cover'}
                    src={
                        'https://private-user-images.githubusercontent.com/119408158/290988300-94d2fb3f-66c0-4aeb-81c6-cfbd8592b9ff.jpg?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTEiLCJleHAiOjE3MDI3MjUxNTYsIm5iZiI6MTcwMjcyNDg1NiwicGF0aCI6Ii8xMTk0MDgxNTgvMjkwOTg4MzAwLTk0ZDJmYjNmLTY2YzAtNGFlYi04MWM2LWNmYmQ4NTkyYjlmZi5qcGc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBSVdOSllBWDRDU1ZFSDUzQSUyRjIwMjMxMjE2JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDIzMTIxNlQxMTA3MzZaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0wNTRjNjRlMjFjMDMxYjZiMDQ4N2JiYzRhNDA0YjFjMTc0OTUyZDMxZGE2YWQ5ODUzN2EyNjcwZGI4M2JmZmY4JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.MXhFu6U-HSmtK0Rq9unsAEQ1WN4fBG_2OyxPg_H9cas'
                    }
                />
            </Flex>
        </Stack>
    );
}

export default Login;