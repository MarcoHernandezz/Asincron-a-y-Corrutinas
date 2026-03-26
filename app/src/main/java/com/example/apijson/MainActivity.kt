package com.example.apijson

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import coil.load

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

        // Referencias a las vistas del layout XML
        val tvNombre = findViewById<TextView>(R.id.tvNombre)
        val tvTitulo = findViewById<TextView>(R.id.tvTitulo)
        val tvFamilia = findViewById<TextView>(R.id.tvFamilia)
        val ivPersonaje = findViewById<ImageView>(R.id.ivPersonaje)
        val btnAnterior = findViewById<Button>(R.id.btnAnterior)
        val btnSiguiente = findViewById<Button>(R.id.btnSiguiente)
        val btnVerTodos = findViewById<Button>(R.id.btnVerTodos)

        // Inicializar el ViewModel
        viewModel = ViewModelProvider(this)[MiApiViewModel::class.java]

        // Observar el personaje actual
        viewModel.personajeActual.observe(this) { personaje ->
            personaje?.let {
                tvNombre.text = it.fullName
                tvTitulo.text = it.title
                tvFamilia.text = it.family
                
                // Cargar imagen con Coil
                ivPersonaje.load(it.imageUrl) {
                    crossfade(true)
                    placeholder(android.R.drawable.progress_horizontal)
                    error(android.R.drawable.stat_notify_error)
                }
            }
        }

        // Observar errores
        viewModel.errorMsg.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // Configurar botones de navegación
        btnAnterior.setOnClickListener {
            viewModel.anteriorPersonaje()
        }

        btnSiguiente.setOnClickListener {
            viewModel.siguientePersonaje()
        }

        // Mostrar lista completa de personajes en un diálogo
        btnVerTodos.setOnClickListener {
            val nombres = viewModel.listaPersonajesNombres.toTypedArray()
            if (nombres.isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Lista de Personajes")
                    .setItems(nombres) { _, which ->
                        viewModel.seleccionarPersonaje(which)
                    }
                    .setNegativeButton("Cerrar", null)
                    .show()
            } else {
                Toast.makeText(this, "Cargando lista...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}