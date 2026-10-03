package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FireTubeBlue
import com.example.ui.theme.FireTubeDarkBlue
import com.example.ui.theme.FireTubeGlossyBlueBottom
import com.example.ui.theme.FireTubeGlossyBlueTop
import com.example.ui.theme.FireTubeLightAccent
import com.example.ui.theme.RetroButtonBorder
import com.example.ui.theme.RetroButtonGlossBottom
import com.example.ui.theme.RetroButtonGlossTop
import com.example.ui.theme.RetroDarkText
import com.example.ui.theme.RetroDislikesRed
import com.example.ui.theme.RetroLikesGreen
import com.example.ui.theme.RetroUploadBorder
import com.example.ui.theme.RetroUploadYellowBottom
import com.example.ui.theme.RetroUploadYellowTop

/**
 * Authentic 2000s FireTube logo: "Fire" (black + flame icon) + glossy blue rounded rect with "Tube"
 * and "Broadcast Yourself™" underneath.
 */
@Composable
fun FireTubeLogo(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .testTag("firetube_logo")
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = FireTubeBlue,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Fire",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF1E1E1E),
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.width(2.dp))
            Box(
                modifier = Modifier
                    .shadow(elevation = 1.5.dp, shape = RoundedCornerShape(7.dp))
                    .clip(RoundedCornerShape(7.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                FireTubeGlossyBlueTop,
                                FireTubeBlue,
                                FireTubeGlossyBlueBottom
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = FireTubeDarkBlue,
                        shape = RoundedCornerShape(7.dp)
                    )
                    .padding(horizontal = 7.dp, vertical = 1.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tube",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
            }
        }
        Text(
            text = "Broadcast Yourself™",
            fontSize = 10.5.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF757575),
            modifier = Modifier.padding(start = 2.dp, top = 0.dp)
        )
    }
}

// Backward compatibility alias
@Composable
fun RetroYouTubeLogo(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    FireTubeLogo(modifier = modifier, onClick = onClick)
}

/**
 * Iconic Yellow/Gold glossy Upload ▾ button from classic YouTube
 */
@Composable
fun RetroUploadButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .testTag("upload_button")
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        RetroUploadYellowTop,
                        Color(0xFFFFCF3A),
                        RetroUploadYellowBottom
                    )
                )
            )
            .border(
                width = 1.dp,
                color = RetroUploadBorder,
                shape = RoundedCornerShape(5.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Color(0x33000000)),
                onClick = onClick
            )
            .defaultMinSize(minHeight = 36.dp)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Upload",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF332200)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "▾",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF553300)
            )
        }
    }
}

/**
 * Skeuomorphic glossy action button (Like, Dislike, Share, Save)
 */
@Composable
fun RetroGlossyActionButton(
    icon: ImageVector? = null,
    iconCustom: (@Composable () -> Unit)? = null,
    text: String,
    isActive: Boolean = false,
    activeColor: Color = Color.Unspecified,
    testTag: String,
    onClick: () -> Unit
) {
    val bgBrush = if (isActive && activeColor != Color.Unspecified) {
        Brush.verticalGradient(
            listOf(
                activeColor.copy(alpha = 0.25f),
                activeColor.copy(alpha = 0.12f)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                RetroButtonGlossTop,
                Color(0xFFEEEEEE),
                RetroButtonGlossBottom
            )
        )
    }

    val borderColor = if (isActive && activeColor != Color.Unspecified) {
        activeColor
    } else {
        RetroButtonBorder
    }

    Box(
        modifier = Modifier
            .testTag(testTag)
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .background(bgBrush)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(6.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Color(0x22000000)),
                onClick = onClick
            )
            .defaultMinSize(minHeight = 34.dp, minWidth = 48.dp)
            .padding(horizontal = 11.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    modifier = Modifier.size(16.dp),
                    tint = if (isActive && activeColor != Color.Unspecified) activeColor else RetroDarkText
                )
                Spacer(modifier = Modifier.width(5.dp))
            } else if (iconCustom != null) {
                iconCustom()
                Spacer(modifier = Modifier.width(5.dp))
            }
            if (text.isNotEmpty()) {
                Text(
                    text = text,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isActive && activeColor != Color.Unspecified) activeColor else RetroDarkText
                )
            }
        }
    }
}

/**
 * 3D Glossy Blue Subscribe Button for FireTube
 */
@Composable
fun RetroSubscribeButton(
    isSubscribed: Boolean,
    testTag: String = "subscribe_button",
    onClick: () -> Unit
) {
    val bgBrush = if (!isSubscribed) {
        Brush.verticalGradient(
            listOf(
                FireTubeGlossyBlueTop,
                FireTubeBlue,
                FireTubeGlossyBlueBottom
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                Color(0xFFE8E8E8),
                Color(0xFFD0D0D0),
                Color(0xFFB8B8B8)
            )
        )
    }

    val borderColor = if (!isSubscribed) FireTubeDarkBlue else Color(0xFF9E9E9E)
    val textColor = if (!isSubscribed) Color.White else Color(0xFF424242)

    Box(
        modifier = Modifier
            .testTag(testTag)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .background(bgBrush)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(6.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Color(0x33FFFFFF)),
                onClick = onClick
            )
            .defaultMinSize(minHeight = 36.dp)
            .padding(horizontal = 16.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSubscribed) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp),
                    tint = textColor
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = if (isSubscribed) "Subscribed" else "Subscribe",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

/**
 * Classic Green & Blue/Indigo FireTube Like/Dislike Rating Bar
 */
@Composable
fun RetroRatingBar(
    likes: Long,
    dislikes: Long,
    modifier: Modifier = Modifier
) {
    val total = (likes + dislikes).coerceAtLeast(1L)
    val likeRatio = (likes.toFloat() / total.toFloat()).coerceIn(0.05f, 0.98f)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(likeRatio)
                    .background(RetroLikesGreen)
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f - likeRatio)
                    .background(FireTubeDarkBlue)
            )
        }
    }
}
