package com.example.examprojet.model

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException



class MasseMonetaireRepository {

    private val client = OkHttpClient()
    private val gson = Gson()
    private val apiKey = "7186a8462cba833800e98bfc5c35e86041d59fd659df036b3b5d77b5"
    private val url = "https://webstat.banque-france.fr/api/explore/v2.1/catalog/datasets/observations/exports/json/?where=series_key+IN+%28%22BSI1.M.FR.N.V.N30.X.1.U2.2300.Z01.E%22%29&order_by=-time_period_start&limit=10"

    fun getMasseMonetaireRate(): List<Observation>? {
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Apikey $apiKey")
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                gson.fromJson(response.body?.string(), Array<Observation>::class.java).toList()
            }
        } catch (e: IOException) {
            println("Error: ${e.message}")
            null
        }
    }
}