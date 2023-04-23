package com.example.sgHelper.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sgHelper.model.EntityId
import com.example.sgHelper.model.EntityType
import com.example.sgHelper.repository.FakeRepository
import com.example.sgHelper.repository.Repository

// Not sure if an activity should go here, I'm following a not-so-great tutorial I think

@Composable
fun DetailsScreen(
    navController: NavController,
    repository: Repository,
    id: EntityId,
    type: EntityType
) {
    val state = remember { mutableStateOf(repository.getEntity(id, type)) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AppBar(
            navController = navController,
            title = state.value.title
        )
        Text(
            text = id,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    val navController = rememberNavController()
    val repository = FakeRepository.alreadySeeded()

    DetailsScreen(
        navController = navController,
        repository = repository,
        id = "123",
        type = EntityType.QUEST,
    )
}