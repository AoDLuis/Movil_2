package org.example.miniproyecto.model


import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


// Model
@Serializable
data class WhoaItem(
    val movie: String,
    val year: Int,
    val release_date: String,
    val director: String,
    val character: String,
    val movie_duration: String,
    val timestamp: String,
    val full_line: String,
    val current_whoa_in_movie: Int,
    val total_whoas_in_movie: Int,
    val whoa_grouping: JsonElement? = null,
    val poster: String
)


