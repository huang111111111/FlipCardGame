package cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame.model

import java.io.Serializable

class CardMatchingGame(
    val count: Int,
    val matchMode: Int = 2  // 2=两张牌配对，3=三张牌配对
) : Serializable {
    var score = 0
        private set

    private val cards: MutableList<Card>

    init {
        cards = mutableListOf()
        val groupCount = count / matchMode
        for (g in 1..groupCount) {
            val rank = Card.rankStrings[(g - 1) % Card.rankStrings.size]
            for (i in 0 until matchMode) {
                val suit = Card.validSuits[i % Card.validSuits.size]
                cards.add(Card(suit = suit, rank = rank))
            }
        }
        cards.shuffle()
    }

    fun cardAtIndex(index: Int): Card {
        return cards[index]
    }

    val cardCount: Int
        get() = cards.size

    fun reset() {
        for (c in cards) {
            c.isChosen = false
            c.isMatched = false
        }
        cards.shuffle()
        score = 0
    }

    val MISMATCH_PENALTY = 2
    val MATCH_BONUS = 4
    val COST_TO_CHOOSE = 1

    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        if (card.isMatched) return

        if (card.isChosen) {
            card.isChosen = false
            return
        }

        score -= COST_TO_CHOOSE
        card.isChosen = true

        val faceUp = cards.filter { it.isChosen && !it.isMatched }

        if (faceUp.size == matchMode) {
            val others = faceUp.subList(0, matchMode - 1).toTypedArray()
            val matchScore = faceUp[matchMode - 1].match(others)
            if (matchScore > 0) {
                score += matchScore * MATCH_BONUS
                for (c in faceUp) c.isMatched = true
            } else {
                score -= MISMATCH_PENALTY
                for (c in faceUp) c.isChosen = false
            }
        }
    }
}
