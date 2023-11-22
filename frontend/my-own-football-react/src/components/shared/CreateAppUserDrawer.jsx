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

const CloseIcon = () => "x";

const CreateAppUserDrawer = ({ fetchAppUsers }) => {

    const { isOpen, onOpen, onClose } = useDisclosure()

    return <>
        <Drawer isOpen={isOpen} onClose={onClose}>
            <DrawerOverlay />
            <DrawerContent>
                <DrawerCloseButton />
                <DrawerHeader>Create your account</DrawerHeader>

                <DrawerBody>
                    <CreateAppUserForm
                        fetchAppUsers={fetchAppUsers}
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

export default CreateAppUserDrawer;