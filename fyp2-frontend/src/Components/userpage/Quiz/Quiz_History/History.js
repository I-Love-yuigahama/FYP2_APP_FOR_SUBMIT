import React from 'react';
import './History.css';

export default function History() {
  // Mock data representing the user's history
  const historyData = [
    {
      id: 1,
      roomCode: "6NTW",
      date: "2026-01-23",
      time: "06:24",
      role: "Host"
    },
    {
      id: 2,
      roomCode: "67TW",
      date: "2025-12-12",
      time: "15:22",
      role: "Participant"
    },
    {
      id: 3,
      roomCode: "693W",
      date: "2025-12-12",
      time: "15:22",
      role: "Host"
    }
  ];

  return (
    <div className="history-page-container">
      {/* Assume your <NavBar /> is handled globally or insert it here */}

      <div className="history-content-wrapper">
        <div className="history-list">
          
          {/* Map through the history data to generate cards */}
          {historyData.map((item) => (
            <div className="history-card" key={item.id}>
              
              {/* Left Side: Room Code */}
              <div className="history-room">
                <span>Room : {item.roomCode}</span>
              </div>

              {/* Center: Date and Time */}
              <div className="history-datetime">
                <span>{item.date} &nbsp;&nbsp; {item.time}</span>
              </div>

              {/* Right Side: Role */}
              <div className="history-role">
                <span>{item.role}</span>
              </div>

            </div>
          ))}

        </div>
      </div>
    </div>
  );
}