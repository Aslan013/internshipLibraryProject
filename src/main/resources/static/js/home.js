
// Kullanıcı menüsü aç/kapa
const userBtn = document.querySelector('.user-btn');
const userMenu = document.querySelector('.user-menu');
userBtn.addEventListener('click', () => {
    userMenu.style.display = userMenu.style.display === 'flex' ? 'none' : 'flex';
});

// Arama Enter tuşu


// Liste tıklama
const popup = document.getElementById('popup');
const popupText = document.getElementById('popupText');
const borrowBtn = document.getElementById('borrowBtn');
const closePopup = document.getElementById('closePopup');

document.getElementById('categoryList').addEventListener('click', (e) => {
    if(e.target.tagName === 'LI') {
        const category = e.target.dataset.category;
        // Search sayfasına yönlendir
        window.location.href = `/books/search?query=${encodeURIComponent(category)}`;
    }
});


document.getElementById('suggestionList').addEventListener('click', (e) => {
    if (e.target.tagName === 'LI') {
        popupText.innerHTML = `<strong>${e.target.textContent}</strong> kitabı seçildi.`;
        popup.style.display = 'flex';
    }
});

borrowBtn.addEventListener('click', () => {
    alert("Kitap ödünç alındı!");
    popup.style.display = 'none';
});

closePopup.addEventListener('click', () => {
    popup.style.display = 'none';
});

// Menü butonları
document.getElementById('profileBtn').addEventListener('click', () => {
window.location.href = '/profile'; 
});


document.getElementById('logoutBtn').onclick = () => {
if(confirm("Çıkış yapmak istediğinize emin misiniz?")) {
    window.location.href = window.location.origin + "/main";
}
};


document.addEventListener('click', async (e) => {
    if (e.target.classList.contains('borrowBtn')) {
        const btn = e.target; // burayı ekle
        const isbn = btn.dataset.isbn;
        const title = btn.dataset.title;

        if(confirm(`"${title}" kitabını ödünç almak istiyor musunuz?`)) {
            try {
                const response = await fetch('/books/borrow', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ isbn })
                });

                const result = await response.json();
                if(result.success) {
                    alert("Kitap başarıyla ödünç alındı!");
                    window.location.reload(); // listeyi güncelle
                } else {
                    alert(result.message || "İşlem başarısız!");
                }
            } catch(err) {
                console.error(err);
                alert("Sunucu hatası!");
            }
        }
    }
});


