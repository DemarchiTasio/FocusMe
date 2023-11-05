package com.android.app.focusmate

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(navController: Any) {
    var habits by remember { mutableStateOf(listOf<Habit>()) }
    val isDialogOpen = remember { mutableStateOf(false) }
    val daysOfWeek = DayOfWeek.values()

    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDialogOpen.value = true },
                modifier = Modifier
                    .padding(start = 8.dp, end = 0.dp, bottom = 55.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Hábito")
            }
        }
    ) {
        Column {
            DaysOfWeekHeader(daysOfWeek)
            HabitList(habits, onCheckChange = { habit, day, checked ->
                habits = habits.map {
                    if (it == habit) {
                        it.copy(
                            checkedDays = it.checkedDays.toMutableMap().apply { put(day, checked) })
                    } else {
                        it
                    }
                }
            })

            if (isDialogOpen.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                ) {
                    Dialog(onDismissRequest = { isDialogOpen.value = false }) {
                        AddHabitForm(
                            onAddHabit = { habitName ->
                                val newHabit = Habit(name = habitName, checkedDays = mutableMapOf())
                                habits = habits + newHabit
                                isDialogOpen.value = false
                            },
                            onClose = { isDialogOpen.value = false }
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaysOfWeekHeader(daysOfWeek: Array<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween // Use SpaceEvenly to spread the days out
    ) {
        Text("Hábito")
        daysOfWeek.forEach { day ->
            Text(
                text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun HabitList(habits: List<Habit>, onCheckChange: (Habit, DayOfWeek, Boolean) -> Unit) {
    Column {
        habits.forEach { habit ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = habit.name,
                    modifier = Modifier
                        .width(68.dp)
                        .align(Alignment.CenterVertically)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DayOfWeek.values().forEach { day ->
                        Checkbox(
                            checked = habit.checkedDays[day] ?: false,
                            onCheckedChange = { checked -> onCheckChange(habit, day, checked) },
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(50))
                        )
                    }
                }

            }
        }
    }
}

data class Habit(val name: String, val checkedDays: Map<DayOfWeek, Boolean>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitForm(
    onAddHabit: (String) -> Unit,
    onClose: () -> Unit,
) {
    var habitName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
            .width(250.dp)
            .wrapContentHeight()
    ) {
        TextField(
            value = habitName,
            onValueChange = { habitName = it },
            label = { Text("Nombre del Hábito") }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onClose) {
                Text("Cancelar")
            }

            Button(onClick = {
                if (habitName.isNotBlank()) {
                    onAddHabit(habitName)
                    onClose()
                }
            }) {
                Text("Agregar")
            }
        }
    }
}

