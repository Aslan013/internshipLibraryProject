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

// Send to backend
const res = await fetch('/users', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(data)
});

if(res.ok){
    alert("Kayıt başarılı!");
    document.getElementById('signupForm').reset();
    window.location.href= "/main";
} else {
    alert("Hata: Alanlar boş olamaz veya kullanıcı zaten var!");
}
});