package com.example.apijson

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MiApiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Ajustar padding para barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener referencia al TextView del layout XML
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        // Inicializar el ViewModel
        viewModel = ViewModelProvider(this)[MiApiViewModel::class.java]

        // Observar los cambios en el resultado y actualizar la UI
        viewModel.resultadoTexto.observe(this) { nuevoTexto ->
            tvResultado.text = nuevoTexto
        }
    }
}