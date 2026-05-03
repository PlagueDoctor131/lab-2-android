package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class Task(
    val description: String,
    var isCompleted: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    // Stores the current text typed by the user
    var taskText by remember { mutableStateOf("") }

    // Stores the list of tasks and updates the UI when changed
    val taskList = remember { mutableStateListOf<Task>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Task Manager",
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        TaskInputField(
            taskText = taskText,
            onTaskTextChange = { taskText = it },
            onAddTask = {
                if (taskText.isNotBlank()) {
                    taskList.add(Task(taskText))
                    taskText = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TaskList(
            tasks = taskList,
            onCheckedChange = { task ->
                val index = taskList.indexOf(task)
                if (index != -1) {
                    taskList[index] = task.copy(isCompleted = !task.isCompleted)
                }
            },
            onDeleteTask = { task ->
                taskList.remove(task)
            }
        )
    }
}

@Composable
fun TaskInputField(
    taskText: String,
    onTaskTextChange: (String) -> Unit,
    onAddTask: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = taskText,
            onValueChange = onTaskTextChange,
            label = { Text("Enter task") },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onAddTask,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE)
            )
        ) {
            Text("Add Task")
        }
    }
}

@Composable
fun TaskList(
    tasks: List<Task>,
    onCheckedChange: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks) { task ->
            TaskItem(
                task = task,
                onCheckedChange = { onCheckedChange(task) },
                onDeleteTask = { onDeleteTask(task) }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: () -> Unit,
    onDeleteTask: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { onCheckedChange() }
        )

        Text(
            text = task.description,
            fontSize = 18.sp,
            color = if (task.isCompleted) Color.Gray else Color.Black,
            textDecoration = if (task.isCompleted) {
                TextDecoration.LineThrough
            } else {
                TextDecoration.None
            },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )

        IconButton(onClick = onDeleteTask) {
            Text("Delete")

        }
    }
}