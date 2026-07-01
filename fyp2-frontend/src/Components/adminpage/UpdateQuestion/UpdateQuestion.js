import React, { useEffect, useState, useRef } from 'react';
import './UpdateQuestion.css';
import QuestionService from '../../service/QuestionService';

export default function UpdateQuestion({ token, question_id, onDone }) {  // ✅ receive props

  const fileInputRef = useRef(null)
  const [imagePreview, setImagePreview] = useState(null)
  const [imageFile, setImageFile] = useState(null)
  const [error, setError] = useState('')
  const [formData, setFormData] = useState({
    questionText: "",
    answerText: "",
    keyword: ""
  })


   // ✅ add this function
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

  // ✅ fetch real question data
  useEffect(() => {
    const fetchQuestion = async () => {
      try {
        const response = await QuestionService.getQuestionById(token, question_id)
        const q = response.questionDTO
        setFormData({
          questionText: q.questionText,
          answerText: q.answerText,
          keyword: q.keyword
        })
        // // ✅ replace old conversion with new getImageSrc
        if (q.image) {
          setImagePreview(getImageSrc(q.image))
        }
      } catch (err) {
        setError(err.message)
      }
    }
    fetchQuestion()
  }, [question_id, token])

  const handleImageClick = () => fileInputRef.current.click()

  const handleFileChange = (e) => {
    const file = e.target.files[0]
    if (file) {
      setImageFile(file)
      setImagePreview(URL.createObjectURL(file))
    }
  }

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
  }

  // ✅ call API on submit
  const handleSubmit = async (e) => {
    e.preventDefault()

    console.log(formData)
    try {
      const data = new FormData()
      data.append('requestUpdateQuestion', new Blob([JSON.stringify({
        questionText: formData.questionText,
        answerText: formData.answerText,
        keyword: formData.keyword
      })], { type: 'application/json' }))

      if (imageFile) data.append('image', imageFile)

      await QuestionService.updateQuestion(token, question_id, data)
      onDone()  // ← go back to list
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="update-page-container">
      <div className="update-content-wrapper">
        <div className="update-card">

          {error && <p style={{ color: 'red' }}>{error}</p>}

          <div className="image-upload-container" onClick={handleImageClick}>
            {imagePreview
              ? <img src={imagePreview} alt="Question Reference" className="editable-image" />
              : <span>No Image</span>
            }
            <div className="image-overlay"><span>Change Image</span></div>
            <input type="file" ref={fileInputRef} onChange={handleFileChange} accept="image/*" style={{ display: 'none' }} />
          </div>

          <form className="update-form" onSubmit={handleSubmit}>
            <div className="form-row">
              <label>Question Text</label>
              <textarea name="questionText" value={formData.questionText} onChange={handleChange} />
            </div>
            <div className="form-row">
              <label>Answer Text</label>
              <textarea name="answerText" value={formData.answerText} onChange={handleChange} />
            </div>
            <div className="form-row">
              <label>Keyword</label>
              <textarea name="keyword" value={formData.keyword} onChange={handleChange} className="keyword-input" />
            </div>
            <div className="submit-row">
              <button type="button" className="action-btn" onClick={onDone}>Cancel</button>
              <button type="submit" className="update-btn">Update</button>
            </div>
          </form>

        </div>
      </div>
    </div>
  )
}