package tugas.c14220066.tasklist

import android.content.Intent
import android.content.SharedPreferences
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private var _nama : MutableList<String> = emptyList<String>().toMutableList()
    private var _tanggal : MutableList<String> = emptyList<String>().toMutableList()
    private var _deskripsi : MutableList<String> = emptyList<String>().toMutableList()
    private var _selesai : MutableList<Boolean> = emptyList<Boolean>().toMutableList()
    private var _favorit : MutableList<Boolean> = emptyList<Boolean>().toMutableList()

    lateinit var sp : SharedPreferences
    var jumlahFavorit : Int = 0

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

        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
//        val gsonInit = Gson()
//        val editorInit = sp.edit()
//        arTaskList.add(Task("Test", "12345", "testing", false, true))
//        val jsonInit = gsonInit.toJson(arTaskList)
//        editorInit.putString("spTask", jsonInit)
//        editorInit.apply()
//        val gson = Gson()
//        val isiSP = sp.getString("spTask", null)
//        val type = object : TypeToken<ArrayList<Task>>() {}.type
//        if (isiSP != null) arTaskList = gson.fromJson(isiSP, type)

        _rvTaskList = findViewById<RecyclerView>(R.id.rvTaskList)
//        if (arTaskList.size == 0) {
//            SiapkanData()
//        } else {
//            arTaskList.forEach {
//                _nama.add(it.nama)
//                _tanggal.add(it.tanggal)
//                _deskripsi.add(it.deskripsi)
//                _selesai.add(it.selesai)
//            }
//            arTaskList.clear()
//        }
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
                _favorit.set(position, dataTask.favorit)
                TambahData()
                TampilkanData()
            }
            else {
                _nama.add(dataTask.nama)
                _tanggal.add(dataTask.tanggal)
                _deskripsi.add(dataTask.deskripsi)
                _selesai.add(dataTask.selesai)
                _favorit.add(dataTask.favorit)
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
        val gson = Gson()
        val isiSP = sp.getString("spTask", null)
        val type = object : TypeToken<ArrayList<Task>>() {}.type
        if (isiSP != null) arTaskList = gson.fromJson(isiSP, type)
        jumlahFavorit = arTaskList.size
//        val isiJumlah = sp.getString("jumlah", null)
//        val jumlahType = object : TypeToken<Int>() {}.type
//        if (isiJumlah != null) jumlahFavorit = gson.fromJson<Int>(isiJumlah, jumlahType).toString().toInt()
        for (position in _nama.indices) {
            val data = Task(
                _nama[position],
                _tanggal[position],
                _deskripsi[position],
                _selesai[position],
                _favorit[position]
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
                val position = position - jumlahFavorit
                _nama.removeAt(position)
                _tanggal.removeAt(position)
                _deskripsi.removeAt(position)
                _selesai.removeAt(position)
                _favorit.removeAt(position)
                TambahData()
                TampilkanData()
            }

            override fun editData(data: Task, position: Int) {
                val position = position - jumlahFavorit
                val intent = Intent(this@MainActivity, Simpan::class.java)
                intent.putExtra("DataTask", data)
                intent.putExtra("Position", position)
                startActivity(intent)
            }

            override fun taskSelesai(position: Int) {
                val position = position - jumlahFavorit
                _selesai.set(position, true)
                TambahData()
                TampilkanData()
            }

            override fun favorit(position: Int) {
                arTaskList[position].favorit = true
                val gson = Gson()
                val isiSP = sp.getString("spTask", null)
                val type = object : TypeToken<ArrayList<Task>>() {}.type
                if (isiSP != null) {
                    var newTaskList = gson.fromJson<ArrayList<Task>>(isiSP, type)
                    newTaskList.add(arTaskList[position])
                    val editor = sp.edit()
                    val json = gson.toJson(newTaskList)
                    editor.putString("spTask", json)
                    editor.putString("jumlah", json)
                    editor.apply()
                }
                TambahData()
                TampilkanData()
            }

            @RequiresApi(35)
            override fun unFavorit(position: Int) {
                val gson = Gson()
                val isiSP = sp.getString("spTask", null)
                val type = object : TypeToken<ArrayList<Task>>() {}.type
                if (isiSP != null) {
                    var newTaskList = gson.fromJson<ArrayList<Task>>(isiSP, type)
                    newTaskList.remove(arTaskList[position])
                    val editor = sp.edit()
                    val json = gson.toJson(newTaskList)
                    editor.putString("spTask", json)
                    editor.apply()
                }
                _nama.addFirst(arTaskList[position].nama)
                _tanggal.addFirst(arTaskList[position].tanggal)
                _deskripsi.addFirst(arTaskList[position].deskripsi)
                _selesai.addFirst(arTaskList[position].selesai)
                _favorit.addFirst(false)
                TambahData()
                TampilkanData()
            }

        })
    }


}