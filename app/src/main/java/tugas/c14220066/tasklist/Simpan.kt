package tugas.c14220066.tasklist

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Simpan : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_simpan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val _etNama = findViewById<EditText>(R.id.etNama)
        val _etTanggal = findViewById<EditText>(R.id.etTanggal)
        val _etDeskripsi = findViewById<EditText>(R.id.etDeskripsi)
        val _btnSimpan = findViewById<Button>(R.id.btnSimpan)

        val dataTask = intent.getParcelableExtra<Task>("DataTask", Task::class.java)
        if (dataTask != null) {
            val position = intent.getParcelableExtra<Int>("Position", Int::class.java)

            _etNama.setText(dataTask.nama)
            _etTanggal.setText(dataTask.tanggal)
            _etDeskripsi.setText(dataTask.deskripsi)

            _btnSimpan.setOnClickListener {
                val data = Task(_etNama.text.toString(), _etTanggal.text.toString(),
                    _etDeskripsi.text.toString(), false, false)
                val intent = Intent(this@Simpan, MainActivity::class.java)
                intent.putExtra("DataTask", data)
                intent.putExtra("Position", position)
                startActivity(intent)
            }
        }
        else {
            _btnSimpan.setOnClickListener {
                val data = Task(_etNama.text.toString(), _etTanggal.text.toString(),
                    _etDeskripsi.text.toString(), false, false)
                val intent = Intent(this@Simpan, MainActivity::class.java)
                intent.putExtra("DataTask", data)
                startActivity(intent)
            }
        }

    }
}