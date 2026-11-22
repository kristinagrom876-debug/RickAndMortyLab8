package com.example.rickandmorty.model

import kotlinx.serialization.Serializable

/** next == null означает конец списка. */
@Serializable
data class CharacterInfo(
    val count: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null
)

@Serializable
data class CharacterPage(
    val info: CharacterInfo,
    val results: List<Character>
)
