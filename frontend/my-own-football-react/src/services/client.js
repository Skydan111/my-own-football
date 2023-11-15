import axios from "axios";

export const getAppUsers = async () => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/users`)
    } catch (e) {
        throw e;
    }
}