package com.faculta.androidlabma.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.faculta.androidlabma.R
import com.faculta.androidlabma.databinding.FragmentLoginBinding
import com.faculta.androidlabma.databinding.FragmentRegisterBinding
import com.faculta.androidlabma.helpers.showToast

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.loginButton.setOnClickListener {
            val userName = binding.usernameInputTIET.text.toString()
            val password = binding.passwordInputTIET.text.toString()
            requireContext().showToast("Username: $userName; Password: $password")
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}