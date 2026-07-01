import React, { useState } from 'react';
import './Create_Quiz.css';
import { useNavigate } from 'react-router-dom';
import QuizService from '../../../service/QuizService';
import { useAuth } from '../../../auth/AuthContext';

// Import your background GIF here (Adjust the filename/path)
import loggedInBgGif from '../../Home/logged_in_bg.gif';

export default function Create_Quiz() {
  const { token } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!token) {
      return setError("Please Login First");
    }

    try {
      console.log("Token being sent:", token);
      const response = await QuizService.createQuizRoom(token);
      
      console.log("Quiz Room Created:", response);
      const roomCode = response.quizDTO.roomCode;
      sessionStorage.setItem('roomCode', roomCode);

      navigate('/quiz_waiting');
    } catch (err) {
      setError(err.message || "Something went wrong");
    }
  };

  return (
    <div 
      className="create-page-container" 
      style={{ backgroundImage: `url(${loggedInBgGif})` }}
    >
      <div className="create-content">
        {/* Title with the decorative dashes matching your image */}
        <h1 className="create-title">
          <span className="title-dash">-</span> Create Quiz <span className="title-dash">-</span>
        </h1>
        
        {error && <p style={{ color: 'red', fontWeight: 'bold' }}>{error}</p>}
        
        <form onSubmit={handleSubmit} className="create-form">
          <button className="create-action-btn glass-ios" type="submit">
            Create
          </button>
        </form>
      </div>
    </div>
  );
}