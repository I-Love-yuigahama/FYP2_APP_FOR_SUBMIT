import axios from 'axios';
import { API_CONFIG } from './Base_url';

const apiClient = axios.create({
    baseURL: API_CONFIG.BASE_URL
});

// ← Always inject the latest token from sessionStorage
apiClient.interceptors.request.use((config) => {
    const token = sessionStorage.getItem('token');
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
});

apiClient.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry && !originalRequest.url.includes('/auth/refresh')) {
            originalRequest._retry = true;
            
            const refreshToken = sessionStorage.getItem('refreshToken');
            
            if (refreshToken) {
                try {
                    const response = await axios.post(`${API_CONFIG.BASE_URL}/auth/refresh`, {
                        refreshtoken: refreshToken
                    });

                    console.log("Refresh response:", response.data);

                    const newAccessToken = response.data.userDTO?.token;
                    const newRefreshToken = response.data.userDTO?.refreshtoken;

                    if (newAccessToken) {
                        sessionStorage.setItem('token', newAccessToken);
                        if (newRefreshToken) {
                            sessionStorage.setItem('refreshToken', newRefreshToken);
                        }

                        window.dispatchEvent(new CustomEvent('tokenUpdated', {
                            detail: {
                                token: newAccessToken,
                                refreshToken: newRefreshToken || refreshToken
                            }
                        }));

                        originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
                        return apiClient(originalRequest);
                    }
                } catch (refreshError) {
                    console.error("Token refresh failed", refreshError);
                    window.dispatchEvent(new Event('forceLogout'));
                }
            } else {
                window.dispatchEvent(new Event('forceLogout'));
            }
        }

        return Promise.reject(error);
    }
);

export default apiClient;