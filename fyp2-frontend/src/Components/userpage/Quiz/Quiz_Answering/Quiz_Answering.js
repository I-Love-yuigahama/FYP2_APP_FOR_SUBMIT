import React, { useCallback, useEffect, useRef, useState } from 'react'
import './Quiz_Answering.css'
import { useNavigate } from 'react-router-dom';
import QuizService from '../../../service/QuizService';
import { useAuth } from '../../../auth/AuthContext';
import useQuizSocket from '../../../useQuizSocket'

export default function Quiz_Answering() {

    const userAnswerRef = useRef("")        // ✅ fix stale closure
    const currentQIndexRef = useRef(0)     // ✅ fix stale closure
    const isSubmittingRef = useRef(false)  // ✅ fix stale closure
    const questionsRef = useRef([])        // ✅ fix stale closure

    const navigate = useNavigate()
    const roomCode = sessionStorage.getItem(`roomCode`)
    const { token } = useAuth()

    // Game State
    const [status, setStatus] = useState("LOADING")
    const [introTimer, setIntroTimer] = useState(3)
    const [questions, setquestions] = useState([])
    const [currentQIndex, setCurrentQIndex] = useState(0)

    // User Input
    const [userAnswer, setUserAnswer] = useState("")
    const [isSubmitting, setIsSubmitting] = useState(false)

    // ✅ keep refs in sync with state
    useEffect(() => { userAnswerRef.current = userAnswer }, [userAnswer])
    useEffect(() => { currentQIndexRef.current = currentQIndex }, [currentQIndex])
    useEffect(() => { isSubmittingRef.current = isSubmitting }, [isSubmitting])
    useEffect(() => { questionsRef.current = questions }, [questions])


    // fetch questions + rejoin
    useEffect(() => {
        if (!token || !roomCode) return;

        const fetchData = async () => {
            try {
                const question = await QuizService.getQuizRoomDetails(token, roomCode)
                const questionList = question.quizDTO.questionDTOList
                setquestions(questionList)
                questionsRef.current = questionList  // ✅ sync ref immediately

                // ✅ restore progress after refresh
                const rejoinData = await QuizService.rejoin(token, roomCode)
                if (rejoinData?.quizDTO?.questionIndex) {
                    const resumeIndex = rejoinData.quizDTO.questionIndex + 1
                    setCurrentQIndex(resumeIndex)
                    currentQIndexRef.current = resumeIndex  // ✅ sync ref immediately
                }

                setStatus(`INTRO`)
            } catch (err) {
                console.log(err)
                navigate("/")
            }
        }

        fetchData()
    }, [token, roomCode, navigate])

    // ✅ submitAnswer uses refs — no stale closure
    const submitAnswer = useCallback(async () => {
        if (isSubmittingRef.current) return;
        
        const currentQuestions = questionsRef.current
        const currentIndex = currentQIndexRef.current
        const currentAnswer = userAnswerRef.current

        if (!currentQuestions[currentIndex]) return;

        isSubmittingRef.current = true
        setIsSubmitting(true)

        const isLast = currentIndex === currentQuestions.length - 1

        try {
            await QuizService.submitAnswer(
                token,
                roomCode,
                {
                    questionId: currentQuestions[currentIndex].id,
                    questionIndex: currentIndex,   // ✅ send index to backend
                    participantAns: currentAnswer, // ✅ from ref, never stale
                    status: isLast ? "SUBMIT" : "ANSWER"
                }
            )

            if (isLast) {
                navigate("/quiz_user_score")
            } else {
                setCurrentQIndex(prev => prev + 1)
                currentQIndexRef.current = currentIndex + 1  // ✅ sync ref
                setUserAnswer("")
                userAnswerRef.current = ""  // ✅ reset ref
                isSubmittingRef.current = false
                setIsSubmitting(false)
            }

        } catch (err) {
            console.error("Submit error:", err)
            isSubmittingRef.current = false
            setIsSubmitting(false)
        }
    },[token, roomCode, navigate])

    // ✅ handleSocketMessage defined before use, submitAnswer already defined above
    const handleSocketMessage = useCallback((data) => {
        if (data.status === 'END') {
            navigate('/quiz_user_score')
        }

        if (data.status === 'TIME_UP') {
            submitAnswer()  // ✅ now uses refs, no stale closure
        }
    }, [navigate, submitAnswer])

    useQuizSocket(roomCode, handleSocketMessage, token)

    // INTRO COUNTDOWN
    useEffect(() => {
        if (status !== `INTRO`) return;

        if (introTimer > 0) {
            const timer = setTimeout(() => setIntroTimer(introTimer - 1), 1000)
            return () => clearTimeout(timer)
        }

        if (introTimer === 0) {
            setStatus(`PLAYING`)
        }
    }, [status, introTimer])


    // image helper
    const getImageSrc = (image) => {
        if (!image) return null
        if (typeof image === 'string') {
            return `data:image/jpeg;base64,${image}`
        }
        if (typeof image === 'object' && !Array.isArray(image)) {
            const byteArray = Object.values(image)
            const base64 = btoa(
                byteArray.reduce((data, byte) => data + String.fromCharCode(byte), '')
            )
            return `data:image/jpeg;base64,${base64}`
        }
        if (Array.isArray(image)) {
            const base64 = btoa(
                image.reduce((data, byte) => data + String.fromCharCode(byte), '')
            )
            return `data:image/jpeg;base64,${base64}`
        }
        return null
    }

    // Intro screen
    if (status === "INTRO") {
        return (
            <div className='fullScreenOverlay'>
                <h1 style={{ fontSize: "90px", color: "white" }}>
                    {introTimer > 0 ? introTimer : "Let's GO!"}
                </h1>
            </div>
        )
    }

    // Loading guard
    const currentQuestion = questions[currentQIndex]
    if (!currentQuestion) return <div>Loading question...</div>


    return (
        <main className="quiz-container">
            <div className="quiz-header-badge question-count">
                {currentQIndex + 1}/{questions.length}
            </div>

            {currentQuestion.image && (
                <div className="question-image-wrapper">
                    <img src={getImageSrc(currentQuestion.image)} alt="Quiz Material" />
                </div>
            )}

            <p className="question-text">{currentQuestion.questionText}</p>

            <textarea
                className="subjective-input"
                placeholder="Type your answer here..."
                value={userAnswer}
                onChange={e => {
                    setUserAnswer(e.target.value)
                    userAnswerRef.current = e.target.value  // ✅ sync ref on type
                }}
            />

            <button
                className="submit-btn"
                onClick={submitAnswer}
                disabled={isSubmitting}
            >
                {isSubmitting ? "Submitting..." : currentQIndex === questions.length - 1 ? "Finish Quiz" : "Submit"}
            </button>
        </main>
    )
}