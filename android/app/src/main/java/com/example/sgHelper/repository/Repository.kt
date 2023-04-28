package com.example.sgHelper.repository

import com.example.sgHelper.model.EntityDetails
import com.example.sgHelper.model.EntityId
import com.example.sgHelper.model.EntityType
import com.example.sgHelper.model.SearchResult

// TODO: Real repository
interface Repository {
    fun getEntity(id: EntityId, type: EntityType): EntityDetails
    fun search(query: String): List<SearchResult>
}