package cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame.model.Card
import cn.edu.sicnu.cs.stu.huangyiyi.flipcardgame.model.CardMatchingGame

// 实验五(4b)：RecyclerView 适配器
class CardAdapter(
    private val game: CardMatchingGame,
    private val onCardClick: (Int) -> Unit   // 回调：点击了第几张牌
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCard: TextView = itemView.findViewById(R.id.tv_card)
    }

    // 创建一个格子的视图
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view).apply {
            itemView.setOnClickListener {
                onCardClick(bindingAdapterPosition)
            }
        }
    }

    // 把数据填进格子
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card: Card = game.cardAtIndex(position)
        when {
            card.isMatched -> {
                holder.tvCard.text = card.toString()
                holder.tvCard.setTextColor(cardColor(card))
                holder.tvCard.setBackgroundColor(Color.LTGRAY)
            }
            card.isChosen -> {
                holder.tvCard.text = card.toString()
                holder.tvCard.setTextColor(cardColor(card))
                holder.tvCard.setBackgroundColor(Color.WHITE)
            }
            else -> {
                holder.tvCard.text = ""
                holder.tvCard.setBackgroundResource(R.drawable.card_back)
            }
        }
    }

    override fun getItemCount(): Int {
        return game.cardCount
    }

    private fun cardColor(card: Card): Int {
        val s = card.toString()
        return if (s.startsWith("♥") || s.startsWith("♦")) Color.RED else Color.BLACK
    }
}
