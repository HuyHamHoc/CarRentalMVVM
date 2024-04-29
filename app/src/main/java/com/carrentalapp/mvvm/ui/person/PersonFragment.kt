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
        binding = FragmentPersonBinding.inflate(inflater, container, false)
        val view = binding.root
        val fullName = requireActivity().intent.getStringExtra("fullName")
        binding.tvPersonName.text = fullName
        binding.btnLogout.setOnClickListener {
            logout()
        }
        return view
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