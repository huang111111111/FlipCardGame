package cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class ProfileFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvStudentId: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvHobby: TextView

    // 个人信息数据类（可序列化）
    data class ProfileData(
        var name: String = "黄艺艺",
        var gender: String = "女",
        var studentId: String = "2024180607",
        var phone: String = "13281023560",
        var email: String = "huangyiyi@example.com",
        var hobby: String = "阅读、游戏"
    ) : Serializable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvName = view.findViewById(R.id.tv_name)
        tvGender = view.findViewById(R.id.tv_gender)
        tvStudentId = view.findViewById(R.id.tv_student_id)
        tvPhone = view.findViewById(R.id.tv_phone)
        tvEmail = view.findViewById(R.id.tv_email)
        tvHobby = view.findViewById(R.id.tv_hobby)
        val btnEdit = view.findViewById<Button>(R.id.btn_edit)

        // 加载保存的个人信息
        val profile = loadProfile()
        refreshUI(profile)

        btnEdit.setOnClickListener {
            // 简单的编辑：弹个输入框改爱好
            val builder = android.app.AlertDialog.Builder(requireContext())
            builder.setTitle("修改爱好")
            val input = EditText(requireContext())
            input.setText(profile.hobby)
            builder.setView(input)
            builder.setPositiveButton("保存") { _, _ ->
                val newProfile = ProfileData(
                    profile.name, profile.gender, profile.studentId,
                    profile.phone, profile.email, input.text.toString()
                )
                saveProfile(newProfile)
                refreshUI(newProfile)
                Toast.makeText(requireContext(), "已保存", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("取消", null)
            builder.show()
        }
    }

    private fun refreshUI(p: ProfileData) {
        tvName.text = "姓名：${p.name}"
        tvGender.text = "性别：${p.gender}"
        tvStudentId.text = "学号：${p.studentId}"
        tvPhone.text = "电话：${p.phone}"
        tvEmail.text = "邮箱：${p.email}"
        tvHobby.text = "爱好：${p.hobby}"
    }

    override fun onPause() {
        super.onPause()
        // 页面离开时自动保存
        val p = ProfileData(
            name = tvName.text.toString().removePrefix("姓名："),
            gender = tvGender.text.toString().removePrefix("性别："),
            studentId = tvStudentId.text.toString().removePrefix("学号："),
            phone = tvPhone.text.toString().removePrefix("电话："),
            email = tvEmail.text.toString().removePrefix("邮箱："),
            hobby = tvHobby.text.toString().removePrefix("爱好：")
        )
        saveProfile(p)
    }

    private fun saveProfile(p: ProfileData) {
        try {
            val fos = requireActivity().openFileOutput("profile_data", Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(p)
            oos.close()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadProfile(): ProfileData {
        return try {
            val fis = requireActivity().openFileInput("profile_data")
            val ois = ObjectInputStream(fis)
            val p = ois.readObject() as ProfileData
            ois.close()
            fis.close()
            p
        } catch (e: Exception) {
            ProfileData() // 第一次打开返回默认值
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}
