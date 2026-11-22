package com.example.rickandmorty.model

import kotlinx.serialization.Serializable

/** Ключи JSON совпадают с camelCase, поэтому @SerialName не нужен. */
@Serializable
data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val episode: List<String>
)
