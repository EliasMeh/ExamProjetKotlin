package com.example.examprojet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examprojet.model.DollarRepository
import com.example.examprojet.model.TauxInteretRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TauxInteretViewModel : ViewModel() {
    private val tauxInteretRepo = TauxInteretRepository()
    val valueMutableList = mutableListOf<Double>()
    val timePeriodMutableList = mutableListOf<String>()
    var errorMessage: String? = null
    var isLoading = true

    fun fetchExchangeRates() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val exchangeRates = tauxInteretRepo.getInteretRate()

                    // Clear the existing data
                    valueMutableList.clear()
                    timePeriodMutableList.clear()

                    // Process the new exchange rates
                    if (exchangeRates != null) {
                        val sortedExchangeRates = exchangeRates.sortedBy { it.time_period }
                        sortedExchangeRates.forEach { observation ->
                            if (observation.obs_value != 0.0) {
                                valueMutableList.add(observation.obs_value)
                                timePeriodMutableList.add(observation.time_period)
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
            }
        }
    }
}