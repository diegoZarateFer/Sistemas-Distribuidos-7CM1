import React, { useState } from "react";
import './App.css';

function App() {
  const [board, setBoard] = useState(Array(9).fill(null));
  const [isXNext, setIsXNext] = useState(true);
  const [winner, setWinner] = useState(null);

  const handleClick = (index) => {
    if (board[index] || winner) return;

    const newBoard = [...board];
    newBoard[index] = isXNext ? "X" : "O";
    setBoard(newBoard);
    setIsXNext(!isXNext);

    checkWinner(newBoard);
  };

  const checkWinner = (board) => {
    const lines = [
      [0, 1, 2], [3, 4, 5], [6, 7, 8],
      [0, 3, 6], [1, 4, 7], [2, 5, 8],
      [0, 4, 8], [2, 4, 6],
    ];

    for (let line of lines) {
      const [a, b, c] = line;
      if (board[a] && board[a] === board[b] && board[a] === board[c]) {
        setWinner(board[a]);
        return;
      }
    }

    if (!board.includes(null)) {
      setWinner("Empate");
    }
  };

  const handleRestart = () => {
    setBoard(Array(9).fill(null));
    setWinner(null);
    setIsXNext(true);
  };

  return (
    <div className="game-container">
      <h1>Juego de Gato</h1>
      <div className="board">
        {board.map((value, index) => (
          <div 
            key={index} 
            className={`square ${value ? 'filled' : ''}`} 
            onClick={() => handleClick(index)}
          >
            {value}
          </div>
        ))}
      </div>
      <div className="status">
        {winner ? (
          winner === "Empate" ? "¡Es un empate!" : `¡El ganador es ${winner}!`
        ) : (
          `Turno de ${isXNext ? "X" : "O"}`
        )}
      </div>
      <button className="restart-btn" onClick={handleRestart}>Reiniciar Juego</button>
    </div>
  );
}

export default App;
