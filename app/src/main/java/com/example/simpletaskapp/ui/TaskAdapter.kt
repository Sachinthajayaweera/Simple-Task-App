package com.example.simpletaskapp.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletaskapp.data.Task
import com.example.simpletaskapp.databinding.ItemTaskBinding

class TaskAdapter(
    private val onTaskClick: (Task) -> Unit,
    private val onTaskChecked: (Task, Boolean) -> Unit,
    private val onDeleteClick: (Task) -> Unit,
    private val onEditClick: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.apply {
                textTitle.text = task.title
                textDescription.text = task.description
                checkBoxCompleted.setOnCheckedChangeListener(null)
                checkBoxCompleted.isChecked = task.isCompleted

                toggleStrikeThrough(textTitle, task.isCompleted)
                toggleStrikeThrough(textDescription, task.isCompleted)

                root.setOnClickListener { onTaskClick(task) }
                
                checkBoxCompleted.setOnCheckedChangeListener { _, isChecked ->
                    toggleStrikeThrough(textTitle, isChecked)
                    toggleStrikeThrough(textDescription, isChecked)
                    onTaskChecked(task, isChecked)
                }

                btnDelete.setOnClickListener { onDeleteClick(task) }
                btnEdit.setOnClickListener { onEditClick(task) }
            }
        }

        private fun toggleStrikeThrough(textView: TextView, isCompleted: Boolean) {
            if (isCompleted) {
                textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                textView.alpha = 0.5f
            } else {
                textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                textView.alpha = 1.0f
            }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }
}
