import React, { useEffect, useState } from 'react';
import './profile.css';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../auth/AuthContext';
import UserService from '../../service/UserService';

export default function Profile() {
    const navigate = useNavigate();
    const { logout, token } = useAuth();

    const [userEmail, setUserEmail] = useState('')
    const [showEdit, setShowEdit] = useState(false)
    const [formData, setFormData] = useState({ email: '', password: '' })
    const [showPassword, setShowPassword] = useState(false)
    const [error, setError] = useState('')
    const [success, setSuccess] = useState('')

    useEffect(() => {
        if (!token) return
        const fetchProfile = async () => {
            try {
                const response = await UserService.getYourProfile(token)
                console.log("Full response:", response)
                console.log("userDTO:", response.userDTO)
                const user = response.userDTO
                setUserEmail(user.email)
                setFormData({ email: user.email, password: '' })
            } catch (err) {
                setError('Failed to load profile')
            }
        }
        fetchProfile()
    }, [token])

    const handleLogout = () => {
        logout()
        navigate('/login')
    }

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value })
    }

    const handleUpdate = async (e) => {
        e.preventDefault()
        setError('')
        setSuccess('')
        try {
            await UserService.updateMyProfile(token, formData)
            setSuccess('Profile updated successfully')
            setUserEmail(formData.email)
            setShowEdit(false)
        } catch (err) {
            setError('Failed to update profile')
        }
    }

    return (
        <main className="profile-page-wrapper">
            <div className="profile-glass-card">

                <header className="profile-header">
                    <h1 className="profile-title">Account Details</h1>
                    <p className="profile-subtitle">Manage your login credentials</p>
                </header>

                {error && <p className="profile-error-alert">{error}</p>}
                {success && <p className="profile-success-alert">{success}</p>}

                {!showEdit ? (
                    <>
                        <section className="profile-details">
                            <div className="detail-item">
                                <div className="detail-text-group">
                                    <span className="detail-label">Email Address</span>
                                    <span className="detail-value">{userEmail}</span>
                                </div>
                            </div>
                            <div className="detail-item">
                                <div className="detail-text-group">
                                    <span className="detail-label">Password</span>
                                    <span className="detail-value">••••••••••••</span>
                                </div>
                            </div>
                        </section>

                        <section className="profile-btn-group">
                            <button className="profile-submit-btn" onClick={() => setShowEdit(true)}>
                                Edit Profile
                            </button>
                            <button className="profile-submit-btn logout-btn" onClick={handleLogout}>
                                Sign Out
                            </button>
                        </section>
                    </>
                ) : (
                    <form className="profile-edit-form" onSubmit={handleUpdate}>
                        
                        <div className={`profile-input-group ${formData.email ? 'has-value' : ''}`}>
                            <input
                                type="email"
                                name="email"
                                className="profile-input-field"
                                value={formData.email}
                                onChange={handleChange}
                                required
                            />
                            <label className="profile-label">Email</label>
                        </div>

                        <div className={`profile-input-group ${formData.password ? 'has-value' : ''}`}>
                            <div className="profile-password-inner-wrapper">
                                <input
                                    type={showPassword ? 'text' : 'password'}
                                    name="password"
                                    className="profile-input-field"
                                    value={formData.password}
                                    onChange={handleChange}
                                />
                                <label className="profile-label">New Password (Optional)</label>
                                <span className="profile-eye-icon" onClick={() => setShowPassword(!showPassword)}>
                                    {showPassword ? '🙈' : '👁️'}
                                </span>
                            </div>
                        </div>

                        <div className="profile-btn-group">
                            <button type="button" className="profile-cancel-btn" onClick={() => setShowEdit(false)}>
                                Cancel
                            </button>
                            <button type="submit" className="profile-submit-btn">
                                Save
                            </button>
                        </div>
                    </form>
                )}

            </div>
        </main>
    );
}