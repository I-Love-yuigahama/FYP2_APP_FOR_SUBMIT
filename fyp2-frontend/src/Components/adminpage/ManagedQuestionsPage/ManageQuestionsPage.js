import React, { useCallback, useEffect, useState } from 'react'
import './ManageQuestionsPage.css'
import QuestionService from '../../service/QuestionService'
import UpdateQuestion from '../../adminpage/UpdateQuestion/UpdateQuestion'
import CreateQuestion from '../../adminpage/CreateQuestion/CreateQuestion'

export default function ManageQuestionsPage() {

  const [questions, setquestions] = useState([])
  const [error, seterror] = useState('')
  const [selectedQuestionId, setselectedQuestionId] = useState(null)
  const [showCreate, setShowCreate] = useState(false)

  const token = sessionStorage.getItem('token')

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

  // ✅ extracted so it can be reused
  const fetchData = useCallback(async () => {
    try {
      const questions_list = await QuestionService.getALlQuestion(token)
      setquestions(questions_list.questionDTOList || [])
    } catch (err) {
      seterror(err.message)
      console.log(err)
    }
  }, [token])

  useEffect(() => {
    if (!token) return
    fetchData()
  }, [fetchData, token])

  const handleDelete = async (questionId) => {

    try {
      console.log(await QuestionService.deleteQuestion(token, questionId))
      fetchData()  // ✅ re-fetch after delete
    } catch (err) {
      seterror(err.message)
    }
  }

  // Show CreateQuestion page
  if (showCreate) {
    return <CreateQuestion token={token} onDone={() => {
      setShowCreate(false)
      fetchData()  // ✅ re-fetch after create
    }} />
  }

  // Show UpdateQuestion page
  if (selectedQuestionId) {
    return (
      <UpdateQuestion
        token={token}
        question_id={selectedQuestionId}
        onDone={() => {
          setselectedQuestionId(null)
          fetchData()  // ✅ re-fetch after update
        }}
      />
    )
  }

  return (
    <div className="question-page-container">
      <div className="question-content-wrapper">
        <div className="data-card">

          {error && <p style={{ color: 'red' }}>{error}</p>}

          <div className="table-header">
            <div className="header-cell"><span className="underline-title">Image</span></div>
            <div className="header-cell"><span className="underline-title">Text</span></div>
            <div className="header-cell"><span className="underline-title">Answer Text</span></div>
            <div className="header-cell"><span className="underline-title">Keyword</span></div>
            <div className="header-cell right-align">
              <button className="btn-add" onClick={() => setShowCreate(true)}>ADD</button>
            </div>
          </div>

          <div className="table-body">
            {questions.map((q) => (
              <div className="table-row" key={q.id}>

                <div className="col-img">
                  {q.image
                    ? <img src={getImageSrc(q.image)} alt="question" />
                    : <span>No Image</span>
                  }
                </div>

                <div className="col-text">
                  <p>{q.questionText}</p>
                </div>

                <div className="col-answer">
                  <p>{q.answerText}</p>
                </div>

                <div className="col-keyword">
                  <p>{q.keyword}</p>
                </div>

                <div className="col-actions">
                  <button className="btn-edit" onClick={() => setselectedQuestionId(q.id)}>EDIT</button>
                  <button className="btn-delete" onClick={() => handleDelete(q.id)}>DELETE</button>
                </div>

              </div>
            ))}
          </div>

        </div>
      </div>
    </div>
  )
}