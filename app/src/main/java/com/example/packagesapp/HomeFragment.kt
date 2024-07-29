package com.example.packagesapp

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.packagesapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var packageAdapter: PackageAdapter
    private lateinit var packageList: MutableList<Package>
    private lateinit var databaseHelper: DatabaseHelper
    lateinit var emptyTextView : TextView
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseHelper = DatabaseHelper()

        packageList = mutableListOf()
        packageAdapter = PackageAdapter(requireContext(), packageList) {
            val action = HomeFragmentDirections.actionHomeFragmentToDeliveryFragment(it.id!!)
            findNavController().navigate(action)
        }

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = packageAdapter
        emptyTextView = binding.emptyTextView

        databaseHelper.loadPackages(requireContext(), object : PackagesCallback {
            override fun onCallback(packages: List<Package>) {
                packageList.clear()
                packageList.addAll(packages)
                packageAdapter.notifyDataSetChanged()
                if (packageList.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    emptyTextView.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    emptyTextView.visibility = View.GONE
                }
            }
        })


        fun refreshPackages() {
            binding.swipeRefreshLayout.isRefreshing = true

            databaseHelper.loadPackages(requireContext(), object : PackagesCallback {
                override fun onCallback(packages: List<Package>) {
                    packageList.clear()
                    packageList.addAll(packages)
                    packageAdapter.notifyDataSetChanged()

                    if (packageList.isEmpty()) {
                        recyclerView.visibility = View.GONE
                        emptyTextView.visibility = View.VISIBLE
                    } else {
                        recyclerView.visibility = View.VISIBLE
                        emptyTextView.visibility = View.GONE
                    }

                    binding.swipeRefreshLayout.isRefreshing = false
                }
            })
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshPackages()
        }

    }
}
