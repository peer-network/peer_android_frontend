package eu.peernetwork.app.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.ui.compose.FeatureLabel
import eu.peernetwork.app.ui.compose.PricingLabel
import eu.peernetwork.app.R as AppRes
import eu.peernetwork.blog.ui.R

@Composable
fun OnboardingActionListing() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PricingLabel(
            lead = painterResource(R.drawable.ic_like),
            trailing = painterResource(R.drawable.ic_gem),
            price = "+ 2",
            label = "Got a like"
        )
        PricingLabel(
            lead = painterResource(R.drawable.ic_like),
            trailing = painterResource(R.drawable.ic_gem),
            price = "+ 2",
            label = "Got a like"
        )
        PricingLabel(
            lead = painterResource(R.drawable.ic_like),
            trailing = painterResource(R.drawable.ic_gem),
            price = "+ 2",
            label = "Got a like"
        )
        PricingLabel(
            lead = painterResource(R.drawable.ic_like),
            trailing = painterResource(R.drawable.ic_gem),
            price = "+ 2",
            label = "Got a like"
        )
    }
}

@Composable
fun OnboardingEngagementListing() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PricingLabel(
            lead = painterResource(R.drawable.ic_like),
            trailing = painterResource(R.drawable.ic_gem),
            price = "+ 2",
            label = "Got a like"
        )
        PricingLabel(
            lead = painterResource(R.drawable.ic_like),
            trailing = painterResource(R.drawable.ic_gem),
            price = "+ 2",
            label = "Got a like"
        )
        PricingLabel(
            lead = painterResource(R.drawable.ic_like),
            trailing = painterResource(R.drawable.ic_gem),
            price = "+ 2",
            label = "Got a like"
        )
        PricingLabel(
            lead = painterResource(R.drawable.ic_like),
            trailing = painterResource(R.drawable.ic_gem),
            price = "+ 2",
            label = "Got a like"
        )
    }
}

@Composable
fun BoxScope.OnboardingFeatureListing() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 12.dp)
            .align(Alignment.Center)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            FeatureLabel(
                "Post and engage more",
                painterResource(AppRes.drawable.bg_unlock),
                modifier = Modifier.weight(1f)
            )
            FeatureLabel(
                title = "Post and engage more",
                painter = painterResource(AppRes.drawable.bg_unlock),
                description = "Coming soon...",
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            FeatureLabel(
                "Post and engage more",
                painterResource(AppRes.drawable.bg_unlock),
                modifier = Modifier.weight(1f)
            )
            FeatureLabel(
                title = "Post and engage more",
                painter = painterResource(AppRes.drawable.bg_unlock),
                description = "Coming soon...",
                modifier = Modifier.weight(1f)
            )
        }
    }
}
