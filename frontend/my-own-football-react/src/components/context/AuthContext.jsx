import {
    createContext,
    useContext,
    useEffect,
    useState
} from "react";
import {login as performLogin} from "../../services/client.js";
import {jwtDecode} from "jwt-decode";

const AuthContext = createContext({});

// eslint-disable-next-line react/prop-types
const AuthProvider = ({children}) => {

    const [appUser, setAppUser] = useState(null);

    const setAppUserFromToken = () => {
        let token = localStorage.getItem("access_token");
        if (token) {
            token = jwtDecode(token);
            setAppUser({
                username: token.sub,
                roles: token.scopes
            });
        }
    }

    useEffect(() => {
        setAppUserFromToken();
    }, []);

    const login = async (usernameAndPassword) => {
        return new Promise((resolve, reject) => {
            performLogin(usernameAndPassword).then(res => {
                const jwtToken = res.headers["authorization"];
                localStorage.setItem("access_token", jwtToken);

                const decodedToken = jwtDecode(jwtToken);

                setAppUser({
                    username: decodedToken.sub,
                    roles: decodedToken.scopes
                });
                resolve(res);
            }).catch(err => {
                reject(err);
            })
        })
    }

    const logOut = () => {
        localStorage.removeItem("access_token");
        setAppUser(null);
    }

    const isAppUserAuthenticated = () => {
        const token = localStorage.getItem("access_token");
        if (!token) {
            return false;
        }

        const {exp: expiration} = jwtDecode(token);
        if (Date.now() > expiration * 1000) {
            logOut();
            return false;
        }

        return true;
    }

    return (
        <AuthContext.Provider value={{
            appUser,
            login,
            logOut,
            isAppUserAuthenticated,
            setAppUserFromToken
        }}>
            {children}
        </AuthContext.Provider>
    )

}

export const useAuth = () => useContext(AuthContext);

export default AuthProvider;