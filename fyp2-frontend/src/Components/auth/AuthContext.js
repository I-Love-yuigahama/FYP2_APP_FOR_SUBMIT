import { createContext, useContext, useState, useEffect } from 'react'

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [token, setToken] = useState(sessionStorage.getItem('token'))
    const [refreshToken, setRefreshToken] = useState(sessionStorage.getItem('refreshToken'))
    const [role, setRole] = useState(sessionStorage.getItem('role'))
    const [email, setEmail] = useState(sessionStorage.getItem('email'))

    const login = (token, refreshToken, role, email) => {
        sessionStorage.setItem('token', token)
        if(refreshToken) sessionStorage.setItem('refreshToken', refreshToken)
        sessionStorage.setItem('role', role)
        sessionStorage.setItem('email', email)
        setToken(token)
        if(refreshToken) setRefreshToken(refreshToken)
        setRole(role)
        setEmail(email)
    }

    const logout = () => {
        sessionStorage.removeItem('token')
        sessionStorage.removeItem('refreshToken')
        sessionStorage.removeItem('role')
        sessionStorage.removeItem('email')
        setToken(null)
        setRefreshToken(null)
        setRole(null)
        setEmail(null)
    }

    // Listen for custom events dispatched by apiClient
    useEffect(() => {
        const handleTokenUpdate = (e) => {
            setToken(e.detail.token);
            setRefreshToken(e.detail.refreshToken);
        };
        const handleForceLogout = () => {
            logout();
        };

        window.addEventListener('tokenUpdated', handleTokenUpdate);
        window.addEventListener('forceLogout', handleForceLogout);

        return () => {
            window.removeEventListener('tokenUpdated', handleTokenUpdate);
            window.removeEventListener('forceLogout', handleForceLogout);
        };
    }, []);

    return (
        <AuthContext.Provider value={{ token, refreshToken, role, email, login, logout, isAuthenticated: !!token }}>
            {children}
        </AuthContext.Provider>
    )
}

export function useAuth() {
    return useContext(AuthContext)
}