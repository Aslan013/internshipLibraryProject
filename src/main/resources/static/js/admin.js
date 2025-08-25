// menü butonu açmaca
const userBtn = document.getElementById('userBtn');
const userMenu = document.getElementById('userMenu');
userBtn.addEventListener('click', () => {
    userMenu.style.display = userMenu.style.display === 'flex' ? 'none' : 'flex';
});

document.getElementById('profileBtn').addEventListener('click', () => {
    alert("Profil sayfasına gidiliyor...");
    window.location.href = "/profile";
});
document.getElementById('logoutBtn').addEventListener('click', () => {
    if(confirm("Çıkış yapmak istediğinize emin misiniz?")) {
        window.location.href = "/main";
    }
});

// menüyü kapatmaca
document.addEventListener('click', (e) => {
    if (!userBtn.contains(e.target) && !userMenu.contains(e.target)) {
        userMenu.style.display = 'none';
    }
});

// Tablar arası geçiş
const tabs = document.querySelectorAll('.tab');
const tabContents = document.querySelectorAll('.tab-content');

tabs.forEach(tab => {
    tab.addEventListener('click', () => {
        tabs.forEach(t => t.classList.remove('active'));
        tab.classList.add('active');

        const target = tab.getAttribute('data-tab');
        tabContents.forEach(tc => {
            tc.id === target ? tc.classList.add('active') : tc.classList.remove('active');
        });

        // Eğer kullanıcı görüntüle tabı seçildiyse veriyi çek
        if (target === "view-user") {
            loadUsers();
        } else if (target === "view-book") {
            loadBooks();
        }

    });
});

// kitap takip tabı
const trackingDiv = document.getElementById('tracking');

fetch('/admin/borrowed-books')
.then(res => res.json())
.then(books => {
    if (books.length === 0) {
        trackingDiv.innerHTML += '<p>Henüz ödünç alınmış kitap yok.</p>';
    } else {
        let html = '<table border="1" style="width:100%; text-align:left;"><tr><th>Kitap Adı</th><th>Kullanıcı</th><th>Ödünç Tarihi</th><th>Son Teslim</th></tr>';
        books.forEach(b => {
            html += `<tr>
                        <td>${b.bookName}</td>
                        <td>${b.username}</td>
                        <td>${b.borrowingDate}</td>
                        <td>${b.lastReturnDate}</td>
                        </tr>`;
        });
        html += '</table>';
        trackingDiv.innerHTML += html;
    }
})
.catch(err => console.error(err));

// kullanıcı ekleme tabı
document.getElementById('saveUserBtn').addEventListener('click', async () => {
    const userData = {
        firstName: document.getElementById('firstName').value.trim(),
        lastName: document.getElementById('lastName').value.trim(),
        username: document.getElementById('username').value.trim(),
        password: document.getElementById('password').value,
        email: document.getElementById('email').value.trim(),
        tc: document.getElementById('tc').value.trim(),
        isAdmin: document.getElementById('isAdmin').checked
    };

    if (!userData.firstName || !userData.lastName || !userData.username || !userData.password || !userData.email || !userData.tc){
        alert("Lütfen tüm alanları doldurun.");
        return;
    }
    if (userData.password.length < 8 || userData.password.length > 16) {
        alert("Şifre 8-16 karakter arasında olmalı.");
        return;
    }
    const tcDigits = userData.tc.replace(/\D/g, "");
    if (tcDigits.length !== 11) {
        alert("TC Kimlik Numarası 11 haneli olmalı.");
        return;
    }

    try {
        const response = await fetch('/admin/add-user', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        });

        const result = await response.json();
        alert(result.message);

        if (result.success) {
            document.getElementById('addUserForm').reset();
        }
    } catch (error) {
        console.error('Hata:', error);
        alert('Kullanıcı eklenirken hata oluştu.');
    }
});

// Kullanıcı görüntüleme tabı


// kitap ekleme tabı
document.getElementById('saveBookBtn').addEventListener('click', async () => {
    const bookData = {
        title: document.getElementById('bookName').value.trim(),
        authorName: document.getElementById('authorName').value.trim(),
        authorSurname: document.getElementById('authorSurname').value.trim(),
        isbn: document.getElementById('isbn').value.trim(),
        category: document.getElementById('category').value.trim(),
        publisher: document.getElementById('publisher').value.trim(),
        pages: document.getElementById('pages').value,
        publishYear: document.getElementById('publishYear').value,
        edition: document.getElementById('edition').value
    };

    if (!bookData.title || !bookData.authorName || !bookData.authorSurname ||!bookData.isbn ||
        !bookData.category ||!bookData.publisher ||!bookData.publishYear||!bookData.pages||!bookData.edition ){
        alert("Kitap Adı, Yazar adı ve ISBN girilmesi zorunludur.");
        return;
    }
    const isbnDigits = bookData.isbn.replace(/\D/g, "");
    if (isbnDigits.length !== 13) {
        alert("ISBN 13 haneli olmalı.");
        return;
    }

    try {
        const response = await fetch('/profile/donate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bookData)
        });

        const result = await response.json();
        alert(result.message);

        if (result.success) {
            document.getElementById('addBookForm').reset();
        }
    } catch (error) {
        console.error('Hata:', error);
        alert('Kitap bağışlama sırasında bir hata oluştu.');
    }
});


// --- Kullanıcıları listeleme ---
const userTbody = document.querySelector('#userListTable tbody');

async function loadUsers() {
    userTbody.innerHTML = '';
    try {
        const res = await fetch('/admin/users');
        const users = await res.json();

        users.forEach(user => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${user.username}</td>
                <td>${user.firstName} ${user.lastName}</td>
                <td>${user.email}</td>
                <td>${user.tc}</td>
                <td>${user.isAdmin ? 'Evet' : 'Hayır'}</td>
                <td>
                    <button class="deleteUserBtn" data-username="${user.username}">Sil</button>
                </td>
            `;
            userTbody.appendChild(tr);
        });

        // Silme butonları
        document.querySelectorAll('.deleteUserBtn').forEach(btn => {
            btn.addEventListener('click', async () => {
                const username = btn.dataset.username;
                if(confirm(`${username} silinsin mi?`)) {
                    const res = await fetch(`/admin/delete-user?username=${username}`, { method: 'DELETE' });
                    const result = await res.json();
                    alert(result.message);
                    if(result.success)
                        btn.closest('tr').remove();
                }
            });
        });
    } catch(err) {
        console.error(err);
        alert('Kullanıcılar yüklenirken hata oluştu.');
    }
}

// --- Kitapları listeleme ---
const bookTbody = document.querySelector('#bookListTable tbody');

async function loadBooks() {
    bookTbody.innerHTML = '';
    try {
        const res = await fetch('/admin/books');
        const books = await res.json();

        books.forEach(book => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${book.title}</td>
                <td>${book.authorName} ${book.authorSurname}</td>
                <td>${book.isbn}</td>
                <td>${book.category}</td>
                <td>${book.publisher}</td>
                <td>${book.pages}</td>
                <td>${book.publishYear}</td>
                <td>${book.edition}</td>
                <td>${book.status ?'uygun' : 'Ödünçte'}</td>
                <td>
                    <button class="deleteBookBtn" data-title="${book.title}" data-isbn="${book.isbn}" data-status="${book.status}">Sil</button>
                </td>
            `;
            bookTbody.appendChild(tr);


        });

        
            // Silme butonlarını ekle
            document.addEventListener('click', async (e) => {
                if(e.target.classList.contains('deleteBookBtn')) {
                    const btn = e.target;
                    const isbn = btn.dataset.isbn;  // data-isbn'dan isbn al
                    const title = btn.dataset.title;  // data-title'dan title al
                    const status = btn.dataset.status === 'true';
                    const row = btn.closest('tr');   // satırı bul

                    if(status){
                        if(confirm(`${title} silinsin mi?`)) {
                            try {
                                const res = await fetch(`/admin/delete-book?isbn=${isbn}`, { method: 'DELETE' });
                                const result = await res.json();

                                alert(result.message);
                                if(result.success) {
                                    row.remove(); // satırı DOM'dan kaldır
                                }
                            } catch(err) {
                                console.error(err);
                                alert('Kitap silinirken hata oluştu.');
                            }
                        }                       
                    }
                    else{
                        alert('Kitap ödünçte olduğu için silinemez.');
                        return;
                    }
                }
            });
    } catch(err) {
        console.error(err);
        alert('Kitaplar yüklenirken hata oluştu.');
    }
}

