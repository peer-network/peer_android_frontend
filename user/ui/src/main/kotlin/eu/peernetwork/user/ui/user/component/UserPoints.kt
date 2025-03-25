package eu.peernetwork.user.ui.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.compose.DesignOption
import eu.peernetwork.user.ui.model.UiPoint
import eu.peernetwork.user.ui.user.point.UserPointModel
import kotlin.collections.forEach

@Composable
fun UserPoints(points: List<UiPoint> = listOf()) {
    Column {
        Row (
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            points.forEach { point ->
                UserPointModel.MAP[point.name]?.let { model ->
                    DesignOption(
                        text = point.available.toString(),
                        painter = painterResource(id = model.icon),
                        contentDescription = stringResource(model.label),
                        onClick = {  }
                    )
                }
            }
        }
    }
}
