package com.example.dogbreedsgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.dogbreedsgallery.navigation.BreedDetailScreen
import com.example.dogbreedsgallery.navigation.BreedListScreen
import com.example.dogbreedsgallery.navigation.FavoritesScreen
import com.example.dogbreedsgallery.ui.screens.BreedDetailScreen as BreedDetailScreenUi
import com.example.dogbreedsgallery.ui.screens.BreedListScreen as BreedListScreenUi
import com.example.dogbreedsgallery.ui.screens.FavoritesScreen as FavoritesScreenUi
import com.example.dogbreedsgallery.ui.theme.DogBreedsGalleryTheme
import com.example.dogbreedsgallery.ui.viewmodel.BreedListViewModel
import com.example.dogbreedsgallery.ui.viewmodel.BreedListViewModelFactory
import com.example.dogbreedsgallery.ui.viewmodel.FavoritesViewModel
import com.example.dogbreedsgallery.ui.viewmodel.FavoritesViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppDependencies.init(this) // Инициализация зависимостей
        enableEdgeToEdge()
        setContent {
            DogBreedsGalleryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainNavHost(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(BreedListScreen)

    NavDisplay(
        modifier = modifier.fillMaxSize(),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {

            // Главный экран (Список пород)
            entry<BreedListScreen> {
                val vm: BreedListViewModel = viewModel(
                    factory = BreedListViewModelFactory(AppDependencies.repository)
                )
                BreedListScreenUi(
                    viewModel = vm,
                    onNavigate = { backStack.add(it) }
                )
            }

            // Экран деталей
            entry<BreedDetailScreen> { key ->
                val vm: BreedListViewModel = viewModel(
                    factory = BreedListViewModelFactory(AppDependencies.repository)
                )
                BreedDetailScreenUi(
                    breedId = key.breedId,
                    viewModel = vm,
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            // Экран избранного
            entry<FavoritesScreen> {
                val vm: FavoritesViewModel = viewModel(
                    factory = FavoritesViewModelFactory(AppDependencies.repository)
                )
                FavoritesScreenUi(
                    viewModel = vm,
                    onNavigate = { backStack.add(it) },
                    onBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}