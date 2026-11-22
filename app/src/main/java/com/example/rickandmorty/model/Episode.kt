package com.example.rickandmorty.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** @SerialName только там, где ключ JSON отличается от camelCase. */
@Serializable
data class Episode(
    val id: Int,
    val name: String,
    @SerialName("air_date")
    val airDate: String,
    val episode: String
)
