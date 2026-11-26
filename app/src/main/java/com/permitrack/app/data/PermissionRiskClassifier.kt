package com.permitrack.app.data

import android.Manifest
import com.permitrack.app.model.*

object PermissionRiskClassifier {
    private val critical = setOf(Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    private val high = setOf(Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.BLUETOOTH_CONNECT)
    private val medium = setOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_PHONE_STATE, Manifest.permission.POST_NOTIFICATIONS)
    fun classify(name: String, protection: String, appLabel: String): PermissionSnapshot {
        val risk = when { name in critical -> RiskLevel.CRITICAL; name in high -> RiskLevel.HIGH; name in medium || protection == "dangerous" -> RiskLevel.MEDIUM; else -> RiskLevel.LOW }
        val potentiallyExcessive = risk == RiskLevel.HIGH || risk == RiskLevel.CRITICAL
        return PermissionSnapshot(name, null, protection, risk, explanation(name), potentiallyExcessive)
    }
    private fun explanation(name: String) = when (name) { Manifest.permission.RECORD_AUDIO -> "Pode capturar áudio do microfone."; Manifest.permission.ACCESS_FINE_LOCATION -> "Pode obter localização precisa."; Manifest.permission.CAMERA -> "Pode usar a câmera."; Manifest.permission.READ_CONTACTS -> "Pode ler contatos."; else -> "Acesso declarado pelo aplicativo; revise o contexto de uso." }
}
