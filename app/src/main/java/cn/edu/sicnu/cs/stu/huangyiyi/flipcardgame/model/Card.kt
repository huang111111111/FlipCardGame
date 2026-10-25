package cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame.model

class Card(
    private var suit: String,
    private var rank: String,
    var isChosen: Boolean = false,
    var isMatched: Boolean = false
) {
    companion object {
        val rankStrings = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        // 四种花色：黑桃、红桃、方块、梅花
        val validSuits = arrayOf("♠", "♥", "♦", "♣")
    }

    override fun toString(): String {
        return suit + rank
    }

    // 实验五(5)：3张牌匹配规则
    // otherCards 传入另外两张牌：三张点数全相同得8分，三张花色全相同得4分，否则0分
    fun match(otherCards: Array<Card>): Int {
        if (otherCards.size != 2) return 0
        if (otherCards.all { it.rank == rank }) return 8
        if (otherCards.all { it.suit == suit }) return 4
        return 0
    }
}
