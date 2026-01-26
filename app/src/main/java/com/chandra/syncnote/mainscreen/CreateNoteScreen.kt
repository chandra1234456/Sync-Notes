package com.chandra.syncnote.mainscreen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.chandra.syncnote.domain.model.Note
import com.chandra.syncnote.navigation.Screen
import com.chandra.syncnote.ui.theme.AppBarTypography
import com.chandra.syncnote.util.dialog.ErrorDialog
import com.chandra.syncnote.util.dialog.MyAlertDialog

@Composable
fun CreateNoteScreen(
    modifier: Modifier = Modifier,
    title: TextFieldValue,
    description: TextFieldValue,
    onTitleChange: (TextFieldValue) -> Unit,
    onDescriptionChange: (TextFieldValue) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(text = "Title", style = AppBarTypography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = onTitleChange,
            placeholder = { Text("Please Enter Title") },
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Description", style = AppBarTypography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            value = description,
            onValueChange = onDescriptionChange,
            placeholder = { Text("Please Enter Description") },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScaffold(
    navController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // LIFTED STATE
    var textTitle by remember { mutableStateOf(TextFieldValue("")) }
    var textDescription by remember { mutableStateOf(TextFieldValue("")) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val shouldShowDialog = remember { mutableStateOf(false) }

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }
    var errorDescription by remember { mutableStateOf("") }

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = { AppDrawer() }) {
        Scaffold(
            floatingActionButton = {
                if (currentRoute == Screen.CreateNote.route) {
                    ExtendedFloatingActionButton(
                        text = { Text("Save Note",style = AppBarTypography.labelLarge) },
                        icon = { Icon(Icons.Rounded.Check, contentDescription = "Save Note") },
                        onClick = {
                            // VALIDATION OPTIONAL
                            if (textTitle.text.isBlank()) {
                                showErrorDialog = true
                                errorText = "Title"
                                errorDescription = "Title cannot be empty"
                                Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT)
                                    .show()
                                return@ExtendedFloatingActionButton
                            }
                            if (textDescription.text.isBlank()) {
                                showErrorDialog = true
                                errorText = "Description"
                                errorDescription = "Description cannot be empty"
                                Toast.makeText(
                                    context,
                                    "Description cannot be empty",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@ExtendedFloatingActionButton
                            }

                            // SAVE NOTE
                            viewModel.addNote(
                                Note(
                                    title = textTitle.text,
                                    content = textDescription.text,
                                    timeStamp = System.currentTimeMillis(),
                                    modifiedDate = null,
                                    isEdited = false
                                )
                            )
                            Toast.makeText(context, "Note Saved Successfully", Toast.LENGTH_SHORT)
                                .show()

                            navController.navigate(Screen.Home.route)
                        }
                    )
                }
            },
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Create New Note",
                            style = AppBarTypography.titleLarge,
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    scrollBehavior = scrollBehavior
                )
                // Show dialog when true
                if (shouldShowDialog.value) {
                    MyAlertDialog(shouldShowDialog = shouldShowDialog) {
                        navController.navigate(Screen.Home.route)
                    }
                }
                if (showErrorDialog) {
                    ErrorDialog(
                        title = errorText,
                        message = errorDescription,
                        onDismiss = { showErrorDialog = false }
                    )
                }
            }
        ) { innerPadding ->

            CreateNoteScreen(
                modifier = Modifier.padding(innerPadding),
                title = textTitle,
                description = textDescription,
                onTitleChange = { textTitle = it },
                onDescriptionChange = { textDescription = it }
            )
        }
    }
}
