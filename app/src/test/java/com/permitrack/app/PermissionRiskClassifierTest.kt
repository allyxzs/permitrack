package com.permitrack.app

import android.Manifest
import com.permitrack.app.data.PermissionRiskClassifier
import com.permitrack.app.model.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PermissionRiskClassifierTest {
    @Test fun microphoneIsCritical() { assertEquals(RiskLevel.CRITICAL, PermissionRiskClassifier.classify(Manifest.permission.RECORD_AUDIO, "dangerous", "Recorder").risk) }
    @Test fun cameraIsHighAndPotentiallyExcessive() { val p = PermissionRiskClassifier.classify(Manifest.permission.CAMERA, "dangerous", "Notes"); assertEquals(RiskLevel.HIGH, p.risk); assertTrue(p.potentiallyExcessive) }
    @Test fun normalPermissionIsLow() { assertEquals(RiskLevel.LOW, PermissionRiskClassifier.classify("android.permission.INTERNET", "normal", "Browser").risk) }
}
