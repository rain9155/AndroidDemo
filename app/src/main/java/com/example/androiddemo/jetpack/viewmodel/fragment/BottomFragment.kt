package com.example.androiddemo.jetpack.viewmodel.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androiddemo.R
import com.example.androiddemo.databinding.FragmentBottomBinding
import com.example.androiddemo.jetpack.viewmodel.SharedViewModel

class BottomFragment : Fragment() {

    companion object{
        private val TAG = this::class.java.simpleName
    }

    private lateinit var binding: FragmentBottomBinding
    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(SharedViewModel::class.java)
        } ?: throw Exception("invalid activity")

        viewModel.getProcess().observe(viewLifecycleOwner, Observer {
            binding.seekBar.progress = it
        })

        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setProcess(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                Log.d(TAG, "onActivityCreated(), onStartTrackingTouch()")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                Log.d(TAG, "onActivityCreated(), onStartTrackingTouch()")
            }

        })
    }
}
