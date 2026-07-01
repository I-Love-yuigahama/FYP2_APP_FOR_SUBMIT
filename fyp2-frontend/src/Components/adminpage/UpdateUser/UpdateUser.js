import React, { useEffect, useState } from 'react';
import './UpdateUser.css';
import UserService from '../../service/UserService';

export default function UpdateUser({ user_id, token, onDone }) {


  
    const validate = (e) => {
        if (e.length < 5) { setError("Password must be at least 5 characters"); return false }
        if (!/[!@#$%^&*(),.?":{}|<>;:]/.test(e)) { setError("Password must contain at least one special character"); return false }
        return true
    }

    const [formData, setFormData] = useState({
        email: '',
        password: ''
        // name: ''  ← add this in future when DB has name column
    })
    const [showPassword, setShowPassword] = useState(false)
    const [error, setError] = useState('')

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await UserService.getUserById(user_id, token)
                setFormData({
                    email: response.userDTO?.email || '',
                    password: ''
                })
            } catch (err) {
                setError(err.message)
            }
        }
        fetchUser()
    }, [user_id, token])

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value })
    }

    const handleSubmit = async (e) => {

      e.preventDefault()
      if(formData.password && !validate(formData.password)) return

      try {
        await UserService.updateUser(user_id, formData, token)
          console.log(formData)
            onDone()
        } catch (err) {
            setError(err.message)
        }
    }

    return (
        <div className="update-user-container">
            <div className="update-user-wrapper">
                <div className="update-user-card">

                    {error && <p style={{ color: 'red' }}>{error}</p>}

                    <form className="update-user-form" onSubmit={handleSubmit}>

                        <div className="form-row">
                            <label>Email</label>
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                placeholder="Enter email"
                            />
                        </div>

                        {/* add this block in future when name column exists
                        <div className="form-row">
                            <label>Name</label>
                            <input
                                type="text"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                placeholder="Enter name"
                            />
                        </div>
                        */}

                        <div className="form-row">
                            <label>New Password</label>
                            <div className="password-input-container">
                                <input
                                    type={showPassword ? "text" : "password"}
                                    name="password"
                                    value={formData.password}
                                    onChange={handleChange}
                                    placeholder="Leave blank to keep current password"
                                />
                                <span
                                    className="eye-icon"
                                    onClick={() => setShowPassword(!showPassword)}
                                >
                                    {showPassword ? '🙈' : '👁️'}
                                </span>
                            </div>
                        </div>

                        <div className="submit-row">
                            <button type="button" className="action-btn" onClick={onDone}>
                                Cancel
                            </button>
                            <button type="submit" className="action-btn">
                                Update
                            </button>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    )
}