package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CommentEntity
import com.example.data.RetroDatabase
import com.example.data.VideoEntity
import com.example.data.VideoRepository
import com.example.ui.components.RetroNavTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class RetroUiState(
    val currentTab: RetroNavTab = RetroNavTab.HOME,
    val selectedVideoId: Long = 1L,
    val currentChannelAuthor: String? = null,
    val searchQuery: String = "",
    val isPublishModalOpen: Boolean = false,
    val isEditModalOpen: Boolean = false,
    val isAuthModalOpen: Boolean = false,
    val authMode: String = "Sign In",
    val signedInUser: String? = null,
    val toastMessage: String? = null
)

class RetroViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VideoRepository
    private val _uiState = MutableStateFlow(RetroUiState())
    val uiState: StateFlow<RetroUiState> = _uiState.asStateFlow()

    init {
        val database = RetroDatabase.getDatabase(application, viewModelScope)
        repository = VideoRepository(database.videoDao(), database.commentDao())
    }

    val allVideos: StateFlow<List<VideoEntity>> = repository.allVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userUploadedVideos: StateFlow<List<VideoEntity>> = repository.userUploadedVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredVideos: StateFlow<List<VideoEntity>> = combine(
        allVideos,
        _uiState
    ) { videos, state ->
        if (state.searchQuery.isBlank()) {
            videos
        } else {
            videos.filter {
                it.title.contains(state.searchQuery, ignoreCase = true) ||
                it.author.contains(state.searchQuery, ignoreCase = true) ||
                it.description.contains(state.searchQuery, ignoreCase = true) ||
                it.category.contains(state.searchQuery, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val currentVideo: StateFlow<VideoEntity?> = _uiState.flatMapLatest { state ->
        repository.observeVideo(state.selectedVideoId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val currentChannelVideos: StateFlow<List<VideoEntity>> = _uiState.flatMapLatest { state ->
        val author = state.currentChannelAuthor
        if (author != null) {
            repository.getVideosByAuthor(author)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val currentComments: StateFlow<List<CommentEntity>> = _uiState.flatMapLatest { state ->
        repository.getComments(state.selectedVideoId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectVideo(id: Long) {
        _uiState.value = _uiState.value.copy(
            selectedVideoId = id,
            currentChannelAuthor = null // return to video player view
        )
    }

    fun openChannel(author: String) {
        _uiState.value = _uiState.value.copy(currentChannelAuthor = author)
    }

    fun closeChannel() {
        _uiState.value = _uiState.value.copy(currentChannelAuthor = null)
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setNavTab(tab: RetroNavTab) {
        _uiState.value = _uiState.value.copy(
            currentTab = tab,
            currentChannelAuthor = null
        )
    }

    fun openPublishModal() {
        _uiState.value = _uiState.value.copy(isPublishModalOpen = true)
    }

    fun closePublishModal() {
        _uiState.value = _uiState.value.copy(isPublishModalOpen = false)
    }

    fun openEditModal() {
        _uiState.value = _uiState.value.copy(isEditModalOpen = true)
    }

    fun closeEditModal() {
        _uiState.value = _uiState.value.copy(isEditModalOpen = false)
    }

    fun openAuthModal(mode: String) {
        _uiState.value = _uiState.value.copy(isAuthModalOpen = true, authMode = mode)
    }

    fun closeAuthModal() {
        _uiState.value = _uiState.value.copy(isAuthModalOpen = false)
    }

    fun signIn(username: String) {
        _uiState.value = _uiState.value.copy(
            signedInUser = username,
            isAuthModalOpen = false,
            toastMessage = "Bem-vindo ao FireTube, $username!"
        )
    }

    /**
     * Publishes a new video cleanly as an independent entry without replacing other videos.
     */
    fun publishVideo(video: VideoEntity, playImmediately: Boolean = true) {
        viewModelScope.launch {
            val author = _uiState.value.signedInUser ?: video.author
            // Crucial: ensure id = 0 so Room auto-generates a brand new unique key
            val newId = repository.insertVideo(
                video.copy(
                    id = 0,
                    author = author,
                    isUserCreated = true,
                    createdAt = System.currentTimeMillis()
                )
            )

            // Seed a welcome comment on the newly created video
            repository.addComment(
                videoId = newId,
                author = "FireTubeTeam",
                text = "Parabéns por publicar seu vídeo no FireTube! Compartilhe o link com seus amigos.",
                colorHex = 0xFF0070EB
            )

            _uiState.value = _uiState.value.copy(
                isPublishModalOpen = false,
                selectedVideoId = if (playImmediately) newId else _uiState.value.selectedVideoId,
                currentChannelAuthor = null,
                toastMessage = "Vídeo publicado com sucesso! Adicionado à lista sem substituir o anterior."
            )
        }
    }

    fun updateVideoDetails(
        id: Long,
        title: String,
        description: String,
        thumbnailUri: String?,
        badgeText: String?,
        filter: String
    ) {
        viewModelScope.launch {
            repository.updateVideoDetails(
                id = id,
                title = title,
                description = description,
                thumbnailUri = thumbnailUri,
                badgeText = badgeText,
                filter = filter
            )
            _uiState.value = _uiState.value.copy(
                isEditModalOpen = false,
                toastMessage = "Vídeo atualizado com sucesso!"
            )
        }
    }

    fun toggleLike() {
        val video = currentVideo.value ?: return
        viewModelScope.launch {
            repository.toggleLike(video)
        }
    }

    fun toggleDislike() {
        val video = currentVideo.value ?: return
        viewModelScope.launch {
            repository.toggleDislike(video)
        }
    }

    fun toggleSubscribe(author: String? = null) {
        val targetAuthor = author ?: currentVideo.value?.author ?: return
        val video = currentVideo.value
        val isCurrentlySubscribed = video?.isSubscribed ?: false
        viewModelScope.launch {
            repository.toggleSubscription(targetAuthor, isCurrentlySubscribed)
        }
    }

    fun addComment(text: String) {
        val video = currentVideo.value ?: return
        val author = _uiState.value.signedInUser ?: "FireUser_${System.currentTimeMillis() % 1000}"
        viewModelScope.launch {
            repository.addComment(
                videoId = video.id,
                author = author,
                text = text,
                colorHex = 0xFF0070EB
            )
        }
    }

    fun clearToast() {
        _uiState.value = _uiState.value.copy(toastMessage = null)
    }
}
