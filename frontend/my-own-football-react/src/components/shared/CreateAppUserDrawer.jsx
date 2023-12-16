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

// eslint-disable-next-line react/prop-types
const CreateAppUserDrawer = ({ fetchAppUsers }) => {

    const { isOpen, onClose } = useDisclosure()

    return <>
        <Drawer isOpen={isOpen} onClose={onClose}>
            <DrawerOverlay />
            <DrawerContent>
                <DrawerCloseButton />
                <DrawerHeader>Create your account</DrawerHeader>

                <DrawerBody>
                    <CreateAppUserForm
                        onSuccess={fetchAppUsers}
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