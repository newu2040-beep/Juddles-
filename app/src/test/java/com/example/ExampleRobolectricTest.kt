package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Juddles", appName)
  }

  @Test
  fun `verify v3_0 themes fonts and notification channel`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    assertEquals(12, com.example.ui.theme.JuddlesAvailableThemes.size)
    assertEquals(8, com.example.ui.theme.JuddlesAvailableFonts.size)
    val typo = com.example.ui.theme.createJuddlesTypography("OUTFIT", isCompact = true)
    assertEquals(13.02f, typo.bodyMedium.fontSize.value, 0.05f)
    com.example.util.JuddlesNotificationManager.ensureNotificationChannel(context)
    assertEquals(50, com.example.data.model.JuddlesCurrencyCatalog.currencies.size)
    val npr = com.example.data.model.JuddlesCurrencyCatalog.findByCode("NPR")
    assertEquals("Nepal", npr?.country)
    assertEquals("रू", npr?.symbol)
    assertEquals(
      "रू4,820.75",
      com.example.data.model.FinancialCalculator.formatCurrencyMinor(482075L, "NPR")
    )
  }
}
