package com.example.androiddemo.jetpack.navigation.people

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.androiddemo.R

class CollectionFragment : Fragment() {

    private var _peopleId = -1
    private var _peopleName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //使用navArgs委托方法来检索参数
        val args by navArgs<CollectionFragmentArgs>()
        _peopleId = args.peopleId
        _peopleName = args.peopleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_collection, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Toast.makeText(
            activity,
            "用户id = $_peopleId, 姓名name = $_peopleName",
            Toast.LENGTH_SHORT
        ).show()
    }
}
