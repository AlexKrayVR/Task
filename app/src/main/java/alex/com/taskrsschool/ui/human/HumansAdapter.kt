package alex.com.taskrsschool.ui.human

import alex.com.taskrsschool.R
import alex.com.taskrsschool.common.DEFAULT_HUMAN_CARD_COLOR
import alex.com.taskrsschool.domain.model.Human
import alex.com.taskrsschool.databinding.ItemHumanBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class HumansAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Human, HumansAdapter.TaskViewHolder>(DiffCallback()) {

    interface OnItemClickListener {
        fun onItemClick(human: Human)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            ItemHumanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemHumanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val human = getItem(position)
                    listener.onItemClick(human)
                }
            }
        }

        fun bind(human: Human) {
            binding.apply {
                humanName.text = human.name
                humanAge.text = human.age.toString()
                humanProfession.text = human.profession.toString()
                if (human.color == DEFAULT_HUMAN_CARD_COLOR) {
                    humanCard.setCardBackgroundColor(
                        ContextCompat.getColor(
                            humanCard.context,
                            R.color.teal_200
                        )
                    )
                } else {
                    humanCard.setCardBackgroundColor(human.color)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Human>() {
        override fun areItemsTheSame(oldItem: Human, newItem: Human) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Human, newItem: Human) = oldItem == newItem
    }
}