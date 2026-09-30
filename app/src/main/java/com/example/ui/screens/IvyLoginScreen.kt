package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.IvyBackground
import com.example.ui.theme.IvyBorder
import com.example.ui.theme.IvyGold
import com.example.ui.theme.IvyGreen
import com.example.ui.theme.IvyNavy
import com.example.ui.theme.IvyTextMuted
import com.example.ui.theme.IvyTextPrimary
import com.example.ui.theme.IvyTextSecondary
import com.example.ui.theme.IvyUrgentRed
import com.example.ui.theme.ivyTextFieldColors

@Composable
fun IvyLoginScreen(
  isAmharic: Boolean,
  errorMessage: String?,
  onLanguageToggle: () -> Unit,
  onLogin: (String, String) -> Unit,
  onCreateAccountClick: (() -> Unit)? = null,
  isLoading: Boolean = false,
  modifier: Modifier = Modifier
) {
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }

  val focusManager = LocalFocusManager.current

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            IvyNavy,
            IvyNavy,
            IvyBackground
          )
        )
      )
  ) {
    // Language toggle in top-right
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 16.dp),
      horizontalArrangement = Arrangement.End,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Surface(
        onClick = onLanguageToggle,
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, IvyGold.copy(alpha = 0.6f)),
        modifier = Modifier.testTag("login_language_toggle")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Language,
            contentDescription = "Language",
            tint = IvyGold,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isAmharic) "English" else "አማርኛ",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }
    }

    // Scrollable login container
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp)
        .padding(top = 60.dp, bottom = 32.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // Branding Crest & Logo
      Box(
        modifier = Modifier
          .size(92.dp)
          .clip(CircleShape)
          .border(3.dp, IvyGold, CircleShape)
          .background(Color.White),
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_app_icon),
          contentDescription = "IVY Childcare Logo",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Title & Subtitle
      Text(
        text = if (isAmharic) "አይቪ የህፃናት ማቆያ እና ቅድመ-ትምህርት" else "IVY Childcare Services",
        fontSize = 22.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.White,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = if (isAmharic) "የወላጆች እና የተቋሙ መረጃ መረብ" else "Parent & Administration Portal",
        fontSize = 13.sp,
        color = Color.White.copy(alpha = 0.85f),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(28.dp))

      // Main Form Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .widthIn(max = 480.dp)
          .testTag("ivy_login_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
        ) {
          Text(
            text = if (isAmharic) "ይግቡ" else "Sign In",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = IvyNavy
          )

          Text(
            text = if (isAmharic) "የተጠቃሚ ስም እና የይለፍ ቃልዎን ያስገቡ" else "Enter your credentials to access your account",
            fontSize = 13.sp,
            color = IvyTextSecondary,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
          )

          // Error Alert Banner (if any)
          if (!errorMessage.isNullOrBlank()) {
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .testTag("login_error_banner"),
              color = IvyUrgentRed.copy(alpha = 0.1f),
              shape = RoundedCornerShape(10.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, IvyUrgentRed.copy(alpha = 0.3f))
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.ErrorOutline,
                  contentDescription = "Error",
                  tint = IvyUrgentRed,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = errorMessage,
                  fontSize = 12.sp,
                  color = IvyUrgentRed,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }

          // Username Field
          Text(
            text = if (isAmharic) "የተጠቃሚ ስም" else "Username",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = IvyTextPrimary,
            modifier = Modifier.padding(bottom = 6.dp)
          )
          OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            leadingIcon = {
              Icon(Icons.Default.Person, contentDescription = null, tint = IvyNavy)
            },
            singleLine = true,
            textStyle = TextStyle(
              color = IvyTextPrimary,
              fontSize = 16.sp,
              fontWeight = FontWeight.Medium
            ),
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.Text,
              imeAction = ImeAction.Next
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_username_input"),
            shape = RoundedCornerShape(12.dp),
            colors = ivyTextFieldColors(containerColor = Color.White)
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Password Field
          Text(
            text = if (isAmharic) "የይለፍ ቃል" else "Password",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = IvyTextPrimary,
            modifier = Modifier.padding(bottom = 6.dp)
          )
          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            leadingIcon = {
              Icon(Icons.Default.Lock, contentDescription = null, tint = IvyNavy)
            },
            trailingIcon = {
              IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                  imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                  contentDescription = if (passwordVisible) "Hide password" else "Show password",
                  tint = IvyNavy
                )
              }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            textStyle = TextStyle(
              color = IvyTextPrimary,
              fontSize = 16.sp,
              fontWeight = FontWeight.Medium
            ),
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.Password,
              imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
              onDone = {
                focusManager.clearFocus()
                if (username.isNotBlank() && password.isNotBlank()) {
                  onLogin(username.trim(), password.trim())
                }
              }
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_password_input"),
            shape = RoundedCornerShape(12.dp),
            colors = ivyTextFieldColors(containerColor = Color.White)
          )

          Spacer(modifier = Modifier.height(24.dp))

          // Submit Button
          val canSubmit = username.isNotBlank() && password.isNotBlank() && !isLoading
          Button(
            onClick = {
              focusManager.clearFocus()
              onLogin(username.trim(), password.trim())
            },
            enabled = canSubmit,
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("login_submit_btn"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = IvyNavy,
              contentColor = Color.White,
              disabledContainerColor = IvyNavy.copy(alpha = 0.4f),
              disabledContentColor = Color.White.copy(alpha = 0.6f)
            )
          ) {
            if (isLoading) {
              androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = IvyGold,
                strokeWidth = 2.5.dp
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = if (isAmharic) "በማረጋገጥ ላይ..." else "Signing in...",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
              )
            } else {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Text(
                  text = if (isAmharic) "ግባ" else "Sign In",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }

          if (onCreateAccountClick != null) {
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedButton(
              onClick = {
                focusManager.clearFocus()
                onCreateAccountClick()
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("login_create_parent_btn"),
              shape = RoundedCornerShape(14.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, IvyGreen),
              colors = ButtonDefaults.outlinedButtonColors(
                contentColor = IvyGreen
              )
            ) {
              Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (isAmharic) "አዲስ ወላጅ? እዚህ ይመዝገቡ" else "New Parent? Register Here",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(32.dp))

      // Footer
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = "IVY Childcare Services • Addis Ababa, Ethiopia",
          fontSize = 11.sp,
          color = IvyTextSecondary,
          textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = if (isAmharic) "ደህንነቱ የተጠበቀ የህፃናት ማቆያ እና ቅድመ-ትምህርት ቤት" else "Safe, Nurturing & Enriching Early Childhood Education",
          fontSize = 11.sp,
          color = IvyTextMuted,
          textAlign = TextAlign.Center
        )
      }
    }
  }
}
