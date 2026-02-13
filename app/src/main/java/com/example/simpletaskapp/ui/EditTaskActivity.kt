package com.example.simpletaskapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.simpletaskapp.R
import com.example.simpletaskapp.data.Task
import com.example.simpletaskapp.databinding.ActivityEditTaskBinding

class EditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditTaskBinding
    private val viewModel: TaskViewModel by viewModels()
    private var taskId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.header_edit_task)

        // Retrieve data from Intent
        taskId = intent.getLongExtra("EXTRA_TASK_ID", -1)
        val title = intent.getStringExtra("EXTRA_TASK_TITLE") ?: ""
        val description = intent.getStringExtra("EXTRA_TASK_DESC") ?: ""

        if (taskId != -1L) {
            binding.editTextTitle.setText(title)
            binding.editTextDescription.setText(description)
        }

        binding.btnSave.setOnClickListener {
            updateTask()
        }
    }

    private fun updateTask() {
        val title = binding.editTextTitle.text.toString().trim()
        val description = binding.editTextDescription.text.toString().trim()

        if (title.isEmpty()) {
            binding.inputLayoutTitle.error = getString(R.string.error_empty_title)
            return
        }

        if (taskId != -1L) {
            val updatedTask = Task(id = taskId, title = title, description = description)
            viewModel.updateTask(updatedTask)
            Toast.makeText(this, R.string.msg_task_updated, Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
