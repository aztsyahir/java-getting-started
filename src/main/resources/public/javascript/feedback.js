// google search bar in the header
const f = document.getElementById('form');
const q = document.getElementById('query');
const google = 'https://www.google.com/search?q=site%3A+';


function submitted(event) {
  event.preventDefault();
  const url = google  + q.value;
  const win = window.open(url, '_blank');
  win.focus();
}

f.addEventListener('submit', submitted);


//Check fullname user
function checkFullname() {
  var elMsg1 = document.getElementById("message1");
  var iFullname = document.getElementById("fullname");
  if (iFullname.value.length < 1) {
    elMsg1.textContent = "*Please enter your fullname"; // Set msg
  }
  else {
    elMsg1.textContent = ""; //Clear message
  }
}
var iFullname = document.getElementById("fullname");
iFullname.addEventListener("blur", checkFullname, false);


//Check email address user
function checkEmail() {
  var elMsg2 = document.getElementById("message2");
  var iEmail = document.getElementById("email");
  var filter = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
  if (iEmail.value === "" || iEmail.value === null) {
    elMsg2.textContent = "*Please enter your email address"; // Set msg
  }
  else if (!iEmail.value.match(filter)) {
    elMsg2.textContent = "*Please enter a valid email address"; // Set msg
  }
  else {
    elMsg2.textContent = ""; //Clear message
  }
}
var iEmail = document.getElementById("email");
iEmail.addEventListener("blur", checkEmail, false);


//Check enquiry
function checkEnquiry() {
  var elMsg3 = document.getElementById("message3");
  var iEnquiry = document.getElementById("enquiryform");
  if (iEnquiry.value === "" || iEnquiry.value === null) {
    elMsg3.textContent = "*Required"; // Set msg
  }
  else {
    elMsg3.textContent = ""; //Clear message
  }
}
var iEnquiry = document.getElementById("enquiryform");
iEnquiry.addEventListener("blur", checkEnquiry, false);


//After complete filling the feedback form, a pop-up message will appear
function submissionFeedback() {
  var iFullname = document.getElementById("fullname");
  var iEmail = document.getElementById("email");
  var filter = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
  var iEnquiry = document.getElementById("enquiryform");
  var feedbackForm = document.getElementById("feedbackform");
  if (iFullname.value.length < 1 || iEmail.value === null || !iEmail.value.match(filter) || iEnquiry.value === "" || iEnquiry.value === null) {
    alert("Fill in the following requirements before submitting your feedback.");
    return false;
  }
  else {
    alert("We have received your feedback. Thank you!");
    feedbackForm.reset();
  }
}