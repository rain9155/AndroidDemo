package com.example.androiddemo.jetpack.navigation.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.androiddemo.R
import com.example.androiddemo.databinding.FragmentHomeBinding

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

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView()")
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnJump.setOnClickListener {
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

}
