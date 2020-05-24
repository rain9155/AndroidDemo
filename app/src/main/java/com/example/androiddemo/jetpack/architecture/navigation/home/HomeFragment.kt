package com.example.androiddemo.jetpack.architecture.navigation.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavAction
import androidx.navigation.fragment.findNavController
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.fragment_home.*

const val KEY_ARTICLE_ID = "articleId"
const val KEY_ARTICLE_AUTHOR = "articleAuthor"

/**
 * 使用Bundle进行参数间传递：
 * 1、在发送数据的目的地定义Bundle，然后调用NavController的带Bundle类型重载的navigate方法；
 * 2、在接收数据目的地使用getArgument方法接收Bundle参数.
 */
class HomeFragment : Fragment() {

    companion object{
        private val TAG = HomeFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView()")
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_jump.setOnClickListener {
            //通过NavController的navigate方法进行跳转，传入action id
            //findNavController().navigate(R.id.action_homeFragment_to_detailFragment)

            val bundle = bundleOf(
                KEY_ARTICLE_ID to 1,
                KEY_ARTICLE_AUTHOR to "rain"
            )
            //通过NavController的navigate方法进行跳转，可以携带一个Bundle类型的参数
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }
}
