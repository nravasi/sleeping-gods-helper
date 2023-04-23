package com.example.sgHelper.ui.details

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sgHelper.model.EntityDetails
import com.example.sgHelper.model.EntityId
import com.example.sgHelper.model.EntityRelationship
import com.example.sgHelper.model.EntityType
import com.example.sgHelper.model.RelationshipType
import com.example.sgHelper.repository.FakeRepository
import com.example.sgHelper.repository.Repository
import com.example.sgHelper.ui.buildDetailsScreenRoute
import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator

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
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Surface {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppBar(
                        navController = navController,
                        title = state.value.title
                    )
                    DetailsContent(
                        navController = navController,
                        entity = state.value,
                        containerHeight = this@BoxWithConstraints.maxHeight
                    )
                }
            }
        }
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
        id = FakeRepository.rucciId,
        type = EntityType.QUEST,
    )
}

@Composable
fun DetailsContent(
    navController: NavController,
    entity: EntityDetails,
    containerHeight: Dp
) {
    Column {
        if (entity.notes.isNotBlank()) {
            DetailsProperty(label = "Notes", value = entity.notes)
            Spacer(modifier = Modifier.height(8.dp))
        }

        for (relationship in entity.relationships) {
            RelationshipProperty(
                navController = navController,
                relationship = relationship,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Add a spacer that always shows part (320 dp) of the fields regardless
        // of the device in order to always leave some content at the top.
        Spacer(modifier =
            Modifier.height((containerHeight - 320.dp).coerceAtLeast(0.dp))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelationshipProperty(
    navController: NavController,
    relationship: EntityRelationship,
) {
    val relationshipText = when (relationship.type) {
        RelationshipType.FOUND_AT -> "Found at"
        RelationshipType.REQUIRED_IN -> "Required in"
        RelationshipType.PROVIDES -> "Provides"
        RelationshipType.REQUIRES -> "Requires"
    }

    val label = "$relationshipText ${relationship.entity.title}"

    Card(
        onClick = {
            val route = buildDetailsScreenRoute(relationship.entity.id, relationship.entity.type)

            navController.navigate(route)
        }
    ) {
        DetailsLabel(label = label)
    }
}

@Composable
fun DetailsProperty(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Divider()

        DetailsLabel(label = label)
        Text(
            text = value,
            modifier = Modifier.defaultMinSize(minHeight = 24.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DetailsLabel(label: String) {
    Text(
        text = label,
        modifier = Modifier.height(28.dp),
        style = MaterialTheme.typography.headlineSmall
    )
}

@Preview(showBackground = true)
@Composable
fun DetailsContentPreview() {

}