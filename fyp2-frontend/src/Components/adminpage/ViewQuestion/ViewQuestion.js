import React from 'react';
import './ViewQuestion.css';

export default function ViewQuestion() {
  // Mock data representing the question you are viewing
  const questionData = {
    imageUrl: "/sample-image.png", // Replace with your actual image path
    questionText: "palisade mesophyll, chloroplasts, light, photosynthesis",
    answerText: "palisade mesophyll, chloroplasts, light, photosynthesis",
    keyword: "palisade mesophyll, chloroplasts, light, photosynthesis"
  };

  const handleBack = () => {
    // Add your navigation logic here (e.g., using react-router-dom's useNavigate)
    console.log("Navigating back to list...");
  };

  return (
    <div className="view-q-container">
      {/* Insert your <Navbar /> component here */}

      <div className="view-q-wrapper">
        <div className="view-q-card">
          
          {/* Read-only Image */}
          <div className="view-image-container">
            <img src={questionData.imageUrl} alt="Question Reference" />
          </div>

          {/* Read-only Data Rows */}
          <div className="view-data-content">
            
            <div className="data-row">
              <span className="data-label">Question<br/>Text</span>
              <p className="data-value">{questionData.questionText}</p>
            </div>

            <div className="data-row">
              <span className="data-label">Answer<br/>Text</span>
              <p className="data-value">{questionData.answerText}</p>
            </div>

            <div className="data-row">
              <span className="data-label">Keyword</span>
              <p className="data-value">{questionData.keyword}</p>
            </div>

            {/* Back Button */}
            <div className="button-row">
              <button onClick={handleBack} className="back-btn">Back</button>
            </div>
            
          </div>

        </div>
      </div>
    </div>
  );
}