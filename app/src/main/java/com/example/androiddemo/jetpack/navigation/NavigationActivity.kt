package com.example.androiddemo.jetpack.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityNavigationBinding

var isLogin = false

/**
 * 参考：
 * https://developer.android.com/guide/navigation
 * https://www.jianshu.com/p/66b93df4b7a6
 *
 * 导航组件Navigation：
 * Navigation可以帮助开发者管理各个Fragment或Activity之间的导航关系，并且可以更加方便的在各个Fragment、Activity之间进行导航
 *
 * 组成：
 * NavGraph: 导航图，代表<navigation>标签中的内容，它包含可各个目的地之间的导航关系；
 * NavHost: 显示导航图中目的地的容器，它有一个getNavController方法返回一个NavController;
 * NavController: 管理NavHost容器中的目的地，它用来进行各个目的地之间的导航(跳转).
 *
 * 一、基本使用：
 * 1、在res/navigation目录下定义包含<navigation>标签的xml文件并编辑，设置某个目的地为起始目的地；
 * 2、向Activity添加(静态或动态)实现了NavHost的容器(NavHostFragment是默认实现)；
 * 3、使用findNavControl方法在Fragment/Activity/View中检索NavController实例，通过NavController完成导航；
 *
 * 二、目的地之间的参数传递：
 * 1、使用Bundle进行参数传递；
 * 2、使用safeArgs进行参数传递;
 * 3、使用ViewModel.
 *
 * 三、ToolBar与NavigationUI完成绑定：
 * 1、检索NavController；
 * 2、定义AppBarConfiguration；
 * 3、调用ToolBar的setupWithNavController方法把NavController、AppBarConfiguration、ToolBar和NavigationUI完成绑定.
 *
 * 四、DrawLayout与NavigationUI完成绑定：
 * 1、检索NavController；
 * 2、调用ToolBar的setupWithNavController方法把NavController、DrawLayout、ToolBar和NavigationUI完成绑定；
 * 3、调用NavigationView的setupWithNavController方法把NavController、NavigationView和NavigationUI完成绑定(Option).
 *
 * 五、BottomNavView与NavigationUI完成绑定：
 * 1、检索NavController；
 * 2、调用BottomNavView的setupWithNavController方法把NavController、BottomNavView与NavigationUi完成绑定.
 *
 * ps:
 * 1、一个NavHost对应一个NavController，一个NavController对应一个NavGraph，一个NavGraph中有很多个NavDestination
 * 2、当Activity没有结合BottomNavigationView使用时，一个Activity最好对应一个NavHost；
 * 3、Navigation替换Fragment的策略采用的是重新创建，即每次onCreateView方法都会被调用；
 * 4、每执行一次navigate操作，都会将一个目的地添加到返回堆栈，每执行一次navigateUp或popBackStack操作都会从栈顶中移除一个目的地.
 */
class NavigationActivity : AppCompatActivity() {

    companion object{
        private val TAG = NavigationActivity::class.java.simpleName
    }

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        navController = findNavController(R.id.nav_host_fragment)

        //AppBarConfiguration构造传入NavGraph，表示Graph中的起始目的地作为顶级目的地
        //val appBarConfiguration = AppBarConfiguration(navController.graph)

        //AppBarConfiguration构造传入一组目的地id，表示这组目的地都为顶级目的地
        val topLevelDestinationIds = setOf(
            R.id.home_fragment,
            R.id.people_fragment
        )
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds
        )

        //NavigationUI与Toolbar绑定，当在NavHost中跳转时，ToolBar左上角的标题与icon也随之变化
        binding.toolBar.setupWithNavController(navController, appBarConfiguration)

        //NavigationUI与Toolbar、DrawLayout绑定, 当在NavHost中跳转时，ToolBar左上角的标题与icon也随之变化
        binding.toolBar.setupWithNavController(navController, binding.drawerLayout)

        //NavigationUI与DrawLayout中的NavigationView绑定，点击menuItem跳转到相应的目的地
        binding.navView.setupWithNavController(navController)

        //NavigationUI与BottomNavView绑定，点击tab(menuItem)跳转到相应的目的地
        binding.bottomNav.setupWithNavController(navController)

        //向NavController添加目的地更改的监听，该接口在当前目的地或其参数发生更改时调用
        navController.addOnDestinationChangedListener{ navController, destination, _ ->
            Log.d(TAG, "addOnDestinationChangedListener(), destination = $destination， curDestination = ${navController.currentDestination}")
            if(topLevelDestinationIds.contains(destination.id)){
                binding.toolBar.visibility = View.VISIBLE
                binding.bottomNav.visibility = View.VISIBLE
            }else{
                binding.toolBar.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 当NavigationView或ToolBar或BottomNavigationView与NavigationUI完成绑定后，
     * 如果menu的中item的id和目的地fragment的id一样，点击该item就会跳转到该Fragment，
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}
