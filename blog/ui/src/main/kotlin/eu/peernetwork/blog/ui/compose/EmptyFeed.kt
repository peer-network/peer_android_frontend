package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.component.DesignError
import com.airbnb.lottie.compose.*
import eu.peernetwork.blog.ui.R
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun EmptyFeed(
    onRefresh: () -> Unit = {}
) {
    val derivedError = remember {
        derivedStateOf {
            Throwable("Explore...")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 1.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.rocketexplore))
        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(128.dp).offset(y = 11.dp),
            isPlaying = true,
            iterations = LottieConstants.IterateForever
        )

        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = "Looks quiet for now. \n" +
                    "Discover what people are up to!",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(2.dp))

        DesignError(
            error = derivedError.value,
            onRetry = onRefresh,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Explore") },
            content = {}
        )
    }
}