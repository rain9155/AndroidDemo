package com.example.androiddemo.jetpack.architecture.navigation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.androiddemo.R
import com.example.androiddemo.jetpack.architecture.navigation.isLogin
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_register_login.setOnClickListener {
            isLogin = true
            //findNavController().navigate(R.id.action_nav_login_to_peopleFragment)
            //回退到people_fragment
            findNavController().popBackStack(R.id.people_fragment, false)
        }
    }
}
