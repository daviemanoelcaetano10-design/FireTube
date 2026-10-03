package com.example.ui.components

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.VideoEntity
import com.example.ui.theme.FireTubeBlue
import com.example.ui.theme.RetroCardBorder
import com.example.ui.theme.RetroDarkText
import com.example.ui.theme.RetroMutedGray
import com.example.ui.theme.RetroYouTubeRed

@Composable
fun RetroVideoFeedItem(
    video: VideoEntity,
    isSelected: Boolean,
    onSelectVideo: () -> Unit,
    onEditVideo: () -> Unit,
    onOpenChannel: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFFF0F0F0) else Color.White)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) RetroYouTubeRed else RetroCardBorder,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onSelectVideo)
            .padding(8.dp)
            .testTag("feed_item_${video.id}")
    ) {
        // Thumbnail with duration badge and optional custom thumbnail badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF222222))
        ) {
            if (!video.thumbnailUri.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(Uri.parse(video.thumbnailUri))
                        .crossfade(true)
                        .build(),
                    contentDescription = video.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (!video.thumbnailDrawableName.isNullOrEmpty()) {
                val resId = remember(video.thumbnailDrawableName) {
                    context.resources.getIdentifier(
                        video.thumbnailDrawableName,
                        "drawable",
                        context.packageName
                    )
                }
                if (resId != 0) {
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = video.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Custom thumbnail badge overlay (e.g. "VLOG #1", "NEW!", "EPIC")
            if (!video.thumbnailBadgeText.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                        .background(Color(0xFFE62117), shape = RoundedCornerShape(3.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = video.thumbnailBadgeText,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }

            // Duration badge in bottom right (e.g. 6:00, 0:56)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .background(Color(0xDD000000), shape = RoundedCornerShape(3.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                val m = video.durationSeconds / 60
                val s = video.durationSeconds % 60
                Text(
                    text = String.format("%d:%02d", m, s),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title and Author row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(video.authorAvatarColor))
                    .clickable { onOpenChannel(video.author) }
                    .testTag("feed_author_avatar_${video.id}"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = video.author.take(1).uppercase(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = video.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetroDarkText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = video.author,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = FireTubeBlue,
                        modifier = Modifier.clickable { onOpenChannel(video.author) }
                    )
                    Text(
                        text = " · ${video.viewsCount} · ${video.timeAgo}",
                        fontSize = 11.5.sp,
                        color = RetroMutedGray,
                        maxLines = 1
                    )
                }
            }

            Box {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = RetroMutedGray,
                        modifier = Modifier.size(16.dp)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Assistir Vídeo") },
                        onClick = {
                            showMenu = false
                            onSelectVideo()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Entrar no Canal de Criador") },
                        onClick = {
                            showMenu = false
                            onOpenChannel(video.author)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Editar Descrição e Miniatura") },
                        onClick = {
                            showMenu = false
                            onEditVideo()
                        }
                    )
                }
            }
        }
    }
}
