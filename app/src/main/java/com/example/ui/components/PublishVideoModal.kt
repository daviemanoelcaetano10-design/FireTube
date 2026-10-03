package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterVintage
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.VideoEntity
import com.example.ui.theme.FireTubeBlue
import com.example.ui.theme.FireTubeDarkBlue
import com.example.ui.theme.RetroCardBorder
import com.example.ui.theme.RetroDarkText
import com.example.ui.theme.RetroMutedGray
import com.example.ui.theme.RetroUploadBorder
import com.example.ui.theme.RetroUploadYellowBottom
import com.example.ui.theme.RetroUploadYellowTop
import com.example.ui.theme.RetroYouTubeRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishVideoModal(
    onDismiss: () -> Unit,
    onPublish: (VideoEntity, Boolean) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var playImmediately by remember { mutableStateOf(true) }

    // Form State
    var title by remember { mutableStateOf("My Classic 2007 Video") }
    var description by remember {
        mutableStateOf("Recorded with my vintage camcorder! Don't forget to rate 5 stars and subscribe. #retro #broadcastyourself")
    }
    var authorName by remember { mutableStateOf("RetroCreator") }
    var category by remember { mutableStateOf("Entertainment") }
    var categoryExpanded by remember { mutableStateOf(false) }

    // Video Selection State
    var selectedVideoUri by remember { mutableStateOf<String?>(null) }
    var selectedVideoTemplate by remember { mutableStateOf("2007 Skatepark Session") }
    var durationSeconds by remember { mutableIntStateOf(145) }

    // Thumbnail State (CRITICAL: User can edit thumbnail)
    var selectedThumbnailUri by remember { mutableStateOf<String?>(null) }
    var selectedThumbnailDrawable by remember { mutableStateOf("thumb_evolution_dance_1791051945417") }
    var thumbnailBadgeText by remember { mutableStateOf("NEW!") }
    var thumbnailFilter by remember { mutableStateOf("NORMAL") } // NORMAL, VINTAGE, CRT, SEPIA, SATURATED

    // Uploading simulation state
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableFloatStateOf(0f) }
    var uploadStatusText by remember { mutableStateOf("") }

    // Image Picker for custom thumbnail (Play policy zero-permission Photo Picker)
    val thumbnailPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                selectedThumbnailUri = uri.toString()
            }
        }
    )

    // Video Picker for selecting video file from gallery
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                selectedVideoUri = uri.toString()
                title = "Video Upload " + System.currentTimeMillis() % 10000
            }
        }
    )

    val categories = listOf("Entertainment", "Comedy", "Music", "Gaming", "Film & Animation", "Vlog")
    val filters = listOf("NORMAL", "VINTAGE", "CRT", "SEPIA", "SATURATED")
    val badgeSuggestions = listOf("NEW!", "EPIC!", "PART 1", "TUTORIAL", "VIRAL", "2007")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 28.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Publish,
                        contentDescription = null,
                        tint = FireTubeBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "FireTube - Publicar Vídeo",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetroDarkText
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Notice: Non-destructive publishing notice
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF90CAF9))
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = FireTubeBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Publicação cumulativa: seu vídeo será adicionado como um novo item independente no feed sem substituir o vídeo atual!",
                        fontSize = 11.5.sp,
                        color = FireTubeDarkBlue,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step 1: Video File Selection
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                border = androidx.compose.foundation.BorderStroke(1.dp, RetroCardBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "1. Select Video Source",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetroDarkText
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                videoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242)),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("pick_device_video_button"),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.Movie, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pick Video", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                selectedVideoUri = null
                                selectedVideoTemplate = "Retro Jam 2007"
                                durationSeconds = 180
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF616161)),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("select_demo_clip_button"),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.VideoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Retro Clip", fontSize = 12.sp)
                        }
                    }

                    if (selectedVideoUri != null) {
                        Text(
                            text = "Selected from device: $selectedVideoUri",
                            fontSize = 11.sp,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.padding(top = 4.dp),
                            maxLines = 1
                        )
                    } else {
                        Text(
                            text = "Template: $selectedVideoTemplate (${durationSeconds}s)",
                            fontSize = 11.sp,
                            color = RetroMutedGray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Step 2: EDIT THUMBNAIL (CRITICAL USER REQUIREMENT)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD54F))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "2. Customize & Edit Thumbnail",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = RetroDarkText
                        )

                        Text(
                            text = "LIVE PREVIEW",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100),
                            modifier = Modifier
                                .background(Color(0xFFFFE082), shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 16:9 Live Thumbnail Preview with chosen Filter and Badge
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black)
                            .border(1.5.dp, Color(0xFFFFA000), shape = RoundedCornerShape(6.dp))
                            .testTag("thumbnail_live_preview")
                    ) {
                        val colorFilter = when (thumbnailFilter) {
                            "SEPIA" -> {
                                ColorFilter.colorMatrix(
                                    ColorMatrix(
                                        floatArrayOf(
                                            0.393f, 0.769f, 0.189f, 0f, 0f,
                                            0.349f, 0.686f, 0.168f, 0f, 0f,
                                            0.272f, 0.534f, 0.131f, 0f, 0f,
                                            0f, 0f, 0f, 1f, 0f
                                        )
                                    )
                                )
                            }
                            "VINTAGE" -> {
                                ColorFilter.colorMatrix(
                                    ColorMatrix(
                                        floatArrayOf(
                                            1.1f, 0f, 0f, 0f, 10f,
                                            0f, 1.0f, 0f, 0f, 10f,
                                            0f, 0f, 0.8f, 0f, -10f,
                                            0f, 0f, 0f, 1f, 0f
                                        )
                                    )
                                )
                            }
                            "SATURATED" -> {
                                val m = ColorMatrix()
                                m.setToSaturation(1.6f)
                                ColorFilter.colorMatrix(m)
                            }
                            else -> null
                        }

                        if (!selectedThumbnailUri.isNullOrEmpty()) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(Uri.parse(selectedThumbnailUri))
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Thumbnail preview",
                                contentScale = ContentScale.Crop,
                                colorFilter = colorFilter,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            val resId = context.resources.getIdentifier(
                                selectedThumbnailDrawable,
                                "drawable",
                                context.packageName
                            )
                            if (resId != 0) {
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = "Preset thumbnail",
                                    contentScale = ContentScale.Crop,
                                    colorFilter = colorFilter,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        // Badge Banner Overlay
                        if (thumbnailBadgeText.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(8.dp)
                                    .background(RetroYouTubeRed, shape = RoundedCornerShape(3.dp))
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = thumbnailBadgeText,
                                    color = Color.White,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.SansSerif
                                )
                            }
                        }

                        // Duration pill in corner
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .background(Color(0xDD000000), shape = RoundedCornerShape(3.dp))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "2:25",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Buttons to Pick Image or Choose Presets
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                thumbnailPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RetroYouTubeRed),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("pick_custom_thumbnail_button"),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pick Image", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                selectedThumbnailUri = null
                                selectedThumbnailDrawable = if (selectedThumbnailDrawable == "thumb_evolution_dance_1791051945417") {
                                    "thumb_retro_video_1791051971650"
                                } else {
                                    "thumb_evolution_dance_1791051945417"
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF455A64)),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("toggle_preset_thumbnail_button"),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.FilterVintage, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Switch Preset", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Edit Thumbnail Badge Text
                    Text(
                        text = "Thumbnail Badge / Banner Text:",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RetroDarkText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = thumbnailBadgeText,
                        onValueChange = { thumbnailBadgeText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("thumbnail_badge_input"),
                        placeholder = { Text("e.g. EPIC, TUTORIAL, VLOG #1") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RetroYouTubeRed,
                            unfocusedBorderColor = RetroCardBorder
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Quick suggestion pills for badge text
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        badgeSuggestions.forEach { suggestion ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (thumbnailBadgeText == suggestion) RetroYouTubeRed else Color(0xFFEEEEEE))
                                    .clickable { thumbnailBadgeText = suggestion }
                                    .padding(horizontal = 9.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = suggestion,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (thumbnailBadgeText == suggestion) Color.White else RetroDarkText
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Filter selector for thumbnail
                    Text(
                        text = "Retro Visual Filter:",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RetroDarkText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        filters.forEach { filter ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (thumbnailFilter == filter) Color(0xFF263238) else Color(0xFFE0E0E0))
                                    .clickable { thumbnailFilter = filter }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = filter,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (thumbnailFilter == filter) Color.White else Color(0xFF424242)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Step 3: Title & Author
            Text(
                text = "3. Video Title & Creator",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = RetroDarkText
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Video Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("publish_title_input"),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RetroYouTubeRed,
                    unfocusedBorderColor = RetroCardBorder
                ),
                shape = RoundedCornerShape(6.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = authorName,
                    onValueChange = { authorName = it },
                    label = { Text("Channel / Author") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("publish_author_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetroYouTubeRed,
                        unfocusedBorderColor = RetroCardBorder
                    ),
                    shape = RoundedCornerShape(6.dp)
                )

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .testTag("category_dropdown"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RetroYouTubeRed,
                            unfocusedBorderColor = RetroCardBorder
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Step 4: EDIT DESCRIPTION (CRITICAL USER REQUIREMENT)
            Text(
                text = "4. Edit Video Description",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = RetroDarkText
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description & Tags") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .testTag("publish_description_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RetroYouTubeRed,
                    unfocusedBorderColor = RetroCardBorder
                ),
                shape = RoundedCornerShape(6.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Quick hashtag helpers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("#retro", "#2007", "#viral", "#camcorder", "#broadcastyourself").forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFEEEEEE))
                            .clickable {
                                if (!description.contains(tag)) {
                                    description = "$description $tag".trim()
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(text = tag, fontSize = 11.sp, color = RetroDarkText)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Uploading progress simulation
            if (isUploading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(6.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = uploadStatusText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetroYouTubeRed
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { uploadProgress },
                        modifier = Modifier.fillMaxWidth(),
                        color = RetroYouTubeRed,
                        trackColor = Color(0xFFDDDDDD)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Option to play immediately or keep current
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { playImmediately = !playImmediately }
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = playImmediately,
                    onCheckedChange = { playImmediately = it },
                    colors = CheckboxDefaults.colors(checkedColor = FireTubeBlue)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Tocar este vídeo imediatamente após postar",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RetroDarkText
                    )
                    Text(
                        text = "Se desmarcado, continuará no vídeo atual enquanto o novo é adicionado à lista.",
                        fontSize = 11.sp,
                        color = RetroMutedGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Big Glossy 3D Publish Video Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(6.dp))
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                RetroUploadYellowTop,
                                Color(0xFFFFCF3A),
                                RetroUploadYellowBottom
                            )
                        )
                    )
                    .border(1.5.dp, RetroUploadBorder, shape = RoundedCornerShape(6.dp))
                    .clickable(enabled = !isUploading && title.isNotBlank()) {
                        isUploading = true
                        coroutineScope.launch {
                            uploadStatusText = "Reading video & custom thumbnail..."
                            uploadProgress = 0.25f
                            delay(600)
                            uploadStatusText = "Encoding 360p FLV container & applying filter..."
                            uploadProgress = 0.65f
                            delay(700)
                            uploadStatusText = "Uploading metadata and publishing..."
                            uploadProgress = 0.95f
                            delay(500)
                            uploadProgress = 1f

                            val newVideo = VideoEntity(
                                id = 0,
                                title = title.trim(),
                                description = description.trim(),
                                author = authorName.ifBlank { "You" },
                                authorAvatarColor = 0xFF1976D2,
                                subscribersCount = "1",
                                viewsCount = "1 view",
                                timeAgo = "Just now",
                                likesCount = 1L,
                                dislikesCount = 0L,
                                commentsCount = 0L,
                                thumbnailDrawableName = if (selectedThumbnailUri == null) selectedThumbnailDrawable else null,
                                thumbnailUri = selectedThumbnailUri,
                                thumbnailBadgeText = thumbnailBadgeText.ifBlank { null },
                                thumbnailFilter = thumbnailFilter,
                                durationSeconds = durationSeconds,
                                category = category,
                                isUserCreated = true,
                                isSubscribed = false,
                                userLikeStatus = 1
                            )
                            onPublish(newVideo, playImmediately)
                        }
                    }
                    .testTag("submit_publish_video_button"),
                contentAlignment = Alignment.Center
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF332200),
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🚀 Publicar no FireTube",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF332200)
                        )
                    }
                }
            }
        }
    }
}
