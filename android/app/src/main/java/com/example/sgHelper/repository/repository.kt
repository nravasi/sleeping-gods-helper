package com.example.sgHelper.repository

import com.example.sgHelper.model.EntityDetails
import com.example.sgHelper.model.EntityType
import com.example.sgHelper.model.SearchResult
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.*
import java.io.InputStream
import kotlin.random.Random

// TODO: I'm sure there's a good way to do this :P
object __TODO_remove_me {
    var fakeData : List<EntityDetails> = listOf()

    fun allEntities(): List<EntityDetails> = fakeData
    fun searchResults(): List<SearchResult> {
        return allEntities().map { entity ->
            SearchResult(id = entity.id, type = entity.type, title = entity.title)
        }
    }
}

fun loadFakeDataFromResourcesFile(inputStream: InputStream): List<EntityDetails> {
    val mapper = jacksonMapperBuilder()
        .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .build()

    return mapper.readValue(inputStream)
}

// TODO
fun anySearchResult(): SearchResult {
    val searchResults = __TODO_remove_me.searchResults()
    val max = searchResults.size - 1
    return searchResults[Random.nextInt(0, max)]
}

fun search(query: String): List<SearchResult> {
    // We have to make the call to the backend here
    // Also debouncing I guess?

    val searchResults = __TODO_remove_me.searchResults()
    return searchResults
        .filter { it.title.startsWith(query, ignoreCase = true) }
}

fun getDetails(id: String, type: EntityType): EntityDetails {
    val allEntities = __TODO_remove_me.allEntities()

    return allEntities
        .find {
            println("expected - actual == id $id - ${it.id} == type $type ${it.type}")
            return@find it.id == id && it.type == type
        }!!
}