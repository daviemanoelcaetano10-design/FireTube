package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VideoEntity
import com.example.ui.components.RetroSubscribeButton
import com.example.ui.components.RetroUploadButton
import com.example.ui.components.RetroVideoFeedItem
import com.example.ui.theme.FireTubeBlue
import com.example.ui.theme.FireTubeDarkBlue
import com.example.ui.theme.FireTubeDeepNavy
import com.example.ui.theme.FireTubeGlossyBlueTop
import com.example.ui.theme.FireTubeLightAccent
import com.example.ui.theme.RetroCardBorder
import com.example.ui.theme.RetroDarkText
import com.example.ui.theme.RetroMutedGray
import com.example.ui.theme.RetroPageBg

enum class ChannelTab {
    VIDEOS,
    ABOUT
}

@Composable
fun RetroChannelScreen(
    channelAuthor: String,
    channelVideos: List<VideoEntity>,
    isSubscribed: Boolean,
    onToggleSubscribe: () -> Unit,
    onSelectVideo: (Long) -> Unit,
    onEditVideo: (VideoEntity) -> Unit,
    onUploadClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackClick()
    }

    var selectedTab by remember { mutableStateOf(ChannelTab.VIDEOS) }

    val primaryVideo = channelVideos.firstOrNull()
    val avatarColor = primaryVideo?.authorAvatarColor ?: 0xFF1976D2
    val subscribersCount = primaryVideo?.subscribersCount ?: "1.2M"
    val totalVideos = channelVideos.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(RetroPageBg)
            .testTag("channel_screen")
    ) {
        // Top Navigation Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(width = 1.dp, color = RetroCardBorder)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("channel_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = RetroDarkText
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = channelAuthor,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetroDarkText,
                    modifier = Modifier.weight(1f)
                )

                RetroUploadButton(onClick = onUploadClick)
            }
        }

        // Channel Banner (Retro 2000s Stylized Cover)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                FireTubeDeepNavy,
                                FireTubeDarkBlue,
                                FireTubeBlue,
                                FireTubeGlossyBlueTop
                            )
                        )
                    )
            ) {
                // Subtle vintage decorative header
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = FireTubeLightAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "FireTube Channel",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Channel Profile Info Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-30).dp)
                    .padding(horizontal = 14.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, RetroCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Large Avatar
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .shadow(2.dp, CircleShape)
                                .clip(CircleShape)
                                .background(Color(avatarColor))
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = channelAuthor.take(1).uppercase(),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = channelAuthor,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RetroDarkText
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Verified Channel",
                                    tint = FireTubeBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "@${channelAuthor.lowercase().replace(" ", "_")}",
                                fontSize = 12.5.sp,
                                color = RetroMutedGray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$subscribersCount inscritos · $totalVideos vídeos",
                                fontSize = 12.sp,
                                color = RetroMutedGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Subscribe & Action Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RetroSubscribeButton(
                            isSubscribed = isSubscribed,
                            onClick = onToggleSubscribe
                        )

                        Text(
                            text = "Canal Oficial FireTube",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = RetroMutedGray
                        )
                    }
                }
            }
        }

        // Channel Tabs Header (VÍDEOS / SOBRE)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-18).dp)
                    .background(Color.White)
                    .border(width = 1.dp, color = RetroCardBorder)
                    .padding(horizontal = 14.dp)
            ) {
                // Videos Tab
                Box(
                    modifier = Modifier
                        .clickable { selectedTab = ChannelTab.VIDEOS }
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                        .testTag("channel_tab_videos"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "VÍDEOS ($totalVideos)",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == ChannelTab.VIDEOS) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == ChannelTab.VIDEOS) FireTubeBlue else RetroMutedGray
                        )
                        if (selectedTab == ChannelTab.VIDEOS) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .height(2.5.dp)
                                    .width(60.dp)
                                    .background(FireTubeBlue, RoundedCornerShape(1.dp))
                            )
                        }
                    }
                }

                // About Tab
                Box(
                    modifier = Modifier
                        .clickable { selectedTab = ChannelTab.ABOUT }
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                        .testTag("channel_tab_about"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "SOBRE",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == ChannelTab.ABOUT) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == ChannelTab.ABOUT) FireTubeBlue else RetroMutedGray
                        )
                        if (selectedTab == ChannelTab.ABOUT) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .height(2.5.dp)
                                    .width(40.dp)
                                    .background(FireTubeBlue, RoundedCornerShape(1.dp))
                            )
                        }
                    }
                }
            }
        }

        // Tab Content
        when (selectedTab) {
            ChannelTab.VIDEOS -> {
                if (channelVideos.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RetroCardBorder)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VideoLibrary,
                                    contentDescription = null,
                                    tint = RetroMutedGray,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Nenhum vídeo publicado ainda neste canal",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RetroDarkText
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Use o botão Upload acima para publicar vídeos.",
                                    fontSize = 12.sp,
                                    color = RetroMutedGray
                                )
                            }
                        }
                    }
                } else {
                    items(channelVideos) { video ->
                        RetroVideoFeedItem(
                            video = video,
                            isSelected = false,
                            onSelectVideo = { onSelectVideo(video.id) },
                            onEditVideo = { onEditVideo(video) }
                        )
                    }
                }
            }

            ChannelTab.ABOUT -> {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RetroCardBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Descrição do Canal",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RetroDarkText
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = primaryVideo?.description ?: "Canal clássico no FireTube. Compartilhando vídeos, criatividade e cultura retrô dos anos 2000.",
                                fontSize = 13.sp,
                                color = RetroDarkText,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Estatísticas",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RetroDarkText
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "• Inscritos: $subscribersCount", fontSize = 12.5.sp, color = RetroMutedGray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "• Vídeos no ar: $totalVideos vídeos", fontSize = 12.5.sp, color = RetroMutedGray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "• Inscrição no FireTube: 23 de abril de 2006", fontSize = 12.5.sp, color = RetroMutedGray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "• País: Brasil / Global", fontSize = 12.5.sp, color = RetroMutedGray)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
