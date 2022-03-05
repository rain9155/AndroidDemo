package com.example.androiddemo.jetpack.navigation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.androiddemo.R
import com.example.androiddemo.databinding.FragmentRegisterBinding
import com.example.androiddemo.jetpack.navigation.isLogin

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegisterLogin.setOnClickListener {
            isLogin = true
            //findNavController().navigate(R.id.action_nav_login_to_peopleFragment)
            //回退到people_fragment
            findNavController().popBackStack(R.id.people_fragment, false)
        }
    }
}
