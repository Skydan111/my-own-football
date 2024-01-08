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
                        '/src/images/login_image.jpg'
                    }
                />
            </Flex>
        </Stack>
    );
}

export default Signup;