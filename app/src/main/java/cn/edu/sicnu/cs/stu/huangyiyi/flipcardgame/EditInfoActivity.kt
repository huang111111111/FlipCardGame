package cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditInfoActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etStudentId: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var cbTravel: CheckBox
    private lateinit var cbReading: CheckBox
    private lateinit var cbGame: CheckBox
    private lateinit var cbShopping: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // 绑定控件
        etName = findViewById(R.id.et_name)
        etStudentId = findViewById(R.id.et_student_id)
        etPhone = findViewById(R.id.et_phone)
        etEmail = findViewById(R.id.et_email)
        rgGender = findViewById(R.id.rg_gender)
        cbTravel = findViewById(R.id.cb_travel)
        cbReading = findViewById(R.id.cb_reading)
        cbGame = findViewById(R.id.cb_game)
        cbShopping = findViewById(R.id.cb_shopping)
        val btnSave = findViewById<Button>(R.id.btn_save)
        val btnCancel = findViewById<Button>(R.id.btn_cancel)

        // 接收从主页面传过来的旧数据，填到输入框
        val intent = intent
        etName.setText(intent.getStringExtra("name") ?: "")
        etStudentId.setText(intent.getStringExtra("studentId") ?: "")
        etPhone.setText(intent.getStringExtra("phone") ?: "")
        etEmail.setText(intent.getStringExtra("email") ?: "")

        val gender = intent.getStringExtra("gender") ?: "女"
        if (gender == "男") {
            rgGender.check(R.id.rb_male)
        } else {
            rgGender.check(R.id.rb_female)
        }

        val hobby = intent.getStringExtra("hobby") ?: ""
        cbTravel.isChecked = hobby.contains("旅游")
        cbReading.isChecked = hobby.contains("阅读")
        cbGame.isChecked = hobby.contains("游戏")
        cbShopping.isChecked = hobby.contains("购物")

        // 保存按钮：把修改后的数据传回主页面
        btnSave.setOnClickListener {
            val result = android.content.Intent()
            result.putExtra("name", etName.text.toString())
            result.putExtra("studentId", etStudentId.text.toString())
            result.putExtra("phone", etPhone.text.toString())
            result.putExtra("email", etEmail.text.toString())

            val selectedGender = if (rgGender.checkedRadioButtonId == R.id.rb_male) "男" else "女"
            result.putExtra("gender", selectedGender)

            // 收集选中的爱好
            val hobbies = mutableListOf<String>()
            if (cbTravel.isChecked) hobbies.add("旅游")
            if (cbReading.isChecked) hobbies.add("阅读")
            if (cbGame.isChecked) hobbies.add("游戏")
            if (cbShopping.isChecked) hobbies.add("购物")
            result.putExtra("hobby", hobbies.joinToString("、"))

            setResult(RESULT_OK, result)
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
            finish()
        }

        // 取消按钮：直接返回，不保存
        btnCancel.setOnClickListener {
            finish()
        }
    }
}
