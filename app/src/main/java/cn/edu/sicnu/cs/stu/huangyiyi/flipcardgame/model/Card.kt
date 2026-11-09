package cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame.model

import java.io.Serializable

class Card(
    private var suit: String,
    private var rank: String,
    var isChosen: Boolean = false,
    var isMatched: Boolean = false
) : Serializable {
    companion object {
        val rankStrings = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        // 四种花色：黑桃、红桃、方块、梅花
        val validSuits = arrayOf("♠", "♥", "♦", "♣")
    }

    override fun toString(): String {
        return suit + rank
    }

    fun getSuit(): String = suit
    fun getRank(): String = rank

    // 匹配规则：otherCards为其他已翻开待匹配的牌
    // 2张模式：otherCards.size==1，同点4分，同花色1分
    // 3张模式：otherCards.size==2，三点全同8分，三花全同4分
    fun match(otherCards: Array<Card>): Int {
        if (otherCards.size == 1) {
            val otherCard = otherCards[0]
            if (otherCard.rank == rank) return 4
            if (otherCard.suit == suit) return 1
            return 0
        } else if (otherCards.size == 2) {
            if (otherCards.all { it.rank == rank }) return 8
            if (otherCards.all { it.suit == suit }) return 4
            return 0
        }
        return 0
    }
}
