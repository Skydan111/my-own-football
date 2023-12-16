import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../context/AuthContext.jsx";

const ProtectedRoute = ({children}) => {



    const { isAppUserAuthenticated } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (!isAppUserAuthenticated()) {
            navigate("/");
        }
    })

    return isAppUserAuthenticated() ? children : "";
}

export default ProtectedRoute;