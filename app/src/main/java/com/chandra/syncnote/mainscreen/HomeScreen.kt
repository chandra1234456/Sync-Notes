package com.chandra.syncnote.mainscreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavHostController
import com.chandra.syncnote.domain.model.OrderOption
import com.chandra.syncnote.domain.model.SortOption
import com.chandra.syncnote.ui.theme.AppBarTypography
import com.chandra.syncnote.util.getAppVersion
import com.chandra.syncnote.util.toastMessage
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val notes by viewModel.filteredNotes.collectAsState()
    val search by viewModel.searchText.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {
        item {
            OutlinedTextField(
                value = search,
                onValueChange = { viewModel.searchText.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(56.dp),
                placeholder = { Text("Search notes...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (search.isNotEmpty()) {
                        IconButton(onClick = { viewModel.searchText.value = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(notes) { note ->
            SyncNoteItem(
                note = note,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Handle note click
                    }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
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
                    BottomSheet {
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
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
                    .clickable{ onMode = if (onMode == "Dark") "Light" else "Dark"},
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Icon
                Icon(
                    imageVector = Icons.Default.Bedtime,
                    contentDescription = "Theme Icon",
                    modifier = Modifier.size(28.dp),
                    tint = Color.Magenta
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
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    var selectedSort by remember { mutableStateOf(SortOption.TITLE) }
    var selectedOrder by remember { mutableStateOf(OrderOption.ASCENDING) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // SORT SECTION
            IconWithText(Icons.AutoMirrored.Filled.Sort, "Sort By")

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                item {
                    DefaultCustomChip(
                        icon = Icons.Default.Title,
                        text = "Title",
                        selected = selectedSort == SortOption.TITLE,
                        onClick = { selectedSort = SortOption.TITLE }
                    )
                }
                item {
                    DefaultCustomChip(
                        icon = Icons.Default.CalendarMonth,
                        text = "Created Date",
                        selected = selectedSort == SortOption.CREATED_DATE,
                        onClick = { selectedSort = SortOption.CREATED_DATE }
                    )
                }
                item {
                    DefaultCustomChip(
                        icon = Icons.Default.Refresh,
                        text = "Modified Date",
                        selected = selectedSort == SortOption.MODIFIED_DATE,
                        onClick = { selectedSort = SortOption.MODIFIED_DATE }
                    )
                }
            }

            // ORDER SECTION
            IconWithText(Icons.Default.SwapVert, "Order By")

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                item {
                    DefaultCustomChip(
                        icon = Icons.Default.ArrowUpward,
                        text = "Ascending",
                        selected = selectedOrder == OrderOption.ASCENDING,
                        onClick = { selectedOrder = OrderOption.ASCENDING }
                    )
                }
                item {
                    DefaultCustomChip(
                        icon = Icons.Default.ArrowDownward,
                        text = "Descending",
                        selected = selectedOrder == OrderOption.DESCENDING,
                        onClick = { selectedOrder = OrderOption.DESCENDING }
                    )
                }
            }
            // BUTTONS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(modifier = Modifier.fillMaxWidth().weight(0.5f),onClick = onDismiss) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(modifier = Modifier.fillMaxWidth().weight(0.5f),onClick = onDismiss) {
                    Text("Confirm")
                }
            }
        }
    }
}









