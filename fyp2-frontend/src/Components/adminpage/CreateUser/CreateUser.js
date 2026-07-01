import React, { useState } from 'react';
import './CreateUser.css';
import UserService from '../../service/UserService';

export default function CreateUser({ onDone }) {

  const [formData, setFormData] = useState({
    username: "",
    password: "",
    role: "USER"
  })
  const [showPassword, setShowPassword] = useState(false)
  const [error, setError] = useState("")

  const token = sessionStorage.getItem('token')

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      console.log("Submitting form with data:", formData); // Debugging line to check formData before submission
      const result = await UserService.createUser(token, formData)
      console.log("Create result:", result)
      onDone()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="create-user-container">
      <div className="create-user-wrapper">
        <div className="create-user-card">

          {error && <p style={{ color: 'red' }}>{error}</p>}

          <form className="create-user-form" onSubmit={handleSubmit}>

            <div className="form-row">
              <label>Email</label>
              <input
                type="email"
                name="email"          // ← change from username to email
                value={formData.email}
                onChange={handleChange}
                placeholder="Enter email"
              />
            </div>

            <div className="form-row">
              <label>Password</label>
              <div className="password-input-container">
                <input
                  type={showPassword ? "text" : "password"}
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  placeholder="Enter User Password"
                />
                <span className="eye-icon" onClick={() => setShowPassword(!showPassword)}>
                  {showPassword ? '🙈' : '👁️'}
                </span>
              </div>
            </div>

            <div className="form-row">
              <label>Role</label>
              <select name="role" value={formData.role} onChange={handleChange}>
                <option value="USER">USER</option>
                <option value="ADMIN">ADMIN</option>
              </select>
            </div>

            <div className="submit-row">
              <button type="button" className="action-btn" onClick={onDone}>Cancel</button>
              <button type="submit" className="action-btn">Create</button>
            </div>

          </form>
        </div>
      </div>
    </div>
  )
}