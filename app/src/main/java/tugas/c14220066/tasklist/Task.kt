package tugas.c14220066.tasklist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task (
    var nama: String,
    var tanggal: String,
    var deskripsi: String,
    var selesai: Boolean,
    var favorit: Boolean
) : Parcelable
