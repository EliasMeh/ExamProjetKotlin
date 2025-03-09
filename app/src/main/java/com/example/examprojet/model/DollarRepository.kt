package com.example.examprojet.model

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

data class Observation(
    val dataset_id: String,
    val series_key: String,
    val title_fr: String,
    val title_en: String,
    val time_period: String,
    val time_period_start: String,
    val time_period_end: String,
    val obs_value: Double,
    val obs_status: String,
    val updated_at: String,
    val observations_attributes_and_values: String,
    val deleted_at: String?,
    val baskets: List<String>,
    val comment_obs: String?,
    val conf_status: String?,
    val embargo_date: String?,
    val obs_com: String?,
    val obs_conf: String,
    val obs_edp_wbb: String?,
    val obs_pre_break: String?,
    val pre_break_value: String?
)

class DollarRepository {

    private val client = OkHttpClient()
    private val gson = Gson()
    private val apiKey = "7186a8462cba833800e98bfc5c35e86041d59fd659df036b3b5d77b5"
    private val url = "https://webstat.banque-france.fr/api/explore/v2.1/catalog/datasets/observations/exports/json/?where=series_key+IN+%28%22EXR.D.USD.EUR.SP00.A%22%29&order_by=-time_period_start&limit=10"

    fun getDollarExchangeRate(): List<Observation>? {
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