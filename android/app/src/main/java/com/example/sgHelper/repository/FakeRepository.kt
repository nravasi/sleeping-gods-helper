package com.example.sgHelper.repository

import com.example.sgHelper.model.EntityDetails
import com.example.sgHelper.model.EntityId
import com.example.sgHelper.model.EntitySummary
import com.example.sgHelper.model.EntityType
import com.example.sgHelper.model.SearchResult
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.InputStream
import java.nio.charset.StandardCharsets

class FakeRepository(private val entities: List<EntityDetails>) : Repository {
    private val searchResults : List<SearchResult> by lazy {
        entities
            .map { SearchResult(entity = EntitySummary(id = it.id, title = it.title, type = it.type)) }
    }

    override fun getEntity(id: EntityId, type: EntityType): EntityDetails {
        return entities
            .find { it.id == id && it.type == type }!!
    }

    override fun search(query: String): List<SearchResult> {
        // TODO: debouncing?
        return searchResults
            .filter { it.entity.title.startsWith(query, ignoreCase = true) }
    }

    companion object {
        fun fromJSON(inputStream: InputStream): FakeRepository {
            val mapper = jacksonMapperBuilder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build()

            val entities: List<EntityDetails> = mapper.readValue(inputStream)

            return FakeRepository(entities = entities)
        }

        fun alreadySeeded(): FakeRepository {
            // TODO: originally I had this fake data under res/raw, but I can't access it from Previews :/

            val data = """
                [
                  {
                    "id": "1adb2749-7858-4c07-893c-312dc9cb8d88",
                    "title": "SKERE",
                    "type": "QUEST",
                    "notes": "texto con emojis\uD83E\uDDC9",
                    "relationships": [
                      {
                        "type": "FOUND_AT",
                        "entity": {
                          "id": "6010e24e-9c35-4330-933d-b4c15bef573b",
                          "title": "2",
                          "type": "LOCATION" 
                        }
                      },
                      {
                        "type": "REQUIRED_IN",
                        "entity": {
                          "id": "a1174dfb-92bc-49ee-81a6-a4c546791655",
                          "title": "42",
                          "type": "LOCATION"
                        }
                      }
                    ]
                  },
                  {
                    "id": "81b9958f-fd21-404a-a979-9de0644271b1",
                    "title": "RUCCI",
                    "type": "QUEST",
                    "notes": "el próximo playthrough hay que ayudarlo",
                    "relationships": [
                      {
                        "type": "FOUND_AT",
                        "entity": {
                          "id": "13be5e56-f629-4ae5-84e0-a323b9e5331f",
                          "title": "19",
                          "type": "LOCATION"
                        }
                      },
                      {
                        "type": "REQUIRED_IN",
                        "entity": {
                          "id": "be703d97-0f80-427b-b321-67d9f2efbc48",
                          "title": "28",
                          "type": "LOCATION"
                        }
                      }
                    ]
                  },
                  {
                    "id": "6010e24e-9c35-4330-933d-b4c15bef573b",
                    "title": "2",
                    "type": "LOCATION",
                    "notes": "",
                    "relationships": [
                      {
                        "type": "PROVIDES",
                        "entity": {
                          "id": "1adb2749-7858-4c07-893c-312dc9cb8d88",
                          "title": "SKERE",
                          "type": "QUEST"
                        }
                      }
                    ]
                  },
                  {
                    "id": "a1174dfb-92bc-49ee-81a6-a4c546791655",
                    "title": "42",
                    "type": "LOCATION",
                    "notes": "the answer",
                    "relationships": [
                      {
                        "type": "REQUIRES",
                        "entity": {
                          "id": "a1174dfb-92bc-49ee-81a6-a4c546791655",
                          "title": "42",
                          "type": "LOCATION"
                        }
                      }
                    ]
                  },
                  {
                    "id": "13be5e56-f629-4ae5-84e0-a323b9e5331f",
                    "title": "19",
                    "type": "LOCATION",
                    "notes": "",
                    "relationships": [
                      {
                        "type": "PROVIDES",
                        "entity": {
                          "id": "81b9958f-fd21-404a-a979-9de0644271b1",
                          "title": "RUCCI",
                          "type": "QUEST"
                        }
                      }
                    ]
                  },
                  {
                    "id": "be703d97-0f80-427b-b321-67d9f2efbc48",
                    "title": "28",
                    "type": "LOCATION",
                    "notes": "el barrio de Flores, estoy seguro de que no va a pasar nada malo",
                    "relationships": [
                      {
                        "type": "REQUIRES",
                        "falopa": true,
                        "entity": {
                          "id": "81b9958f-fd21-404a-a979-9de0644271b1",
                          "title": "RUCCI",
                          "type": "QUEST"
                        }
                      }
                    ]
                  }
                ]
            """.trimIndent()

            return fromJSON(data.byteInputStream(StandardCharsets.UTF_8))
        }
    }
}