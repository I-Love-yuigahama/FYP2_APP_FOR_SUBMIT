import React from 'react';
import TiltCard from '../components/TiltCard';

export default function QuestionSection() {
  return (
    <section className="section section-pink question-section">
      <h2 className="title animate-on-scroll">Question</h2>
      <p className="section-subtitle animate-on-scroll">
        All questions are sourced from SPM trail past year papers and<br />
        free online resources - no made-up answers.
      </p>

      <div className="cards-container grid-3">
        <TiltCard delayClass="delay-1">
          <div className="card-icon-box pink-box">
            <svg width="34" height="34" viewBox="0 0 24 24" fill="none" stroke="#ab7b81" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <path d="M8 4 C 8 12, 16 12, 16 20" /><path d="M16 4 C 16 12, 8 12, 8 20" />
              <line x1="9" y1="7" x2="15" y2="7" /><line x1="10.5" y1="10" x2="13.5" y2="10" />
              <line x1="10.5" y1="14" x2="13.5" y2="14" /><line x1="9" y1="17" x2="15" y2="17" />
            </svg>
          </div>
          <h3 className="card-title">Biology</h3>
          <p className="card-desc">Core Biology syllabus questions covering cells, genetics, ecology, and more.</p>
          <div className="card-badge pink-badge">SPM syllabus</div>
        </TiltCard>

        <TiltCard delayClass="delay-2">
          <div className="card-icon-box pink-box">
            <svg width="34" height="34" viewBox="0 0 24 24" fill="none" stroke="#ab7b81" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
              <polyline points="14 2 14 8 20 8" />
              <path d="m18 13-6.5 6.5a2 2 0 0 1-1.33.55H8v-2.17a2 2 0 0 1 .55-1.33L15 10" />
              <path d="m15 10 3 3" />
            </svg>
          </div>
          <h3 className="card-title">SPM trail questions</h3>
          <p className="card-desc">Question Are from Past year and Past Few SPM trial questions</p>
          <div className="card-badge pink-badge">PAST YEAR</div>
        </TiltCard>

        <TiltCard delayClass="delay-3">
          <div className="card-icon-box green-box">
            <svg width="34" height="34" viewBox="0 0 24 24" fill="none" stroke="#a3b342" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" />
              <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" />
            </svg>
          </div>
          <h3 className="card-title">Subjective Based</h3>
          <p className="card-desc">Subjective based question Answered in your own word, this would test the understanding deeply.</p>
          <div className="card-badge green-badge">Subjective Type</div>
        </TiltCard>
      </div>
    </section>
  );
}