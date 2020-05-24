package com.example.androiddemo.jetpack.architecture.navigation.other

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController

import com.example.androiddemo.R

class SettingFragment : Fragment() {

    companion object{
        private val TAG = SettingFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner){
            Log.d(TAG, "onActivityCreated(), onBackPressedCallback()")
            findNavController().navigateUp()
        }

    }
}
