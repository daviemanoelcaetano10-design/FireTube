package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.VideoEntity
import com.example.ui.RetroViewModel
import com.example.ui.components.EditVideoModal
import com.example.ui.components.PublishVideoModal
import com.example.ui.components.RetroBottomNav
import com.example.ui.components.RetroCommentsCard
import com.example.ui.components.RetroHeader
import com.example.ui.components.RetroNavTab
import com.example.ui.components.RetroVideoDetails
import com.example.ui.components.RetroVideoFeedItem
import com.example.ui.components.RetroVideoPlayer
import com.example.ui.theme.FireTubeBlue
import com.example.ui.theme.FireTubeDarkBlue
import com.example.ui.theme.RetroCardBorder
import com.example.ui.theme.RetroDarkText
import com.example.ui.theme.RetroMutedGray
import com.example.ui.theme.RetroPageBg

@Composable
fun RetroHomeScreen(
    viewModel: RetroViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val allVideos by viewModel.filteredVideos.collectAsStateWithLifecycle()
    val userVideos by viewModel.userUploadedVideos.collectAsStateWithLifecycle()
    val currentVideo by viewModel.currentVideo.collectAsStateWithLifecycle()
    val channelVideos by viewModel.currentChannelVideos.collectAsStateWithLifecycle()
    val comments by viewModel.currentComments.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var videoToEdit by remember { mutableStateOf<VideoEntity?>(null) }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (uiState.currentChannelAuthor == null) {
                RetroHeader(
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = viewModel::onSearchQueryChange,
                    onUploadClick = viewModel::openPublishModal,
                    onSignInClick = { viewModel.openAuthModal("Sign In") },
                    onSignUpClick = { viewModel.openAuthModal("Sign Up") }
                )
            }
        },
        bottomBar = {
            RetroBottomNav(
                currentTab = uiState.currentTab,
                onTabSelected = viewModel::setNavTab
            )
        },
        containerColor = RetroPageBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Check if user is viewing a creator channel
            if (uiState.currentChannelAuthor != null) {
                RetroChannelScreen(
                    channelAuthor = uiState.currentChannelAuthor!!,
                    channelVideos = channelVideos,
                    isSubscribed = channelVideos.firstOrNull()?.isSubscribed ?: false,
                    onToggleSubscribe = { viewModel.toggleSubscribe(uiState.currentChannelAuthor) },
                    onSelectVideo = viewModel::selectVideo,
                    onEditVideo = { video ->
                        videoToEdit = video
                        viewModel.openEditModal()
                    },
                    onUploadClick = viewModel::openPublishModal,
                    onBackClick = viewModel::closeChannel
                )
            } else {
                when (uiState.currentTab) {
                    RetroNavTab.HOME -> {
                        val activeVideo = currentVideo ?: allVideos.firstOrNull()

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(RetroPageBg)
                                .testTag("home_feed_list")
                        ) {
                            if (activeVideo != null) {
                                // 1. Classic Video Player
                                item(key = "player_${activeVideo.id}") {
                                    RetroVideoPlayer(
                                        video = activeVideo,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                // 2. Video Details & Interactive Controls
                                item(key = "details_${activeVideo.id}") {
                                    RetroVideoDetails(
                                        video = activeVideo,
                                        onToggleLike = viewModel::toggleLike,
                                        onToggleDislike = viewModel::toggleDislike,
                                        onToggleSubscribe = viewModel::toggleSubscribe,
                                        onEditVideoClick = {
                                            videoToEdit = activeVideo
                                            viewModel.openEditModal()
                                        },
                                        onOpenChannel = viewModel::openChannel
                                    )
                                }

                                // 3. Comments Preview Card matching the screenshot
                                item(key = "comments_${activeVideo.id}") {
                                    val totalCountStr = when {
                                        activeVideo.commentsCount >= 1000 -> "${activeVideo.commentsCount / 1000}K"
                                        else -> activeVideo.commentsCount.toString()
                                    }
                                    RetroCommentsCard(
                                        comments = comments,
                                        totalCountString = totalCountStr,
                                        onAddComment = viewModel::addComment
                                    )
                                }

                                // 4. Section for User Uploaded Videos (preserves every video without replacing!)
                                if (userVideos.isNotEmpty()) {
                                    item(key = "user_videos_header") {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 6.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF90CAF9))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Seus Vídeos Publicados (${userVideos.size})",
                                                    fontSize = 13.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = FireTubeDarkBlue
                                                )
                                                Spacer(modifier = Modifier.weight(1f))
                                                Text(
                                                    text = "Todos preservados",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = FireTubeBlue
                                                )
                                            }
                                        }
                                    }

                                    items(
                                        items = userVideos.filter { it.id != activeVideo.id },
                                        key = { "user_${it.id}" }
                                    ) { video ->
                                        RetroVideoFeedItem(
                                            video = video,
                                            isSelected = false,
                                            onSelectVideo = { viewModel.selectVideo(video.id) },
                                            onEditVideo = {
                                                videoToEdit = video
                                                viewModel.openEditModal()
                                            },
                                            onOpenChannel = viewModel::openChannel
                                        )
                                    }
                                }

                                // 5. Section Header for Up Next / Related Videos
                                item(key = "related_header") {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = "Up Next / Vídeos Recomendados",
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RetroDarkText
                                        )
                                    }
                                }
                            }

                            // 6. Related / Next Videos List
                            val relatedVideos = allVideos.filter { it.id != activeVideo?.id && !it.isUserCreated }
                            items(
                                items = relatedVideos,
                                key = { it.id }
                            ) { video ->
                                RetroVideoFeedItem(
                                    video = video,
                                    isSelected = false,
                                    onSelectVideo = { viewModel.selectVideo(video.id) },
                                    onEditVideo = {
                                        videoToEdit = video
                                        viewModel.openEditModal()
                                    },
                                    onOpenChannel = viewModel::openChannel
                                )
                            }

                            item(key = "bottom_space") {
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    }

                    RetroNavTab.EXPLORE -> {
                        RetroExploreScreen(
                            videos = allVideos,
                            onSelectVideo = { id ->
                                viewModel.selectVideo(id)
                                viewModel.setNavTab(RetroNavTab.HOME)
                            },
                            onEditVideo = { video ->
                                videoToEdit = video
                                viewModel.openEditModal()
                            },
                            onOpenChannel = viewModel::openChannel
                        )
                    }

                    RetroNavTab.COMMUNITY -> {
                        RetroCommunityScreen()
                    }
                }
            }

            // Publish Video Modal
            if (uiState.isPublishModalOpen) {
                PublishVideoModal(
                    onDismiss = viewModel::closePublishModal,
                    onPublish = { newVideo, playImmediately ->
                        viewModel.publishVideo(newVideo, playImmediately)
                    }
                )
            }

            // Edit Video Modal (allowing user to edit description and thumbnail)
            if (uiState.isEditModalOpen && videoToEdit != null) {
                EditVideoModal(
                    video = videoToEdit!!,
                    onDismiss = viewModel::closeEditModal,
                    onSave = { title, description, thumbnailUri, badgeText, filter ->
                        viewModel.updateVideoDetails(
                            id = videoToEdit!!.id,
                            title = title,
                            description = description,
                            thumbnailUri = thumbnailUri,
                            badgeText = badgeText,
                            filter = filter
                        )
                    }
                )
            }

            // Auth Modal
            if (uiState.isAuthModalOpen) {
                RetroAuthModal(
                    mode = uiState.authMode,
                    onDismiss = viewModel::closeAuthModal,
                    onConfirm = viewModel::signIn
                )
            }
        }
    }
}
