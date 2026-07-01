import React, { useCallback, useEffect, useRef, useState } from 'react'
import './Quiz_waiting.css'
import QuizService from '../../../service/QuizService'
import { useNavigate } from 'react-router-dom'
import useQuizSocket from '../../../useQuizSocket'
import { useAuth } from '../../../auth/AuthContext';

export default function Quiz_waiting() {

    const navigate = useNavigate()
    const { token, email } = useAuth()
    const roomCode = sessionStorage.getItem('roomCode')

    const [hostName, setHostName] = useState('')
    const [error, setError] = useState('')
    const [participants, setParticipants] = useState([])
    const [isHost, setIsHost] = useState(false)

    const isHostRef = useRef(false)
    const hostEmailRef = useRef('')

    useEffect(() => {
        if (!token || !roomCode || !email) return

        const fetchData = async () => {
            try {
                const quizDetails = await QuizService.getQuizRoomDetails(token, roomCode)
                const hostEmail = quizDetails.quizDTO.host.email

                hostEmailRef.current = hostEmail
                setHostName(hostEmail)

                const allParticipants = quizDetails.quizDTO.gamePlayerDTOList || []
                setParticipants(allParticipants.filter(p => p.userEmail !== hostEmail))

                if (email === hostEmail) {
                    setIsHost(true)
                    isHostRef.current = true
                    sessionStorage.setItem('isHost', 'true')
                } else {
                    setIsHost(false)
                    isHostRef.current = false
                    sessionStorage.setItem('isHost', 'false')
                }

            } catch (err) {
                setError("Failed to load quiz room")
                console.log(err)
            }
        }

        fetchData()
    }, [token, email, roomCode])

    const handleSocketMessage = useCallback((data) => {
        if (data.gamePlayerDTOList) {
            setParticipants(data.gamePlayerDTOList.filter(p => p.userEmail !== hostEmailRef.current))
        }

        if (data.status === 'START') {
            if (isHostRef.current) {
                navigate('/quiz_leaderboard')
            } else {
                navigate('/quiz_answering')
            }
        }
    }, [navigate])

    useQuizSocket(roomCode, handleSocketMessage, token)

    const handleSubmit = async (e) => {
        e.preventDefault()
        if (!isHost) return

        try {
            await QuizService.startQuizRoom(token, roomCode)
        } catch (err) {
            setError("Failed to start quiz")
            console.log(err)
        }
    }

    return (
        <div className="waiting-room-screen">
            <h1 className="waiting-room-title">WAITING ROOM</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}

            <form onSubmit={handleSubmit} style={{ width: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>

                <div className="host-container">
                    <p className="section-label">Host</p>
                    <div className="user-pill" style={{ margin: '0 auto', minWidth: '200px' }}>
                        {hostName}
                    </div>
                </div>

                <div className="room-code-display">
                    {roomCode}
                </div>

                <p className="section-label">Participant</p>
                <div className="participant-grid">
                    {participants.map((participant, index) => (
                        <div key={index} className="user-pill">
                            {participant.userName}
                        </div>
                    ))}
                </div>

                {isHost
                    ? <button className="start-btn" type='submit'>Start</button>
                    : <p className="waiting-text">Waiting for host to start...</p>
                }
            </form>
        </div>
    )
}