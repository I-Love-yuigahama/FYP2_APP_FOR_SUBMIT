import React, { useEffect, useState } from 'react'
import './Quiz_user_score.css'
import { useNavigate, useLocation } from 'react-router-dom'
import QuizService from '../../../service/QuizService'
import { useAuth } from '../../../auth/AuthContext';


export default function Quiz_user_score() {

    const navigate = useNavigate()
    const location = useLocation()

    const roomCode = location.state?.roomCode || sessionStorage.getItem('roomCode')
    // const token = sessionStorage.getItem('token')
    const {token} = useAuth()

    const [userScore, setUserScore] = useState(null)
    const [error, setError] = useState('')

    useEffect(() => {
        if (!token || !roomCode) return

        const fetchData = async () => {
            try {
                console.log("=== fetching result for roomCode:", roomCode)
                const userResult = await QuizService.getParticipantResult(token, roomCode)
                console.log("=== raw result:", userResult)
                setUserScore(userResult.gamePlayerDTO || null)
            } catch (err) {
                setError("Failed to get your result")
                console.log(err)
            }
        }

        fetchData()
    }, [token, roomCode])

    const handleBtn = () => {
        // only clear sessionStorage if coming from active quiz, not history
        if (!location.state?.roomCode) {
            sessionStorage.removeItem('roomCode')
            navigate("/")
        }
        // navigate(-1)  // ← go back to wherever they came from
    }

    return (
        <div className="app-wrapper">
            <main className="results-container">
                <div className="congrats-card">
                    <h1 className="congrats-title">CONGRATULATIONS</h1>
                    {error && <p style={{ color: 'red' }}>{error}</p>}

                    {userScore ? (
                        <div className="results-stats">
                            <p>Score: {userScore.score} / 20</p>
                        </div>
                    ) : (
                        <p>Loading your score...</p>
                    )}
                </div>

                <button className="home-btn" onClick={handleBtn}>
                    Back
                </button>
            </main>
        </div>
    )
}