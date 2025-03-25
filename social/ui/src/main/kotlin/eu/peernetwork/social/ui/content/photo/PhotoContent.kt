package eu.peernetwork.social.ui.content.photo

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.compose.DesignOption
import eu.peernetwork.core.ui.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
private fun PhotoContent(
    username: MutableState<String>,
    userId: MutableState<String>,
    timeStamp: MutableState<String>,
    photos: List<Int>,
    indicatorColors: PhotoCarouselIndicator = PhotoCarouselIndicator.Default,
    avatar: @Composable () -> Unit
) {
    var showFullScreen by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxWidth()) {
        if (showFullScreen) {
            ZoomableImage(
                photos = photos,
                initialPage = selectedImage,
                onClose = { showFullScreen = false }
            )
        } else {
            Column {
                Box(modifier = Modifier.fillMaxWidth()) {
                    PhotoCarousel(
                        count = photos.size,
                        initialPage = selectedImage,
                        colors = indicatorColors,
                        onPhotoClick = { index ->
                            selectedImage = index
                            showFullScreen = true
                        }
                    ) { page ->
                        Image(
                            painter = painterResource(id = photos[page]),
                            contentDescription = "Content Picture",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillWidth,
                        )
                    }

                    Box(modifier = Modifier.clickable(role = Role.Button) {}) {
                        avatar()
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 70.dp)
                    ) {
                        Row(modifier = Modifier.padding(top = 8.dp)) {
                            Text(
                                text = username.value,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = userId.value,
                                fontSize = 10.sp,
                                color = Color.DarkGray
                            )
                        }
                        Text(
                            text = timeStamp.value,
                            fontSize = 10.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding()
                        )
                    }
                    DesignOutlinedButton(
                        onClick = {},
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Follow",
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding()
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_like),
                            contentDescription = "Icon",
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.Transparent)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth()
                        .background(color = Color.Black)
                ) {
                    Row(modifier = Modifier.fillMaxHeight()) {
                        DesignOption(
                            text = "2",
                            modifier = Modifier.fillMaxHeight(),
                            painter = painterResource(id = R.drawable.ic_like),
                            contentDescription = "Action Icon",
                            tint = Color.White,
                            onClick = {}
                        )
                        DesignOption(
                            text = "1",
                            modifier = Modifier.fillMaxHeight(),
                            painter = painterResource(id = R.drawable.ic_dislike),
                            contentDescription = "Action Icon",
                            tint = Color.White,
                            onClick = {}
                        )
                        DesignOption(
                            text = "3",
                            modifier = Modifier.fillMaxHeight(),
                            painter = painterResource(id = R.drawable.ic_chat_outline),
                            contentDescription = "Action Icon",
                            tint = Color.White,
                            onClick = {}
                        )
                        DesignOption(
                            text = "86",
                            modifier = Modifier.fillMaxHeight(),
                            painter = painterResource(id = R.drawable.ic_profile_outline),
                            contentDescription = "Action Icon",
                            tint = Color.Gray,
                            onClick = {}
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        DesignOption(
                            text = "",
                            modifier = Modifier.fillMaxHeight(),
                            painter = painterResource(id = R.drawable.ic_wallet_outline),
                            contentDescription = "Action Icon",
                            tint = Color.White,
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewPhotoContent() {
    PeerTheme {
        val photos = listOf(
            eu.peernetwork.social.ui.R.mipmap.test1,
            eu.peernetwork.social.ui.R.mipmap.test2,
            eu.peernetwork.social.ui.R.mipmap.test1,
        )

        PhotoContent(
            username = remember { mutableStateOf("Sandro") },
            userId = remember { mutableStateOf("#030604") },
            timeStamp = remember { mutableStateOf("2 Hours Ago") },
            photos = photos,
            indicatorColors = PhotoCarouselIndicator(
                active = Color.Black,
                inactive = Color.Gray
            ),
            avatar = {
                Image(
                    painter = painterResource(id = eu.peernetwork.social.ui.R.mipmap.test2),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(8.dp)
                        .clip(CircleShape)
                )
            }
        )
    }
}