import React, { useState } from 'react';
import './SignUp.css';
import { useNavigate } from 'react-router-dom';
import UserService from '../../service/UserService';
import { useAuth } from '../AuthContext';
import loginDeskImg from './login_desk.jpg'; // Adjust path if needed

export default function SignUp() {
    const navigate = useNavigate();
    const { login } = useAuth();

    const [Email, setEmail] = useState('');
    const [Password, setPassword] = useState('');
    const [ConfirmPassword, setConfirmPassword] = useState('');
    const ROLE = 'USER';
    
    const [error, setError] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const validate = (e) => {
        if (e.length < 5) { setError("Password must be at least 5 characters"); return false; }
        if (!/[!@#$%^&*(),.?":{}|<>]/.test(e)) { setError("Password must contain at least one special character"); return false; }
        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // 1. Validate if passwords match
        if (Password !== ConfirmPassword) {
            setError("Passwords do not match");
            return;
        }

        // 2. Validate strength rules
        if (!validate(Password)) return;

        try {
            const userData = await UserService.register({ email: Email, password: Password, role: ROLE });

            if (userData.statusCode === 200) {
                const loginData = await UserService.login({ email: Email, password: Password });

                if (loginData.userDTO?.token) {
                    login(loginData.userDTO.token, loginData.userDTO.refreshtoken, loginData.userDTO.role, Email);
                    navigate('/');
                } else {
                    setError(loginData.message);
                }
            } else {
                setError(userData.message);
            }
        } catch (error) {
            console.error("Register Failed:", error);
            setError("Something went wrong, please try again");
        }
    };

    return (
        <div className="signup-page-container">
            <div className="signup-content-wrapper">
                
                {/* Left Side: Floating Desk Image Section */}
                <div className="signup-image-section">
                    <img src={loginDeskImg} alt="Study Desk Setup" className="signup-proto-img" />
                </div>

                {/* Right Side: Interactive Sign Up Form Section */}
                <div className="signup-form-section">
                    <form onSubmit={handleSubmit} className="signup-proto-form">
                        <h1 className="signup-welcome-title">Welcome</h1>

                        {error && <p className="signup-error-alert">{error}</p>}

                        {/* Email Input Group */}
                        <div className={`signup-input-group ${Email ? 'has-value' : ''}`}>
                            <input
                                type="email"
                                className="signup-input-field"
                                value={Email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                            <label className="signup-label">Email</label>
                        </div>

                        {/* Password Input Group */}
                        <div className={`signup-input-group ${Password ? 'has-value' : ''}`}>
                            <div className="signup-password-inner-wrapper">
                                <input
                                    type={showPassword ? "text" : "password"}
                                    className="signup-input-field"
                                    value={Password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                />
                                <label className="signup-label">Password</label>
                                <span className="signup-eye-icon" onClick={() => setShowPassword(!showPassword)}>
                                    {showPassword ? '🙈' : '👁️'}
                                </span>
                            </div>
                        </div>

                        {/* Confirm Password Input Group */}
                        <div className={`signup-input-group ${ConfirmPassword ? 'has-value' : ''}`}>
                            <div className="signup-password-inner-wrapper">
                                <input
                                    type={showConfirmPassword ? "text" : "password"}
                                    className="signup-input-field"
                                    value={ConfirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                    required
                                />
                                <label className="signup-label">Confirm Password</label>
                                <span className="signup-eye-icon" onClick={() => setShowConfirmPassword(!showConfirmPassword)}>
                                    {showConfirmPassword ? '🙈' : '👁️'}
                                </span>
                            </div>
                        </div>

                        <p className="signup-login-link" onClick={() => navigate(`/login`)}>
                            Got an account? Login here
                        </p>

                        {/* Styled Pill Submit Button */}
                        <button className="signup-submit-btn" type='submit'>Register</button>
                    </form>
                </div>

            </div>
        </div>
    );
}