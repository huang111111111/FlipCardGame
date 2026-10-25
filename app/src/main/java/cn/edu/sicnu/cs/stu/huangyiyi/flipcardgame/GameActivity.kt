package cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame.model.CardMatchingGame

class GameActivity : AppCompatActivity() {

    private lateinit var game: CardMatchingGame
    private lateinit var rvCards: RecyclerView
    private lateinit var tvScore: TextView
    private lateinit var adapter: CardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        rvCards = findViewById(R.id.rv_cards)
        tvScore = findViewById(R.id.tv_score)
        val btnReset = findViewById<Button>(R.id.btn_reset)
        val btnExit = findViewById<Button>(R.id.btn_exit)

        // 实验五(4d)：横屏竖屏牌数不同 —— 竖屏24张(6列x4行)，横屏30张(10列x3行)
        val landscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val cardCount = if (landscape) 30 else 24
        val columns = if (landscape) 10 else 6

        game = CardMatchingGame(cardCount)
        adapter = CardAdapter(game) { position ->
            game.chooseCardAtIndex(position)
            adapter.notifyDataSetChanged()
            updateScore()
        }
        rvCards.layoutManager = GridLayoutManager(this, columns)
        rvCards.adapter = adapter
        updateScore()

        // 重置按钮：调用 Model 的 reset()
        btnReset.setOnClickListener {
            game.reset()
            adapter.notifyDataSetChanged()
            updateScore()
        }

        btnExit.setOnClickListener { exitWithScore() }
    }

    private fun updateScore() {
        tvScore.text = getString(R.string.label_score) + game.score
    }

    private fun exitWithScore() {
        val result = Intent()
        result.putExtra("score", game.score)
        setResult(RESULT_OK, result)
        finish()
    }

    override fun onBackPressed() {
        exitWithScore()
        super.onBackPressed()
    }
}
