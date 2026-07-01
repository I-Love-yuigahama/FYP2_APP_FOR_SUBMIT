import React from 'react';
import { useNavigate } from 'react-router-dom';
import Section5_bg from '../download.jpg';
import loggedInBgGif from '../logged_in_bg.gif';

export default function CTASection({ isAuthenticated }) {
  const navigate = useNavigate();

  if (isAuthenticated) {
    return (
      <section
        className="section cta-section-logged-in"
        style={{ backgroundImage: `url(${loggedInBgGif})` }}
      >
        <div className="logged-in-content animate-on-scroll">
          <h2 className="logged-in-title center-text">Create or Join Quiz Now</h2>
          <div className="cta-actions-box animate-on-scroll delay-1">
            <div className="cta-buttons">
              <button onClick={() => navigate('/create_quiz')} className="cta-btn glass-btn-create">Create</button>
              <button onClick={() => navigate('/join_quiz')} className="cta-btn glass-btn-join">Join</button>
            </div>
          </div>
        </div>
      </section>
    );
  }

  return (
    <section className="section section-white cta-section">
      <h2 className="large-title animate-on-scroll center-text">
        Ready to give your<br />Student a little test?
      </h2>
      <div className="cta-content animate-on-scroll delay-1">
        <img src={Section5_bg} alt="Give Em Hell Left" className="cta-image" />
        <div className="cta-actions-box">
          <h3 className="cta-join">Join Us Now</h3>
          <div className="cta-buttons">
            <button onClick={() => navigate('/login')} className="cta-btn glass-btn-pink">Login</button>
            <button onClick={() => navigate('/signup')} className="cta-btn glass-btn-green">Sign up</button>
          </div>
        </div>
        <img src={Section5_bg} alt="Give Em Hell Right" className="cta-image" />
      </div>
    </section>
  );
}