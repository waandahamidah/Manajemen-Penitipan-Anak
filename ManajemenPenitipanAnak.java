import java.text.SimpleDateFormat;  // Untuk memformat tanggal dan waktu
import java.util.Date;  // Pastikan ini ada untuk menggunakan Date
import java.util.*;  // Untuk utilitas seperti Scanner, List, dll.
import java.sql.*;  // Untuk operasi basis data SQL

// Antarmuka yang mendefinisikan operasi CRUD dasar
interface CRUDOperations {    
    void create();  // Menambahkan data baru
    void read();  // Menampilkan data
    void update();  // Memperbarui data
    void delete();  // Menghapus data
}

// Kelas abstrak yang mewakili entitas umum di penitipan anak
abstract class EntityPenitipan {
    protected String id;  // ID entitas
    protected String nama;  // Nama entitas

    // Konstruktor untuk menginisialisasi id dan nama entitas
    public EntityPenitipan(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    // Metode abstrak untuk menampilkan detail entitas
    public abstract void displayDetails();  
}

// Kelas Anak yang mewarisi dari EntityPenitipan
class Anak extends EntityPenitipan {
    private int umur;  // Umur anak
    private String statusKesehatan;  // Status kesehatan anak

    // Konstruktor untuk inisialisasi anak
    public Anak(String id, String nama, int umur, String statusKesehatan) {
        super(id, nama);  // Memanggil konstruktor superclass
        this.umur = umur;
        this.statusKesehatan = statusKesehatan;
    }

    // Implementasi metode displayDetails untuk menampilkan detail anak
    @Override
    public void displayDetails() {
        // Menampilkan ID, nama, umur, dan status kesehatan anak
        System.out.printf("%-10s%-20s%-10d%-20s\n", id, nama, umur, statusKesehatan);
    }

    // Getter untuk umur anak
    public int getUmur() {
        return umur;
    }
}

// Kelas utama untuk manajemen penitipan anak
public class ManajemenPenitipanAnak implements CRUDOperations {
    // URL, username, dan password untuk koneksi basis data
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/penitipan_anak";  
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "naisya1";

    // Daftar anak yang terdaftar
    private List<Anak> daftarAnak = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);  // Untuk mengambil input dari pengguna

    // Menentukan warna untuk tampilan
    private static final String RESET = "\033[0m";
    private static final String GREEN = "\033[0;32m";
    private static final String RED = "\033[0;31m";
    private static final String CYAN = "\033[0;36m";
    private static final String YELLOW = "\033[0;33m";
    private static final String MAGENTA = "\033[0;35m";  // Menambahkan warna ungu

    // Fungsi utama untuk menjalankan aplikasi
    public static void main(String[] args) {
        ManajemenPenitipanAnak app = new ManajemenPenitipanAnak();
        app.run();  // Menjalankan aplikasi
    }

    // Metode untuk menjalankan aplikasi dengan pilihan menu
    private void run() {
        boolean keluar = false;
        System.out.println(YELLOW + "Selamat Datang di Sistem Manajemen Penitipan Anak" + RESET);
        tampilkanTanggalWaktuSaatIni();  // Menampilkan tanggal dan waktu saat ini

        // Loop untuk menampilkan menu utama dan menerima input pilihan
        while (!keluar) {
            System.out.println(MAGENTA + "Menu Utama:");
            System.out.println("1. Tambah Data Anak Baru");
            System.out.println("2. Tampilkan Semua Data Anak");
            System.out.println("3. Perbarui Informasi Anak");
            System.out.println("4. Hapus Data Anak");
            System.out.println("5. Hitung Jumlah Anak Terdaftar");
            System.out.println("6. Cari Anak Berdasarkan ID");
            System.out.println("7. Cari Anak Berdasarkan Kondisi Kesehatan");
            System.out.println("8. Keluar Sistem" + RESET);

            // Meminta pengguna memilih menu
            System.out.print(YELLOW + "Pilih menu yang diinginkan: " + RESET);
            int pilihan = masukkanAngkaInteger("Pilihan");

            // Melakukan aksi berdasarkan pilihan menu
            switch (pilihan) {
                case 1 -> create();  // Menambahkan data anak baru
                case 2 -> read();  // Menampilkan semua data anak
                case 3 -> update();  // Memperbarui data anak
                case 4 -> delete();  // Menghapus data anak
                case 5 -> hitungTotalAnak();  // Menghitung total anak terdaftar
                case 6 -> cariAnakBerdasarkanID();  // Mencari anak berdasarkan ID
                case 7 -> cariAnakBerdasarkanStatusKesehatan();  // Mencari anak berdasarkan status kesehatan
                case 8 -> {  // Keluar dari sistem
                    keluar = true;
                    System.out.println(GREEN + "Terima kasih telah menggunakan sistem." + RESET);
                }
                default -> System.out.println(RED + "Pilihan tidak valid. Silakan coba lagi." + RESET);  // Menangani pilihan yang salah
            }
        }
    }

    // Metode untuk menampilkan tanggal dan waktu saat ini
    private void tampilkanTanggalWaktuSaatIni() {
        Date tanggal = new Date(System.currentTimeMillis());
        SimpleDateFormat formatTanggal = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println(YELLOW + "Tanggal dan Waktu: " + formatTanggal.format(tanggal) + RESET);
    }

    // Metode untuk menerima input angka integer dari pengguna dengan validasi
    private int masukkanAngkaInteger(String prompt) {
        while (true) {
            try {
                System.out.print(CYAN + prompt + ": " + RESET);
                int angka = Integer.parseInt(scanner.nextLine());
                if (angka <= 0 || angka >= 12) {
                    System.out.println(RED + "Umur harus antara 1 dan 12 tahun." + RESET);
                } else {
                    return angka;
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + prompt + " harus berupa angka." + RESET);
            }
        }
    }

    // Metode untuk menerima input string dari pengguna dengan validasi
    private String masukkanString(String prompt) {
        while (true) {
            System.out.print(CYAN + prompt + ": " + RESET);
            String input = scanner.nextLine();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(RED + prompt + " tidak boleh kosong." + RESET);
        }
    }

    // Implementasi metode create untuk menambahkan data anak baru
    @Override
    public void create() {
        String id = masukkanString("ID Anak");
        String nama = masukkanString("Nama Anak");
        int umur = masukkanAngkaInteger("Umur Anak");
        String statusKesehatan = masukkanString("Status Kesehatan Anak");

        Anak anak = new Anak(id, nama, umur, statusKesehatan);  // Membuat objek anak
        daftarAnak.add(anak);  // Menambahkan anak ke dalam daftar

        // Menyimpan data anak ke basis data
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "INSERT INTO anak (id, nama, umur, status_kesehatan) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, nama);
            statement.setInt(3, umur);
            statement.setString(4, statusKesehatan);
            statement.executeUpdate();
            System.out.println(GREEN + "Data berhasil disimpan." + RESET);
        } catch (SQLException e) {
            System.out.println(RED + "Terjadi kesalahan: " + e.getMessage() + RESET);
        }
    }

    // Implementasi metode read untuk menampilkan semua data anak
    @Override
    public void read() {
        daftarAnak.clear();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM anak";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Menampilkan header tabel
            System.out.println(GREEN + "+-----------------------------------------------------+");
            System.out.printf("%-10s%-20s%-10s%-20s\n", "ID", "Nama", "Umur", "Status Kesehatan");
            System.out.println("+-----------------------------------------------------+" + RESET);

            // Menampilkan data anak satu per satu
            while (resultSet.next()) {
                Anak anak = new Anak(
                    resultSet.getString("id"),
                    resultSet.getString("nama"),
                    resultSet.getInt("umur"),
                    resultSet.getString("status_kesehatan")
                );
                daftarAnak.add(anak);
                anak.displayDetails();  // Menampilkan detail anak
            }
            System.out.println("+-----------------------------------------------------+");
        } catch (SQLException e) {
            System.out.println(RED + "Terjadi kesalahan: " + e.getMessage() + RESET);
        }
    }

    // Implementasi metode update untuk memperbarui data anak
    @Override
    public void update() {
        String id = masukkanString("Masukkan ID Anak yang ingin diperbarui");
        String namaBaru = masukkanString("Nama Baru");
        int umurBaru = masukkanAngkaInteger("Umur Baru");
        String statusKesehatanBaru = masukkanString("Status Kesehatan Baru");

        // Memperbarui data anak di basis data
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "UPDATE anak SET nama = ?, umur = ?, status_kesehatan = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, namaBaru);
            statement.setInt(2, umurBaru);
            statement.setString(3, statusKesehatanBaru);
            statement.setString(4, id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(GREEN + "Data berhasil diperbarui." + RESET);
            } else {
                System.out.println(RED + "ID tidak ditemukan." + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED + "Terjadi kesalahan: " + e.getMessage() + RESET);
        }
    }

    // Implementasi metode delete untuk menghapus data anak berdasarkan ID
    @Override
    public void delete() {
        String id = masukkanString("Masukkan ID Anak yang ingin dihapus");

        // Menghapus data anak dari basis data
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "DELETE FROM anak WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println(GREEN + "Data berhasil dihapus." + RESET);
            } else {
                System.out.println(RED + "ID tidak ditemukan." + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED + "Terjadi kesalahan: " + e.getMessage() + RESET);
        }
    }

    // Fungsi untuk menghitung total anak terdaftar
    private void hitungTotalAnak() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT COUNT(*) AS total FROM anak";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                int total = resultSet.getInt("total");
                System.out.println(GREEN + "Total anak yang terdaftar: " + total + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED + "Terjadi kesalahan: " + e.getMessage() + RESET);
        }
    }

    // Fungsi untuk mencari anak berdasarkan ID
    private void cariAnakBerdasarkanID() {
        String id = masukkanString("Masukkan ID Anak yang ingin dicari");
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM anak WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Anak anak = new Anak(
                    resultSet.getString("id"),
                    resultSet.getString("nama"),
                    resultSet.getInt("umur"),
                    resultSet.getString("status_kesehatan")
                );
                anak.displayDetails();
            } else {
                System.out.println(RED + "Anak dengan ID tersebut tidak ditemukan." + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED + "Terjadi kesalahan: " + e.getMessage() + RESET);
        }
    }

    // Fungsi untuk mencari anak berdasarkan status kesehatan
    private void cariAnakBerdasarkanStatusKesehatan() {
        String status = masukkanString("Masukkan Status Kesehatan Anak yang ingin dicari");
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM anak WHERE status_kesehatan = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, status);
            ResultSet resultSet = statement.executeQuery();

            boolean found = false;
            while (resultSet.next()) {
                Anak anak = new Anak(
                    resultSet.getString("id"),
                    resultSet.getString("nama"),
                    resultSet.getInt("umur"),
                    resultSet.getString("status_kesehatan")
                );
                anak.displayDetails();
                found = true;
            }
            if (!found) {
                System.out.println(RED + "Tidak ada anak dengan status kesehatan tersebut." + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED + "Terjadi kesalahan: " + e.getMessage() + RESET);
        }
    }
}