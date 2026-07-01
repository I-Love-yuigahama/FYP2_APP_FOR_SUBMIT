import React, { useState } from 'react';
import './RecoveryPassword_OTP.css';
import { useNavigate, useLocation } from 'react-router-dom';
import loginDeskImg from '../login_desk.jpg';
import UserService from '../../../service/UserService';

export default function RecoveryPassword_OTP() {
    const navigate = useNavigate();
    const { state } = useLocation();
    const email = state?.email;

    const [otpCode, setOtpCode] = useState('');
    const [error, setError] = useState('');
    const [successMsg, setSuccessMsg] = useState('');
    const [loading, setLoading] = useState(false);
    const [resending, setResending] = useState(false);

    if (!email) {
        navigate('/recoveryPasswordEmail');
        return null;
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccessMsg('');
        setLoading(true);

        try {
            const response = await UserService.verifyOtp(otpCode, email);

            if (response.statusCode === 200) {
                navigate('/recoveryPasswordNewPassword', { state: { email } });
            } else {
                setError(response.message || 'Invalid OTP, please try again.');
            }
        } catch (err) {
            console.error('OTP Verification Failed:', err);
            setError('Invalid or expired OTP, please try again.');
        } finally {
            setLoading(false);
        }
    };

    const handleResendOtp = async () => {
        setError('');
        setSuccessMsg('');
        setResending(true);

        try {
            const response = await UserService.sendRecoveryEmail(email);

            if (response.statusCode === 200) {
                setSuccessMsg('A new OTP has been sent to your email.');
                setOtpCode('');
            } else {
                setError(response.message || 'Failed to resend OTP.');
            }
        } catch (err) {
            console.error('Resend OTP Failed:', err);
            setError('Failed to resend OTP. Please try again.');
        } finally {
            setResending(false);
        }
    };

    return (
        <div className="otp-page-container">
            <div className="otp-content-wrapper">

                <div className="otp-image-section">
                    <img src={loginDeskImg} alt="Study Desk Setup" className="otp-proto-img" />
                </div>

                <div className="otp-form-section">
                    <form onSubmit={handleSubmit} className="otp-proto-form">
                        <h1 className="otp-welcome-title">Recover Password</h1>
                        <p className="otp-subtitle">Enter the OTP sent to <strong>{email}</strong></p>

                        {error && <p className="otp-error-alert">{error}</p>}
                        {successMsg && <p className="otp-success-alert">{successMsg}</p>}

                        <div className={`otp-input-group ${otpCode ? 'has-value' : ''}`}>
                            <input
                                type="text"
                                className="otp-input-field"
                                value={otpCode}
                                onChange={(e) => setOtpCode(e.target.value)}
                                maxLength={6}
                                required
                            />
                            <label className="otp-label">Enter OTP</label>
                        </div>

                        <button className="otp-submit-btn" type="submit" disabled={loading || resending}>
                            {loading ? 'Verifying...' : 'Verify'}
                        </button>

                        <p
                            className="otp-back-text"
                            onClick={!resending ? handleResendOtp : undefined}
                            style={{ cursor: resending ? 'not-allowed' : 'pointer', opacity: resending ? 0.6 : 1 }}
                        >
                            {resending ? 'Sending...' : 'Resend OTP'}
                        </p>
                    </form>
                </div>

            </div>
        </div>
    );
}