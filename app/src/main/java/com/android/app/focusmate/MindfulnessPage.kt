package com.android.app.focusmate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class MeditationSession(
    val id: Int,
    val title: String,
    val color: Color, // Color para representar el video
    val tags: List<String>
)

@Composable
fun MindfulnessScreen(navController: Any) {
    // Datos de ejemplo para el MVP
    val meditationSessions = listOf(
        MeditationSession(
            id = 1,
            title = "Meditación Guiada para el Estrés y Enfoque",
            color = Color(0xFFD1C4E9), // Color púrpura claro
            tags = listOf("- estrés", "+ enfoque")
        ),
        MeditationSession(
            id = 2,
            title = "Meditación Guiada para la Ansiedad",
            color = Color(0xFFB3E5FC), // Color azul claro
            tags = listOf("- ansiedad", "+ calma")
        ),
        MeditationSession(
            id = 3,
            title = "Meditación Guiada para la Creatividad",
            color = Color(0xFFC8E6C9), // Color verde claro
            tags = listOf("+ creatividad", "+ inspiración")
        ),
        MeditationSession(
            id = 4,
            title = "Meditación Guiada para la Felicidad",
            color = Color(0xFFFFF9C4), // Color amarillo claro
            tags = listOf("+ felicidad", "+ positividad")
        )
    )

    LazyColumn {
        items(meditationSessions) { session ->
            MeditationSessionCard(session)
        }
    }
}

@Composable
fun MeditationSessionCard(session: MeditationSession) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .background(session.color)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = session.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(8.dp)
            )
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                session.tags.forEach { tag ->
                    Chip(tag = tag)
                }
            }
        }
    }
}

@Composable
fun Chip(tag: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .border(1.dp, Color.Gray, RoundedCornerShape(50))
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.LightGray.copy(alpha = 0.3f))
    ) {
        Text(text = tag)
    }
}
