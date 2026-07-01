import React, { useState } from 'react';
import './RecoveryPassword_NewPassword.css';
import { useNavigate, useLocation } from 'react-router-dom';
import loginDeskImg from '../login_desk.jpg';
import UserService from '../../../service/UserService';
import { useAuth } from '../../AuthContext';

export default function RecoveryPassword_NewPassword() {
    const navigate = useNavigate();
    const { login } = useAuth();

    // Receive the email passed from the OTP page
    const { state } = useLocation();
    const email = state?.email;

    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const [showNewPassword, setShowNewPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    // Guard: if someone lands here without a verified email, send them back
    if (!email) {
        navigate('/recoveryPasswordEmail');
        return null;
    }

    const validate = (password) => {
        if (password.length < 5) {
            setError("Password must be at least 5 characters");
            return false;
        }
        if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
            setError("Password must contain at least one special character");
            return false;
        }
        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccessMessage('');

        if (newPassword !== confirmPassword) {
            setError("Passwords do not match");
            return;
        }

        if (!validate(newPassword)) return;

        setLoading(true);

        try {
            // Step 1: Change the password
            const changeResponse = await UserService.changePassword(email, newPassword);

            if (changeResponse.statusCode !== 200) {
                setError(changeResponse.message || "Failed to update password.");
                return;
            }

            // Step 2: Auto-login with the new password
            const loginResponse = await UserService.login({ email, password: newPassword });

            if (loginResponse.userDTO?.token) {
                setSuccessMessage("Password reset successful! Logging you in...");
                setTimeout(() => {
                    login(loginResponse.userDTO.token, loginResponse.userDTO.refreshtoken, loginResponse.userDTO.role, email);
                    navigate('/');
                }, 1500);
            } else {
                // Password changed but auto-login failed — fallback to login page
                setSuccessMessage("Password reset successful! Redirecting to login...");
                setTimeout(() => navigate('/login'), 1500);
            }

        } catch (err) {
            console.error("Password Reset Failed:", err);
            setError("Something went wrong, please try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="newpass-page-container">
            <div className="newpass-content-wrapper">

                <div className="newpass-image-section">
                    <img src={loginDeskImg} alt="Study Desk Setup" className="newpass-proto-img" />
                </div>

                <div className="newpass-form-section">
                    <form onSubmit={handleSubmit} className="newpass-proto-form">
                        <h1 className="newpass-welcome-title">New Password</h1>

                        {error && <p className="newpass-error-alert">{error}</p>}
                        {successMessage && <p className="newpass-success-alert">{successMessage}</p>}

                        <div className={`newpass-input-group ${newPassword ? 'has-value' : ''}`}>
                            <div className="newpass-password-inner-wrapper">
                                <input
                                    type={showNewPassword ? "text" : "password"}
                                    className="newpass-input-field"
                                    value={newPassword}
                                    onChange={(e) => setNewPassword(e.target.value)}
                                    required
                                />
                                <label className="newpass-label">New Password</label>
                                <span className="newpass-eye-icon" onClick={() => setShowNewPassword(!showNewPassword)}>
                                    {showNewPassword ? '🙈' : '👁️'}
                                </span>
                            </div>
                        </div>

                        <div className={`newpass-input-group ${confirmPassword ? 'has-value' : ''}`}>
                            <div className="newpass-password-inner-wrapper">
                                <input
                                    type={showConfirmPassword ? "text" : "password"}
                                    className="newpass-input-field"
                                    value={confirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                    required
                                />
                                <label className="newpass-label">Confirm Password</label>
                                <span className="newpass-eye-icon" onClick={() => setShowConfirmPassword(!showConfirmPassword)}>
                                    {showConfirmPassword ? '🙈' : '👁️'}
                                </span>
                            </div>
                        </div>

                        <button className="newpass-submit-btn" type="submit" disabled={loading}>
                            {loading ? "Updating..." : "Confirm"}
                        </button>
                    </form>
                </div>

            </div>
        </div>
    );
}