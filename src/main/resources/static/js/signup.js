document.getElementById('signupForm').addEventListener('submit', async (e) => {
e.preventDefault(); // stop the page from reloading

// Gather data from inputs
const data = {
    firstName: document.getElementById('firstName').value,
    lastName: document.getElementById('lastName').value,
    tc: document.getElementById('tc').value,
    username: document.getElementById('username').value,
    password: document.getElementById('password').value,
    email: document.getElementById('email').value

};

if(!/^[a-zA-Z0-9]+$/.test(data.username) || !/^[a-zA-Z0-9]+$/.test(data.password)){
    alert("lütfen kullanıcı adı ve şifrede Türkçe karakter kullanmayınız.");
    return; // Form gönderilmez
}


// Send to backend
const res = await fetch('/users', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(data)
});
const result = await res.text();
if(res.ok){
    alert("Kayıt başarılı!");
    document.getElementById('signupForm').reset();
    window.location.href= "/main";
} else {
    alert("Hata: " + result);
}
});