// google search bar in the header
const f = document.getElementById('form');
const q = document.getElementById('query');
const google = 'https://www.google.com/search?q=site%3A+';


function submitted(event) {
  event.preventDefault();
  const url = google + q.value;
  const win = window.open(url, '_blank');
  win.focus();
}

f.addEventListener('submit', submitted);


//video
var vid = document.getElementsByClassName("videocakeMp4");
vid.volume = 0.3;


function myHello(){
  document.getElementById("hello").textContent = "It's sweet, it's fluffy, it's delicious.";
  document.getElementById("hello").style.fontSize = "3rem";
  document.getElementById("hello").style.color = "#8B4513";
}