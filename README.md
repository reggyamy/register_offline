# 📋 Register Offline
Aplikasi Android untuk pendaftaran member secara offline-first, dengan sinkronisasi data ke server saat koneksi tersedia.

---

## 🚀 Cara Menjalankan

### 1. Clone Repository
```bash
git clone https://github.com/username/register-offline.git
cd register-offline
```

### 2. Setup `local.properties`
Tambahkan BASE_URL di file `local.properties` (buat jika belum ada):
```properties
BASE_URL=https://api-test.partaiperindo.com/api/v1/
```

### 3. Run
Buka di Android Studio → **Run** atau `Shift + F10`

> **Requirement:** Android Studio Hedgehog+, minSdk 24, JDK 17

---

## 🛠 Tech Stack

| Layer | Library |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | Clean Architecture (MVVM) |
| DI | Hilt |
| Network | Retrofit + OkHttp + Chucker |
| Local DB | Room |
| Preferences | EncryptedSharedPreferences + DataStore |
| Async | Kotlin Flow + Coroutines |
| Image | Coil |

---

## 🏗 Struktur Project

```
app/
├── data/
│   ├── local/
│   │   ├── dao/          → Room DAO
│   │   ├── entity/       → Room Entity
│   │   └── preferences/  → AuthPreferences, UserDataStore
│   ├── remote/
│   │   ├── network/      → ApiService, Retrofit setup
│   │   ├── request/      → Request models
│   │   └── response/     → Response models
│   ├── utils/
│   │   └── mapper/       → data mapper
│   └── repository/       → Implementasi Repository
├── domain/
│   ├── model/            → Domain models
│   ├── repository/       → Interface Repository
│   └── usecase/          → Use cases
├── di/
│   ├── hilt module/      → hilt module
└── presentation/
    ├── ui/               → screen & ViewModel
    ├── theme/            → theme
    └── navigation/       → Navigation graph
```

---

## 🔄 Alur Aplikasi

### 1. Login & Session
```
User input email + password
        ↓
Hit POST /login → dapat token
        ↓
Token disimpan ke EncryptedSharedPreferences
        ↓
Tidak perlu login ulang selama token masih ada
```

### 2. Profile (Smart Fetch)
```
User buka halaman Profile
        ↓
Cek DataStore → ada data?
   ├── Ada  → tampilkan dari lokal (tidak hit API)
   └── Tidak ada → hit GET /profile
                        ↓
                   Simpan ke DataStore
                        ↓
                   Tampilkan data
```

### 3. Tambah Member (Offline-First)
```
User isi form member + foto KTP
        ↓
Simpan ke Room Database dengan status "Draft"
        ↓
Tampil di tab Draft
        ↓
Saat koneksi tersedia → Upload ke server (multipart)
        ↓
Berhasil → data dihapus dari Room → pindah ke tab Sudah Di-Upload
```

### 4. Bulk Upload
```
User klik "Upload Semua"
        ↓
Ambil semua data Draft dari Room
        ↓
Loop satu per satu → hit POST /member (multipart)
        ↓
Berhasil → hapus dari Room
Gagal    → skip, tetap di Draft
```

---

## 📱 Fitur

- ✅ Login dengan penyimpanan token aman (tidak login berulang)
- ✅ Tambah member secara offline (disimpan sebagai Draft)
- ✅ Upload member satu per satu atau sekaligus (Bulk Upload)
- ✅ Profile fetch sekali, selanjutnya dari lokal
- ✅ Foto KTP dengan kompresi sebelum upload
- ✅ Logout dengan konfirmasi (menghapus token & session & data user & data member)

---

## 🔐 Keamanan

| Data | Penyimpanan |
|---|---|
| Token | EncryptedSharedPreferences (AES256) |
| Profile (nama & email) | DataStore |
| Data member | Room Database |

---

## 📹 Demo

> Lihat file `demo.mp4` 

---

## 👤 Author
**Reggya** — Technical Test Mobile Developer
