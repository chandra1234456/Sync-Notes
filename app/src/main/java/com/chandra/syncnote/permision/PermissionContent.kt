package com.chandra.syncnote.permision

/*@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun PermissionContent(
    permissionState: PermissionState,
    uiState: PermissionUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .padding(MaterialTheme.spacing.extraMedium)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.storage_access),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(MaterialTheme.spacing.small))

            Text(
                text = stringResource(R.string.storage_access_permission_text),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(MaterialTheme.spacing.extraMedium))

            if (uiState is PermissionUiState.Denied && uiState.canRequestAgain) {
                MusicmaxOutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = permissionState::launchPermissionRequest
                ) {
                    Text(stringResource(R.string.grant_access))
                }
            }

            if (uiState is PermissionUiState.Denied && uiState.permanentlyDenied) {
                MusicmaxOutlinedButton(
                    modifier = Modifier
                        .padding(top = MaterialTheme.spacing.smallMedium)
                        .fillMaxWidth(),
                    onClick = { context.openSettings() }
                ) {
                    Text(stringResource(R.string.settings))
                }
            }
        }
    }
}*/
