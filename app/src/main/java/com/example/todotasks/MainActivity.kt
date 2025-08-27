package com.example.todotasks

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todotasks.ui.floataction.MyFloatActionButton
import com.example.todotasks.ui.pagertab.PagerTabLayout
import com.example.todotasks.ui.theme.TodoTasksTheme
import com.example.todotasks.ui.topbar.MyTopBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called with ViewModel: $mainViewModel")
        enableEdgeToEdge()
        setContent {
            TodoTasksTheme {
                val listTabGroup by mainViewModel.listTabGroup.collectAsStateWithLifecycle()
                val taskDelegate = mainViewModel
                var isShowAddTaskBottomSheet by remember{ mutableStateOf(false) }
                var isShowAddTaskCollectionBottomSheet by remember{ mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    mainViewModel.eventFlow.collect {
                        when(it){
                            MainEvent.RequestAddNewCollection -> {
                                isShowAddTaskCollectionBottomSheet = true
                            }
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        MyFloatActionButton{
                                isShowAddTaskBottomSheet = true
                        }
                    }
                ){ innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding).fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MyTopBar(taskDelegate)
                        if (listTabGroup.isNotEmpty()) {
                            PagerTabLayout(listTabGroup, taskDelegate)
                        } else {
                            Text("NO TASKS AVAILABLE!!!")
                        }

                        if (isShowAddTaskBottomSheet){
                            var inputTaskContent by remember { mutableStateOf("") }
                            ModalBottomSheet({
                                isShowAddTaskBottomSheet = false
                            }) {
                                Text("Input Task Content", modifier = Modifier.fillMaxWidth())
                                TextField(
                                    value = inputTaskContent,
                                    onValueChange = { inputTaskContent = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Button({
                                    if (inputTaskContent.isNotEmpty()) {
                                        taskDelegate.addNewTaskToCurrentCollection(inputTaskContent)
                                        inputTaskContent = ""
                                    }
                                    isShowAddTaskBottomSheet = false
                                }, modifier = Modifier.fillMaxWidth()) {
                                    Text("Add Task")
                                }
                            }
                        }

                        if (isShowAddTaskCollectionBottomSheet) {
                            var inputTaskCollection by remember { mutableStateOf("") }
                            ModalBottomSheet({
                                isShowAddTaskCollectionBottomSheet = false
                            }) {
                                Text("Input Task Collection", modifier = Modifier.fillMaxWidth())
                                TextField(
                                    value = inputTaskCollection,
                                    onValueChange = { inputTaskCollection = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Button({
                                    if (inputTaskCollection.isNotEmpty()) {
                                        taskDelegate.addNewCollection(inputTaskCollection)
                                        inputTaskCollection = ""
                                    }
                                    isShowAddTaskCollectionBottomSheet = false
                                }, modifier = Modifier.fillMaxWidth()) {
                                    Text("Add Task Collection")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

