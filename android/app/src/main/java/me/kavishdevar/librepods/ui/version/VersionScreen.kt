package me.kavishdevar.librepods.ui.version

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.ui.component.StyledScaffold
import me.kavishdevar.librepods.ui.component.StyledTopAppBar
import me.kavishdevar.librepods.ui.theme.LibrePodsTheme

@Composable
fun VersionScreen(
    viewModel: VersionViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    VersionContent(
        state = state
    )
}

@Composable
fun VersionContent(
    state: VersionUiState,
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val backdrop = rememberLayerBackdrop()

    StyledScaffold(
        topBar = {
            StyledTopAppBar(
                title = {
                    Text(stringResource(R.string.customize_adaptive_audio))
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .layerBackdrop(backdrop)
                .padding(horizontal = 16.dp)
        ) {
//            Spacer(Modifier.height(spacerHeight))

            // Optional: Section header (like iOS Settings)
            Text(
                text = stringResource(R.string.version).uppercase(),
                style = typography.labelLarge.copy(color = colorScheme.onSurfaceVariant),
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .padding(start = 4.dp)
            )

            // iOS-style grouped card (e.g., like a Settings section)
            Surface(
                shape = RoundedCornerShape(13.dp),
                color = colorScheme.surface,
                tonalElevation = 0.dp, // no shadow — iOS uses blur, not shadows
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    VersionRow(label = "${stringResource(R.string.version)} 1", value = state.version1)
                    HorizontalDivider(color = colorScheme.outline, thickness = 0.33.dp)
                    VersionRow(label = "${stringResource(R.string.version)} 2", value = state.version2)
                    HorizontalDivider(color = colorScheme.outline, thickness = 0.33.dp)
                    VersionRow(label = "${stringResource(R.string.version)} 3", value = state.version3)
                }
            }
        }
    }
}

@Composable
private fun VersionRow(
    label: String,
    value: String?,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = typography.bodyLarge.copy(color = colorScheme.onSurface)
        )
        Text(
            text = value ?: "N/A",
            style = typography.bodyLarge.copy(color = colorScheme.onSurfaceVariant)
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun VersionContentPreview() {
    LibrePodsTheme {
        VersionContent(
            state = VersionUiState()
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun VersionContentDarkPreview() {
    LibrePodsTheme {
        VersionContent(
            state = VersionUiState()
        )
    }
}
