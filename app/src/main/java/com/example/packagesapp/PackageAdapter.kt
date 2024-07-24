package com.example.packagesapp

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.packagesapp.databinding.ItemPackageBinding

class PackageAdapter(private val context: Context, private val packageList: List<Package>, private val onDeliverClick: (Package) -> Unit) :
    RecyclerView.Adapter<PackageAdapter.PackageViewHolder>() {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("PackagePrefs", Context.MODE_PRIVATE)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val binding = ItemPackageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PackageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        val currentPackage = packageList[position]
        holder.bind(currentPackage)
    }

    override fun getItemCount() = packageList.size



    inner class PackageViewHolder(private val binding: ItemPackageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(packageItem: Package) {
            binding.receiverName.text = "Receiver: ${packageItem.receiver}"
            binding.senderName.text = "Sender: ${packageItem.sender}"
            binding.receiverNumber.text = "Phone: ${packageItem.receiverPhone}"
            binding.price.text = "Price: ${packageItem.price}"

            binding.deliveryButton.setOnClickListener {
                sharedPreferences.edit().apply {
                    putString("packageId", packageItem.id)
                    putString("receiverName", packageItem.receiver)
                    putString("senderName", packageItem.sender)
                    putString("receiverPhone", packageItem.receiverPhone)
                    putString("price", packageItem.price)
                    apply()
                }
                onDeliverClick(packageItem)
            }
        }
    }

}
