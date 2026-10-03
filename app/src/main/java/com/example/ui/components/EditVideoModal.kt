package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterVintage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.ui.theme.RetroCardBorder
import com.example.ui.theme.RetroDarkText
import com.example.ui.theme.RetroUploadBorder
import com.example.ui.theme.RetroUploadYellowBottom
import com.example.ui.theme.RetroUploadYellowTop
import com.example.ui.theme.RetroYouTubeRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVideoModal(
    video: VideoEntity,
    onDismiss: () -> Unit,
    onSave: (title: String, description: String, thumbnailUri: String?, badgeText: String?, filter: String) -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title by remember { mutableStateOf(video.title) }
    var description by remember { mutableStateOf(video.description) }
    var selectedThumbnailUri by remember { mutableStateOf(video.thumbnailUri) }
    var thumbnailBadgeText by remember { mutableStateOf(video.thumbnailBadgeText ?: "") }
    var thumbnailFilter by remember { mutableStateOf(video.thumbnailFilter) }

    val thumbnailPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                selectedThumbnailUri = uri.toString()
            }
        }
    )

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
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = RetroYouTubeRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Edit Video Details",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetroDarkText
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Thumbnail Customization Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD54F))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Edit Thumbnail & Effects",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = RetroDarkText
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Preview
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black)
                            .border(1.5.dp, Color(0xFFFFA000), shape = RoundedCornerShape(6.dp))
                            .testTag("edit_thumbnail_preview")
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
                        } else if (!video.thumbnailDrawableName.isNullOrEmpty()) {
                            val resId = context.resources.getIdentifier(
                                video.thumbnailDrawableName,
                                "drawable",
                                context.packageName
                            )
                            if (resId != 0) {
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = "Thumbnail",
                                    contentScale = ContentScale.Crop,
                                    colorFilter = colorFilter,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

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
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

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
                                .testTag("edit_pick_image_button"),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("New Thumbnail", fontSize = 12.sp)
                        }

                        if (selectedThumbnailUri != null) {
                            Button(
                                onClick = { selectedThumbnailUri = null },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF607D8B)),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("reset_thumbnail_button"),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("Reset Image", fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Badge text
                    OutlinedTextField(
                        value = thumbnailBadgeText,
                        onValueChange = { thumbnailBadgeText = it },
                        label = { Text("Badge / Overlay Text") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_badge_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RetroYouTubeRed,
                            unfocusedBorderColor = RetroCardBorder
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

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

                    // Filter selector
                    Text(text = "Filter:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
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

            // Title Editor
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("edit_title_input"),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RetroYouTubeRed,
                    unfocusedBorderColor = RetroCardBorder
                ),
                shape = RoundedCornerShape(6.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description Editor (CRITICAL REQUIREMENT)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Edit Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .testTag("edit_description_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RetroYouTubeRed,
                    unfocusedBorderColor = RetroCardBorder
                ),
                shape = RoundedCornerShape(6.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Changes Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
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
                    .clickable {
                        onSave(
                            title.trim(),
                            description.trim(),
                            selectedThumbnailUri,
                            thumbnailBadgeText.ifBlank { null },
                            thumbnailFilter
                        )
                    }
                    .testTag("save_edit_video_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Save Changes",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF332200)
                )
            }
        }
    }
}
