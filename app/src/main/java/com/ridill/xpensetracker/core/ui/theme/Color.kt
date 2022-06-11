package com.ridill.xpensetracker.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val MoneyGreen10 = Color(0xFF062D1A)
val MoneyGreen20 = Color(0xFF0B5B33)
val MoneyGreen30 = Color(0xFF11884D)
val MoneyGreen40 = Color(0xFF16B666)
val MoneyGreen80 = Color(0xFFA4F4CC)
val MoneyGreen90 = Color(0xFFD2F9E6)

val DarkGreen10 = Color(0xFF052E1A)
val DarkGreen20 = Color(0xFF0B5B33)
val DarkGreen30 = Color(0xFF10894D)
val DarkGreen40 = Color(0xFF15B766)
val DarkGreen80 = Color(0xFFA4F4CC)
val DarkGreen90 = Color(0xFFD1FAE6)

val Violet10 = Color(0xFF2D061A)
val Violet20 = Color(0xFF5B0B33)
val Violet30 = Color(0xFF88114D)
val Violet40 = Color(0xFFB61666)
val Violet80 = Color(0xFFF4A4CC)
val Violet90 = Color(0xFFF9D2E6)

val Red10 = Color(0xFF410001)
val Red20 = Color(0xFF680003)
val Red30 = Color(0xFF930006)
val Red40 = Color(0xFFBA1B1B)
val Red80 = Color(0xFFFFB4A9)
val Red90 = Color(0xFFFFDAD4)

val Grey10 = Color(0xFF191C1D)
val Grey20 = Color(0xFF2D3132)
val Grey90 = Color(0xFFE0E3E3)
val Grey95 = Color(0xFFEFF1F1)
val Grey99 = Color(0xFFFBFDFD)

val GreenGrey20 = Color(0xFF21452f)
val GreenGrey30 = Color(0xFF316847)
val GreenGrey50 = Color(0xFF52ad76)
val GreenGrey60 = Color(0xFF74be92)
val GreenGrey80 = Color(0xFFbadec8)
val GreenGrey90 = Color(0xFFdcefe4)

val LightPrimary = Color(0xFF006D3B)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFF8DF9B0)
val LightOnPrimaryContainer = Color(0xFF00210E)
val LightSecondary = Color(0xFF4F6353)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFD2E8D4)
val LightOnSecondaryContainer = Color(0xFF0D1F13)
val LightTertiary = Color(0xFF3A646F)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFFBEEAF6)
val LightOnTertiaryContainer = Color(0xFF001F26)
val LightError = Color(0xFFBA1A1A)
val LightErrorContainer = Color(0xFFFFDAD6)
val LightOnError = Color(0xFFFFFFFF)
val LightOnErrorContainer = Color(0xFF410002)
val LightBackground = Color(0xFFFBFDF8)
val LightOnBackground = Color(0xFF191C19)
val LightSurface = Color(0xFFFBFDF8)
val LightOnSurface = Color(0xFF191C19)
val LightSurfaceVariant = Color(0xFFDDE5DB)
val LightOnSurfaceVariant = Color(0xFF414942)
val LightOutline = Color(0xFF717971)
val LightInverseOnSurface = Color(0xFFF0F1EC)
val LightInverseSurface = Color(0xFF2E312E)
val LightInversePrimary = Color(0xFF71DC96)
val LightShadow = Color(0xFF000000)

val DarkPrimary = Color(0xFF71DC96)
val DarkOnPrimary = Color(0xFF00391C)
val DarkPrimaryContainer = Color(0xFF00522B)
val DarkOnPrimaryContainer = Color(0xFF8DF9B0)
val DarkSecondary = Color(0xFFB6CCB8)
val DarkOnSecondary = Color(0xFF223527)
val DarkSecondaryContainer = Color(0xFF384B3C)
val DarkOnSecondaryContainer = Color(0xFFD2E8D4)
val DarkTertiary = Color(0xFFA2CEDA)
val DarkOnTertiary = Color(0xFF023640)
val DarkTertiaryContainer = Color(0xFF214C57)
val DarkOnTertiaryContainer = Color(0xFFBEEAF6)
val DarkError = Color(0xFFFFB4AB)
val DarkErrorContainer = Color(0xFF93000A)
val DarkOnError = Color(0xFF690005)
val DarkOnErrorContainer = Color(0xFFFFDAD6)
val DarkBackground = Color(0xFF191C19)
val DarkOnBackground = Color(0xFFE1E3DE)
val DarkSurface = Color(0xFF191C19)
val DarkOnSurface = Color(0xFFE1E3DE)
val DarkSurfaceVariant = Color(0xFF414942)
val DarkOnSurfaceVariant = Color(0xFFC1C9BF)
val DarkOutline = Color(0xFF8B938A)
val DarkInverseOnSurface = Color(0xFF191C19)
val DarkInverseSurface = Color(0xFFE1E3DE)
val DarkInversePrimary = Color(0xFF006D3B)
val DarkShadow = Color(0xFF000000)

val seed = Color(0xFF118C4F)

@Composable
fun xtGradient(): Brush = Brush.horizontalGradient(
    colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.inversePrimary
    )
)

object ContentAlpha {
    const val PERCENT_08 = 0.08f
    const val PERCENT_16 = 0.16f
    const val PERCENT_32 = 0.32f
    const val PERCENT_40 = 0.40f
}