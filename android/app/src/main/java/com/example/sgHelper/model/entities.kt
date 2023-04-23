package com.example.sgHelper.model

typealias EntityId = String

data class SearchResult(
    val id: EntityId,
    val title: String,
    val type: EntityType
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
    // Maybe use a sealed hierarchy for this? this is clearly two different classes but I'm lazy
    val foundAt: EntityDetailsReference? = null,
    val requiredIn: List<EntityDetailsReference> = listOf(),
    val provides: List<EntityDetailsReference> = listOf(),
    val requires: List<EntityDetailsReference> = listOf()
)

data class EntityDetailsReference(
    val id: EntityId,
    val type: EntityType,
    val title: String,
    // no se si pensamos hacerlo open source :P
    val falopa: Boolean = false
)