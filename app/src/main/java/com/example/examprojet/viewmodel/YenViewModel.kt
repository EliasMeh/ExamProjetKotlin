package com.example.examprojet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examprojet.model.YenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class YenViewModel : ViewModel() {
    private val yenRepo = YenRepository()
    val valueMutableList = mutableListOf<Double>()
    val timePeriodMutableList = mutableListOf<String>()
    var errorMessage: String? = null
    var isLoading = true

    fun fetchExchangeRates() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val exchangeRates = yenRepo.getYenRate()

                    valueMutableList.clear()
                    timePeriodMutableList.clear()

                    if (exchangeRates != null) {
                        println("Fetched exchange rates: $exchangeRates")
                        val sortedExchangeRates = exchangeRates.sortedBy { it.time_period }
                        sortedExchangeRates.forEach { observation ->
                            println("Processing observation: $observation")
                            if (observation.obs_value != 0.0) {
                                valueMutableList.add(observation.obs_value)
                                timePeriodMutableList.add(observation.time_period)
                                println("Added time period: ${observation.time_period}")
                            }
                        }
                    } else {
                        errorMessage = "Failed to fetch data: exchangeRates is null."
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Error fetching data: ${e.message}"
            } finally {
                isLoading = false
                println("Time periods: $timePeriodMutableList")
            }
        }
    }
}