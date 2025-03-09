package com.example.examprojet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.examprojet.ui.theme.ExamProjetTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.examprojet.components.MyLineChartParent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.examprojet.components.LoadingContent
import com.example.examprojet.model.ListItem
import com.example.examprojet.components.SearchBar
import com.example.examprojet.ui.theme.DeepBlue
import com.example.examprojet.ui.theme.Gold
import com.example.examprojet.viewmodel.DollarSingViewModel
import com.example.examprojet.viewmodel.DollarViewModel
import com.example.examprojet.viewmodel.InflationViewModel
import com.example.examprojet.viewmodel.LivreViewModel
import com.example.examprojet.viewmodel.MasseMonetaireViewModel
import com.example.examprojet.viewmodel.TauxInteretViewModel
import com.example.examprojet.viewmodel.YenViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExamProjetTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val items = listOf(
        ListItem(
            imageSrc = R.drawable.dollars,
            name = "Dollar Americain",
            description = "Vision quotidienne du Dollar Américain",
            destination = "dollar"
        ),
        ListItem(
            imageSrc = R.drawable.tauxdinteret,
            name = "Taux d'interet",
            description = "Taux des crédits nouveaux à l'habitat (hors négociations) aux particuliers",
            destination = "tauxinteret"
        ),
        ListItem(
            imageSrc = R.drawable.inflation,
            name = "Taux d'inflation",
            description = "Inflation actuelle percue, moyenne pondérée, brut, en pourcentage sur un an",
            destination = "inflation"
        ),
        ListItem(
            imageSrc = R.drawable.massemonetaire,
            name = "Masse monétaire M3",
            description = "Agrégats monétaires France, contribution française à M3 hors monnaie fiduciaire",
            destination = "massemonetaire"
        ),
        ListItem(
            imageSrc = R.drawable.yens,
            name = "Yen Japonais",
            description = "Vision quotidienne du Yen",
            destination = "yen"
        ),
        ListItem(
            imageSrc = R.drawable.dollarsingapour,
            name = "Dollar de Singapour",
            description = "Vision quotidienne du Dollar de Singapour",
            destination = "dollarsing"
        ),
        ListItem(
            imageSrc = R.drawable.livresterling,
            name = "Livres Sterling",
            description = "Vision quotidienne du Livre Sterling",
            destination = "livre"
        )
    )

    Column {
        SearchBar { query ->
            searchQuery = query
        }
        ItemList(navController, items.filter { it.name?.contains(searchQuery, ignoreCase = true) ?: false })
    }
}

@Composable
fun ItemList(navController: NavController, items: List<ListItem>) {
    LazyColumn {
        items(items) { item ->
            ListItemView(item) {
                navController.navigate(item.destination)
            }
        }
    }
}

@Composable
fun ListItemView(item: ListItem, onClick: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = item.imageSrc),
                contentDescription = item.name,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(item.name ?: "Name not available", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(item.description ?: "Description not available", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun DollarAmericain(navController: NavController, viewModel: DollarViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.fetchExchangeRates()
    }

    LoadingContent(
        isLoading = viewModel.isLoading,
        isEmpty = viewModel.valueMutableList.isEmpty(),
        errorMessage = viewModel.errorMessage,
        onRetry = { viewModel.fetchExchangeRates() }
    ) {
        val floatValuesList = viewModel.valueMutableList.map { it.toFloat() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Vision quotidienne du Dollar Américain", fontSize = 20.sp, modifier = Modifier.background(Gold).padding(8.dp))
            Spacer(modifier = Modifier.height(16.dp))
            MyLineChartParent(floatValuesList, viewModel.timePeriodMutableList,
                xAxisLabel = "Date",
                yAxisLabel = "Taux de change (USD/EUR)",
                dateFormat = "MM-dd"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Go Back", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun TauxInteret(navController: NavController, viewModel: TauxInteretViewModel = viewModel(), dateFormat: String = "yy-MM") {
    LaunchedEffect(Unit) {
        viewModel.fetchExchangeRates()
    }

    LoadingContent(
        isLoading = viewModel.isLoading,
        isEmpty = viewModel.valueMutableList.isEmpty(),
        errorMessage = viewModel.errorMessage,
        onRetry = { viewModel.fetchExchangeRates() }
    ) {
        val floatValuesList = viewModel.valueMutableList.map { it.toFloat() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Taux d'intérêt aux particuliers", fontSize = 20.sp, modifier = Modifier.background(Gold).padding(8.dp))
            Spacer(modifier = Modifier.height(16.dp))
            MyLineChartParent(floatValuesList, viewModel.timePeriodMutableList, dateFormat,
                xAxisLabel = "Date",
                yAxisLabel = "Taux d'intérêt (%)")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Go Back", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun Inflation(navController: NavController, viewModel: InflationViewModel = viewModel(), dateFormat: String = "yy-MM") {
    LaunchedEffect(Unit) {
        viewModel.fetchExchangeRates()
    }

    LoadingContent(
        isLoading = viewModel.isLoading,
        isEmpty = viewModel.valueMutableList.isEmpty(),
        errorMessage = viewModel.errorMessage,
        onRetry = { viewModel.fetchExchangeRates() }
    ) {
        val floatValuesList = viewModel.valueMutableList.map { it.toFloat() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Taux d'inflation", fontSize = 20.sp, modifier = Modifier.background(Gold).padding(8.dp))
            Spacer(modifier = Modifier.height(16.dp))
            MyLineChartParent(floatValuesList, viewModel.timePeriodMutableList, dateFormat,
                xAxisLabel = "Date par trimestre",
                yAxisLabel = "Taux d'inflation (%)")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Go Back", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun MasseMonetaire(navController: NavController, viewModel: MasseMonetaireViewModel = viewModel(), dateFormat: String = "yy-MM") {
    LaunchedEffect(Unit) {
        viewModel.fetchExchangeRates()
    }

    LoadingContent(
        isLoading = viewModel.isLoading,
        isEmpty = viewModel.valueMutableList.isEmpty(),
        errorMessage = viewModel.errorMessage,
        onRetry = { viewModel.fetchExchangeRates() }
    ) {
        val floatValuesList = viewModel.valueMutableList.map { it.toFloat() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Masse monétaire", fontSize = 20.sp, modifier = Modifier.background(Gold).padding(8.dp))
            Spacer(modifier = Modifier.height(16.dp))
            MyLineChartParent(floatValuesList,
                viewModel.timePeriodMutableList,
                dateFormat,
                labelValueFormatter = { value -> String.format("%.2f MD", value / 1_000_000) },
                xAxisLabel = "Date",
                yAxisLabel = "Masse Monétaire (en milliers de milliards d'euros)"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Go Back", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun Yen(navController: NavController, viewModel: YenViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.fetchExchangeRates()
    }

    LoadingContent(
        isLoading = viewModel.isLoading,
        isEmpty = viewModel.valueMutableList.isEmpty(),
        errorMessage = viewModel.errorMessage,
        onRetry = { viewModel.fetchExchangeRates() }
    ) {
        val floatValuesList = viewModel.valueMutableList.map { it.toFloat() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Vision quotidienne du Yen", fontSize = 20.sp, modifier = Modifier.background(Gold).padding(8.dp))
            Spacer(modifier = Modifier.height(16.dp))
            MyLineChartParent(floatValuesList, viewModel.timePeriodMutableList,
                xAxisLabel = "Date",
                yAxisLabel = "Taux de change (YEN/EUR)",
                dateFormat = "MM-dd"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Go Back", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun DollarSing(navController: NavController, viewModel: DollarSingViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.fetchExchangeRates()
    }

    LoadingContent(
        isLoading = viewModel.isLoading,
        isEmpty = viewModel.valueMutableList.isEmpty(),
        errorMessage = viewModel.errorMessage,
        onRetry = { viewModel.fetchExchangeRates() }
    ) {
        val floatValuesList = viewModel.valueMutableList.map { it.toFloat() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Vision quotidienne du Dollar Singapouréen", fontSize = 20.sp, modifier = Modifier.background(Gold).padding(8.dp))
            Spacer(modifier = Modifier.height(16.dp))
            MyLineChartParent(floatValuesList, viewModel.timePeriodMutableList,
                xAxisLabel = "Date",
                yAxisLabel = "Taux de change (SGD$/EUR)",
                dateFormat = "MM-dd"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Go Back", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun Livres(navController: NavController, viewModel: LivreViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.fetchExchangeRates()
    }

    LoadingContent(
        isLoading = viewModel.isLoading,
        isEmpty = viewModel.valueMutableList.isEmpty(),
        errorMessage = viewModel.errorMessage,
        onRetry = { viewModel.fetchExchangeRates() }
    ) {
        val floatValuesList = viewModel.valueMutableList.map { it.toFloat() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Vision quotidienne du Livre Sterling", fontSize = 20.sp, modifier = Modifier.background(Gold).padding(8.dp))
            Spacer(modifier = Modifier.height(16.dp))
            MyLineChartParent(floatValuesList, viewModel.timePeriodMutableList,
                xAxisLabel = "Date",
                yAxisLabel = "Taux de change (£/EUR)",
                dateFormat = "MM-dd"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Go Back", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Application de graphiques BDF") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepBlue,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DeepBlue
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = MaterialTheme.colorScheme.onPrimary) },
                    label = { Text("Home", color = MaterialTheme.colorScheme.onPrimary) },
                    selected = false,
                    onClick = { navController.navigate("home") }
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
            composable("dollar") { DollarAmericain(navController) }
            composable("tauxinteret") { TauxInteret(navController) }
            composable("inflation") { Inflation(navController) }
            composable("massemonetaire") { MasseMonetaire(navController) }
            composable("yen") { Yen(navController) }
            composable("dollarsing") { DollarSing(navController) }
            composable("livre") { Livres(navController) }


        }
    }
}
