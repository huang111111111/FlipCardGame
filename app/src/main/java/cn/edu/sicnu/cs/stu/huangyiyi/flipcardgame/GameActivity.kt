package cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame.model.Card

class GameActivity : AppCompatActivity() {

    private lateinit var game: cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame.model.CardMatchingGame
    private lateinit var cardsGrid: GridLayout
    private lateinit var tvScore: TextView
    private lateinit var cardButtons: MutableList<Button>
    private val CARD_COUNT = 16

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        cardsGrid = findViewById(R.id.cards_grid)
        tvScore = findViewById(R.id.tv_score)
        val btnReset = findViewById<Button>(R.id.btn_reset)
        val btnExit = findViewById<Button>(R.id.btn_exit)

        startNewGame()

        btnReset.setOnClickListener {
            startNewGame()
        }

        btnExit.setOnClickListener {
            val result = android.content.Intent()
            result.putExtra("score", game.score)
            setResult(RESULT_OK, result)
            finish()
        }
    }

    private fun startNewGame() {
        game = cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame.model.CardMatchingGame(CARD_COUNT)
        cardsGrid.removeAllViews()
        cardButtons = mutableListOf()

        for (i in 0 until CARD_COUNT) {
            val button = Button(this)
            val params = GridLayout.LayoutParams()
            params.width = 0
            params.height = 0
            params.columnSpec = GridLayout.spec(i % 4, 1f)
            params.rowSpec = GridLayout.spec(i / 4, 1f)
            button.layoutParams = params
            button.textSize = 28f
            // 去掉Button默认的内边距，让背景图铺满
            button.setPadding(0, 0, 0, 0)

            val index = i
            button.setOnClickListener {
                game.chooseCardAtIndex(index)
                updateCards()
            }

            cardsGrid.addView(button)
            cardButtons.add(button)
        }

        updateCards()
    }

    private fun updateCards() {
        for (i in 0 until CARD_COUNT) {
            val card: Card = game.cardAtIndex(i)
            val button = cardButtons[i]

            when {
                card.isMatched -> {
                    // 已匹配：显示牌面，灰色
                    button.text = card.toString()
                    button.setTextColor(getCardColor(card))
                    button.setBackgroundColor(Color.LTGRAY)
                    button.isEnabled = false
                }
                card.isChosen -> {
                    // 翻开：白色背景，显示花色+点数
                    button.text = card.toString()
                    button.setTextColor(getCardColor(card))
                    button.setBackgroundColor(Color.WHITE)
                }
                else -> {
                    // 未翻开：用扑克牌背面图片
                    button.text = ""
                    button.setBackgroundResource(R.drawable.card_back)
                }
            }
        }
        tvScore.text = getString(R.string.label_score) + game.score
    }

    private fun getCardColor(card: Card): Int {
        val s = card.toString()
        return if (s.startsWith("♥") || s.startsWith("♦")) {
            Color.RED
        } else {
            Color.BLACK
        }
    }

    override fun onBackPressed() {
        val result = android.content.Intent()
        result.putExtra("score", game.score)
        setResult(RESULT_OK, result)
        super.onBackPressed()
    }
}
