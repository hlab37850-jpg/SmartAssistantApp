package com.smart.assistant.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smart.assistant.data.local.entity.ShopSettingsEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: ShopSettingsEntity,
    onSaveSettings: (ShopSettingsEntity) -> Unit
) {
    var shopName by remember { mutableStateOf(settings.shopName) }
    var phone by remember { mutableStateOf(settings.phone) }
    var whatsapp by remember { mutableStateOf(settings.whatsapp) }
    var reminderTemplate by remember { mutableStateOf(settings.reminderMessageTemplate) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("إعدادات المحل والتذكيرات") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = shopName,
                onValueChange = { shopName = it },
                label = { Text("اسم المحل") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("رقم الهاتف") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = whatsapp,
                onValueChange = { whatsapp = it },
                label = { Text("رقم الواتساب") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = reminderTemplate,
                onValueChange = { reminderTemplate = it },
                label = { Text("قالب رسالة التذكير") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            Button(
                onClick = {
                    onSaveSettings(
                        settings.copy(
                            shopName = shopName,
                            phone = phone,
                            whatsapp = whatsapp,
                            reminderMessageTemplate = reminderTemplate
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("حفظ الإعدادات")
            }
        }
    }
}
