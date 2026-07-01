import React, { useState } from 'react';
import './Login.css';
import UserService from '../../service/UserService';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';
// Import your desk background image here (adjust path if needed)
import loginDeskImg from './login_desk.jpg'; 

export default function Login() {
    const navigate = useNavigate();
    const { login } = useAuth();

    const [Email, setEmail] = useState('');
    const [Password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [showPassword, setShowPassword] = useState(false);

    const validate = (e) => {
        if (e.length < 5) { setError("Password must be at least 5 characters"); return false; }
        if (!/[!@#$%^&*(),.?":{}|<>;:]/.test(e)) { setError("Password must contain at least one special character"); return false; }
        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (Password && !validate(Password)) return;

        try {
            const userData = await UserService.login({ email: Email, password: Password });
            console.log(userData);

            if (userData.userDTO?.token) {
                login(userData.userDTO.token, userData.userDTO.refreshtoken, userData.userDTO.role, Email);

                if (userData.userDTO.role === "USER") {
                    navigate('/');
                } else if (userData.userDTO.role === "ADMIN") {
                    navigate('/userList');
                }
            } else {
                setError(userData.error || userData.message || "Invalid email or password");
            }
        } catch (error) {
            console.error("Login Failed:", error);
            setError("Something went wrong, please try again");
        }
    };

     const handleForgetPassword = () => {
    navigate(`/recoveryPasswordEmail`);
  }

    return (
        <div className="login-page-container">
            <div className="login-content-wrapper">
                
                {/* Left Side: Desktop Image Section */}
                <div className="login-image-section">
                    <img src={loginDeskImg} alt="Study Desk Setup" className="login-proto-img" />
                </div>

                {/* Right Side: Interactive Login Form Section */}
                <div className="login-form-section">
                    <form onSubmit={handleSubmit} className="login-proto-form">
                        <h1 className="login-welcome-title">Welcome</h1>

                        {error && <p className="login-error-alert">{error}</p>}

                        {/* Email Input Group */}
                        <div className={`proto-input-group ${Email ? 'has-value' : ''}`}>
                            <input
                                type="email"
                                className="proto-input-field"
                                value={Email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                            {/* Label moved AFTER input for CSS sibling targeting */}
                            <label className="proto-label">Email</label>
                        </div>

                        {/* Password Input Group */}
                        <div className={`proto-input-group ${Password ? 'has-value' : ''}`}>
                            <div className="proto-password-inner-wrapper">
                                <input
                                    type={showPassword ? "text" : "password"}
                                    className="proto-input-field"
                                    value={Password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                />
                                {/* Label moved AFTER input */}
                                <label className="proto-label">Password</label>
                                
                                <span className="proto-eye-icon" onClick={() => setShowPassword(!showPassword)}>
                                    {showPassword ? '🙈' : '👁️'}
                                </span>
                            </div>
                        </div>

                        {/* Helper Links (Sign Up & Forget Password on same line) */}
                        <div className="login-form-links">
                            <span className="signup-link" onClick={() => navigate('/signup')}>
                                Haven't have account?
                            </span>
                            <span className="forget-link" onClick={handleForgetPassword}>
                                Forget Password
                            </span>
                        </div>

                        {/* Styled Pill Login Button */}
                        <button className="proto-submit-btn" type='submit'>Login</button>
                    </form>
                </div>

            </div>
        </div>
    );
}