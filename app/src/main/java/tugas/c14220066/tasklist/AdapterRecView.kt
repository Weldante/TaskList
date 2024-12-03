package tugas.c14220066.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterRecView (private val listTask: ArrayList<Task>) : RecyclerView.
    Adapter<AdapterRecView.ListViewHolder> () {

        private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {

        fun delData(position: Int)
        fun editData(data: Task, position: Int)
        fun taskSelesai(position: Int)
        fun favorit(position: Int)
        fun unFavorit(position: Int)

    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var _namaTask = itemView.findViewById<TextView>(R.id.tvNama)
            var _tanggalTask = itemView.findViewById<TextView>(R.id.tvTanggal)
            var _deskripsiTask = itemView.findViewById<TextView>(R.id.tvDeskripsi)
            var _btnHapus = itemView.findViewById<Button>(R.id.btnHapus)
            var _btnEdit = itemView.findViewById<Button>(R.id.btnEdit)
            var _btnSelesai = itemView.findViewById<Button>(R.id.btnSelesai)
            var _tvSelesai = itemView.findViewById<TextView>(R.id.tvSelesai)
            var _btnFavorit = itemView.findViewById<Button>(R.id.btnFavorit)
            var _btnUnfavorit = itemView.findViewById<Button>(R.id.btnUnfavorit)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTask.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var task = listTask[position]

        holder._namaTask.setText(task.nama)
        holder._tanggalTask.setText(task.tanggal)
        holder._deskripsiTask.setText(task.deskripsi)

        holder._btnHapus.setOnClickListener {
            onItemClickCallback.delData(position)
        }

        holder._btnEdit.setOnClickListener {
            onItemClickCallback.editData(listTask[position], position)
        }

        holder._btnSelesai.setOnClickListener {
            onItemClickCallback.taskSelesai(position)
        }

        holder._btnFavorit.setOnClickListener {
            holder._btnFavorit.visibility = View.GONE
            holder._btnUnfavorit.visibility = View.VISIBLE
            onItemClickCallback.favorit(position)
        }

        holder._btnUnfavorit.setOnClickListener {
            holder._btnFavorit.visibility = View.VISIBLE
            holder._btnUnfavorit.visibility = View.GONE
            onItemClickCallback.unFavorit(position)
        }

        if (task.selesai) {
            holder._btnHapus.visibility = View.GONE
            holder._btnEdit.visibility = View.GONE
            holder._btnSelesai.visibility = View.GONE
            holder._tvSelesai.visibility = View.VISIBLE
        }

    }

}