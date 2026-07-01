import apiClient from "../Config/apiClient";
import { API_CONFIG } from "../Config/Base_url"

class UserService
{
    static BASE_URL = API_CONFIG.BASE_URL;

    
    static async register(formData)
    {
        console.log(process.env.REACT_APP_TEST);       // should print "hello"
        console.log(process.env.REACT_APP_API_URL);
        console.log("API Base URL:", this.BASE_URL); // Debugging line to check the BASE_URL value
        try{
            const response = await apiClient.post(`${this.BASE_URL}/auth/register`, formData)
            return response.data;
        }
        catch(err)
        {
                throw err;
        }

    }

    static async login(formData)
    {
        try{
            const response = await apiClient.post(`${this.BASE_URL}/auth/login`, formData)
            return response.data;
        }
        catch(err)
        {
            throw err;
        }

    }

    static async getAllUsers(token)
    {
        try{
            const response = await apiClient.get(`${this.BASE_URL}/admin/get-all-user`, {headers:{Authorization: `Bearer ${token}`}})
            return response.data;
        }
        catch(err)
        {
            throw err;
        }
    }

    static async getYourProfile(token)
    {
        try{
                 const response = await apiClient.get(`${this.BASE_URL}/adminuser/get-profile`,{

                headers:{Authorization: `Bearer ${token}`}

            })
            return response.data;
        }catch(err)
        {
            throw err;
        }
    }

    static  async getUserById(userId,token)
    {

        try{
            const response = await apiClient.get(`${this.BASE_URL}/admin/get-user/${userId}`,{

                headers:{Authorization: `Bearer ${token}`}

            })
            return response.data;
        }catch(err)
        {
            throw err;
        }

    }

    static async deleteUser(userId,token)
    {

        try{
            const response = await apiClient.delete(`${this.BASE_URL}/admin/delete-user/${userId}`,{

                headers:{Authorization: `Bearer ${token}`}

            })
            return response.data;
        }catch(err)
        {
            throw err;
        }

    }

    static async updateUser(userId, userData,token)
    {

        try{
            const response = await apiClient.put(`${this.BASE_URL}/admin/update-user/${userId}`,userData,{

                headers:{Authorization: `Bearer ${token}`}

            })
            return response.data;
        }catch(err)
        {
            throw err;
        }
    }

    // Add these 3 methods inside the UserService class

    static async sendRecoveryEmail(email) {
        try {
            const response = await apiClient.post(`${this.BASE_URL}/forgotPassword/verifyMail/${email}`);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async verifyOtp(otp, email) {
        try {
            const response = await apiClient.post(`${this.BASE_URL}/forgotPassword/verifyOTP/${otp}/${email}`);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async changePassword(email, newPassword) {
        try {
            const response = await apiClient.post(`${this.BASE_URL}/forgotPassword/changePassword`, {
                email,
                password: newPassword,
            });
            return response.data;
        } catch (err) {
            throw err;
        }
    }


    static async createUser(token, formData) {
        try {
            console.log("Creating user with data:", formData); // Debugging line to check formData
            const response = await apiClient.post(`${this.BASE_URL}/auth/register`, formData, {
                headers: { Authorization: `Bearer ${token}` }
            })
            return response.data;
        } catch (err) {
            throw err;
        }
    }   

    static async updateMyProfile(token, formData) {
    try {
        const response = await apiClient.put(`${this.BASE_URL}/user/update-profile`, formData, {
            headers: { Authorization: `Bearer ${token}` }
        })
        return response.data;
    } catch (err) {
        throw err;
    }
}


    //Authenthication CHECKER

    static logout()
    {
        sessionStorage.removeItem('token') 
        sessionStorage.removeItem('role') 
    }

    static isAuthenticated()
    {
        const token = sessionStorage.getItem('token')
        return !!token
        // The double exclamation mark !! converts a value to a boolean.
    }

    static isUser()
    {
        const role = sessionStorage.getItem('role') 
        return role === 'USER';
    }

    static isAdmin()
    {
        const role = sessionStorage.getItem('role') 
        return role === 'ADMIN';
    }

    static adminOnly()
    {
        return this.isAuthenticated() && this.isAdmin()
    }




}

export default UserService;