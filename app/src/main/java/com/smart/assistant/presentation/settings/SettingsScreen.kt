package com.smart.assistant.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smart.assistant.data.local.entity.ShopSettingsEntity

@Composable
fun SettingsScreen(
    settings: ShopSettingsEntity = ShopSettingsEntity(),
    onSave: (ShopSettingsEntity) -> Unit = {}
) {
    var shopName by remember { mutableStateOf(settings.shopName) }
    var phone by remember { mutableStateOf(settings.phone) }
    var whatsapp by remember { mutableStateOf(settings.whatsapp) }
    var template by remember { mutableStateOf(settings.reminderMessageTemplate) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = shopName,
            onValueChange = { shopName = it },
            label = { Text("اسم المحل") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("رقم الهاتف") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = whatsapp,
            onValueChange = { whatsapp = it },
            label = { Text("رقم الواتساب") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = template,
            onValueChange = { template = it },
            label = { Text("قالب رسالة التذكير") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onSave(
                    ShopSettingsEntity(
                        id = settings.id,
                        shopName = shopName,
                        phone = phone,
                        whatsapp = whatsapp,
                        reminderMessageTemplate = template
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("حفظ التغييرات")
        }
    }
}
