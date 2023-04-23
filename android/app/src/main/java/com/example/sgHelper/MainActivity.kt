package com.example.sgHelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Surface
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sgHelper.model.EntityType
import com.example.sgHelper.repository.__TODO_remove_me
import com.example.sgHelper.repository.anySearchResult
import com.example.sgHelper.repository.loadFakeDataFromResourcesFile
import com.example.sgHelper.repository.search
import com.example.sgHelper.ui.theme.SleepingGodsHelperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        __TODO_remove_me.fakeData = loadFakeDataFromResourcesFile(resources.openRawResource(R.raw.fake_data_seed))

        setContent {
            SleepingGodsHelperTheme() {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    val queryState = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        SearchView(state = queryState)
        SearchList(navController = navController, state = queryState)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    MainScreen(navController = navController)
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController = navController)
        }
        composable(
            "details/{type}/{id}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                },
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            // TODO: Error handling
            // TODO: Also nesting

            backStackEntry.arguments?.getString("type")?.let { typeString ->
                // TODO: I'm sure that EnumType is great, but I don't want to learn it
                EntityType.fromString(typeString)?.let { type ->
                    backStackEntry.arguments?.getString("id")?.let { id ->
                        DetailsScreen(
                            navController = navController,
                            id = id,
                            type = type
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    // TODO: This broke with the move to a json file in res/
    Navigation()
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
fun SearchList(navController: NavController, state: MutableState<TextFieldValue>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        val query = state.value.text
        val results = search(query)
        items(
            count = results.size,
            key = { i -> results[i].id },
            itemContent = { i ->
                val result = results[i]

                SearchListItem(
                    result = result,
                    onTap = { id, type ->
                        println("id = $id")
                        val route = "details/${type.name.lowercase()}/$id"
                        println(route)
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

    SearchList(navController = navController, state = queryState)
}

@Composable
fun SearchListItem(result: com.example.sgHelper.model.SearchResult, onTap: (id: String, type: EntityType) -> Unit) {
    Row(
       modifier = Modifier
           .clickable {
               println("result = $result")
               onTap(result.id, result.type)
           }
           .background(MaterialTheme.colorScheme.primaryContainer)
           .height(80.dp)
           .fillMaxWidth()
           .padding(PaddingValues(8.dp, 16.dp))
    ) {
        Column {
            Text(
                text = result.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = result.type.name.uppercase(),
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
    SearchListItem(result = anySearchResult(), onTap = { _, _ -> })
}