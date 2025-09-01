package com.example.todotasks.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todotasks.ui.home.HomePage
import com.example.todotasks.ui.theme.TodoTasksTheme

@Composable
fun TodoNavGraph() {
    TodoTasksTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            startDestination = NavScreen.HOME.route
        ){
            composable(route = NavScreen.HOME.route){
                HomePage()
            }

        }
    }
}

enum class NavScreen(val route: String) {
    HOME("home"),
    TASK("task/{taskId}"),
    COLLECTION("collection/{collectionId}"),
    SETTINGS("settings")
}