import {
    Formik,
    Form,
    useField
} from 'formik';
import * as Yup from 'yup';
import {
    Alert,
    AlertIcon,
    Box,
    Button,
    FormLabel,
    Input,
    Stack
} from "@chakra-ui/react";
import {updateAppUser} from "../../services/client.js";
import {successNotification, errorNotification} from "../../services/notification.js";

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

// eslint-disable-next-line react/prop-types
const UpdateAppUserForm = ({ fetchAppUsers, initialValues, appUserId }) => {
    return (
        <>
            <Formik
                initialValues={ initialValues }
                validationSchema={Yup.object({
                    name: Yup.string()
                        .max(15, 'Must be 15 characters or less')
                        .required('Required'),
                    email: Yup.string()
                        .email('Invalid email address')
                        .required('Required')
                })}
                onSubmit={(updatedAppUser, {setSubmitting}) => {
                    setSubmitting(true);
                    updateAppUser(appUserId, updatedAppUser)
                        .then(() => {
                            successNotification(
                                "User was updated",
                                `${updatedAppUser.name} was successfully updated`
                            )
                            fetchAppUsers();
                        }).catch(err => {
                        errorNotification(
                            "What are you doing?",
                            err.response.data.message
                        )
                    }).finally(() => {
                        setSubmitting(false);
                    })
                }}
            >
                {({isValid, isSubmitting, dirty}) => (
                    <Form>
                        <Stack spacing={"24px"}>
                            <MyTextInput
                                label="Name"
                                name="name"
                                type="text"
                                placeholder="Andriy Pavelko"
                            />

                            <MyTextInput
                                label="Email Address"
                                name="email"
                                type="email"
                                placeholder="uaf@mail.com"
                            />

                            <Button disabled={!(isValid && dirty) || !isSubmitting} type="submit">Submit</Button>
                        </Stack>
                    </Form>
                )}
            </Formik>
        </>
    );
};

export default UpdateAppUserForm;