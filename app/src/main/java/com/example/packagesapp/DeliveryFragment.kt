package com.example.packagesapp

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import com.example.packagesapp.databinding.FragmentDeliveryBinding
import com.example.packagesapp.databinding.FragmentProfileBinding

class DeliveryFragment : Fragment() {

    private var _binding: FragmentDeliveryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeliveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences1 = requireActivity().getSharedPreferences("loginPref", MODE_PRIVATE)
        val username = sharedPreferences1.getString("username", null)

        val sharedPreferences = requireActivity().getSharedPreferences("PackagePrefs", MODE_PRIVATE)
        val receiverName = sharedPreferences.getString("receiverName", "")
        val senderName = sharedPreferences.getString("senderName", "")
        val receiverPhone = sharedPreferences.getString("receiverPhone", "")
        val price = sharedPreferences.getString("price", "")
        val packageId = sharedPreferences.getString("packageId", "")

        binding.rName.text = "Receiver Name : $receiverName"
        binding.rPhone.text = "Receiver Phone : $receiverPhone"
        binding.sender.text = "Sender : $senderName"
        binding.rPrice.text = "Price : $price"

        binding.payButton.setOnClickListener {
            if(!binding.radioCash.isChecked && !binding.radioApplePay.isChecked && !binding.radioVisa.isChecked && !binding.radioPaypal.isChecked){
                binding.radioCash.error = "Please select a payment method"
                return@setOnClickListener
            }

            val db = DatabaseHelper()
            db.deletePackage(packageId!!,username!!,requireContext())

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)

        }
    }
}
