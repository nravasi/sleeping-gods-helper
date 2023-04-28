package com.example.sgHelper.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sgHelper.model.EntityId
import com.example.sgHelper.model.EntityType
import com.example.sgHelper.repository.FakeRepository
import com.example.sgHelper.repository.Repository
import com.example.sgHelper.ui.details.DetailsScreen
import com.example.sgHelper.ui.search.SearchScreen

fun buildDetailsScreenRoute(id: EntityId, type: EntityType): String {
    return "details/${type.name.lowercase()}/${id}"
}

@Composable
fun Navigation(repository: Repository) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            SearchScreen(
                navController = navController,
                repository = repository,
            )
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
                            repository = repository,
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
    Navigation(repository = FakeRepository.alreadySeeded())
}