package eu.peernetwork.core.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignBottomSheet(
    showSheet: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    tag: String,
    handleBackPress: Boolean = true,
    initialValue: SheetValue = SheetValue.Hidden,
    sheetPeekHeight: Dp = 400.dp,
    color: Color = MaterialTheme.colorScheme.tertiaryContainer,
    background: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val focus = remember { FocusRequester() }
    val tag = remember { tag }
    var height by remember { mutableIntStateOf(with(density) {
        sheetPeekHeight.toPx().toInt() })
    }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberStandardBottomSheetState(
        initialValue = initialValue,
        skipHiddenState = false
    )
    val initialized = remember { mutableStateOf(false) }
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    DesignOverlayHost(tag, visible = true) {
        overlay {
            background()
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetPeekHeight = with(density) { height.toDp() },
                sheetContainerColor = color,
                sheetShadowElevation = 0.dp,
                sheetContent = {
                    Box (
                        modifier = modifier
                            .focusRequester(focus)
                            .focusable()
                            .onGloballyPositioned { coordinates ->
                                if (coordinates.size.height < height) {
                                    height = coordinates.size.height
                                }
                            }
                    ) { content() }
                },
                sheetSwipeEnabled = true,
                sheetDragHandle = { },
            ) { }
        }
    }
    LaunchedEffect(showSheet.value) {
        if (showSheet.value) {
            focus.requestFocus()
            sheetState.partialExpand()
        } else {
            sheetState.hide()
        }
    }
    LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
        if (scaffoldState.bottomSheetState.currentValue == SheetValue.Hidden && initialized.value) {
            onDismissRequest()
        }
        initialized.value = true
    }
    BackHandler(enabled = showSheet.value && handleBackPress) {
        coroutineScope.launch {
            onDismissRequest()
            sheetState.hide()
        }
    }
}

@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun PreviewDesignBottomSheet() {
    PeerTheme {
        var showSheet = remember { mutableStateOf(false) }
        DesignOverlay {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = {
                            showSheet.value = true
                        }
                    ) {
                        Text("Open Bottom Sheet")
                    }
                    Text(
                        "Main content remains visible",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
            DesignBottomSheet(
                showSheet = showSheet,
                tag = "designBottomSheet",
                onDismissRequest = { showSheet.value = false },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        repeat(20) { index ->
                            Text(
                                "Item $index",
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            if (index < 19) Divider()
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showSheet.value = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Close Sheet")
                        }
                    }
                }
            )
        }
    }
}
