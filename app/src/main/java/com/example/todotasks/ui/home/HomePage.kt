package com.example.todotasks.ui.home

import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todotasks.MainEvent
import com.example.todotasks.MainViewModel
import com.example.todotasks.ui.MenuActiveCollection
import com.example.todotasks.ui.floataction.MyFloatActionButton
import com.example.todotasks.ui.pagertab.PagerTabLayout
import com.example.todotasks.ui.topbar.MyTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    mainViewModel: MainViewModel = hiltViewModel()
){
    val listTabGroup by mainViewModel.listTabGroup.collectAsStateWithLifecycle(emptyList())
    val taskDelegate = mainViewModel
    var isShowAddTaskBottomSheet by remember{ mutableStateOf(false) }
    var isShowAddTaskCollectionBottomSheet by remember{ mutableStateOf(false) }

    var isShowMenuActiveCollection by remember { mutableStateOf<List<MenuActiveCollection>?>(null) }

    LaunchedEffect(Unit) {
        mainViewModel.eventFlow.collect {
            when(it){
                MainEvent.RequestAddNewCollection -> {
                    isShowAddTaskCollectionBottomSheet = true
                }
                is MainEvent.RequestBottomSheetOption -> {
                    Log.d("HomePage", "Received RequestBottomSheetOption event for taskId: ${it}" )
                    isShowMenuActiveCollection = it.list
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
            MyTopBar()

            if (listTabGroup.isNotEmpty()) {
                PagerTabLayout(
                    listTabGroup,
                    taskDelegate,
                )
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

            if (!isShowMenuActiveCollection.isNullOrEmpty()){
                ModalBottomSheet({
                    isShowMenuActiveCollection = null
                }) {
                    Text("Menu Active Collection", modifier = Modifier.fillMaxWidth())
                    isShowMenuActiveCollection?.forEach { menuItem ->
                        Button({
                            menuItem.action.invoke()
                            isShowMenuActiveCollection = null
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text(menuItem.name)
                        }
                    }
                }
            }
        }
    }
}