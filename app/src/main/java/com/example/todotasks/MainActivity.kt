package com.example.todotasks

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todotasks.ui.floataction.MyFloatActionButton
import com.example.todotasks.ui.pagertab.PagerTabLayout
import com.example.todotasks.ui.theme.TodoTasksTheme
import com.example.todotasks.ui.topbar.MyTopBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called with ViewModel: $mainViewModel")
        enableEdgeToEdge()
        setContent {
            TodoTasksTheme {
                val listTabGroup by mainViewModel.listTabGroup.collectAsStateWithLifecycle()
                val taskDelegate = mainViewModel
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        MyFloatActionButton(
                            Modifier
                                .background(
                                    color = Color.Black.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .size(58.dp)
                                .clip(RoundedCornerShape(12.dp))) {
                                Log.d("MainActivity", "FloatingActionButton Clicked")
                            }
                        }
                ){ innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding).fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MyTopBar()
                        PagerTabLayout(
                            state = listTabGroup,
                            taskDelegate = taskDelegate
                        )
                    }
                }
            }
        }
    }
}

