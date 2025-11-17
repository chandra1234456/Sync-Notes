package com.chandra.syncnote.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.chandra.syncnote.domain.model.Note
import com.chandra.syncnote.navigation.Screen
import com.chandra.syncnote.ui.theme.AppBarTypography
import com.chandra.syncnote.util.dialog.MyAlertDialog
import com.chandra.syncnote.util.toastMessage
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun CreateNoteScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var textTitle by remember { mutableStateOf(TextFieldValue("")) }
        var textDescription by remember { mutableStateOf(TextFieldValue("")) }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = "Title ",style = AppBarTypography.bodyMedium)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textTitle,
            maxLines = 2,
            minLines = 1,
            textStyle = AppBarTypography.bodyMedium,
            onValueChange = {
                textTitle = it
            },
            placeholder = { Text(text = "Please Enter Title",style = AppBarTypography.bodyMedium) },
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Description ", style = AppBarTypography.bodyMedium)
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            value = textDescription,
            maxLines = 10,
            textStyle = AppBarTypography.bodyMedium,
            minLines = 1,
            onValueChange = {
                textDescription = it
            },
            placeholder = { Text(text = "Please Enter Description here !",style = AppBarTypography.bodyMedium) },
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))

        Button(modifier = Modifier.height(48.dp),
            onClick = {
            viewModel.addNote(
                Note(
                    title = textTitle.text,
                    content = textDescription.text,
                    timeStamp = System.currentTimeMillis()
                )
            )
                context.toastMessage("Note Saved SuccessFully")
                navController.navigate(Screen.Home.route)
        }) {
            Text(text = "Save Note",style = AppBarTypography.labelLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScaffold(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val shouldShowDialog = remember { mutableStateOf(false) } // 1
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { AppDrawer() }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Add Note",
                            style = AppBarTypography.titleLarge,
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            shouldShowDialog.value = true
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Menu")
                        }
                    },
                    /*actions = {
                        IconButton(onClick = { context.toastMessage("Sort") }) {
                            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
                        }
                    },*/
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    scrollBehavior = scrollBehavior
                )
                // Show dialog when true
                if (shouldShowDialog.value) {
                    MyAlertDialog(shouldShowDialog = shouldShowDialog){
                        navController.navigate(Screen.Home.route)
                    }
                }
            }
        ) { innerPadding ->
            // Content goes here, respecting Scaffold padding
            CreateNoteScreen(Modifier.padding(innerPadding), navController = navController)
        }
    }
}
