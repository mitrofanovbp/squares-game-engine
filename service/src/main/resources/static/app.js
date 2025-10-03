const API_BASE = '/api/squares';

const api = {
  nextMove: async (board) => {
    const res = await fetch(`${API_BASE}/nextMove`, {
      method: 'POST',
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify(board)
    });
    if(res.status===204) return null;
    return res.json();
  },
  gameStatus: async (board) => {
    const res = await fetch(`${API_BASE}/gameStatus`, {
      method: 'POST',
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify(board)
    });
    return res.json();
  }
};

const el = (q)=>document.querySelector(q);
let size = 10, userColor = 'w', board, finished=false;

const idx = (x,y)=> y*size + x;

function emptyData(n){ return ' '.repeat(n*n); }

function render(){
  const root = el('#board');
  root.style.gridTemplateColumns = `repeat(${size}, 36px)`;
  root.innerHTML = '';
  for(let y=0;y<size;y++){
    for(let x=0;x<size;x++){
      const div = document.createElement('div');
      div.className = 'cell' + (finished?' disabled':'');
      div.dataset.x = x; div.dataset.y = y;
      const c = board.data[idx(x,y)];
      if(c==='w' || c==='b'){
        const p = document.createElement('div'); p.className = 'piece '+c; div.appendChild(p);
      }
      if(!finished && c===' ' && board.nextPlayerColor===userColor){
        div.addEventListener('click', onUserMove);
      }
      root.appendChild(div);
    }
  }
}

function setStatus(text){ el('#status').textContent = text; }

async function onUserMove(e){
  const x = +e.currentTarget.dataset.x; const y = +e.currentTarget.dataset.y;
  if(board.data[idx(x,y)]!==' ' || finished) return;

  // Пользователь ставит фишку
  board.data = board.data.substring(0, idx(x,y)) + userColor + board.data.substring(idx(x,y)+1);
  board.nextPlayerColor = userColor==='w' ? 'b' : 'w';

  await afterAnyMove();
  if(!finished && board.nextPlayerColor!==' ' && board.nextPlayerColor!==userColor){
    await computerTurn();
  }
}

async function computerTurn(){
  const move = await api.nextMove(board);
  if(move && (move.color==='w' || move.color==='b')){
    const {x,y,color} = move;
    board.data = board.data.substring(0, idx(x,y)) + color + board.data.substring(idx(x,y)+1);
    board.nextPlayerColor = color==='w' ? 'b' : 'w';
  }
  await afterAnyMove();
}

async function afterAnyMove(){
  const s = await api.gameStatus(board);
  finished = (s.status!==0);
  if(s.status===0){
    setStatus(board.nextPlayerColor===userColor? 'Ваш ход' : 'Ход компьютера');
  }else if(s.status===1){
    setStatus(s.color==='w' ? 'Победа: белые' : 'Победа: чёрные');
  }else if(s.status===2){
    setStatus('Ничья');
  }
  render();
}

function start(){
  size = +el('#size').value; userColor = el('#userColor').value;
  board = { size, data: emptyData(size), nextPlayerColor: 'w' };
  finished=false;
  setStatus(board.nextPlayerColor===userColor? 'Ваш ход':'Ход компьютера');
  render();
  if(board.nextPlayerColor!==userColor) computerTurn();
}

el('#start').addEventListener('click', start);
start();
