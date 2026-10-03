package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VideoEntity
import com.example.ui.components.RetroVideoFeedItem
import com.example.ui.theme.RetroCardBorder
import com.example.ui.theme.RetroDarkText
import com.example.ui.theme.RetroHyperlinkBlue
import com.example.ui.theme.RetroLikesGreen
import com.example.ui.theme.RetroMutedGray
import com.example.ui.theme.RetroYouTubeRed

@Composable
fun RetroExploreScreen(
    videos: List<VideoEntity>,
    onSelectVideo: (Long) -> Unit,
    onEditVideo: (VideoEntity) -> Unit,
    onOpenChannel: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Viral Classics", "Comedy", "Music", "Vlog", "2000s Memories")
    var selectedCategory by remember { mutableStateOf("All") }

    val filtered = remember(videos, selectedCategory) {
        if (selectedCategory == "All") videos
        else videos.filter { it.category.contains(selectedCategory, ignoreCase = true) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .testTag("explore_screen")
    ) {
        item {
            // Category Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = cat == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) RetroYouTubeRed else Color(0xFFEEEEEE))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = cat,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else RetroDarkText
                        )
                    }
                }
            }
        }

        item {
            // Retro Trending Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFE082))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color(0xFFF57F17),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Featured Videos of the Week",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4037)
                        )
                        Text(
                            text = "Hand-picked internet hall-of-fame clips and user uploads.",
                            fontSize = 11.5.sp,
                            color = Color(0xFF795548)
                        )
                    }
                }
            }
        }

        items(filtered) { video ->
            RetroVideoFeedItem(
                video = video,
                isSelected = false,
                onSelectVideo = { onSelectVideo(video.id) },
                onEditVideo = { onEditVideo(video) },
                onOpenChannel = onOpenChannel
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

data class CommunityPost(
    val author: String,
    val color: Long,
    val timeAgo: String,
    val content: String,
    var likes: Int
)

@Composable
fun RetroCommunityScreen(
    modifier: Modifier = Modifier
) {
    val posts = remember {
        listOf(
            CommunityPost(
                author = "FlashMaster2000",
                color = 0xFF5C6BC0,
                timeAgo = "1 hour ago",
                content = "What was the very first video you ever watched on YouTube back in 2006? For me it was Evolution of Dance in school computer lab!",
                likes = 1420
            ),
            CommunityPost(
                author = "WebcamNostalgia",
                color = 0xFFFF7043,
                timeAgo = "3 hours ago",
                content = "Tip for posting videos: Remember you can customize your thumbnail badge with 'EPIC' and use the CRT filter before publishing!",
                likes = 890
            ),
            CommunityPost(
                author = "RetroArchivist",
                color = 0xFF26A69A,
                timeAgo = "Yesterday",
                content = "Evolution of Dance hit 820M views! Judson Laipply truly created a piece of internet history.",
                likes = 3450
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .testTag("community_screen")
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, RetroCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        tint = RetroYouTubeRed,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Community Bulletins & Forums",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = RetroDarkText
                        )
                        Text(
                            text = "Broadcast your thoughts and share nostalgic memories.",
                            fontSize = 12.sp,
                            color = RetroMutedGray
                        )
                    }
                }
            }
        }

        items(posts) { post ->
            var likeCount by remember { mutableStateOf(post.likes) }
            var isLiked by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, RetroCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(post.color)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = post.author.take(1).uppercase(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = post.author,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = RetroDarkText
                            )
                            Text(
                                text = post.timeAgo,
                                fontSize = 11.sp,
                                color = RetroMutedGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = post.content,
                        fontSize = 13.5.sp,
                        color = RetroDarkText,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                isLiked = !isLiked
                                likeCount = if (isLiked) likeCount + 1 else likeCount - 1
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Like post",
                            tint = if (isLiked) RetroLikesGreen else RetroMutedGray,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = likeCount.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isLiked) RetroLikesGreen else RetroMutedGray
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
