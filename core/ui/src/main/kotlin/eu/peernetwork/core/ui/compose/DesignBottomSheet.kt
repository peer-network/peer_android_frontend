package eu.peernetwork.core.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignBottomSheet(
    showSheet: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    sheetContent: @Composable () -> Unit,
    sheetPeekHeight: Dp = 400.dp,
    scaffoldContent: @Composable () -> Unit
) {
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = sheetPeekHeight,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) { sheetContent() }
        },
        sheetSwipeEnabled = true,
        sheetDragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    ) { scaffoldContent() }
    LaunchedEffect(showSheet.value) {
        coroutineScope.launch {
            if (showSheet.value) {
                sheetState.partialExpand()
            } else {
                sheetState.hide()
            }
        }
    }
    LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
        if (scaffoldState.bottomSheetState.currentValue == SheetValue.Hidden) {
            onDismissRequest()
        }
    }
}

@Composable
@Preview
fun PreviewDesignBottomSheet() {
    PeerTheme {
        var showSheet = remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        DesignBottomSheet(
            showSheet = showSheet,
            onDismissRequest = { showSheet.value = false },
            sheetPeekHeight = 400.dp,
            sheetContent = {
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
            },
            scaffoldContent = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    showSheet.value = true
                                }
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
            }
        )
    }
}
