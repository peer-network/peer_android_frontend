package eu.peernetwork.app.ui.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.ui.compose.FeatureLabel
import eu.peernetwork.app.ui.compose.PricingLabel
import eu.peernetwork.app.R as AppRes
import eu.peernetwork.blog.ui.R
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.absoluteValue

@Composable
fun OnboardingActionListing(
    actionTokenPrices: Map<String, Int>
) {
    val post = actionTokenPrices["post"] ?: 0
    val like = actionTokenPrices["like"] ?: 0
    val comment = actionTokenPrices["comment"] ?: 0
    val dislike = actionTokenPrices["dislike"] ?: 0
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PricingLabel(
            lead = painterResource(R.drawable.ic_camera_outline),
            trailing = painterResource(AppRes.drawable.ic_icon),
            price = post.toString(),
            label = stringResource(AppRes.string.onboarding_option_extra_post),
            color = MaterialTheme.colorScheme.onBackground
        )
        PricingLabel(
            lead = painterResource(R.drawable.ic_love_outline),
            trailing = painterResource(AppRes.drawable.ic_icon),
            price = like.toString(),
            label = stringResource(AppRes.string.onboarding_option_extra_like),
            color = MaterialTheme.colorScheme.onBackground
        )
        PricingLabel(
            lead = painterResource(R.drawable.ic_comment_outline),
            trailing = painterResource(AppRes.drawable.ic_icon),
            price = comment.toString(),
            label = stringResource(AppRes.string.onboarding_option_extra_comments),
            color = MaterialTheme.colorScheme.onBackground
        )
        PricingLabel(
            lead = painterResource(R.drawable.ic_hate_outline),
            trailing = painterResource(AppRes.drawable.ic_icon),
            price = dislike.toString(),
            label = stringResource(AppRes.string.onboarding_option_dislike),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun OnboardingEngagementListing(
    actionGemsReturns: Map<String, Double>
) {
    val like = actionGemsReturns["like"] ?: 0.0
    val dislike = actionGemsReturns["dislike"] ?: 0.0
    val comment = actionGemsReturns["comment"] ?: 0.0
    val view = actionGemsReturns["view"] ?: 0.0
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PricingLabel(
            lead = painterResource(R.drawable.ic_love),
            trailing = painterResource(R.drawable.ic_gem),
            price = like.toGemLabel(),
            trailingSize = 16.dp,
            label = stringResource(AppRes.string.onboarding_got_like)
        )
        PricingLabel(
            lead = painterResource(R.drawable.ic_hate),
            trailing = painterResource(R.drawable.ic_gem),
            price = dislike.toGemLabel(),
            trailingSize = 16.dp,
            label = stringResource(AppRes.string.onboarding_got_dislike)
        )
        PricingLabel(
            lead = painterResource(R.drawable.ic_comment),
            trailing = painterResource(R.drawable.ic_gem),
            price = comment.toGemLabel(),
            trailingSize = 16.dp,
            label = stringResource(AppRes.string.onboarding_got_comment)
        )
        PricingLabel(
            lead = painterResource(R.drawable.ic_view),
            trailing = painterResource(R.drawable.ic_gem),
            price = view.toGemLabel(),
            trailingSize = 16.dp,
            label = stringResource(AppRes.string.onboarding_got_view)
        )
    }
}
private fun Double.toGemLabel(): String {
    val abs = absoluteValue
    val base = if (abs == abs.toLong().toDouble()) abs.toLong().toString() else trimZeros()
    return when {
        this > 0 -> "+ $base"
        this < 0 -> "- $base"
        else -> "0"
    }
}

private fun Double.trimZeros(): String {
    val s = toString()
    return if (s.contains('.')) s.trimEnd('0').trimEnd('.') else s
}


@Composable
fun BoxScope.OnboardingFeatureListing() {
    val contents = persistentListOf(
        Triple(
            painterResource(AppRes.drawable.bg_unlock),
            stringResource(AppRes.string.onboarding_post_and_engage),
            null
        ),
        Triple(
            painterResource(AppRes.drawable.bg_ad),
            stringResource(AppRes.string.onboarding_boost_your_content),
            stringResource(AppRes.string.onboarding_coming_soon)
        ),
        Triple(
            painterResource(AppRes.drawable.bg_shop),
            stringResource(AppRes.string.onboarding_shop_in_app),
            stringResource(AppRes.string.onboarding_coming_soon)
        ),
        Triple(
            painterResource(AppRes.drawable.bg_transfer),
            stringResource(AppRes.string.onboarding_cash_out),
            stringResource(AppRes.string.onboarding_cash_out_working_on_license)
        )
    )
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 12.dp)
            .align(Alignment.Center)
    ) {
        items(contents) { item ->
            FeatureLabel(
                title = item.second,
                painter = item.first,
                description = item.third,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun OnboardingActionListingPreview() {
    DesignTheme {
        OnboardingActionListing(
            actionTokenPrices = mapOf(
                "post" to 20,
                "like" to 3,
                "comment" to 1,
                "dislike" to 3
            )
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun OnboardingEngagementListingPreview() {
    DesignTheme {
        OnboardingEngagementListing(
            actionGemsReturns = mapOf(
                "like" to 5.0,
                "dislike" to -3.0,
                "comment" to 2.0,
                "view" to 0.25
            )
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun OnboardingFeatureListingPreview() {
    DesignTheme {
        Box { OnboardingFeatureListing() }
    }
}
