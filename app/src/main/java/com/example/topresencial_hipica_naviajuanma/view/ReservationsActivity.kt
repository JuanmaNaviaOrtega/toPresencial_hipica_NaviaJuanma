package com.example.topresencial_hipica_naviajuanma.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.topresencial_hipica_naviajuanma.R
import com.example.topresencial_hipica_naviajuanma.data.entity.Reservation
import com.example.topresencial_hipica_naviajuanma.view.adapters.ReservationAdapter
import com.example.topresencial_hipica_naviajuanma.viewmodel.ReservationViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.net.URLEncoder
import java.util.*

class ReservationsActivity : AppCompatActivity() {
    private lateinit var reservationViewModel: ReservationViewModel
    private lateinit var adapter: ReservationAdapter
    private var allReservations = listOf<Reservation>()
    private var filteredReservations = listOf<Reservation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservations)

        // Configuración del Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        // Botón de retroceso
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }

        reservationViewModel = ViewModelProvider(this).get(ReservationViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = ReservationAdapter { reservation ->
            showReservationOptions(reservation)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        reservationViewModel.allReservations.observe(this) { reservations ->
            reservations?.let {
                allReservations = it
                filteredReservations = it
                adapter.submitList(it)
            }
        }

        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            showAddReservationDialog()
        }

        findViewById<Button>(R.id.btnSearch).setOnClickListener {
            showSearchDialog()
        }
    }

    private fun showReservationOptions(reservation: Reservation) {
        val options = arrayOf("Editar", "Eliminar", "Enviar WhatsApp", "Cancelar")

        AlertDialog.Builder(this)
            .setTitle("Opciones de Reserva")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEditReservationDialog(reservation)
                    1 -> showDeleteConfirmation(reservation)
                    2 -> sendWhatsAppMessage(reservation)
                }
            }
            .show()
    }

    private fun showAddReservationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_reservation, null)
        val dateEditText = dialogView.findViewById<EditText>(R.id.etDate)
        val timeEditText = dialogView.findViewById<EditText>(R.id.etTime)

        dateEditText.setOnClickListener {
            showDatePicker { selectedDate ->
                dateEditText.setText(selectedDate)
            }
        }

        timeEditText.setOnClickListener {
            showTimePicker { selectedTime ->
                timeEditText.setText(selectedTime)
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Nueva Reserva")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val riderName = dialogView.findViewById<EditText>(R.id.etRiderName).text.toString()
                val phone = dialogView.findViewById<EditText>(R.id.etPhone).text.toString()
                val horseName = dialogView.findViewById<EditText>(R.id.etHorseName).text.toString()
                val date = dateEditText.text.toString()
                val time = timeEditText.text.toString()
                val comments = dialogView.findViewById<EditText>(R.id.etComments).text.toString()

                if (validateReservationInput(riderName, phone, horseName, date, time)) {
                    val reservation = Reservation(
                        riderName = riderName,
                        phone = phone,
                        horseName = horseName,
                        date = date,
                        time = time,
                        comments = comments
                    )

                    reservationViewModel.insert(reservation)
                    sendWhatsAppMessage(reservation)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun validateReservationInput(
        riderName: String,
        phone: String,
        horseName: String,
        date: String,
        time: String
    ): Boolean {
        if (riderName.isEmpty() || phone.isEmpty() || horseName.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }

        if (time != "10:00" && time != "11:00") {
            Toast.makeText(this, "Solo se permiten paseos a las 10:00 o 11:00", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun showEditReservationDialog(reservation: Reservation) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_reservation, null)

        dialogView.findViewById<EditText>(R.id.etRiderName).setText(reservation.riderName)
        dialogView.findViewById<EditText>(R.id.etPhone).setText(reservation.phone)
        dialogView.findViewById<EditText>(R.id.etHorseName).setText(reservation.horseName)
        val dateEditText = dialogView.findViewById<EditText>(R.id.etDate)
        dateEditText.setText(reservation.date)
        val timeEditText = dialogView.findViewById<EditText>(R.id.etTime)
        timeEditText.setText(reservation.time)
        dialogView.findViewById<EditText>(R.id.etComments).setText(reservation.comments)

        dateEditText.setOnClickListener {
            showDatePicker { selectedDate ->
                dateEditText.setText(selectedDate)
            }
        }

        timeEditText.setOnClickListener {
            showTimePicker { selectedTime ->
                timeEditText.setText(selectedTime)
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Editar Reserva")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val riderName = dialogView.findViewById<EditText>(R.id.etRiderName).text.toString()
                val phone = dialogView.findViewById<EditText>(R.id.etPhone).text.toString()
                val horseName = dialogView.findViewById<EditText>(R.id.etHorseName).text.toString()
                val date = dateEditText.text.toString()
                val time = timeEditText.text.toString()
                val comments = dialogView.findViewById<EditText>(R.id.etComments).text.toString()

                if (validateReservationInput(riderName, phone, horseName, date, time)) {
                    val updatedReservation = reservation.copy(
                        riderName = riderName,
                        phone = phone,
                        horseName = horseName,
                        date = date,
                        time = time,
                        comments = comments
                    )

                    reservationViewModel.update(updatedReservation)
                    Toast.makeText(this, "Reserva actualizada", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showDeleteConfirmation(reservation: Reservation) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que quieres eliminar esta reserva?")
            .setPositiveButton("Eliminar") { _, _ ->
                reservationViewModel.delete(reservation)
                Toast.makeText(this, "Reserva eliminada", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showSearchDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_search, null)
        val dateEditText = dialogView.findViewById<EditText>(R.id.etSearchDate)
        val timeEditText = dialogView.findViewById<EditText>(R.id.etSearchTime)

        dateEditText.setOnClickListener {
            showDatePicker { selectedDate ->
                dateEditText.setText(selectedDate)
            }
        }

        timeEditText.setOnClickListener {
            showTimePicker { selectedTime ->
                timeEditText.setText(selectedTime)
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Buscar Reservas")
            .setView(dialogView)
            .setPositiveButton("Buscar") { _, _ ->
                val date = dateEditText.text.toString()
                val time = timeEditText.text.toString()

                if (date.isNotEmpty() && time.isNotEmpty()) {
                    filterReservations(date, time)
                } else {
                    Toast.makeText(this, "Por favor ingrese fecha y hora", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Mostrar Todas") { _, _ ->
                showAllReservations()
            }
            .setNeutralButton("Cancelar", null)
            .show()
    }

    private fun filterReservations(date: String, time: String) {
        filteredReservations = allReservations.filter {
            it.date == date && it.time == time
        }
        adapter.submitList(filteredReservations)

        if (filteredReservations.isEmpty()) {
            Toast.makeText(this, "No hay reservas para esta fecha y hora", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAllReservations() {
        filteredReservations = allReservations
        adapter.submitList(allReservations)
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, day ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, day)
                }

                // Validar que sea sábado (6) o domingo (7)
                if (selectedDate.get(Calendar.DAY_OF_WEEK) !in listOf(Calendar.SATURDAY, Calendar.SUNDAY)) {
                    Toast.makeText(this, "Solo se permiten reservas los sábados y domingos", Toast.LENGTH_LONG).show()
                    return@DatePickerDialog
                }

                onDateSelected("${day}/${month + 1}/$year")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Mejora: Deshabilitar días no permitidos en el calendario
        datePicker.datePicker.minDate = calendar.timeInMillis
        datePicker.show()
    }

    private fun showTimePicker(onTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hour, minute ->
                // Solo permitir horas exactas (sin minutos)
                if (minute != 0) {
                    Toast.makeText(this, "Solo se permiten horas en punto (minutos en 00)", Toast.LENGTH_SHORT).show()
                    return@TimePickerDialog
                }

                // Solo 10:00 o 11:00
                if (hour !in 10..11) {
                    Toast.makeText(this, "Solo se permiten paseos a las 10:00 o 11:00", Toast.LENGTH_LONG).show()
                    return@TimePickerDialog
                }

                onTimeSelected("$hour:${if (minute < 10) "0$minute" else minute}")
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            0, // Minutos siempre en 0
            true
        ).show()
    }

    private fun sendWhatsAppMessage(reservation: Reservation) {
        val message = "Reserva confirmada:\n" +
                "Caballo: ${reservation.horseName}\n" +
                "Fecha: ${reservation.date}\n" +
                "Hora: ${reservation.time}"

        try {
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=${reservation.phone}&text=${URLEncoder.encode(message, "UTF-8")}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al abrir WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
}