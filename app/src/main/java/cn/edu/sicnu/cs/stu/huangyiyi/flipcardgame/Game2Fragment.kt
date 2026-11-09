package cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame.model.Card
import cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame.model.CardMatchingGame
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class Game2Fragment : Fragment() {

    private lateinit var game: CardMatchingGame
    private lateinit var cardsGrid: GridLayout
    private lateinit var tvScore: TextView
    private lateinit var cardButtons: MutableList<Button>
    private val CARD_COUNT = 12 // 6组×2张

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardsGrid = view.findViewById(R.id.cards_grid)
        tvScore = view.findViewById(R.id.tv_score)
        val btnReset = view.findViewById<Button>(R.id.btn_reset)

        // 加载保存的游戏进度，没有就新建
        game = loadGame() ?: CardMatchingGame(CARD_COUNT, matchMode = 2)

        buildCards()
        updateCards()

        btnReset.setOnClickListener {
            game.reset()
            updateCards()
        }
    }

    private fun buildCards() {
        cardsGrid.removeAllViews()
        cardButtons = mutableListOf()
        cardsGrid.columnCount = 4

        for (i in 0 until game.cardCount) {
            val button = Button(requireContext())
            val params = GridLayout.LayoutParams()
            params.width = 0
            params.height = 0
            params.columnSpec = GridLayout.spec(i % 4, 1f)
            params.rowSpec = GridLayout.spec(i / 4, 1f)
            params.setMargins(4, 4, 4, 4)
            button.layoutParams = params
            button.textSize = 24f
            button.setPadding(0, 0, 0, 0)

            val index = i
            button.setOnClickListener {
                game.chooseCardAtIndex(index)
                updateCards()
            }

            cardsGrid.addView(button)
            cardButtons.add(button)
        }
    }

    private fun updateCards() {
        for (i in 0 until game.cardCount) {
            val card: Card = game.cardAtIndex(i)
            val button = cardButtons[i]

            when {
                card.isMatched -> {
                    button.text = card.toString()
                    button.setTextColor(getCardColor(card))
                    button.setBackgroundColor(Color.LTGRAY)
                    button.isEnabled = false
                }
                card.isChosen -> {
                    button.text = card.toString()
                    button.setTextColor(getCardColor(card))
                    button.setBackgroundColor(Color.WHITE)
                }
                else -> {
                    button.text = ""
                    button.setBackgroundResource(R.drawable.card_back)
                }
            }
        }
        tvScore.text = "得分：${game.score}"
    }

    private fun getCardColor(card: Card): Int {
        val s = card.toString()
        return if (s.startsWith("♥") || s.startsWith("♦")) {
            Color.RED
        } else {
            Color.BLACK
        }
    }

    override fun onPause() {
        super.onPause()
        saveGame() // 页面离开时保存游戏进度
    }

    private fun saveGame() {
        try {
            val fos = requireActivity().openFileOutput("game2_data", Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(game)
            oos.close()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadGame(): CardMatchingGame? {
        return try {
            val fis = requireActivity().openFileInput("game2_data")
            val ois = ObjectInputStream(fis)
            val g = ois.readObject() as CardMatchingGame
            ois.close()
            fis.close()
            g
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = Game2Fragment()
    }
}
