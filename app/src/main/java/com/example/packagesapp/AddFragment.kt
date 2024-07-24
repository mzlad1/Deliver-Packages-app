package com.example.packagesapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import com.example.packagesapp.databinding.FragmentAddBinding


class AddFragment : Fragment() {
    lateinit var binding: FragmentAddBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)
        binding.addPackageButton.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("loginPref", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("username", null)
            if (username == null) {
                Toast.makeText(requireContext(), "Login required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val receiver = binding.ReceiverName.text.toString()
            val sender = binding.SenderName.text.toString()
            val receiverPhone = binding.ReceiverPhone.text.toString()
            val price = binding.Price.text.toString()
            val newPackage = Package(receiver, sender, receiverPhone, price)
            val databaseHelper = DatabaseHelper()
            databaseHelper.addPackage(requireContext(), username, newPackage)
            val intent : Intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}