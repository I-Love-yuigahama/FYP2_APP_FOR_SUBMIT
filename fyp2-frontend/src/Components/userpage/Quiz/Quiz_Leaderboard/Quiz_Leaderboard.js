import React, { useCallback, useEffect, useRef, useState } from 'react'
import './Quiz_Leaderboard.css'
import { useNavigate, useLocation } from 'react-router-dom'
import QuizService from '../../../service/QuizService'
import useQuizSocket from '../../../useQuizSocket'
import { useAuth } from '../../../auth/AuthContext';

export default function Quiz_Leaderboard() {

    const navigate = useNavigate()
    const location = useLocation()
    const endTimeoutRef = useRef(null)

    const roomCode = location.state?.roomCode || sessionStorage.getItem('roomCode')

    const { token, email } = useAuth()  // ← add email

    const [participants, setParticipants] = useState([])
    const [isHost, setIsHost] = useState(false)
    const [error, setError] = useState('')
    const isHostRef = useRef(false)

    useEffect(() => {
        if (!token || !roomCode || !email) return  // ← guard email

        const fetchData = async () => {
            try {
                const [leaderboardData, quizDetail] = await Promise.all([
                    QuizService.getLeaderboard(token, roomCode),
                    QuizService.getQuizRoomDetails(token, roomCode)
                    // ← removed UserService.getYourProfile
                ])

                console.log("Leaderboard data:", leaderboardData)  // ← add this
                console.log("Quiz detail:", quizDetail)     

                setParticipants(leaderboardData.leaderBoardDTOList || [])

                const hostEmail = quizDetail.quizDTO.host.email
                if (email === hostEmail) {  // ← use email from context
                    setIsHost(true)
                    isHostRef.current = true
                }

            } catch (err) {
                setError('Failed to get leaderboard')
                console.log(err)
            }
        }

        fetchData()
    }, [email, token, roomCode])  // ← depend on email so it re-runs once email is available

    // ← wrap in useCallback so it doesn't re-create on every render
    const handleSocketMessage = useCallback((data) => {
        console.log("Socket message on leaderboard:", data)
        if (data.status === 'END') {
            clearTimeout(endTimeoutRef.current)
            if (isHostRef.current) {
                navigate('/')
            } else {
                navigate('/quiz_user_score')
            }
            return
        }

        if (data.status === 'SCORE_UPDATE') {
            console.log("SCORE_UPDATE received! New leaderboard:", data.leaderBoardDTOList)
            setParticipants(data.leaderBoardDTOList || [])

            // Only auto-navigate participants (not the host) after 30s of inactivity
            // The host should only leave via the "End Quiz" button
            if (!isHostRef.current) {
                clearTimeout(endTimeoutRef.current)
                endTimeoutRef.current = setTimeout(() => {
                    navigate('/quiz_user_score')
                }, 30000)
            }
        }
    }, [navigate])

    useQuizSocket(roomCode, handleSocketMessage, token)

    // Cleanup timeout on unmount
    useEffect(() => {
        return () => clearTimeout(endTimeoutRef.current)
    }, [])

    const handleEnd = async () => {
        try {
            await QuizService.endQuizSession(token, roomCode)
            sessionStorage.removeItem('roomCode')
            clearTimeout(endTimeoutRef.current)
            navigate('/')
        } catch (err) {
            setError("Failed to end quiz")
        }
    }

    const containerHeight = participants.length * 85;

    return (
        <main className="leaderboard-screen">
            <h1 className="leaderboard-title">LEADERBOARD</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}

            <div className="leaderboard-list" style={{ height: `${containerHeight}px` }}>
                {participants.map((participant, index) => (
                    <div
                        key={participant.username}
                        className="user-score-row"
                        style={{
                            transform: `translateY(${index * 85}px)`
                        }}
                    >
                        <span className="player-name">{participant.username}</span>
                        <span className="player-score">{participant.score}</span>
                    </div>
                ))}
            </div>

            <div className="leaderboard-actions">
                {isHost && (
                    <button className="home-btn glass-btn" onClick={handleEnd}>
                        End Quiz
                    </button>
                )}
            </div>
        </main>
    )
}