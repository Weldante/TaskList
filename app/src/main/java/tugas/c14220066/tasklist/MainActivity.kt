package tugas.c14220066.tasklist

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var _nama : MutableList<String>
    private lateinit var _tanggal : MutableList<String>
    private lateinit var _deskripsi : MutableList<String>
    private lateinit var _selesai : MutableList<Boolean>

    private var arTaskList = arrayListOf<Task>()

    private lateinit var _rvTaskList : RecyclerView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _rvTaskList = findViewById<RecyclerView>(R.id.rvTaskList)
        SiapkanData()
        TambahData()
        TampilkanData()

        val _btnTambah = findViewById<Button>(R.id.btnTambah)
        _btnTambah.setOnClickListener {
            val intent = Intent(this@MainActivity, Simpan::class.java)
            startActivity(intent)
        }

        val dataTask = intent.getParcelableExtra<Task>("DataTask", Task::class.java)
        if (dataTask != null) {
            val position = intent.getParcelableExtra<Int>("Position", Int::class.java)
            if (position != null) {
                _nama.set(position, dataTask.nama)
                _tanggal.set(position, dataTask.tanggal)
                _deskripsi.set(position, dataTask.deskripsi)
                _selesai.set(position, dataTask.selesai)
                TambahData()
                TampilkanData()
            }
            else {
                _nama.add(dataTask.nama)
                _tanggal.add(dataTask.tanggal)
                _deskripsi.add(dataTask.deskripsi)
                _selesai.add(dataTask.selesai)
                TambahData()
                TampilkanData()
            }
        }

    }

    fun SiapkanData () {
        _nama = mutableListOf<String>("Tugas PABA", "hebat")
        _tanggal = mutableListOf<String>("20 Desember 2024", "20 Agustus")
        _deskripsi = mutableListOf<String>("Lihat Classroom", "Test")
        _selesai = mutableListOf<Boolean>(true, false)
    }

    fun TambahData() {
        arTaskList.clear()
        for (position in _nama.indices) {
            val data = Task(
                _nama[position],
                _tanggal[position],
                _deskripsi[position],
                _selesai[position]
            )
            arTaskList.add(data)
        }
    }

    fun TampilkanData() {
        _rvTaskList.layoutManager = LinearLayoutManager(this)
        val adapterTaskList = AdapterRecView(arTaskList)
        _rvTaskList.adapter = adapterTaskList

        adapterTaskList.setOnItemClickCallback(object : AdapterRecView.OnItemClickCallback{
            override fun delData(position: Int) {
                _nama.removeAt(position)
                _tanggal.removeAt(position)
                _deskripsi.removeAt(position)
                _selesai.removeAt(position)
                TambahData()
                TampilkanData()
            }

            override fun editData(data: Task, position: Int) {
                val intent = Intent(this@MainActivity, Simpan::class.java)
                intent.putExtra("DataTask", data)
                intent.putExtra("Position", position)
                startActivity(intent)
            }

            override fun taskSelesai(position: Int) {
                _selesai.set(position, true)
                TambahData()
                TampilkanData()
            }

        })
    }


}