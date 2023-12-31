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
    Select, Stack
} from "@chakra-ui/react";
import { saveAppUser } from "../../services/client.js";
import { successNotification, errorNotification } from "../../services/notification.js";


// eslint-disable-next-line react/prop-types
const MyTextInput = ({ label, ...props }) => {

    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon />
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

// eslint-disable-next-line react/prop-types
const MySelect = ({ label, ...props }) => {
    const [field, meta] = useField(props);

    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Select {...field} {...props} size='lg'/>
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon />
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

const CreateAppUserForm = ({ onSuccess }) => {
    return (
        <>
            <Formik
                initialValues={{
                    name: '',
                    email: '',
                    password: '',
                    age: 0,
                    team: '',
                }}
                validationSchema={Yup.object({
                    name: Yup.string()
                        .max(15, 'Must be 15 characters or less')
                        .required('Required'),
                    email: Yup.string()
                        .email('Invalid email address')
                        .required('Required'),
                    password: Yup.string()
                        .min(8, 'Must be 8 characters or more')
                        .required('Required'),
                    team: Yup.string()
                        .oneOf(
                            [
                                'SHAKHTAR', 'DYNAMO', 'ZORIA', 'KOLOS',
                                'OLEKSANDRIA', 'DNIPRO_1', 'CHORNOMORETS', 'RUKH',
                                'OBOLON', 'VORSKLA', 'LNZ', 'VERES',
                                'METALIST1925', 'MINAJ', 'KRYVBAS', 'POLISSIA'
                            ],
                            'Invalid name of team'
                        )
                        .required('Required'),
                })}
                onSubmit={(appUser, { setSubmitting }) => {
                    setSubmitting(true);
                    saveAppUser(appUser)
                        .then(res => {
                            successNotification(
                                "User was created",
                                `${appUser.name} was successfully created`
                            )
                            onSuccess(res.headers["authorization"]);
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
                {({ isValid, isSubmitting }) => (
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

                            <MyTextInput
                                label="Password"
                                name="password"
                                type="password"
                            />

                            <MySelect label="Team" name="team">
                                <option value="">Select your team</option>
                                <option value="SHAKHTAR">Shakhtar</option>
                                <option value="DYNAMO">Dynamo</option>
                                <option value="ZORIA">Zoria</option>
                                <option value="KOLOS">Kolos</option>
                                <option value="OLEKSANDRIA">Oleksandria</option>
                                <option value="DNIPRO_1">Dnipro 1</option>
                                <option value="CHORNOMORETS">Chornomorets</option>
                                <option value="RUKH">Rukh</option>
                                <option value="OBOLON">Obolon</option>
                                <option value="VORSKLA">Vorskla</option>
                                <option value="LNZ">LNZ</option>
                                <option value="VERES">Veres</option>
                                <option value="METALIST1925">Metalist 1925</option>
                                <option value="MINAJ">Minaj</option>
                                <option value="KRYVBAS">Kryvbas</option>
                                <option value="POLISSIA">Polissia</option>
                            </MySelect>

                            <Button disabled={ !isValid || !isSubmitting } type="submit">Submit</Button>
                        </Stack>
                    </Form>
                )}
            </Formik>
        </>
    );
};

export default CreateAppUserForm;