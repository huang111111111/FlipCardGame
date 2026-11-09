package cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val profileFragment = ProfileFragment.newInstance()
    private val game2Fragment = Game2Fragment.newInstance()
    private val game3Fragment = Game3Fragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // APP标题：姓名+学号
        title = "黄艺艺2024180607"

        // 默认显示个人信息页
        if (savedInstanceState == null) {
            replaceFragment(profileFragment)
        }

        // 底部导航切换
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> {
                    replaceFragment(profileFragment)
                    true
                }
                R.id.nav_game2 -> {
                    replaceFragment(game2Fragment)
                    true
                }
                R.id.nav_game3 -> {
                    replaceFragment(game3Fragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
