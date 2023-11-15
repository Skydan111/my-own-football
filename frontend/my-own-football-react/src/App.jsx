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

function App() {

    const [appUsers, setAppUsers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        getAppUsers().then(res => {
            setAppUsers(res.data);
        }).catch(err => {
            console.log(err);
        }).finally(() => {
            setLoading(false);
        })
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
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App