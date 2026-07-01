import React, { useRef } from 'react';

export default function TiltCard({ children, delayClass }) {
  const cardRef = useRef(null);
  const is3DUnlocked = useRef(false);

  const handleMouseMove = (e) => {
    if (!cardRef.current) return;
    const rect = cardRef.current.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;
    const centerX = rect.width / 2;
    const centerY = rect.height / 2;
    const deltaX = x - centerX;
    const deltaY = y - centerY;
    const normalizedX = Math.abs(deltaX) / centerX;
    const normalizedY = Math.abs(deltaY) / centerY;
    const distance = Math.max(normalizedX, normalizedY);

    if (!is3DUnlocked.current && distance < 0.15) {
      is3DUnlocked.current = true;
    }

    const baseLift = 'translateY(-8px) scale3d(1.02, 1.02, 1.02)';

    if (is3DUnlocked.current) {
      const intensity = 1 - distance;
      const rotateX = (deltaY / centerY) * -35 * intensity;
      const rotateY = (deltaX / centerX) * 35 * intensity;
      const dynamicShadow = `15px 20px 40px rgba(0, 0, 0, ${0.15 + (0.1 * intensity)})`;
      cardRef.current.style.transition = 'transform 0.1s ease-out, box-shadow 0.1s ease-out';
      cardRef.current.style.transform = `perspective(1000px) ${baseLift} rotateX(${rotateX}deg) rotateY(${rotateY}deg)`;
      cardRef.current.style.boxShadow = dynamicShadow;
    } else {
      cardRef.current.style.transition = 'transform 0.2s ease-out, box-shadow 0.2s ease-out';
      cardRef.current.style.transform = `perspective(1000px) ${baseLift} rotateX(0deg) rotateY(0deg)`;
      cardRef.current.style.boxShadow = '15px 25px 45px rgba(0, 0, 0, 0.15)';
    }
  };

  const handleMouseLeave = () => {
    if (!cardRef.current) return;
    is3DUnlocked.current = false;
    cardRef.current.style.transition = 'transform 0.5s ease-out, box-shadow 0.5s ease-out';
    cardRef.current.style.transform = `perspective(1000px) translateY(0) scale3d(1, 1, 1) rotateX(0deg) rotateY(0deg)`;
    cardRef.current.style.boxShadow = '12px 15px 30px rgba(0, 0, 0, 0.18)';
    setTimeout(() => {
      if (cardRef.current) {
        cardRef.current.style.transition = '';
        cardRef.current.style.transform = '';
        cardRef.current.style.boxShadow = '';
      }
    }, 500);
  };

  return (
    <div
      ref={cardRef}
      className={`card question-card-3d animate-on-scroll ${delayClass}`}
      onMouseMove={handleMouseMove}
      onMouseLeave={handleMouseLeave}
    >
      {children}
    </div>
  );
}