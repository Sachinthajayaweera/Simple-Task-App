package com.example.simpletaskapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpletaskapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // ViewModel survives configuration changes (rotation), preserving state.
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = TaskAdapter(
            onTaskClick = { task ->
                val intent = Intent(this, EditTaskActivity::class.java).apply {
                    putExtra("EXTRA_TASK_ID", task.id)
                    putExtra("EXTRA_TASK_TITLE", task.title)
                    putExtra("EXTRA_TASK_DESC", task.description)
                }
                startActivity(intent)
            },
            onTaskChecked = { task, isChecked ->
                viewModel.onTaskCheckedChanged(task, isChecked)
            },
            onDeleteClick = { task ->
                viewModel.deleteTask(task)
            },
            onEditClick = { task ->
                val intent = Intent(this, EditTaskActivity::class.java).apply {
                    putExtra("EXTRA_TASK_ID", task.id)
                    putExtra("EXTRA_TASK_TITLE", task.title)
                    putExtra("EXTRA_TASK_DESC", task.description)
                }
                startActivity(intent)
            }
        )

        binding.tasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = adapter
        }

        viewModel.allTasks.observe(this) { tasks ->
            adapter.submitList(tasks)
            binding.emptyView.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fabAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }
}
