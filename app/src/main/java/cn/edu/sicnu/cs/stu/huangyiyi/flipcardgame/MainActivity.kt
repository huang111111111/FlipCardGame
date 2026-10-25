package cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvStudentId: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvHobby: TextView
    private lateinit var tvHighScore: TextView

    // 个人信息（默认值）
    private var userName = "黄艺艺"
    private var userGender = "女"
    private var studentId = "2024180607"
    private var userPhone = "13281023560"
    private var userEmail = "huangyiyi@example.com"
    private var userHobby = "耍起"
    private var highScore = 0

    // 接收修改页返回的数据
    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            userName = data?.getStringExtra("name") ?: userName
            userGender = data?.getStringExtra("gender") ?: userGender
            studentId = data?.getStringExtra("studentId") ?: studentId
            userPhone = data?.getStringExtra("phone") ?: userPhone
            userEmail = data?.getStringExtra("email") ?: userEmail
            userHobby = data?.getStringExtra("hobby") ?: userHobby
            refreshInfo()
        }
    }

    // 接收游戏页返回的最高分
    private val gameLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val score = data?.getIntExtra("score", 0) ?: 0
            if (score > highScore) {
                highScore = score
                refreshInfo()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 绑定控件
        tvName = findViewById(R.id.tv_name)
        tvGender = findViewById(R.id.tv_gender)
        tvStudentId = findViewById(R.id.tv_student_id)
        tvPhone = findViewById(R.id.tv_phone)
        tvEmail = findViewById(R.id.tv_email)
        tvHobby = findViewById(R.id.tv_hobby)
        tvHighScore = findViewById(R.id.tv_high_score)
        val btnEdit = findViewById<Button>(R.id.btn_edit_info)
        val btnGame = findViewById<Button>(R.id.btn_play_game)

        // 刷新显示
        refreshInfo()

        // 跳转到修改信息页
        btnEdit.setOnClickListener {
            val intent = Intent(this, EditInfoActivity::class.java)
            intent.putExtra("name", userName)
            intent.putExtra("gender", userGender)
            intent.putExtra("studentId", studentId)
            intent.putExtra("phone", userPhone)
            intent.putExtra("email", userEmail)
            intent.putExtra("hobby", userHobby)
            editLauncher.launch(intent)
        }

        // 跳转到翻牌游戏页
        btnGame.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            gameLauncher.launch(intent)
        }
    }

    private fun refreshInfo() {
        tvName.text = getString(R.string.label_name) + userName
        tvGender.text = getString(R.string.label_gender) + userGender
        tvStudentId.text = getString(R.string.label_student_id) + studentId
        tvPhone.text = getString(R.string.label_phone) + userPhone
        tvEmail.text = getString(R.string.label_email) + userEmail
        tvHobby.text = getString(R.string.label_hobby) + userHobby
        tvHighScore.text = getString(R.string.label_high_score) + highScore
    }

    // 实验五(3)：右上角显示菜单
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // 实验五(3)：点击菜单弹出“关于”对话框
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.about_title)
                    .setMessage(R.string.about_message)
                    .setPositiveButton(R.string.ok, null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
