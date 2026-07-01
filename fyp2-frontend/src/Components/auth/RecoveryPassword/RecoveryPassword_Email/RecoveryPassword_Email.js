import React, { useState } from 'react';
import './RecoveryPassword_Email.css';
import { useNavigate } from 'react-router-dom';
import loginDeskImg from '../login_desk.jpg';
import UserService from '../../../service/UserService';

export default function RecoveryPassword_Email() {
    const navigate = useNavigate();
    const [Email, setEmail] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setMessage('');
        setLoading(true);

        try {
            const response = await UserService.sendRecoveryEmail(Email);
                
            if (response.statusCode === 200) {
                setMessage("OTP sent! Redirecting...");
                setTimeout(() => {
                    // Pass email to the OTP page via navigation state
                    navigate('/recoveryPasswordOTP', { state: { email: Email } });
                }, 1500);
            } else {
                setError(response.message || "Failed to send OTP. Please try again.");
            }
        } catch (err) {
            console.error("Recovery Failed:", err);
            setError("Something went wrong, please try again.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="recovery-page-container">
            <div className="recovery-content-wrapper">

                <div className="recovery-image-section">
                    <img src={loginDeskImg} alt="Study Desk Setup" className="recovery-proto-img" />
                </div>

                <div className="recovery-form-section">
                    <form onSubmit={handleSubmit} className="recovery-proto-form">
                        <h1 className="recovery-welcome-title">Recover</h1>
                        <p className="recovery-subtitle">Enter your email to receive an OTP reset code.</p>

                        {error && <p className="recovery-error-alert">{error}</p>}
                        {message && <p className="recovery-success-alert">{message}</p>}

                        <div className={`recovery-input-group ${Email ? 'has-value' : ''}`}>
                            <input
                                type="email"
                                className="recovery-input-field"
                                value={Email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                            <label className="recovery-label">Email</label>
                        </div>

                        <button className="recovery-submit-btn" type="submit" disabled={loading}>
                            {loading ? "Sending..." : "Send OTP"}
                        </button>

                        <p className="recovery-back-text" onClick={() => navigate('/login')}>
                            Wait, I remember my password
                        </p>
                    </form>
                </div>

            </div>
        </div>
    );
}