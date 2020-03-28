package com.example.androiddemo.material

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.androiddemo.R
import kotlinx.android.synthetic.main.activity_toolbar.*

/**
 * Toolbar使用：
 * 1、引入库依赖androidx.appcompat:appcompat:1.1.0
 * 2、设置主题为NoActionBar
 */
class ToolbarActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //透明状态栏效果
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView: View = window.decorView
            val option =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        }

        setContentView(R.layout.activity_toolbar)

        //这个设置以后等于转成使用Actionbar的特性
        setSupportActionBar(tool_bar)

        //加载菜单（toolbar做法）,如果不使用setSupportActionBar(mTooBar); 则要用以下方法加载菜单
        //mTooBar.inflateMenu(R.menu.toolbar_menu);

        tool_bar.setNavigationOnClickListener{
            finish()
        }

        floating_button.setOnClickListener{
            val toast = Toast.makeText(this@ToolbarActivity, null, Toast.LENGTH_SHORT)
            toast.setText("hello world")
            toast.show()
        }

        collapsing_toolbar_layout.title = "标题"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
}