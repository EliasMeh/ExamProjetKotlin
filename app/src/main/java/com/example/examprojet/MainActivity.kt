package com.example.examprojet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.examprojet.ui.theme.ExamProjetTheme
import com.example.examprojet.model.DollarRepository
import kotlinx.coroutines.withContext
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavHost()
        }
    }
}

//HomeScreen

@Composable
fun HomeScreen(navController: NavController) {
    Column {

        Text("Home Screen", fontSize = 24.sp)
        Button(onClick = { navController.navigate("dollar") }) {
            Text("Go to Dollar")
        }
    }
}

//Dollar Americain

@Composable
fun DollarAmericain(navController: NavController) {
    // Initialize state variables to hold the values
    val valueMutableList = remember { mutableStateListOf<Double>() }
    val timePeriodMutableList = remember { mutableStateListOf<String>() }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val isLoading = remember { mutableStateOf(true) } // Track loading state

    // Fetch the exchange rates asynchronously
    LaunchedEffect(Unit) {
        try {
            withContext(Dispatchers.IO) {
                val dollarRepo = DollarRepository()
                val exchangeRates = dollarRepo.getDollarExchangeRate()

                // Clear the existing data
                valueMutableList.clear()
                timePeriodMutableList.clear()

                // Process the new exchange rates
                if (exchangeRates != null) {
                    exchangeRates.forEach { observation ->
                        if (observation.obs_value != 0.0) {
                            valueMutableList.add(observation.obs_value)
                            timePeriodMutableList.add(observation.time_period)
                        }
                    }
                } else {
                    errorMessage.value = "Failed to fetch data: exchangeRates is null."
                }
            }
        } catch (e: Exception) {
            errorMessage.value = "Error fetching data: ${e.message}"
        } finally {
            isLoading.value = false // Hide loading indicator when done
        }
    }

    // Handle loading, empty data, and errors
    if (isLoading.value) {
        // Show loading spinner while data is being fetched
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Loading data...")
        }
    } else {
        // Handle empty data or display chart if data is available
        if (valueMutableList.isEmpty()) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("No data available")
                errorMessage.value?.let { Text("Error: $it") }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.popBackStack() }) {
                    Text("Go Back")
                }
            }
        } else {
            // Convert valueMutableList (Double) to a list of Int for BasicComboChart
            val intValuesList = valueMutableList.map { it.toInt() }

            // UI Layout
            Column {
                Text("Vision quotidienne du Dollar Américain", fontSize = 20.sp)

                Button(onClick = { navController.popBackStack() }) {
                    Text("Go Back")
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun DollarAmericainPreview() {
    ExamProjetTheme {
        DollarAmericain(rememberNavController())
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My App") })
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { navController.navigate("home") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = "Details") },
                    label = { Text("Details") },
                    selected = false,
                    onClick = { navController.navigate("details") }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeScreen(navController) }
            //composable("details") { DetailsScreen(navController) }
            composable("dollar") { DollarAmericain(navController) }
        }
    }
}

