package com.example.ui.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VideoEntity
import com.example.ui.theme.RetroCardBorder
import com.example.ui.theme.RetroDarkText
import com.example.ui.theme.RetroDislikesRed
import com.example.ui.theme.RetroHyperlinkBlue
import com.example.ui.theme.RetroLikesGreen
import com.example.ui.theme.RetroMutedGray
import com.example.ui.theme.RetroYouTubeRed

@Composable
fun RetroVideoDetails(
    video: VideoEntity,
    onToggleLike: () -> Unit,
    onToggleDislike: () -> Unit,
    onToggleSubscribe: () -> Unit,
    onEditVideoClick: () -> Unit,
    onOpenChannel: (String) -> Unit = {},
    snackbarHostState: SnackbarHostState? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isDescriptionExpanded by remember(video.id) { mutableStateOf(false) }
    var showMoreMenu by remember { mutableStateOf(false) }
    var showChannelInfoDialog by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        // Video Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = video.title,
                fontSize = 17.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = RetroDarkText,
                modifier = Modifier
                    .weight(1f)
                    .testTag("video_title")
            )

            if (video.isUserCreated) {
                Box(
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(4.dp))
                        .border(1.dp, Color(0xFF81C784), shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "YOUR POST",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        // Metadata: Views · timeAgo · snippet ... more
        val descriptionSnippet = if (video.description.length > 38 && !isDescriptionExpanded) {
            video.description.take(38) + "... "
        } else {
            video.description
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isDescriptionExpanded = !isDescriptionExpanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${video.viewsCount} · ${video.timeAgo} · $descriptionSnippet",
                fontSize = 12.sp,
                color = RetroMutedGray,
                lineHeight = 16.sp,
                modifier = Modifier.weight(1f, fill = false)
            )
            Text(
                text = if (isDescriptionExpanded) " less" else " more",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = RetroHyperlinkBlue,
                modifier = Modifier.testTag("description_more_button")
            )
        }

        // Expanded Description Box
        AnimatedVisibility(visible = isDescriptionExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(Color(0xFFF9F9F9), shape = RoundedCornerShape(6.dp))
                    .border(1.dp, RetroCardBorder, shape = RoundedCornerShape(6.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = video.description,
                    fontSize = 13.sp,
                    color = RetroDarkText,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Category: ${video.category}",
                        fontSize = 11.5.sp,
                        color = RetroMutedGray
                    )
                    Text(
                        text = "Edit Description & Thumbnail",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetroHyperlinkBlue,
                        modifier = Modifier
                            .testTag("edit_details_shortcut")
                            .clickable(onClick = onEditVideoClick)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Iconic Green/Red Ratio Bar (Likes vs Dislikes)
        RetroRatingBar(
            likes = video.likesCount,
            dislikes = video.dislikesCount,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Glossy Action Buttons: Like, Dislike, Share, Save, More
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Like Button
            RetroGlossyActionButton(
                icon = Icons.Default.ThumbUp,
                text = formatCount(video.likesCount),
                isActive = video.userLikeStatus == 1,
                activeColor = RetroLikesGreen,
                testTag = "like_button",
                onClick = onToggleLike
            )

            // Dislike Button
            RetroGlossyActionButton(
                icon = Icons.Default.ThumbDown,
                text = formatCount(video.dislikesCount),
                isActive = video.userLikeStatus == -1,
                activeColor = RetroDislikesRed,
                testTag = "dislike_button",
                onClick = onToggleDislike
            )

            // Share Button
            RetroGlossyActionButton(
                icon = Icons.Default.Share,
                text = "",
                testTag = "share_button",
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Watch '${video.title}' on YouTube Retro! https://youtu.be/retro_${video.id}")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Share Video")
                    context.startActivity(shareIntent)
                }
            )

            // Bookmark/Save Button
            RetroGlossyActionButton(
                icon = Icons.Default.BookmarkBorder,
                text = if (isBookmarked) "Saved" else "",
                isActive = isBookmarked,
                activeColor = RetroHyperlinkBlue,
                testTag = "save_button",
                onClick = { isBookmarked = !isBookmarked }
            )

            // Edit Shortcut Button
            RetroGlossyActionButton(
                icon = Icons.Default.Edit,
                text = "Edit",
                testTag = "edit_video_button",
                onClick = onEditVideoClick
            )

            // More Options "..."
            Box {
                RetroGlossyActionButton(
                    icon = Icons.Default.MoreHoriz,
                    text = "",
                    testTag = "more_options_button",
                    onClick = { showMoreMenu = true }
                )

                DropdownMenu(
                    expanded = showMoreMenu,
                    onDismissRequest = { showMoreMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit Description & Thumbnail") },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                        onClick = {
                            showMoreMenu = false
                            onEditVideoClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Copy Video URL") },
                        leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                        onClick = {
                            showMoreMenu = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(RetroCardBorder)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Channel Info Row with circular avatar, name, subscribers, "more info" and Subscribe button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenChannel(video.author) }
                    .testTag("channel_entry_row")
            ) {
                // Circular Avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(1.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(video.authorAvatarColor)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = video.author.take(1).uppercase(),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = video.author,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = RetroDarkText
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = video.subscribersCount,
                            fontSize = 11.5.sp,
                            color = RetroMutedGray
                        )
                    }
                    Text(
                        text = "entrar no canal ›",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetroHyperlinkBlue,
                        modifier = Modifier
                            .testTag("author_enter_channel")
                    )
                }
            }

            // Glossy Red 3D Subscribe Button
            RetroSubscribeButton(
                isSubscribed = video.isSubscribed,
                onClick = onToggleSubscribe
            )
        }
    }

    // Channel Info Dialog
    if (showChannelInfoDialog) {
        AlertDialog(
            onDismissRequest = { showChannelInfoDialog = false },
            title = {
                Text(text = "About ${video.author}", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(text = "Subscribers: ${video.subscribersCount}", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Joined: 2006", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Broadcast Yourself™ verified retro channel creator.",
                        fontSize = 12.sp,
                        color = RetroMutedGray
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showChannelInfoDialog = false }) {
                    Text("Close", color = RetroYouTubeRed)
                }
            }
        )
    }
}

private fun formatCount(count: Long): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%.0fK", count / 1_000.0)
        else -> count.toString()
    }
}
