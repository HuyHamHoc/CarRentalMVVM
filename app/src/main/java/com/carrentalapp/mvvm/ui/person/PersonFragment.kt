package com.carrentalapp.mvvm.ui.person

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carrentalapp.mvvm.databinding.FragmentPersonBinding
import com.carrentalapp.mvvm.ui.signin.SignInActivity

class PersonFragment : Fragment() {
    private lateinit var binding: FragmentPersonBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPersonBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.tvPersonName.text = getFullName()

        binding.btnLogout.setOnClickListener {
            logout()
        }
        return view
    }

    private fun getFullName(): String? {
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("fullName", "")
    }


    private fun logout() {
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        val intent = Intent(requireActivity(), SignInActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}