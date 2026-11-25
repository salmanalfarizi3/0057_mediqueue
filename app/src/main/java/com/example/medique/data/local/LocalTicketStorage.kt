package com.example.medique.data.local



import android.content.Context
import com.example.medique.data.model.LocalTicketFile
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class LocalTicketStorage(
    private val context: Context,
    private val gson: Gson = Gson()
) {
    private val ticketsDir: File
        get() = File(context.filesDir, "tickets").apply {
            if (!exists()) mkdirs()
        }

    // Create Local File (REQ-26)
    suspend fun saveTicket(ticket: LocalTicketFile): Result<LocalTicketFile> =
        withContext(Dispatchers.IO) {
            runCatching {
                val safeName = ticket.namaFile
                    .ifBlank { "tiket_${ticket.pendaftaranId}_${ticket.nomorAntrean}.json" }
                    .let { if (it.endsWith(".json")) it else "$it.json" }
                    .replace(Regex("[^a-zA-Z0-9._-]"), "_")

                val finalTicket = ticket.copy(namaFile = safeName)
                val targetFile = File(ticketsDir, safeName)
                targetFile.writeText(gson.toJson(finalTicket), Charsets.UTF_8)
                finalTicket
            }
        }

    // Read All Local Files (REQ-27)
    suspend fun getAllTickets(): List<LocalTicketFile> = withContext(Dispatchers.IO) {
        ticketsDir.listFiles { file -> file.isFile && file.extension == "json" }
            ?.mapNotNull { file ->
                runCatching {
                    gson.fromJson(file.readText(Charsets.UTF_8), LocalTicketFile::class.java)
                }.getOrNull()
            }
            ?.sortedByDescending { it.pendaftaranId }
            ?: emptyList()
    }

    // Read Single Local File (REQ-27)
    suspend fun getTicketByFileName(fileName: String): LocalTicketFile? =
        withContext(Dispatchers.IO) {
            val file = File(ticketsDir, fileName)
            if (!file.exists()) return@withContext null
            runCatching {
                gson.fromJson(file.readText(Charsets.UTF_8), LocalTicketFile::class.java)
            }.getOrNull()
        }

    // Update Local File - catatan_pribadi (REQ-28)
    suspend fun updatePersonalNote(fileName: String, newNote: String): Result<LocalTicketFile> =
        withContext(Dispatchers.IO) {
            runCatching {
                val file = File(ticketsDir, fileName)
                require(file.exists()) { "File tiket tidak ditemukan" }
                val existing = gson.fromJson(file.readText(Charsets.UTF_8), LocalTicketFile::class.java)
                val updated = existing.copy(catatanPribadi = newNote)
                file.writeText(gson.toJson(updated), Charsets.UTF_8)
                updated
            }
        }

    // Delete Local File (REQ-29)
    suspend fun deleteTicket(fileName: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val file = File(ticketsDir, fileName)
            require(file.exists()) { "File tiket tidak ditemukan" }
            if (!file.delete()) error("Gagal menghapus file tiket")
        }
    }

    // Search Local File berdasarkan nomor antrean atau nama poli (REQ-30)
    suspend fun searchTickets(query: String): List<LocalTicketFile> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return getAllTickets()
        return getAllTickets().filter { item ->
            item.nomorAntrean.lowercase().contains(q) ||
                    item.namaPoli.lowercase().contains(q)
        }
    }
}