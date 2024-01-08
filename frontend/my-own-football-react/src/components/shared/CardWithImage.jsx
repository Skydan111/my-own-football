import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    Tag,
    useColorModeValue,
    Button,
    AlertDialogOverlay,
    AlertDialogContent,
    AlertDialogHeader,
    AlertDialogBody,
    AlertDialog,
    AlertDialogFooter,
    useDisclosure,
} from '@chakra-ui/react';
import {deleteAppUser} from "../../services/client.js";
import {errorNotification, successNotification} from "../../services/notification.js";
import {useRef} from "react";
import UpdateAppUserDrawer from "./UpdateAppUserDrawer.jsx";

const getAvatarsSrc = team => {
    if (team === 'DYNAMO') {
        return '/src/images/logo/dynamo_logo.svg.png';
    } else if (team === 'SHAKHTAR') {
        return '/src/images/logo/schakhtar_logo.svg.png';
    } else if (team === 'ZORIA') {
        return '/src/images/logo/zorya_logo.svg.png';
    } else if (team === 'KOLOS') {
        return '/src/images/logo/kolos_logo.svg.png';
    } else if (team === 'OLEKSANDRIA') {
        return '/src/images/logo/oleksandriya_logo.svg.png';
    } else if (team === 'DNIPRO_1') {
        return '/src/images/logo/dnipro_logo.png';
    } else if (team === 'CHORNOMORETS') {
        return '/src/images/logo/chornomorets_logo.jpeg';
    } else if (team === 'RUKH') {
        return '/src/images/logo/rukh_logo_2019.png';
    } else if (team === 'OBOLON') {
        return '/src/images/logo/obolon_logo.png';
    } else if (team === 'VORSKLA') {
        return '/src/images/logo/vorskla_logo.svg.png';
    } else if (team === 'LNZ') {
        return '/src/images/logo/lnz_logo.png';
    } else if (team === 'VERES') {
        return '/src/images/logo/veres_logo.png';
    } else if (team === 'METALIST1925') {
        return '/src/images/logo/metalist_1925_logo.png';
    } else if (team === 'MINAJ') {
        return '/src/images/logo/mynai_logo.png';
    } else if (team === 'KRYVBAS') {
        return '/src/images/logo/kryvbas_logo.svg.png';
    } else if (team === 'POLISSIA') {
        return '/src/images/logo/polissya_logo.png';
    }
}

// eslint-disable-next-line react/prop-types
export default function CardWithImage({id, name, email, team,  fetchAppUsers}) {

    const {isOpen, onOpen, onClose} = useDisclosure()
    const cancelRef = useRef()

    return (
        <Center py={6}>
            <Box
                maxW={'300px'}
                minW={'300px'}
                w={'full'}
                m={2}
                p={2}
                bg={useColorModeValue('white', 'gray.800')}
                boxShadow={'lg'}
                rounded={'md'}
                overflow={'hidden'}>
                <Image
                    h={'120px'}
                    w={'full'}
                    src={
                        'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
                    }
                    objectFit={'cover'}
                />
                <Flex justify={'center'} mt={-12}>
                    <Avatar
                        size={'xl'}
                        name={name}
                        src={getAvatarsSrc(team)}
                        alt={'Author'}
                        css={{
                            border: '2px solid white',
                        }}
                    />
                </Flex>

                <Box p={6}>
                    <Stack spacing={2} align={'center'} mb={5}>
                        <Tag borderRadius={"full"}>{id}</Tag>
                        <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                            {name}
                        </Heading>
                        <Text color={'gray.500'}>{email}</Text>
                    </Stack>
                </Box>
                <Stack direction={'row'} justify={'center'} spacing={6} p={4}>
                    <Stack>
                        <UpdateAppUserDrawer
                            initialValues={{name, email}}
                            appUserId={id}
                            fetchAppUsers={fetchAppUsers}
                        />
                    </Stack>
                    <Stack>
                        <Button
                            bg={'red.400'}
                            color={'white'}
                            rounded={'full'}
                            _hover={{
                                transform: 'translateY(-2px)',
                                boxShadow: 'lg'
                            }}
                            _focus={{
                                bg: 'grey.500'
                            }}
                            onClick={onOpen}
                        >
                            Delete
                        </Button>
                        <AlertDialog
                            isOpen={isOpen}
                            leastDestructiveRef={cancelRef}
                            onClose={onClose}
                        >
                            <AlertDialogOverlay>
                                <AlertDialogContent>
                                    <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                                        Delete User
                                    </AlertDialogHeader>

                                    <AlertDialogBody>
                                        Are you sure you want to delete {name}? You can't undo this action afterwards.
                                    </AlertDialogBody>

                                    <AlertDialogFooter>
                                        <Button ref={cancelRef} onClick={onClose}>
                                            Cancel
                                        </Button>
                                        <Button colorScheme='red' onClick={() => {
                                            deleteAppUser(id).then(() => {
                                                successNotification(
                                                    'User deleted',
                                                    `${name} was successfully deleted`
                                                )
                                                fetchAppUsers();
                                            }).catch(err => {
                                                errorNotification(
                                                    err.code,
                                                    err.response.data.message
                                                )
                                            }).finally(() => {
                                                onClose()
                                            })
                                        }} ml={3}>
                                            Delete
                                        </Button>
                                    </AlertDialogFooter>
                                </AlertDialogContent>
                            </AlertDialogOverlay>
                        </AlertDialog>
                    </Stack>
                </Stack>
            </Box>
        </Center>
    );
}