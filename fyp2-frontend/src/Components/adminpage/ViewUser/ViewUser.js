import React, { useState } from 'react';
import './ViewUser.css';

export default function ViewUser() {
  // Mock data for the user being viewed
  const userData = {
    username: "User1",
    password: "secretpassword123" 
  };

  // State to toggle password visibility
  const [showPassword, setShowPassword] = useState(false);

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const handleBack = () => {
    // Add your navigation logic here to go back to the user list
    console.log("Navigating back...");
  };

  // Helper function to generate dummy bullets for the hidden password
  const maskedPassword = "•".repeat(8);

  return (
    <div className="view-user-container">
      {/* Insert your <Navbar /> component here */}

      <div className="view-user-wrapper">
        <div className="view-user-card">
          
          <div className="view-user-content">
            
            {/* Username Row */}
            <div className="data-row">
              <span className="data-label">Username</span>
              <span className="data-value">{userData.username}</span>
            </div>

            {/* Password Row */}
            <div className="data-row">
              <span className="data-label">Password</span>
              <div className="password-display-container">
                <span className="data-value password-text">
                  {showPassword ? userData.password : maskedPassword}
                </span>
                
                {/* SVG Eye Icon Toggle */}
                <span className="eye-icon" onClick={togglePasswordVisibility}>
                  {showPassword ? (
                    <svg viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" strokeWidth="2" fill="none" strokeLinecap="round" strokeLinejoin="round">
                      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                      <circle cx="12" cy="12" r="3"></circle>
                    </svg>
                  ) : (
                    <svg viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" strokeWidth="2" fill="none" strokeLinecap="round" strokeLinejoin="round">
                      <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                      <line x1="1" y1="1" x2="23" y2="23"></line>
                    </svg>
                  )}
                </span>
              </div>
            </div>

            {/* Back Button */}
            <div className="button-row">
              <button onClick={handleBack} className="action-btn">Back</button>
            </div>
            
          </div>

        </div>
      </div>
    </div>
  );
}