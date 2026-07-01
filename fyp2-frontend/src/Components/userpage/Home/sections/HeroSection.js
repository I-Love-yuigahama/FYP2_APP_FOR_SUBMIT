import React from 'react';
import heroBg from '../home_page_frist_part_bg.jpg';

export default function HeroSection({ scrollY }) {
  return (
    <section
      className="section hero-section"
      style={{ backgroundImage: `url(${heroBg})` }}
    >
      <div
        className="hero-content-wrapper"
        style={{
          transform: `translateY(${scrollY * 0.4}px)`,
          opacity: 1 - scrollY / 600
        }}
      >
        <h1 className="typewriter-text">Quiz_Science</h1>
        <p className="subtitle">is to validate your understanding</p>
      </div>
    </section>
  );
}