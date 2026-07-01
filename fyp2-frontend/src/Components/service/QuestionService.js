import apiClient from "../Config/apiClient";
import { API_CONFIG } from "../Config/Base_url";

class QuestionService {

    static BASE_URL = API_CONFIG.BASE_URL;

    // POST /admin/add-question (multipart/form-data)
    static async createQuestion(token, formData) {
        try {
            const response = await apiClient.post(
                `${this.BASE_URL}/admin/add-question`,
                formData,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        // don't set Content-Type — axios sets it automatically for FormData
                    }
                }
            )
            return response.data
        } catch (err) {
            throw err
        }
    }

    // GET /admin/get-all-question
    static async getALlQuestion(token) {
        try {
            const response = await apiClient.get(
                `${this.BASE_URL}/admin/get-all-question`,
                {
                    headers: { Authorization: `Bearer ${token}` }
                }
            )
            return response.data
        } catch (err) {
            throw err
        }
    }

    // GET /admin/get-question/{questionId}
    static async getQuestionById(token, questionId) {
        try {
            const response = await apiClient.get(
                `${this.BASE_URL}/admin/get-question/${questionId}`,
                {
                    headers: { Authorization: `Bearer ${token}` }
                }
            )
            return response.data
        } catch (err) {
            throw err
        }
    }

    // DELETE /admin/delete-question/{questionId}
    static async deleteQuestion(token, questionId) {
        try {
            const response = await apiClient.delete(
                `${this.BASE_URL}/admin/delete-question/${questionId}`,
                {
                    headers: { Authorization: `Bearer ${token}` }
                }
            )
            return response.data
        } catch (err) {
            throw err
        }
    }

    // POST /admin/update-question/{questionId} (multipart/form-data)
    static async updateQuestion(token, questionId, formData) {
        try {
            const response = await apiClient.post(
                `${this.BASE_URL}/admin/update-question/${questionId}`,
                formData,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                        // don't set Content-Type — axios sets it automatically for FormData
                    }
                }
            )
            return response.data
        } catch (err) {
            throw err
        }
    }

}

export default QuestionService;