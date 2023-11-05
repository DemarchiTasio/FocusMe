package com.android.app.focusmate

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.YearMonth
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import java.util.Locale.*

data class Activity(
    val id: Int,
    val name: String,
    val date: LocalDate,
    val description: String,
    val time: String,
    var isCompleted: Boolean = false,
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(navController: Any) {
    var activities = remember { mutableStateListOf<Activity>() }
    var selectedDay = remember { mutableStateOf(LocalDate.now()) }
    val year = remember { mutableStateOf(LocalDate.now().year) }
    val month = remember { mutableStateOf(LocalDate.now().monthValue) }
    var isFormOpen = remember { mutableStateOf(false) }

    // Tus funciones aquí (addActivity, deleteActivity, completeActivity)
    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    fun deleteActivity(activity: Activity) {
        activities.remove(activity)
    }

    fun completeActivity(activity: Activity) {
        val index = activities.indexOf(activity)
        activities[index] = activity.copy(isCompleted = !activity.isCompleted)
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.new_logo_app),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                )
                Text(
                    text = "Actividades",
                    fontSize = 26.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .padding(start = 55.dp)
                )
            }
                CalendarGrid(
                    year = year,
                    month = month,
                    activities = activities,
                    onDayClick = { selectedDay.value = it },
                    onActivityClick = { /* mostrar detalles */ }
                )

            Divider(
                modifier = Modifier
                    .width(350.dp)
                    .padding(vertical = 20.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Mis Actividades")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 15.dp),
                    text = "Done"
                )
                Text(text = "Activity")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "Delete")
                }

            }


            DayView(
                activities = activities.filter { it.date == selectedDay.value },
                onActivityClick = { /* mostrar detalles */ },
                onActivityDelete = { deleteActivity(it) },
                onActivityComplete = { completeActivity(it) }
            )
        }

        FloatingActionButton(
            onClick = { isFormOpen.value = true },
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 70.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Actividad")
        }

        if (isFormOpen.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
            Dialog(
                onDismissRequest = { isFormOpen.value = false }
            ) {
                ActivityForm(
                    activityCount = activities.size,
                    selectedDay = selectedDay.value,
                    onAddActivity = { addActivity(it); isFormOpen.value = false },
                    onClose = { isFormOpen.value = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityForm(
    activityCount: Int,
    selectedDay: LocalDate,
    onAddActivity: (Activity) -> Unit,
    onClose: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("12:00") }
    var date by remember { mutableStateOf(selectedDay) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(Color.White)
            .width(250.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre de la actividad") }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(onClick = { showTimePicker = true }) {
                Text("Tiempo")
            }

            if (showTimePicker) {
                TimePicker(time) { time = it }
                showTimePicker = false
            }

            Button(onClick = { showDatePicker = true }) {
                Text("Fecha")
            }

            if (showDatePicker) {
                DatePicker(date) { date = it }
                showDatePicker = false
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { onClose() }) {
                Text("Cancelar")
            }

            Button(onClick = {
                onAddActivity(
                    Activity(
                        id = activityCount,
                        name = name,
                        date = date,
                        description = "",
                        time = time
                    )
                )
                onClose()
            }) {
                Text("Agregar")
            }
        }
    }
}


@Composable
fun TimePicker(
    initialTime: String,
    onTimeChange: (String) -> Unit,
) {
    // Implementación del selector de tiempo
    val context = LocalContext.current

    val timeParts = initialTime.split(":").map { it.toInt() }
    val initialHour = timeParts[0]
    val initialMinute = timeParts[1]

    TimePickerDialog(
        context,
        { _, hour, minute ->
            val newTime = "$hour:$minute"
            onTimeChange(newTime)
        },
        initialHour,
        initialMinute,
        true // Usar formato de 24 horas
    ).show()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    initialDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
) {
    // Implementación del selector de fecha
    val context = LocalContext.current
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val newDate = LocalDate.of(year, month + 1, dayOfMonth)
            onDateChange(newDate)
        },
        initialDate.year,
        initialDate.monthValue - 1,
        initialDate.dayOfMonth
    ).show()
}

@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarGrid(
    year: MutableState<Int>,
    month: MutableState<Int>,
    activities: List<Activity>,
    onDayClick: (LocalDate) -> Unit,
    onActivityClick: (Activity) -> Unit,
) {
    val (firstDayOfMonth, daysInMonth) = getMonthInfo(year.value, month.value)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7  // Lunes es 1, Domingo es 7
    val weeksInMonth = ((daysInMonth + firstDayOfWeek - 1) + 6) / 7  // Redondeo hacia arriba

    // Obtenemos la fecha actual
    val today = LocalDate.now()

        Column(
            modifier = Modifier
                .padding(10.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(Color(0xCC53859B))
                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 4.dp)

        ) {
            // Botones de navegación
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {

                Icon(Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Last Month",
                    modifier = Modifier
                        .clickable {
                            month.value--
                            if (month.value < 1) {
                                month.value = 12
                                year.value--
                            }
                        })
                val monthName = firstDayOfMonth.month.getDisplayName(TextStyle.FULL, getDefault())
                Text("$monthName - ${year.value}")
                Icon(Icons.Default.KeyboardArrowRight,
                    contentDescription = "Next Month",
                    modifier = Modifier
                        .clickable {
                            month.value++
                            if (month.value > 12) {
                                month.value = 1
                                year.value++
                            }
                        })

            }



        // Días de la semana
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            arrayOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom").forEach { day ->
                Text(day, fontWeight = FontWeight.Bold)
            }
        }

        // Calendario
        for (week in 0 until weeksInMonth) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (dayOfWeek in 1..7) {
                    val date =
                        firstDayOfMonth.plusDays((week * 7 + dayOfWeek - firstDayOfWeek).toLong())
                    if (date.monthValue == month.value) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(46.dp)
                                .clickable { onDayClick(date) }
                        ) {
                            // Resaltamos el día actual
                            if (date == today) {
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(text = date.dayOfMonth.toString())
                            }

                            if (activities.any { it.date == date }) {
                                // Mostrar punto debajo de la fecha
                                Box(
                                    contentAlignment = Alignment.BottomCenter,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Canvas(modifier = Modifier.size(4.dp)) {
                                        drawCircle(color = Color.Red)
                                    }
                                }
                            }
                        }
                    } else {
                        // Días fuera del mes actual
                        Box(modifier = Modifier.size(48.dp)) {}
                    }
                }
            }
        }
    }
}


@Composable
fun DayView(
    activities: List<Activity>,
    onActivityClick: (Activity) -> Unit,
    onActivityDelete: (Activity) -> Unit,
    onActivityComplete: (Activity) -> Unit,
) {
    LazyColumn {
        items(activities) { activity ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onActivityClick(activity) }
//                    .background(Color.Red),
            ) {
                Checkbox(
                    checked = activity.isCompleted,
                    onCheckedChange = { onActivityComplete(activity) }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 13.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${activity.time} -  ${activity.name}")

                    Icon(Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier
                            .padding(end = 25.dp)
                            .clickable {
                                onActivityDelete(activity)
                            })
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getMonthInfo(year: Int, month: Int): Pair<LocalDate, Int> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val lastDayOfMonth = YearMonth.of(year, month).atEndOfMonth()
    val daysInMonth = lastDayOfMonth.dayOfMonth
    return Pair(firstDayOfMonth, daysInMonth)
}