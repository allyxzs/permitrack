package com.permitrack.permittrack.model

sealed class RiskLevel {
    object LOW : RiskLevel()
    object MEDIUM : RiskLevel()
    object HIGH : RiskLevel()
    object CRITICAL : RiskLevel()
}