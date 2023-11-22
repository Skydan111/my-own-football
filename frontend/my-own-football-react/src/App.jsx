import {
    Wrap,
    WrapItem,
    Spinner,
    Text
} from '@chakra-ui/react';
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {useEffect, useState} from "react";
import {getAppUsers} from "./services/client.js";
import CardWithImage from "./components/CardWithImage.jsx";
import {errorNotification} from "./services/notification.js";

function App() {

    const [appUsers, setAppUsers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setError] = useState("");

    const fetchAppUsers = () => {
        setLoading(true);
        getAppUsers().then(res => {
            setAppUsers(res.data);
        }).catch(err => {
            setError(err.response.data.message)
            errorNotification(
                "What are you doing?",
                err.response.data.message
            )
        }).finally(() => {
            setLoading(false);
        })
    }

    useEffect(() => {
        fetchAppUsers();
    }, []);

    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }

    if (err) {
        return (
            <SidebarWithHeader>
                <Text>Oops there was an error</Text>
            </SidebarWithHeader>
        )
    }

    if (appUsers.length <= 0) {
        return (
            <SidebarWithHeader>
                <Text>No players found</Text>
            </SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            <Wrap justify={"center"} spacing={"30px"}>
                { appUsers.map((appUser, index) => (
                    <WrapItem key={index}>
                        <CardWithImage
                            {...appUser}
                            fetchAppUsers={fetchAppUsers}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App