import React from 'react';

export default function QuizSection() {
  return (
    <section className="section section-white quiz-section">
      <h2 className="title animate-on-scroll">Quiz</h2>
      <p className="section-subtitle animate-on-scroll">Start a quiz in 4 steps</p>

      <div className="cards-container grid-4">
        <div className="card card-pink card-step animate-on-scroll delay-1">
          <div className="step-number">1</div>
          <div className="card-icon-large"><div className="icon-user"></div></div>
          <h3 className="card-title center-text">Register</h3>
          <p className="card-desc center-text">Create a free<br />account to access<br />the quiz function</p>
        </div>

        <div className="card card-pink card-step animate-on-scroll delay-2">
          <div className="step-number">2</div>
          <div className="card-icon-large"><div className="icon-file"></div></div>
          <h3 className="card-title center-text">Create</h3>
          <p className="card-desc center-text">Create a free<br />account to access<br />the quiz function</p>
        </div>

        <div className="card card-pink card-step animate-on-scroll delay-3">
          <div className="step-number">3</div>
          <div className="card-icon-large"><div className="icon-brain"></div></div>
          <h3 className="card-title center-text">Answer</h3>
          <p className="card-desc center-text">Each session has 5<br />questions with a<br />timer.</p>
        </div>

        <div className="card card-pink card-step animate-on-scroll delay-4">
          <div className="step-number">4</div>
          <div className="card-icon-large"><div className="icon-wreath"></div></div>
          <h3 className="card-title center-text">Leaderboard</h3>
          <p className="card-desc center-text">Live rankings<br />update in real<br />time as you play.</p>
        </div>
      </div>

      <p className="section-footer-text animate-on-scroll">
        Simple enough for students, powerful enough for teachers to<br />host live sessions.
      </p>
    </section>
  );
}