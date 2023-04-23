package com.example.sgHelper.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sgHelper.model.EntityDetails
import com.example.sgHelper.model.EntitySummary
import com.example.sgHelper.model.EntityType
import com.example.sgHelper.model.SearchResult
import com.example.sgHelper.repository.FakeRepository
import com.example.sgHelper.repository.Repository

@Composable
fun SearchScreen(
    navController: NavController,
    repository: Repository,
) {
    val queryState = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        SearchView(state = queryState)
        SearchList(
            navController = navController,
            repository = repository,
            state = queryState,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    val navController = rememberNavController()
    val repository = FakeRepository(
        entities = listOf(
            EntityDetails(id = "123", title = "A", type = EntityType.QUEST),
            EntityDetails(id = "234", title = "B", type = EntityType.QUEST),
            EntityDetails(id = "345", title = "1", type = EntityType.LOCATION),
        )
    )

    SearchScreen(
        navController = navController,
        repository = repository
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp),
            )
        },
        trailingIcon = {
            val hasQuery = state.value.text.isNotEmpty()
            if (hasQuery) {
                // clear search
                IconButton(
                    onClick = {
                        state.value = TextFieldValue("")
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(36.dp)
                    )
                }
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors()
    )
}

@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    SearchView(state = textState)
}

@Composable
fun SearchList(
    navController: NavController,
    repository: Repository,
    state: MutableState<TextFieldValue>
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        val query = state.value.text
        val results = repository.search(query)
        items(
            count = results.size,
            key = { i -> results[i].entity.id },
            itemContent = { i ->
                SearchListItem(
                    result = results[i],
                    onTap = { result ->
                        val entity = result.entity

                        val route = "details/${entity.type.name.lowercase()}/${entity.id}"

                        navController.navigate(route) {
                            // Copied verbatim for the tutorial I'm following
                            // https://johncodeos.com/how-to-add-search-in-list-with-jetpack-compose/
                            //         ^ great name

                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo("main") {
                                saveState = true
                            }

                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true

                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                )
            }
        )
    }
}

@Preview
@Composable
fun SearchListPreview() {
    val navController = rememberNavController()
    val queryState = remember { mutableStateOf(TextFieldValue("")) }
    val repository = FakeRepository(
        entities = listOf(
            EntityDetails(id = "123", title = "A", type = EntityType.QUEST),
            EntityDetails(id = "234", title = "B", type = EntityType.QUEST),
            EntityDetails(id = "345", title = "1", type = EntityType.LOCATION),
        )
    )

    SearchList(
        navController = navController,
        state = queryState,
        repository = repository
    )
}

@Composable
fun SearchListItem(
    result: SearchResult,
    onTap: (SearchResult) -> Unit
) {
    val entity = result.entity

    Row(
        modifier = Modifier
            .clickable { onTap(result) }
            .background(MaterialTheme.colorScheme.primaryContainer)
            .height(80.dp)
            .fillMaxWidth()
            .padding(PaddingValues(8.dp, 16.dp))
    ) {
        Column {
            Text(
                text = entity.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = entity.type.name.uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListItemPreview() {
    SearchListItem(
        result = SearchResult(entity = EntitySummary(id = "123", title = "DOVE", type = EntityType.QUEST)),
        onTap = { _ -> },
    )
}