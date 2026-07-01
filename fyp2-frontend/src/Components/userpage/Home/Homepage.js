import React, { useEffect, useState } from 'react';
import './HomePage.css';
import { useAuth } from '../../auth/AuthContext';
import HeroSection from './sections/HeroSection';
import QuestionSection from './sections/QuestionSection';
import QuizSection from './sections/QuizSection';
import FeaturesSection from './sections/FeaturesSection';
import CTASection from './sections/CTASection';
import FooterSection from './sections/FooterSection';

export default function Homepage() {
  const [scrollY, setScrollY] = useState(0);
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    const handleScroll = () => setScrollY(window.scrollY);
    window.addEventListener('scroll', handleScroll);

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add('is-visible');
          } else {
            entry.target.classList.remove('is-visible');
          }
        });
      },
      { threshold: 0.1, rootMargin: "0px 0px -10% 0px" }
    );

    const elements = document.querySelectorAll('.animate-on-scroll');
    elements.forEach((el) => observer.observe(el));

    return () => {
      window.removeEventListener('scroll', handleScroll);
      elements.forEach((el) => observer.unobserve(el));
    };
  }, []);

  return (
    <div className="homepage-container">
      <HeroSection scrollY={scrollY} />
      <QuestionSection />
      <QuizSection />
      <FeaturesSection />
      <CTASection isAuthenticated={isAuthenticated} />
      <FooterSection />
    </div>
  );
}