package com.example.androiddemo.jetpack.architecture.navigation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.androiddemo.R

class DetailFragment : Fragment() {

    private var _articleId: Int? = null
    private var _articleAuthor: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //通过getArguments方法接收Bundle
        val bundle = arguments
        _articleId = bundle?.getInt(KEY_ARTICLE_ID, -1)
        _articleAuthor = bundle?.getString(KEY_ARTICLE_AUTHOR, "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Toast.makeText(
            activity,
            "文章id =$_articleId, 作者author = $_articleAuthor",
            Toast.LENGTH_SHORT
        ).show()
    }



}
