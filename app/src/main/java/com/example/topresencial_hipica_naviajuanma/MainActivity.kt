package com.example.topresencial_hipica_naviajuanma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.topresencial_hipica_naviajuanma.view.ReservationsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnEjercicio1 = findViewById<Button>(R.id.btnEjercicio1)
        btnEjercicio1.setOnClickListener {
            startActivity(Intent(this, ReservationsActivity::class.java))
        }
    }
}