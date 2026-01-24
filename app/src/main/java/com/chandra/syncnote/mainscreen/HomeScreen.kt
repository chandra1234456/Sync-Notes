package com.chandra.syncnote.mainscreen

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.chandra.syncnote.domain.model.Note
import com.chandra.syncnote.domain.model.OrderOption
import com.chandra.syncnote.domain.model.SortOption
import com.chandra.syncnote.ui.theme.AppBarTypography
import com.chandra.syncnote.util.getAppVersion
import com.chandra.syncnote.util.toastMessage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val notes by viewModel.filteredNotes.collectAsStateWithLifecycle()
    val search by viewModel.searchText.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Hold dismiss states per note id
    val dismissStates = remember { mutableStateMapOf<Int, SwipeToDismissBoxState>() }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 30.dp)
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(6.dp)
        ) {

            // Search Field
            item {
                OutlinedTextField(
                    value = search,
                    onValueChange = { viewModel.searchText.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(56.dp),
                    placeholder = { Text("Search notes…") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    trailingIcon = {
                        if (search.isNotEmpty()) {
                            IconButton(onClick = { viewModel.searchText.value = "" }) {
                                Icon(Icons.Default.Close, "Clear search")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.height(12.dp))
            }

            // Notes List
            items(
                items = notes,
                key = { it.id ?: 0 }
            ) { note ->

                val dismissState = dismissStates.getOrPut(note.id ?: 0) {
                    rememberSwipeToDismissBoxState(initialValue = SwipeToDismissBoxValue.Settled)
                }

                SwipeToDeleteNote(
                    note = note,
                    dismissState = dismissState,
                    onDelete = { viewModel.deleteNote(note) },
                    onDismiss = {
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "Note deleted",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Short
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.restoreNote(note)
                            }
                            dismissState.reset()
                        }
                    }
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun SwipeToDeleteNote(
    note: Note,
    dismissState: SwipeToDismissBoxState,
    onDelete: () -> Unit,
    onDismiss: (SwipeToDismissBoxValue) -> Unit
) {
    val scope = rememberCoroutineScope()
    val onDeleteUpdated by rememberUpdatedState(onDelete)

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false, // swipe only right to left
        backgroundContent = {
            DeleteBackground(dismissState)
        },
        content = {
            SyncNoteItem(note = note)
        },
        onDismiss = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                scope.launch {
                    try {
                        onDeleteUpdated()
                        onDismiss(value)
                    } catch (e: Exception) {
                        Log.e("SwipeDelete", "Delete failed", e)
                    }
                }
                true
            } else false
        }
    )
}

@Composable
fun DeleteBackground(
    dismissState: SwipeToDismissBoxState
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart)
            MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surface,
        label = "delete_bg"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(end = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete note",
            tint = Color.Red
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteAppScaffold(
    navController: NavHostController
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var showSheet by remember { mutableStateOf(false) }

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
                            "Sync Note",
                            style = AppBarTypography.titleLarge,
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            context.toastMessage("Menu")
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            showSheet = true
                        }) {
                            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    scrollBehavior = scrollBehavior
                )
                if (showSheet) {
                    val viewModel: NoteViewModel = hiltViewModel()
                    BottomSheet(viewModel = viewModel) {
                        showSheet = false
                    }
                }
            }
        ) { innerPadding ->
            // Content goes here, respecting Scaffold padding
            HomeContent(Modifier.padding(innerPadding))
        }
    }
}

@Composable
@Preview
fun AppDrawer() {
    val context = LocalContext.current
    var onMode by remember { mutableStateOf("Dark") }
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .heightIn(max = 400.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            val (versionName, versionCode) = context.getAppVersion()
            Text(
                "About us",
                style = AppBarTypography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                "Version : $versionName",
                style = AppBarTypography.titleSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .clickable { onMode = if (onMode == "Dark") "Light" else "Dark" },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Icon
                Icon(
                    imageVector = Icons.Default.Bedtime,
                    contentDescription = "Theme Icon",
                    modifier = Modifier.size(28.dp),
                    tint = Color.Black
                )
                Spacer(Modifier.width(16.dp))
                // Texts
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Theme Settings",
                        style = AppBarTypography.titleSmall
                    )
                    Text(
                        "Switch To $onMode Mode",
                        style = AppBarTypography.bodySmall,
                        color = Color.Gray
                    )
                }
                // Right Arrow
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Forward Arrow",
                    modifier = Modifier.size(18.dp)
                )
            }
            NavigationDrawerItem(
                icon = {
                    /*Icon(
                    Icons.Filled.Update,
                    contentDescription = "Version"
                )*/
                },
                label = { },
                selected = false,
                onClick = { /* Handle click */ }
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    viewModel: NoteViewModel,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val filter by viewModel.notesFilter.collectAsStateWithLifecycle()

    var selectedSort by remember { mutableStateOf(filter.sortBy) }
    var selectedOrder by remember { mutableStateOf(filter.orderBy) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // SORT SECTION
            IconWithText(Icons.AutoMirrored.Filled.Sort, "Sort By")

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                val sortOptions = listOf(
                    SortOption.TITLE to Icons.Default.Title,
                    SortOption.CREATED_DATE to Icons.Default.CalendarMonth,
                    SortOption.MODIFIED_DATE to Icons.Default.Refresh
                )
                items(sortOptions) { (option, icon) ->
                    DefaultCustomChip(
                        icon = icon,
                        text = option.name.replace("_", " ").capitalize(),
                        selected = selectedSort == option,
                        onClick = { selectedSort = option }
                    )
                }
            }

            // ORDER SECTION
            IconWithText(Icons.Default.SwapVert, "Order By")

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                val orderOptions = listOf(
                    OrderOption.ASCENDING to Icons.Default.ArrowUpward,
                    OrderOption.DESCENDING to Icons.Default.ArrowDownward
                )
                items(orderOptions) { (option, icon) ->
                    DefaultCustomChip(
                        icon = icon,
                        text = option.name.capitalize(),
                        selected = selectedOrder == option,
                        onClick = { selectedOrder = option }
                    )
                }
            }

            // BUTTONS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onDismiss
                ) { Text("Cancel") }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.updateFilter(selectedSort, selectedOrder)
                        onDismiss()
                    }
                ) { Text("Confirm") }
            }
        }
    }
}










