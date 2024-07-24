package com.example.packagesapp

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import com.example.packagesapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("loginPref", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        val email = sharedPreferences.getString("email", "")
        val name = sharedPreferences.getString("name", "")
        val phone = sharedPreferences.getString("phone", "")
        val address = sharedPreferences.getString("address", "")

        binding.UserName2.text = username
        binding.Email.text = email
        binding.Name.text = name
        binding.phone.text = phone
        binding.address.text = address


        binding.btnSignOutProfile.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("loginPref", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()
            val intent = Intent(requireContext(), Login::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
