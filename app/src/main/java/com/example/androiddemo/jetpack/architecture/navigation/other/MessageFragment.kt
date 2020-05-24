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


class MessageFragment : Fragment() {

    companion object{
        private val TAG = MessageFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Activity的onBackPressedDispatcher添加OnBackPressedCallback回调, enable为true表示自行处理系统的返回行为
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, enabled = true){
            Log.d(TAG, "onActivityCreated(), onBackPressedCallback()")
            findNavController().popBackStack()
        }

    }
}
