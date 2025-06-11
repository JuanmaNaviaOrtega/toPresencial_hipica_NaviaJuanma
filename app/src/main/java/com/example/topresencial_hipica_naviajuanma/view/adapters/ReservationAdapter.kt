package com.example.topresencial_hipica_naviajuanma.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.topresencial_hipica_naviajuanma.R
import com.example.topresencial_hipica_naviajuanma.data.entity.Reservation


class ReservationAdapter(private val onClick: (Reservation) -> Unit) :
    ListAdapter<Reservation, ReservationAdapter.ReservationViewHolder>(ReservationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reservation, parent, false)
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = getItem(position)
        holder.bind(reservation)
        holder.itemView.setOnClickListener { onClick(reservation) }
    }

    class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvRiderName: TextView = itemView.findViewById(R.id.tvRiderName)
        private val tvHorseName: TextView = itemView.findViewById(R.id.tvHorseName)
        private val tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)

        fun bind(reservation: Reservation) {
            tvRiderName.text = reservation.riderName
            tvHorseName.text = "Caballo: ${reservation.horseName}"
            tvDateTime.text = "${reservation.date} - ${reservation.time}"
        }
    }

    class ReservationDiffCallback : DiffUtil.ItemCallback<Reservation>() {
        override fun areItemsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
            return oldItem == newItem
        }
    }
}