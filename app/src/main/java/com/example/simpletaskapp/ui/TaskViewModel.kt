package com.example.simpletaskapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.simpletaskapp.data.AppDatabase
import com.example.simpletaskapp.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val taskDao = AppDatabase.getDatabase(application).taskDao()
    
    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks().asLiveData()

    fun addTask(title: String, description: String) = viewModelScope.launch(Dispatchers.IO) {
        val newTask = Task(title = title, description = description)
        taskDao.insertTask(newTask)
    }

    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.updateTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.deleteTask(task)
    }
    
    fun onTaskCheckedChanged(task: Task, isChecked: Boolean) {
        val updatedTask = task.copy(isCompleted = isChecked)
        updateTask(updatedTask)
    }
}
