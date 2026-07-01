import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import './NavBar.css'
import { useAuth } from '../auth/AuthContext'

export default function NavBar() {
    const { isAuthenticated, email, role, logout } = useAuth()
    const [isDropdownOpen, setIsDropdownOpen] = useState(false)
    const navigate = useNavigate()

    const handleLogout = () => {
        logout()
        navigate('/login')
    }

    return (
        <nav className='navbar'>
            <div className='logo-section'>
                <Link to="/" className='logo-container'>
                    <img src="/path-to-your-logo.png" alt="SScience Logo" className='logo-img' />
                </Link>
                <div className='brand-text'>
                    <h1>SScience</h1>
                    <span>Subjective quiz</span>
                </div>
            </div>

            <div className='nav-links'>
                <ul>
                    {role === 'USER' && (
                        <>
                            <li><Link to="/" className='nav-link active'>Home</Link></li>
                            <li><Link to="/create_quiz" className='nav-link'>Create Quiz</Link></li>
                            <li><Link to="/about" className='nav-link'>About Us</Link></li>
                        </>
                    )}

                    {role === 'ADMIN' && (
                        <>
                            <li><Link to="/" className='nav-link active'>Home</Link></li>
                            <li><Link to="/userList" className='nav-link'>User Panel</Link></li>
                            <li><Link to="/questionList" className='nav-link'>Question Panel</Link></li>
                        </>
                    )}

                    {!isAuthenticated && (
                        <>
                            <li><Link to="/" className='nav-link active'>Home</Link></li>
                            <li><Link to="/about" className='nav-link'>About Us</Link></li>
                        </>
                    )}
                </ul>
            </div>

            <div className='nav-actions'>
                {isAuthenticated ? (
                    <>
                        {role === 'USER' && (
                            <Link to="/join_quiz" className='room-code-link'>RoomCode</Link>
                        )}

                        <div className='divider'></div>

                        <div className='user-dropdown-container'>
                            <span
                                className='username-toggle'
                                onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                            >
                                {email}
                            </span>

                            {isDropdownOpen && role === 'USER' && (
                                <div className='dropdown-menu'>
                                    <Link
                                        to="/profile"
                                        className='dropdown-item'
                                        onClick={() => setIsDropdownOpen(false)}
                                    >
                                        Profile
                                    </Link>
                                    <div className='dropdown-divider'></div>
                                    <Link
                                        to="/history"
                                        className='dropdown-item'
                                        onClick={() => setIsDropdownOpen(false)}
                                    >
                                        History
                                    </Link>
                                </div>
                            )}
                        </div>

                        <button className='nav-btn glass-green' onClick={handleLogout}>Logout</button>
                    </>
                ) : (
                    <>
                        <Link to="/login" className='nav-btn glass-pink'>Login</Link>
                        <Link to="/signup" className='nav-btn glass-green'>Sign up</Link>
                    </>
                )}
            </div>
        </nav>
    )
}