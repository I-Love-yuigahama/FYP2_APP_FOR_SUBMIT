import React from 'react';

export default function FeaturesSection() {
  return (
    <section className="section section-pink features-section">
      <h2 className="title animate-on-scroll">Features</h2>
      <p className="section-subtitle animate-on-scroll">Built for students & teachers</p>

      <div className="cards-container grid-3">
        <div className="card feature-card animate-on-scroll delay-1">
          <div className="card-icon-box pink-box">
            <svg width="34" height="34" viewBox="0 0 24 24" fill="none" stroke="#ab7b81" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <path d="M8 4 C 8 12, 16 12, 16 20" /><path d="M16 4 C 16 12, 8 12, 8 20" />
              <line x1="9" y1="7" x2="15" y2="7" /><line x1="10.5" y1="10" x2="13.5" y2="10" />
              <line x1="10.5" y1="14" x2="13.5" y2="14" /><line x1="9" y1="17" x2="15" y2="17" />
            </svg>
          </div>
          <h3 className="card-title">Biology</h3>
          <p className="card-desc">Core Biology syllabus questions covering cells, genetics, ecology, and more.</p>
        </div>

        <div className="card feature-card animate-on-scroll delay-2">
          <div className="card-icon-box pink-box">
            <svg width="34" height="34" viewBox="0 0 24 24" fill="none" stroke="#ab7b81" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <line x1="18" y1="20" x2="18" y2="10" /><line x1="12" y1="20" x2="12" y2="4" /><line x1="6" y1="20" x2="6" y2="14" />
            </svg>
          </div>
          <h3 className="card-title">Leaderboard</h3>
          <p className="card-desc">Scores update live so students remain engaged. Teachers save the results instantly.</p>
        </div>

        <div className="card feature-card animate-on-scroll delay-3">
          <div className="card-icon-box pink-box">
            <svg width="34" height="34" viewBox="0 0 24 24" fill="none" stroke="#ab7b81" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <path d="M12 20h9" /><path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z" />
            </svg>
          </div>
          <h3 className="card-title">Subjective</h3>
          <p className="card-desc">Student Answer in Subjective - there won't have any MCQ</p>
        </div>

        <div className="card feature-card animate-on-scroll delay-4">
          <div className="card-icon-box pink-box">
            <svg width="34" height="34" viewBox="0 0 24 24" fill="none" stroke="#ab7b81" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <line x1="12" y1="5" x2="12" y2="19" /><line x1="5" y1="12" x2="14" y2="12" />
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
              <polyline points="14 2 14 8 20 8" />
            </svg>
          </div>
          <h3 className="card-title">Create quiz</h3>
          <p className="card-desc">Build and share custom quizzes. Add your own questions for any topic.</p>
        </div>

        <div className="card feature-card animate-on-scroll delay-5">
          <div className="card-icon-box pink-box">
            <svg width="34" height="34" viewBox="0 0 24 24" fill="none" stroke="#ab7b81" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <circle cx="12" cy="12" r="10" /><polyline points="12 6 12 12 16 14" />
            </svg>
          </div>
          <h3 className="card-title">Timed sessions</h3>
          <p className="card-desc">5 questions per session with a countdown - simulates real exam pressure.</p>
        </div>

        <div className="card feature-card animate-on-scroll delay-6">
          <div className="card-icon-box green-box">
            <svg width="34" height="34" viewBox="0 0 24 24" fill="none" stroke="#a3b342" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" /><path d="m9 11 2 2 4-4" />
            </svg>
          </div>
          <h3 className="card-title">Verified questions</h3>
          <p className="card-desc">All questions sourced from SPM trail past years papers</p>
        </div>
      </div>

      <p className="section-footer-text animate-on-scroll">
        Whether you're self-studying or a teacher hosting a class session - everything you need is here.
      </p>
    </section>
  );
}