import React, { useState } from 'react'
import './Quiz_RoomCode.css'
import { useNavigate } from 'react-router-dom';
import QuizService from '../../../service/QuizService';
import { useAuth } from '../../../auth/AuthContext';

export default function Quiz_RoomCode() {

    const[roomCode, setroomCode] = useState("");
    const navigate = useNavigate();
    const [error, seterror] =useState(``);

    const {token} = useAuth();

    const validate = (roomCode) => {

      // const functionName = (parameters) => {  // function body  return result;};

      if(roomCode.length < 4){seterror('Invalid RoomCOde'); return false}
      return true
    }

    const handleSubmit = async (e) =>{

          e.preventDefault() 
      const isvalid = validate(roomCode)

      if(isvalid)
      {

          try{
            // const token = sessionStorage.getItem(`token`)
            const userData = await QuizService.joinQuizRoom(token,roomCode)
            
            if (userData.statusCode === 200 && userData.quizDTO) {
                sessionStorage.setItem('roomCode', userData.quizDTO.roomCode)
                navigate('/quiz_waiting')
            } else {
                seterror(userData.error || "Failed to join quiz")
            }
              
          }catch(err)
          {
            seterror(err.message)
          }
      }
     
    }

  return (

    <div className="room-code-page">
      <main className="enter-code-screen">
        <h1 className="enter-code-title">Enter Code</h1>

        <form onSubmit={handleSubmit}>
            <p  className={error ? "enter-code-error" :"enter-code-instruction" }>
              { error ? error : "Please Enter the Room Code to Join the Session"}
              {/* error ? error : "Please Enter the Room Code to Join the Session" = error !== "" ? "enter-code-error" : "enter-code-instruction"
                condition ? true : false 

              error has value?  ──YES──→  show the error message
                  ──NO───→  show the default instruction text 
                  It works because in JavaScript an empty string "" is falsy:
                  */}
            </p>

            <input 
              type="text" 
              name='roomCode'
              // value={roomCode}
              className="code-input-field" 
              maxLength="6"
              placeholder="7N8P"
              value={roomCode}
              onChange={(e) =>{
                  setroomCode(e.target.value)
              }}
            />

            <button className="join-btn" type='submit'>Join</button>
        </form>
      </main>
    </div>

  )
}