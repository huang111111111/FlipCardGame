package cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame.model

class CardMatchingGame(count: Int) {
    var score = 0
        private set

    private val cards: MutableList<Card>

    init {
        cards = mutableListOf()
        // 实验五：3张为一组，每组点数相同（竖屏24张=8组，横屏30张=10组）
        val groupCount = count / 3
        for (g in 1..groupCount) {
            val rank = Card.rankStrings[g - 1]
            for (suit in Card.validSuits.take(3)) {
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

    // 实验五(4c)：重置游戏——牌全部翻回、重新洗牌、分数清零
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

    // 实验五(5)：3张牌游戏的核心逻辑
    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        if (card.isMatched) return            // 已配对的牌不能再点

        if (card.isChosen) {
            card.isChosen = false             // 再点一次 = 翻回去
            return
        }

        score -= COST_TO_CHOOSE
        card.isChosen = true

        // 收集所有正面朝上、还没配对的牌
        val faceUp = cards.filter { it.isChosen && !it.isMatched }

        // 凑满3张才判定
        if (faceUp.size == 3) {
            val others = arrayListOf(faceUp[0], faceUp[1])
            val matchScore = faceUp[2].match(others.toTypedArray())
            if (matchScore > 0) {
                score += matchScore * MATCH_BONUS
                for (c in faceUp) c.isMatched = true   // 配对成功：三张保留牌面
            } else {
                score -= MISMATCH_PENALTY
                for (c in faceUp) c.isChosen = false   // 不匹配：三张翻回去
            }
        }
    }
}
