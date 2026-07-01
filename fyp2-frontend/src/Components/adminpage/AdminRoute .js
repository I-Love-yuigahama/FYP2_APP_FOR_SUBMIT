import { Navigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export default function AdminRoute({ children }) {
    const { isAuthenticated, role } = useAuth();

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;  // not logged in at all
    }

    if (role !== 'ADMIN') {
        return <Navigate to="/" replace />;       // logged in but not admin
    }

    return children;
}