import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";
import CreateAppUserForm from "./CreateAppUserForm.jsx";
import UpdateAppUserForm from "./UpdateAppUserForm.jsx";

const CloseIcon = () => "x";

const UpdateAppUserDrawer = ({ fetchAppUsers, initialValues, appUserId }) => {

    const { isOpen, onOpen, onClose } = useDisclosure()

    return <>
        <Button
            bg={'gray'}
            color={'white'}
            rounded={'full'}
            _hover={{
                transform: 'translateY(-2px)',
                boxShadow: 'lg'
            }}
            onClick={onOpen}
        >
            Update
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose}>
            <DrawerOverlay />
            <DrawerContent>
                <DrawerCloseButton />
                <DrawerHeader>Update your account</DrawerHeader>

                <DrawerBody>
                    <UpdateAppUserForm
                        fetchAppUsers={ fetchAppUsers }
                        initialValues={ initialValues }
                        appUserId={ appUserId }
                    />
                </DrawerBody>

                <DrawerFooter>
                    <Button
                        type={<CloseIcon />}
                        colorScheme={"red"}
                        onClick={onClose}
                    >
                        Close
                    </Button>
                </DrawerFooter>
            </DrawerContent>
        </Drawer>
    </>
}

export default UpdateAppUserDrawer;