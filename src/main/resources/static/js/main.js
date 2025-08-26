// Modal açma/kapatma
const modal = document.getElementById("forgotModal");
document.getElementById("forgotBtn").onclick = () => modal.style.display = "block";
document.getElementById("closeModal").onclick = () => modal.style.display = "none";
window.onclick = (e) => { if(e.target === modal) modal.style.display = "none"; };

// Form gönderme
document.getElementById("forgotForm").addEventListener("submit", async function(e){
    e.preventDefault();
    const formData = new FormData(this);
    const params = new URLSearchParams(formData);

    const response = await fetch("/forgot-password", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params
    });

    const text = await response.text();
    document.getElementById("message").innerText = text;
});