
// Kullanıcı menüsü
const userBtn = document.getElementById('userBtn');
const userMenu = document.getElementById('userMenu');

userBtn.addEventListener('click', () => {
  userMenu.style.display = userMenu.style.display === 'flex' ? 'none' : 'flex';
});

window.addEventListener('click', (e) => {
  if (!userBtn.contains(e.target) && !userMenu.contains(e.target)) {
    userMenu.style.display = 'none';
  }
});

document.getElementById('profileBtn').addEventListener('click', () => {
  alert('Profil sayfasındasınız.');
  userMenu.style.display = 'none';
});

document.getElementById('logoutBtn').addEventListener('click', () => {
  if (confirm('Çıkış yapmak istediğinize emin misiniz?')) {
    window.location.href = 'main'; // Gerçek yönlendirme
  }
});

// Tab geçişleri
const tabs = document.querySelectorAll('.tab');
const tabContents = document.querySelectorAll('.tab-content');
tabs.forEach(tab => {
  tab.addEventListener('click', () => {
    tabs.forEach(t => t.classList.remove('active'));
    tabContents.forEach(c => c.classList.remove('active'));

    tab.classList.add('active');
    document.getElementById(tab.dataset.tab).classList.add('active');
  });
});


// Elimdeki kitaplar listesine ekle
const myBooksList = document.getElementById('myBooksList');

// Kitap geçmişi listesine ekle
const historyList = document.getElementById('historyList');

async function loadBorrowedBooks() {
  try {
    const response = await fetch('/profile/borrowed-books');
    const data = await response.json();


    myBooksList.innerHTML = '';
    historyList.innerHTML = '';

    data.currentBooks.forEach(b => {
      const li = document.createElement('li');

      const lastReturn = new Date(b.lastReturnDate);
      const today = new Date();
      let cezaText = '';

      if (lastReturn < today) {
        const diffTime = Math.ceil((today - lastReturn) / (1000 * 60 * 60 * 24));
        const ceza = diffTime * 10;
        cezaText = ` - Ceza: ${ceza} TL`;
      }

      li.textContent = `${b.bookName} - son teslim: ${b.lastReturnDate}${cezaText}`;

      li.addEventListener('click', async () => {
        if (confirm(`"${b.bookName}" iade etmek istiyor musunuz?`)) {
          if (cezaText) {
            // Modal aç, ödeme yapıldıktan sonra iade işlemi yapılacak
            openPaymentModal();

            // payBtn.onclick içinde işlemi yapacak şekilde değiştir
            payBtn.onclick = async () => {
              alert("Ödeme alındı!");
              modal.style.display = "none";

              // Backend update
              try {
                const response = await fetch('/profile/return-book', {
                  method: 'POST',
                  headers: { 'Content-Type': 'application/json' },
                  body: JSON.stringify({ isbn: b.isbn, username: b.username })
                });

                const result = await response.json();
                if (result.success) {
                  alert("Kitap iade edildi!");
                  li.remove(); // listeden kaldır
                } else {
                  alert("İade sırasında hata oluştu!");
                }
              } catch (err) {
                console.error(err);
              }
            };
          } else {
            // Ceza yoksa direkt iade
            try {
              const response = await fetch('/profile/return-book', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ isbn: b.isbn, username: b.username })
              });

              const result = await response.json();
              if (result.success) {
                alert("Kitap iade edildi!");
                li.remove();
              } else {
                alert("İade sırasında hata oluştu!");
              }
            } catch (err) {
              console.error(err);
            }
          }
        }
      });


      myBooksList.appendChild(li);
    });


    data.historyBooks.forEach(b => {
      const li = document.createElement('li');
      li.textContent = `${b.bookName} - alındı: ${b.borrowingDate} - iade: ${b.actualReturnDate}`;
      historyList.appendChild(li);
    });
  } catch (err) {
    console.error(err);
  }
}


// Sayfa yüklenince çağır
loadBorrowedBooks();


document.getElementById('updateInfoBtn').addEventListener('click', async () => {
  const firstName = document.getElementById('nameField').value.trim();
  const lastName = document.getElementById('surnameField').value.trim();
  const email = document.getElementById('emailField').value.trim();
  const tc = document.getElementById('idField').value.trim();

  if (!firstName || !lastName || !email || !tc) {
    alert('Lütfen tüm alanları doldurun.');
    return;
  }

  try {
    const formData = new FormData();
    formData.append('firstName', firstName);
    formData.append('lastName', lastName);
    formData.append('email', email);
    formData.append('tc', tc);

    const response = await fetch('/profile/update', {
      method: 'POST',
      body: formData
    });

    const result = await response.text();
    if (result === 'success') {
      alert('Kişisel bilgiler güncellendi!');
    } else {
      alert('Güncelleme sırasında hata oluştu.');
    }
  } catch (error) {
    alert('Bağlantı hatası.');
  }
});

// Şifre değiştir
document.getElementById('changePasswordBtn').onclick = async () => {
  const oldPass = document.getElementById('oldPassword').value;
  const newPass = document.getElementById('newPassword').value;
  const newPassAgain = document.getElementById('newPasswordAgain').value;

  if (!oldPass || !newPass || !newPassAgain) {
    alert("Tüm alanları doldurun!");
    return;
  }
  if (newPass !== newPassAgain) {
    alert("Yeni şifreler aynı olmalı!");
    return;
  }

  try {
    const response = await fetch('/profile/change-password', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ oldPassword: oldPass, newPassword: newPass })
    });

    const result = await response.json();
    alert(result.message);
  } catch (err) {
    console.error(err);
    alert("Şifre değiştirilemedi!");
  }
};


// Kitap bağışla kaydet
const donateSaveBtn = document.getElementById('donateSaveBtn');

donateSaveBtn.addEventListener('click', async () => {
  const bookData = {
    title: document.getElementById('donateBookName').value.trim(),
    authorName: document.getElementById('donateAuthorName').value.trim(),
    authorSurname: document.getElementById('donateAuthorSurname').value.trim(),
    isbn: document.getElementById('donateIsbn').value.trim(),
    category: document.getElementById('donateCategory').value.trim(),
    publisher: document.getElementById('donatePublisher').value.trim(),
    pages: document.getElementById('donatePages').value,
    publishYear: document.getElementById('donateYear').value,
    edition: document.getElementById('donateEdition').value
  };

  try {
    const response = await fetch('/profile/donate', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(bookData)
    });

    const result = await response.json();
    alert(result.message);

    if (result.success) {
      // Formu temizle
      document.querySelectorAll('#donate input').forEach(input => input.value = '');
    }
  } catch (error) {
    console.error('Hata:', error);
    alert('Kitap bağışlama sırasında bir hata oluştu.');
  }
});

const modal = document.getElementById("paymentModal");
const closeModal = document.getElementById("closeModal");
const payBtn = document.getElementById("modalPayBtn");

// Modal açmak
function openPaymentModal() {
  modal.style.display = "flex";
}

// Modal kapatmak
closeModal.onclick = () => modal.style.display = "none";
window.onclick = (e) => { if (e.target === modal) modal.style.display = "none"; }
