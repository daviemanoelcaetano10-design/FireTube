package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RetroHyperlinkBlue
import com.example.ui.theme.RetroMutedGray
import com.example.ui.theme.RetroSearchBorder

@Composable
fun RetroHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onUploadClick: () -> Unit,
    onSignInClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFAFAFA))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        // Row 1: Logo on left, "Sign up | Sign in" on right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RetroYouTubeLogo(
                onClick = { /* Reset or scroll to top */ }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sign up",
                    fontSize = 12.5.sp,
                    color = RetroHyperlinkBlue,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .testTag("sign_up_button")
                        .clickable(onClick = onSignUpClick)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
                Text(
                    text = "|",
                    fontSize = 12.sp,
                    color = Color(0xFFB0B0B0),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Text(
                    text = "Sign in",
                    fontSize = 12.5.sp,
                    color = RetroHyperlinkBlue,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .testTag("sign_in_button")
                        .clickable(onClick = onSignInClick)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Row 2: Search Box with Search icon inside + Upload ▾ button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Retro Search Input Bar
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(18.dp))
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .border(width = 1.dp, color = RetroSearchBorder, shape = RoundedCornerShape(18.dp))
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("search_input"),
                        textStyle = TextStyle(
                            fontSize = 13.5.sp,
                            color = Color(0xFF222222)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Search",
                                    fontSize = 13.5.sp,
                                    color = RetroMutedGray
                                )
                            }
                            innerTextField()
                        }
                    )

                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChange("") },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search",
                                tint = RetroMutedGray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF555555),
                        modifier = Modifier
                            .size(18.dp)
                            .testTag("search_icon_button")
                            .clickable { focusManager.clearFocus() }
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Iconic Yellow Upload ▾ Button
            RetroUploadButton(
                onClick = onUploadClick
            )
        }
    }
}
