package org.example.miniproyecto.network


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.miniproyecto.model.WhoaItem


    object ApiService {



        private val client = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true // Ignorar campos desconocidos en JSON
                })
            }
        }

        suspend fun getRandomWhoas(count: Int): List<WhoaItem> {
            return client.get("https://whoa.onrender.com/whoas/random?results=$count").body()
        }



        suspend fun getRandomWhoa(): WhoaItem {
            return client.get("https://whoa.onrender.com/whoas/random").body<List<WhoaItem>>().first()
        }

    }
