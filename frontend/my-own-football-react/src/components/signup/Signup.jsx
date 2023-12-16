import {useAuth} from "../context/AuthContext.jsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {Flex, Heading, Image, Link, Stack} from "@chakra-ui/react";
import CreateAppUserForm from "../shared/CreateAppUserForm.jsx";

const Signup = () => {
    const { appUser, setAppUserFromToken } = useAuth();
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
                    <Heading fontSize={'2xl'} mb={15} alignSelf={"center"}>Register for an account</Heading>
                    <CreateAppUserForm onSuccess={(token) => {
                        localStorage.setItem("access_token", token);
                        setAppUserFromToken();
                        navigate("/dashboard");
                    }}/>
                    <Link color={"blue.500"} href={"/"}>
                        Have an account? Login now.
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

export default Signup;