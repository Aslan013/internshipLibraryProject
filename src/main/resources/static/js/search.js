// Keep the user menu functionality
const userBtn = document.querySelector('.user-btn');
const userMenu = document.querySelector('.user-menu');
userBtn.addEventListener('click', () => {
    userMenu.style.display = userMenu.style.display === 'flex' ? 'none' : 'flex';
});

// Keep the menu buttons
document.getElementById('profileBtn').addEventListener('click', () => {
    window.location.href = '/profile'; 
});

document.getElementById('logoutBtn').onclick = () => {
    if(confirm("Çıkış yapmak istediğinize emin misiniz?")) {
        window.location.href = window.location.origin + "/main";
    }
};

// Keep the borrow book functionality - THIS IS THE IMPORTANT PART
document.addEventListener('click', async (e) => {
    if (e.target.classList.contains('borrowBtn')) {
        const btn = e.target;
        const bookId = btn.dataset.id;
        const title = btn.dataset.title;

        if(confirm(`"${title}" kitabını ödünç almak istiyor musunuz?`)) {
            try {
                const response = await fetch('/books/borrow', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ isbn: bookId })
                });

                const result = await response.json();

                if(result.success) {
                    alert("Kitap başarıyla ödünç alındı!");
                    window.location.reload();
                } else {
                    alert(result.message || "İşlem başarısız!");
                }
            } catch (err) {
                console.error(err);
                alert("Sunucu hatası!");
            }
        }
    }
});