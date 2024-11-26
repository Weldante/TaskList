package tugas.c14220066.tasklist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task (
    var nama: String,
    var tanggal: String,
    val deskripsi: String,
    val selesai: Boolean
) : Parcelable
