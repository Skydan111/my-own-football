import axios from "axios";

export const getAppUsers = async () => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/users`)
    } catch (e) {
        throw e;
    }
}

export const saveAppUser = async (appUser) => {
    try {
        return await axios.post(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/users`,
            appUser
        )
    } catch (e) {
        throw e;
    }
}

export const updateAppUser = async (id, update) => {
    try {
        return await axios.put(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/users/${id}`,
            update
        )
    } catch (e) {
        throw e;
    }
}

export const deleteAppUser = async (id) => {
    try {
        return await axios.delete(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/users/${id}`
        )
    } catch (e) {
        throw e;
    }
}