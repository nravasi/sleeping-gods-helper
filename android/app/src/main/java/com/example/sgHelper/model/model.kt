package com.example.sgHelper.model

typealias EntityId = String

data class EntitySummary(
    val id: EntityId,
    val title: String,
    val type: EntityType
)

data class SearchState(
    val results: List<SearchResult> = listOf(),
    val query: String = "",
)

data class SearchResult(
    val entity: EntitySummary
)

enum class EntityType {
    QUEST,
    LOCATION;

    companion object {
        fun fromString(string: String): EntityType? =
            when (string.uppercase()) {
                "QUEST" -> QUEST
                "LOCATION" -> LOCATION
                else -> null
            }
    }
}

data class EntityDetails(
    val id: EntityId,
    val title: String,
    val type: EntityType,
    val notes: String = "",
    val relationships: List<EntityRelationship> = listOf(),
)

enum class RelationshipType {
    FOUND_AT,
    REQUIRED_IN,
    PROVIDES,
    REQUIRES;
}

data class EntityRelationship(
    val type: RelationshipType,
    val entity: EntitySummary,
    // no se si pensamos hacerlo open source :P
    val falopa: Boolean = false
)