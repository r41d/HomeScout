package android.example.homescout

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScanResultAdapter(
    private val items: List<ScanResult>,
    private val onClickListener: ((device: ScanResult) -> Unit)
) : RecyclerView.Adapter<ScanResultAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_scan_result, parent,false)

        return ViewHolder(view, onClickListener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    class ViewHolder(
        private val view: View,
        private val onClickListener: ((device: ScanResult) -> Unit)
    ) : RecyclerView.ViewHolder(view) {

        private val deviceName: TextView = view.findViewById(R.id.device_name)
        private val macAddress: TextView = view.findViewById(R.id.mac_address)
        private val signalStrength: TextView = view.findViewById(R.id.signal_strength)

        @SuppressLint("MissingPermission")
        fun bind(result: ScanResult) {
            // Log.i("Adapter", "view id: ${view.parent}")
            deviceName.text = result.device.name ?: "Unnamed"
            macAddress.text = result.device.address
            signalStrength.text = "${result.rssi} dBm"
            view.setOnClickListener { onClickListener.invoke(result) }
        }
    }
}
