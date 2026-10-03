package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.RetroYouTubeLogo
import com.example.ui.theme.RetroCardBorder
import com.example.ui.theme.RetroDarkText
import com.example.ui.theme.RetroUploadBorder
import com.example.ui.theme.RetroUploadYellowBottom
import com.example.ui.theme.RetroUploadYellowTop
import com.example.ui.theme.RetroYouTubeRed

@Composable
fun RetroAuthModal(
    mode: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                RetroYouTubeLogo()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (mode == "Sign Up") "Create your YouTube Account" else "Sign in to YouTube",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = RetroDarkText
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Join the community to broadcast videos, rate 5 stars, and leave comments.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_username_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetroYouTubeRed,
                        unfocusedBorderColor = RetroCardBorder
                    ),
                    shape = RoundedCornerShape(6.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_password_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetroYouTubeRed,
                        unfocusedBorderColor = RetroCardBorder
                    ),
                    shape = RoundedCornerShape(6.dp)
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .shadow(1.dp, shape = RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(RetroUploadYellowTop, RetroUploadYellowBottom)
                        )
                    )
                    .border(1.dp, RetroUploadBorder, RoundedCornerShape(4.dp))
                    .clickable {
                        val name = username.ifBlank { "RetroStar" }
                        onConfirm(name)
                    }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                    .testTag("auth_confirm_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mode,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF332200)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}
