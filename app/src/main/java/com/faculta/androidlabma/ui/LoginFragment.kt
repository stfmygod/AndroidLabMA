package com.faculta.androidlabma.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.faculta.androidlabma.R
import com.faculta.androidlabma.data.network.ApiService
import com.faculta.androidlabma.databinding.FragmentLoginBinding
import com.faculta.androidlabma.databinding.FragmentRegisterBinding
import com.faculta.androidlabma.helpers.showToast
import com.faculta.androidlabma.ui.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val viewModel = LoginViewModel()
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
            viewModel.login(userName, password, requireActivity() as MainActivity)
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, {
            requireContext().showToast(it)
        })

        viewModel.canNavigateToNextDestination.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(R.id.action_loginFragment_to_objectListFragment)
                Log.d("LoginFragment", (requireActivity() as MainActivity).getToken() ?: "")
            }
        })


    }
}