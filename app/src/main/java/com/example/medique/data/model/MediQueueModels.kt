package com.example.medique.data.model



import com.google.gson.annotations.SerializedName

enum class UserRole {
    ADMIN, PASIEN
}

enum class QueueStatus {
    MENUNGGU, DIPANGGIL, SELESAI, DIBATALKAN
}

data class UserDto(
    val id: Int,
    val nik: String,
    @SerializedName("nama_lengkap") val namaLengkap: String,
    val email: String,
    @SerializedName("no_telepon") val noTelepon: String,
    val alamat: String,
    val role: UserRole
)

data class AuthResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    val user: UserDto
)

data class PoliDto(
    val id: Int,
    @SerializedName("kode_poli") val kodePoli: String,
    @SerializedName("nama_poli") val namaPoli: String,
    @SerializedName("nama_dokter_jaga") val namaDokterJaga: String,
    @SerializedName("jam_operasional") val jamOperasional: String,
    @SerializedName("kuota_harian") val kuotaHarian: Int,
    @SerializedName("sisa_kuota") val sisaKuota: Int
)

data class PendaftaranDto(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("poli_id") val poliId: Int,
    @SerializedName("nama_poli") val namaPoli: String? = null,
    @SerializedName("nama_dokter_jaga") val namaDokterJaga: String? = null,
    @SerializedName("jam_operasional") val jamOperasional: String? = null,
    @SerializedName("nama_pasien") val namaPasien: String? = null,
    @SerializedName("tanggal_kunjungan") val tanggalKunjungan: String,
    val keluhan: String,
    @SerializedName("nomor_antrean") val nomorAntrean: String,
    @SerializedName("status_antrean") val statusAntrean: QueueStatus
)

/**
 * Entitas file lokal (.json) di Context.filesDir (Tanpa Room Database)
 */
data class LocalTicketFile(
    @SerializedName("nama_file") val namaFile: String,
    @SerializedName("pendaftaran_id") val pendaftaranId: Int,
    @SerializedName("nomor_antrean") val nomorAntrean: String,
    @SerializedName("nama_pasien") val namaPasien: String,
    @SerializedName("nama_poli") val namaPoli: String,
    @SerializedName("dokter_jaga") val dokterJaga: String,
    @SerializedName("tanggal_kunjungan") val tanggalKunjungan: String,
    @SerializedName("jam_operasional") val jamOperasional: String,
    @SerializedName("catatan_pribadi") val catatanPribadi: String = ""
)