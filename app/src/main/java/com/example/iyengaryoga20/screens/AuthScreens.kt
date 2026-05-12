package com.example.iyengaryoga20.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onLoginClick: (String) -> Unit) {
    var phone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Вход", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Введите номер телефона", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { if (it.length <= 12) phone = it },
            label = { Text("Телефон (+7...)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onLoginClick(phone) },
            enabled = phone.length > 5,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Продолжить", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun RegistrationScreen(
    phone: String,
    onRegisterClick: (name: String, email: String, dob: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }

    // --- НОВОЕ СОСТОЯНИЕ ГАЛОЧКИ ---
    var isPrivacyAccepted by remember { mutableStateOf(false) }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Телефон: $phone", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("ФИО") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = dob, onValueChange = { input ->
            val digitsOnly = input.filter { it.isDigit() }
            if (digitsOnly.length <= 8) dob = digitsOnly
        }, label = { Text("Дата рождения") }, placeholder = { Text("ДД.ММ.ГГГГ") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), visualTransformation = DateVisualTransformation(), modifier = Modifier.fillMaxWidth(), colors = textFieldColors)

        Spacer(modifier = Modifier.height(16.dp))

        // --- БЛОК С ГАЛОЧКОЙ ПОЛИТИКИ КОНФИДЕНЦИАЛЬНОСТИ ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isPrivacyAccepted,
                onCheckedChange = { isPrivacyAccepted = it },
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )
            Text(
                text = "Я согласен на обработку персональных данных и принимаю Политику конфиденциальности",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onRegisterClick(name, email, dob) },
            // Кнопка активна ТОЛЬКО если заполнены ФИО, Email и стоит галочка!
            enabled = name.isNotBlank() && email.isNotBlank() && isPrivacyAccepted,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Завершить", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

// --- КЛАСС ДЛЯ ВСТАВКИ ТОЧЕК В ДАТУ ---
class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Берем строку (только цифры)
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        var out = ""

        // Вставляем точки после 2-й и 4-й цифры
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1 || i == 3) out += "."
        }

        // Правила перемещения курсора (чтобы курсор не прыгал, когда вставляется точка)
        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset + 1
                if (offset <= 8) return offset + 2
                return 10
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                if (offset <= 10) return offset - 2
                return 8
            }
        }

        return TransformedText(AnnotatedString(out), offsetTranslator)
    }
}