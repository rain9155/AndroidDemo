package com.example.androiddemo.jetpack.navigation.people

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.androiddemo.R
import com.example.androiddemo.jetpack.navigation.isLogin
import kotlinx.android.synthetic.main.fragment_people.*

/**
 * 使用safeArgs进行参数传递:
 * 1、在gradlew文件中引入safeArgs插件；
 * 2、在接收目的地中定义<argument>；
 * 3、build project后safeArgs插件会为我们生成XXDirection类(代表当前目的地的action操作)和XXArgs类(代表跳转目的地的接收参数)；
 * 4、在需要发送参数的目的地调用NavController带NavDirections类型重载的navigate方法；
 * 5、在需要接收参数的目的地使用getArgument方法或navArgs委托方法来检索参数值.
 */
class PeopleFragment : Fragment() {

    companion object{
        private val TAG = PeopleFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView()")
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_jump.setOnClickListener {
            //findNavController().navigate(R.id.action_peopleFragment_to_collectionFragment)

            //根据生成的XXDirections获得对应的操作NavDirections实例，然后在构造中传入需要传递的参数值
            val action = PeopleFragmentDirections.actionPeopleFragmentToCollectionFragment(
                1,
                "rain"
            )
            //调用带NavDirections类型重载的navigate方法，传入NavDirections实例
            findNavController().navigate(action)

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(!isLogin){
            findNavController().navigate(R.id.action_peopleFragment_to_nav_login)
        }
    }
}
