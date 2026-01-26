package com.chandra.syncnote.mainscreen


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.chandra.syncnote.navigation.Screen
import com.chandra.syncnote.ui.theme.AppBarTypography
import com.chandra.syncnote.util.dialog.ErrorDialog
import com.chandra.syncnote.util.dialog.MyAlertDialog

@Composable
fun EditNoteScreen(
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
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScaffold(
    navController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // States for title and description
    var textTitle by remember { mutableStateOf(TextFieldValue("")) }
    var textDescription by remember { mutableStateOf(TextFieldValue("")) }

    // State to show dialogs
    val shouldShowDialog = remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }
    var errorDescription by remember { mutableStateOf("") }

    // Get noteId from nav arguments
    val noteId = navController.currentBackStackEntry
        ?.arguments
        ?.getInt("noteId") ?: return

    // Load note once
    LaunchedEffect(noteId) {
        viewModel.getNoteById(noteId)
    }

    // Collect note from StateFlow
    val note by viewModel.editNote.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    // Determine if anything was edited
    val isEdited = remember(textTitle.text, textDescription.text, note) {
        note != null && (textTitle.text != note!!.title || textDescription.text != note!!.content)
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Note", style = AppBarTypography.labelLarge) },
                navigationIcon = {
                    IconButton(onClick = { shouldShowDialog.value = true }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                scrollBehavior = scrollBehavior
            )

            // Show dialogs if needed
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
        },
        floatingActionButton = {
            // Only show FAB if note is loaded
            note?.let { currentNote ->
                ExtendedFloatingActionButton(
                    text = { Text("Save Note",style = AppBarTypography.labelLarge) },
                    icon = { Icon(Icons.Rounded.Check, contentDescription = "Save Note") },
                    onClick = {
                        // Validation
                        if (textTitle.text.isBlank()) {
                            showErrorDialog = true
                            errorText = "Title"
                            errorDescription = "Title cannot be empty"
                            Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                            return@ExtendedFloatingActionButton
                        }
                        if (textDescription.text.isBlank()) {
                            showErrorDialog = true
                            errorText = "Description"
                            errorDescription = "Description cannot be empty"
                            Toast.makeText(context, "Description cannot be empty", Toast.LENGTH_SHORT).show()
                            return@ExtendedFloatingActionButton
                        }

                        // Update note
                        viewModel.updateNote(
                            currentNote.copy(
                                title = textTitle.text,
                                content = textDescription.text,
                                modifiedDate = System.currentTimeMillis(),
                                isEdited = true
                            )
                        )
                        Toast.makeText(context, "Note updated successfully", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },
                    // Disable/enable based on changes
                   // modifier = Modifier.alpha(if (isEdited) 1f else 0.5f)
                )
            }
        }
    ) { innerPadding ->
        // Populate fields once note is loaded
        note?.let { currentNote ->
            if (textTitle.text.isEmpty() && textDescription.text.isEmpty()) {
                textTitle = TextFieldValue(currentNote.title)
                textDescription = TextFieldValue(currentNote.content)
            }
        }

        EditNoteScreen(
            modifier = Modifier.padding(innerPadding),
            title = textTitle,
            description = textDescription,
            onTitleChange = { textTitle = it },
            onDescriptionChange = { textDescription = it }
        )
    }

}

