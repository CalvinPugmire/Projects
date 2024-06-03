async function joinLobby() {
  if (localStorage.getItem('userName') === document.querySelector('#host')?.value) {
    return;
  }
  joinOrHost(`/api/auth/join`);
}

async function hostLobby() {
  if (localStorage.getItem('userName') !== document.querySelector('#host')?.value) {
    return;
  }
  joinOrHost(`/api/auth/host`);
}

async function joinOrHost(endpoint) {
  const host = document.querySelector('#host')?.value;
  if (host === "") {
    return;
  }

  const response0 = await fetch(`/api/lobbycount/${host}`);
  const body0 = await response0.json();
  if (body0.lobbyCount >= 2) {
    return;
  }

  const response = await fetch(endpoint, {
    method: 'post',
    body: JSON.stringify({ username: localStorage.getItem('userName'), host: host }),
    headers: {
      'Content-type': 'application/json; charset=UTF-8',
    },
  });
  const body = await response.json();

  if (response?.status === 200) {
    window.location.href = 'game.html';
  }
}

function logout() {
  fetch(`/api/auth/logout/${localStorage.getItem('userName')}`, {
    method: 'delete',
  }).then(() => (window.location.href = '/'));
}

function displayPicture(data) {
  const containerEl = document.querySelector("#nameplate");

  const width = containerEl.offsetWidth;
  const height = containerEl.offsetHeight;

  const imgUrl = `https://api.qrserver.com/v1/create-qr-code/?size=${width}x${height}&data=${data.url}`;
  const imgEl = document.createElement("img");
  imgEl.setAttribute("src", imgUrl);
  containerEl.appendChild(imgEl);
}
  
function callService(url, displayCallback) {
  fetch(url)
    .then((response) => response)
    .then((data) => {
      displayCallback(data);
    });
}

const random = Math.floor(Math.random() * 1000);
callService(`https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=${localStorage.getItem('userName')}`, displayPicture);