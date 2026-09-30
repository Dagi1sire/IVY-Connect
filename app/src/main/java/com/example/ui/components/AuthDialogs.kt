package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.UserAccountEntity
import com.example.ui.theme.IvyBackground
import com.example.ui.theme.IvyBorder
import com.example.ui.theme.IvyGold
import com.example.ui.theme.IvyGreen
import com.example.ui.theme.IvyNavy
import com.example.ui.theme.IvyNavyContainer
import com.example.ui.theme.IvyTextMuted
import com.example.ui.theme.IvyTextPrimary
import com.example.ui.theme.IvyTextSecondary
import com.example.ui.theme.IvyUrgentRed
import com.example.ui.theme.ivyTextFieldColors

@Composable
fun IvyLoginDialog(
  isAmharic: Boolean,
  errorMessage: String?,
  onDismiss: () -> Unit,
  onLogin: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("ivy_login_dialog"),
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier
          .padding(24.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = CircleShape,
              color = IvyNavy,
              modifier = Modifier.size(44.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.AdminPanelSettings,
                  contentDescription = null,
                  tint = IvyGold,
                  modifier = Modifier.size(26.dp)
                )
              }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = if (isAmharic) "የአይቪ መግቢያ" else "IVY Portal Login",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IvyNavy
              )
              Text(
                text = if (isAmharic) "አስተዳዳሪ ወይም የወላጅ አካውንት" else "Admin or Parent Account",
                style = MaterialTheme.typography.bodySmall,
                color = IvyTextSecondary
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = IvyTextMuted)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Username Field
        OutlinedTextField(
          value = username,
          onValueChange = { username = it },
          label = { Text(if (isAmharic) "የተጠቃሚ ስም (Username)" else "Username") },
          leadingIcon = {
            Icon(Icons.Default.Person, contentDescription = null, tint = IvyNavy)
          },
          singleLine = true,
          textStyle = TextStyle(color = IvyTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("login_username_input"),
          shape = RoundedCornerShape(12.dp),
          colors = ivyTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password Field
        OutlinedTextField(
          value = password,
          onValueChange = { password = it },
          label = { Text(if (isAmharic) "የይለፍ ቃል (Password)" else "Password") },
          placeholder = { Text("••••••••") },
          leadingIcon = {
            Icon(Icons.Default.Key, contentDescription = null, tint = IvyNavy)
          },
          trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
              Icon(
                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                tint = IvyNavy
              )
            }
          },
          visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
          keyboardActions = KeyboardActions(onDone = {
            if (username.isNotBlank() && password.isNotBlank()) {
              onLogin(username, password)
            }
          }),
          singleLine = true,
          textStyle = TextStyle(color = IvyTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("login_password_input"),
          shape = RoundedCornerShape(12.dp),
          colors = ivyTextFieldColors(containerColor = Color.White)
        )

        if (!errorMessage.isNullOrBlank()) {
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            color = IvyUrgentRed.copy(alpha = 0.1f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = errorMessage,
              color = IvyUrgentRed,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium,
              modifier = Modifier.padding(10.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        Button(
          onClick = { onLogin(username, password) },
          enabled = username.isNotBlank() && password.isNotBlank(),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("login_submit_btn"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = IvyNavy,
            contentColor = Color.White
          )
        ) {
          Text(
            text = if (isAmharic) "ግባ (Log In)" else "Sign In",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
        }
      }
    }
  }
}

@Composable
fun CreateParentAccountDialog(
  isAmharic: Boolean,
  onDismiss: () -> Unit,
  onCreateAccount: (fullName: String, username: String, pass: String, phone: String, email: String, children: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var fullName by remember { mutableStateOf("") }
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }

  // Child details
  var childName by remember { mutableStateOf("") }
  var selectedDepartment by remember { mutableStateOf("Preschool") }
  var className by remember { mutableStateOf("") }
  var additionalChildren by remember { mutableStateOf("") }

  var validationError by remember { mutableStateOf<String?>(null) }

  val departments = listOf(
    "Preschool",
    "Toddler",
    "Infant",
    "Developmental Daycare",
    "Special Needs Department"
  )

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("create_parent_dialog"),
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = CircleShape,
              color = IvyGreen,
              modifier = Modifier.size(40.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.PersonAdd,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(24.dp)
                )
              }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = if (isAmharic) "አዲስ የወላጅ አካውንት ፍጠር" else "Create Parent Account",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = IvyNavy
              )
              Text(
                text = if (isAmharic) "ለአዳዲስ ወላጆች ፈጣን ምዝገባ" else "Register new parent with IVY credentials",
                style = MaterialTheme.typography.bodySmall,
                color = IvyTextSecondary
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = IvyTextMuted)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Full Name
        OutlinedTextField(
          value = fullName,
          onValueChange = {
            fullName = it
            // Auto suggest username if empty
            if (username.isBlank() && it.isNotBlank()) {
              username = it.lowercase().replace(" ", "").take(8)
            }
          },
          label = { Text(if (isAmharic) "የወላጅ ሙሉ ስም *" else "Parent Full Name *") },
          leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = IvyNavy) },
          singleLine = true,
          textStyle = TextStyle(color = IvyTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("parent_fullname_input"),
          shape = RoundedCornerShape(10.dp),
          colors = ivyTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Username
        OutlinedTextField(
          value = username,
          onValueChange = { username = it.filter { char -> !char.isWhitespace() } },
          label = { Text(if (isAmharic) "የተጠቃሚ ስም (Username) *" else "Username (for Login) *") },
          leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, tint = IvyNavy) },
          singleLine = true,
          textStyle = TextStyle(color = IvyTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("parent_username_input"),
          shape = RoundedCornerShape(10.dp),
          colors = ivyTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Password with auto-generate option
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(if (isAmharic) "የይለፍ ቃል (Password) *" else "Password *") },
            leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, tint = IvyGold) },
            singleLine = true,
            textStyle = TextStyle(color = IvyTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
              .weight(1f)
              .testTag("parent_password_input"),
            shape = RoundedCornerShape(10.dp),
            colors = ivyTextFieldColors(containerColor = Color.White)
          )
          Spacer(modifier = Modifier.width(6.dp))
          OutlinedButton(
            onClick = {
              val randomCode = (1000..9999).random()
              password = "ivy$randomCode"
            },
            shape = RoundedCornerShape(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
          ) {
            Text("Random", fontSize = 11.sp)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Phone Number
        OutlinedTextField(
          value = phone,
          onValueChange = { phone = it },
          label = { Text(if (isAmharic) "ስልክ ቁጥር" else "Phone Number") },
          leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = IvyNavy) },
          singleLine = true,
          textStyle = TextStyle(color = IvyTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium),
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = ivyTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = IvyBorder)
        Spacer(modifier = Modifier.height(14.dp))

        // Enrolled Child Section
        Text(
          text = if (isAmharic) "የልጁ/ልጅቷ መረጃ (Enrolled Child)" else "Enrolled Child Information",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = IvyNavy
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = childName,
          onValueChange = { childName = it },
          label = { Text(if (isAmharic) "የልጁ ስም" else "Child's First Name (Optional)") },
          leadingIcon = { Icon(Icons.Default.School, contentDescription = null, tint = IvyGreen) },
          singleLine = true,
          textStyle = TextStyle(color = IvyTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("child_name_input"),
          shape = RoundedCornerShape(10.dp),
          colors = ivyTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = if (isAmharic) "የትምህርት ክፍል (Department):" else "Department / Program:",
          fontSize = 12.sp,
          color = IvyTextSecondary,
          fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Department Choice Chips
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          departments.take(3).chunked(2).forEach { rowDepts ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              rowDepts.forEach { dept ->
                FilterChip(
                  selected = selectedDepartment == dept,
                  onClick = { selectedDepartment = dept },
                  label = { Text(dept, fontSize = 11.sp) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = IvyNavy,
                    selectedLabelColor = Color.White
                  )
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Optional additional children
        OutlinedTextField(
          value = additionalChildren,
          onValueChange = { additionalChildren = it },
          label = { Text(if (isAmharic) "ተጨማሪ ልጆች (አስፈላጊ ከሆነ)" else "Additional Children (Optional)") },
          placeholder = { Text("e.g. Hana (Toddler), Liya (Infant)") },
          singleLine = true,
          textStyle = TextStyle(color = IvyTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium),
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = ivyTextFieldColors(containerColor = Color.White)
        )

        if (validationError != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = validationError ?: "",
            color = IvyUrgentRed,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
          onClick = {
            if (fullName.isBlank() || username.isBlank() || password.isBlank()) {
              validationError = if (isAmharic) "እባክዎ ሙሉ ስም፣ የተጠቃሚ ስም እና የይለፍ ቃል ያስገቡ" else "Please enter Parent Name, Username, and Password"
            } else {
              validationError = null
              val combinedChildren = if (childName.isNotBlank()) {
                if (additionalChildren.isNotBlank()) {
                  "$childName ($selectedDepartment), $additionalChildren"
                } else {
                  "$childName ($selectedDepartment)"
                }
              } else if (additionalChildren.isNotBlank()) {
                additionalChildren
              } else {
                "Child ($selectedDepartment)"
              }
              onCreateAccount(
                fullName.trim(),
                username.trim(),
                password.trim(),
                phone.trim(),
                email.trim(),
                combinedChildren
              )
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("confirm_create_parent_btn"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = IvyGreen, contentColor = Color.White)
        ) {
          Icon(Icons.Default.Check, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isAmharic) "አካውንቱን ፍጠር (Create Account)" else "Create Parent Account",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }
      }
    }
  }
}

@Composable
fun ParentAccountDetailsDialog(
  account: UserAccountEntity,
  isAmharic: Boolean,
  onDismiss: () -> Unit,
  onDeleteAccount: (String) -> Unit,
  onChangePassword: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var showDeleteConfirm by remember { mutableStateOf(false) }
  var showChangePassword by remember { mutableStateOf(false) }
  var newPasswordInput by remember { mutableStateOf("") }

  if (showDeleteConfirm) {
    AlertDialog(
      onDismissRequest = { showDeleteConfirm = false },
      title = { Text(if (isAmharic) "አካውንት ይሰረዝ?" else "Delete Parent Account?") },
      text = {
        Text(
          if (isAmharic)
            "ይህን የወላጅ አካውንት (${account.fullName} - ${account.username}) በእርግጥ መሰረዝ ይፈልጋሉ?"
          else
            "Are you sure you want to delete the account for ${account.fullName} (@${account.username})?"
        )
      },
      confirmButton = {
        Button(
          onClick = {
            onDeleteAccount(account.username)
            showDeleteConfirm = false
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = IvyUrgentRed)
        ) {
          Text(if (isAmharic) "ሰርዝ" else "Delete")
        }
      },
      dismissButton = {
        TextButton(onClick = { showDeleteConfirm = false }) {
          Text(if (isAmharic) "ተመለስ" else "Cancel")
        }
      }
    )
  }

  if (showChangePassword) {
    AlertDialog(
      onDismissRequest = { showChangePassword = false },
      title = { Text(if (isAmharic) "የይለፍ ቃል ቀይር" else "Change Password") },
      text = {
        Column {
          Text(
            text = if (isAmharic) "አዲስ የይለፍ ቃል ያስገቡ ለ ${account.username}:" else "Enter new password for @${account.username}:",
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = newPasswordInput,
            onValueChange = { newPasswordInput = it },
            label = { Text("New Password") },
            singleLine = true,
            textStyle = TextStyle(color = IvyTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ivyTextFieldColors(containerColor = Color.White)
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (newPasswordInput.isNotBlank()) {
              onChangePassword(account.username, newPasswordInput)
              showChangePassword = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = IvyNavy)
        ) {
          Text(if (isAmharic) "አስቀምጥ" else "Save")
        }
      },
      dismissButton = {
        TextButton(onClick = { showChangePassword = false }) {
          Text(if (isAmharic) "ተመለስ" else "Cancel")
        }
      }
    )
  }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = modifier
        .fillMaxWidth()
        .padding(8.dp),
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = account.fullName,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = IvyNavy
            )
            Text(
              text = "@${account.username}",
              fontSize = 13.sp,
              color = IvyGreen,
              fontWeight = FontWeight.SemiBold
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = IvyTextMuted)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Credentials Box
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          color = IvyNavy.copy(alpha = 0.05f),
          border = androidx.compose.foundation.BorderStroke(1.dp, IvyNavy.copy(alpha = 0.15f))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = if (isAmharic) "የመግቢያ ዝርዝሮች (Credentials):" else "Login Credentials:",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = IvyNavy
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "Username:", fontSize = 13.sp, color = IvyTextSecondary)
              Text(text = account.username, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = IvyTextPrimary)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "Password:", fontSize = 13.sp, color = IvyTextSecondary)
              Text(text = account.password, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = IvyGold)
            }

            if (account.childrenNames.isNotBlank()) {
              Spacer(modifier = Modifier.height(6.dp))
              HorizontalDivider(color = IvyBorder)
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = if (isAmharic) "ልጆች:" else "Enrolled Children:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = IvyTextSecondary
              )
              Text(
                text = account.childrenNames,
                fontSize = 12.sp,
                color = IvyTextPrimary
              )
            }

            if (account.phone.isNotBlank()) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Phone: ${account.phone}",
                fontSize = 12.sp,
                color = IvyTextSecondary
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Copy Credentials button
            Button(
              onClick = {
                val clipText = """
                  IVY Childcare Services - Parent Portal Login
                  Welcome ${account.fullName}!
                  
                  Portal Login:
                  Username: ${account.username}
                  Password: ${account.password}
                  Children: ${account.childrenNames}
                """.trimIndent()
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("IVY Parent Credentials", clipText)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Credentials copied to clipboard!", Toast.LENGTH_SHORT).show()
              },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = IvyNavy),
              modifier = Modifier.fillMaxWidth()
            ) {
              Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (isAmharic) "መረጃውን ኮፒ አድርግ (Copy)" else "Copy Credentials to Share",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Secondary Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = {
              newPasswordInput = ""
              showChangePassword = true
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text(if (isAmharic) "የይለፍ ቃል ቀይር" else "Reset Password", fontSize = 12.sp)
          }

          OutlinedButton(
            onClick = { showDeleteConfirm = true },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = IvyUrgentRed),
            modifier = Modifier.weight(1f)
          ) {
            Text(if (isAmharic) "አካውንቱን ሰርዝ" else "Delete Account", fontSize = 12.sp)
          }
        }
      }
    }
  }
}
