import React, { useState, useRef } from 'react';
import './CreateQuestion.css';
import QuestionService from '../../service/QuestionService';

export default function CreateQuestion({ token, onDone }) {  // ✅ receive props

  const fileInputRef = useRef(null)
  const [imagePreview, setImagePreview] = useState(null)
  const [imageFile, setImageFile] = useState(null)  // ✅ store actual file
  const [error, setError] = useState('')
  const [formData, setFormData] = useState({
    questionText: "",
    answerText: "",
    keyword: ""
  })

  const handleImageClick = () => fileInputRef.current.click()

  const handleFileChange = (e) => {
    const file = e.target.files[0]
    if (file) {
      setImageFile(file)                           // ✅ store file for API
      setImagePreview(URL.createObjectURL(file))   // preview
    }
  }

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      // ✅ build FormData for multipart/form-data
      const data = new FormData()
      data.append('questionDTO', new Blob([JSON.stringify({
        questionText: formData.questionText,
        answerText: formData.answerText,
        keyword: formData.keyword
      })], { type: 'application/json' }))

      console.log(data);

      if (imageFile) data.append('image', imageFile)

      await QuestionService.createQuestion(token, data)
      onDone()  // ← go back to list
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="create-q-container">
      <div className="create-q-wrapper">
        <div className="create-q-card">

          {error && <p style={{ color: 'red' }}>{error}</p>}

          <div className="image-upload-circle" onClick={handleImageClick}>
            {imagePreview
              ? <img src={imagePreview} alt="Preview" className="uploaded-image" />
              : <span className="placeholder-text">Image</span>
            }
            <div className="image-overlay">
              <span>{imagePreview ? "Change Image" : "Upload Image"}</span>
            </div>
            <input type="file" ref={fileInputRef} onChange={handleFileChange} accept="image/*" style={{ display: 'none' }} />
          </div>

          <form className="create-q-form" onSubmit={handleSubmit}>
            <div className="form-row">
              <label>Question<br />Text</label>
              <textarea name="questionText" value={formData.questionText} onChange={handleChange} placeholder="Add Question Text" />
            </div>
            <div className="form-row">
              <label>Answer<br />Text</label>
              <textarea name="answerText" value={formData.answerText} onChange={handleChange} placeholder="Add Answer Text" />
            </div>
            <div className="form-row">
              <label>Keyword</label>
              <textarea name="keyword" value={formData.keyword} onChange={handleChange} placeholder="Add keyword" className="keyword-input" />
            </div>
            <div className="submit-row">
              <button type="button" className="action-btn" onClick={onDone}>Cancel</button>
              <button type="submit" className="action-btn">Create</button>
            </div>
          </form>

        </div>
      </div>
    </div>
  )
}