package androidx.compose.material.icons.automirrored.filled

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private fun buildAutoMirroredIcon(name: String, pathData: androidx.compose.ui.graphics.vector.PathBuilder.() -> Unit): ImageVector {
  return ImageVector.Builder(
    name = name,
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
    autoMirror = true
  ).apply {
    path(fill = SolidColor(Color.Black), pathBuilder = pathData)
  }.build()
}

val Icons.AutoMirrored.Filled.ArrowBackIos: ImageVector by lazy {
  buildAutoMirroredIcon("AutoMirrored.Filled.ArrowBackIos") {
    moveTo(17.51f, 3.87f); lineTo(15.73f, 2.1f); lineTo(5.84f, 12f)
    lineTo(15.74f, 21.9f); lineTo(17.51f, 20.13f); lineTo(9.38f, 12f); lineTo(17.51f, 3.87f); close()
  }
}

val Icons.AutoMirrored.Filled.Logout: ImageVector by lazy {
  buildAutoMirroredIcon("AutoMirrored.Filled.Logout") {
    moveTo(17f, 7f); lineTo(15.59f, 8.41f); lineTo(18.17f, 11f); horizontalLineTo(8f)
    verticalLineTo(13f); horizontalLineTo(18.17f); lineTo(15.59f, 15.58f); lineTo(17f, 17f)
    lineTo(22f, 12f); lineTo(17f, 7f); close()
    moveTo(4f, 5f); horizontalLineTo(12f); verticalLineTo(3f); horizontalLineTo(4f)
    curveTo(2.9f, 3f, 2f, 3.9f, 2f, 5f); verticalLineTo(19f); curveTo(2f, 20.1f, 2.9f, 21f, 4f, 21f)
    horizontalLineTo(12f); verticalLineTo(19f); horizontalLineTo(4f); verticalLineTo(5f); close()
  }
}

val Icons.AutoMirrored.Filled.ReceiptLong: ImageVector by lazy {
  buildAutoMirroredIcon("AutoMirrored.Filled.ReceiptLong") {
    moveTo(19.5f, 3.5f); lineTo(18f, 2f); lineTo(16.5f, 3.5f); lineTo(15f, 2f)
    lineTo(13.5f, 3.5f); lineTo(12f, 2f); lineTo(10.5f, 3.5f); lineTo(9f, 2f)
    lineTo(7.5f, 3.5f); lineTo(6f, 2f); verticalLineTo(14f); horizontalLineTo(3f)
    verticalLineTo(19f); curveTo(3f, 20.66f, 4.34f, 22f, 6f, 22f); horizontalLineTo(18f)
    curveTo(19.66f, 22f, 21f, 20.66f, 21f, 19f); verticalLineTo(2f); lineTo(19.5f, 3.5f); close()
    moveTo(15f, 20f); horizontalLineTo(6f); curveTo(5.45f, 20f, 5f, 19.55f, 5f, 19f)
    verticalLineTo(16f); horizontalLineTo(15f); verticalLineTo(20f); close()
    moveTo(19f, 19f); curveTo(19f, 19.55f, 18.55f, 20f, 18f, 20f)
    curveTo(17.45f, 20f, 17f, 19.55f, 17f, 19f); verticalLineTo(14f); horizontalLineTo(8f)
    verticalLineTo(5f); horizontalLineTo(19f); verticalLineTo(19f); close()
  }
}

val Icons.AutoMirrored.Filled.TrendingUp: ImageVector by lazy {
  buildAutoMirroredIcon("AutoMirrored.Filled.TrendingUp") {
    moveTo(16f, 6f); lineTo(18.29f, 8.29f); lineTo(13.41f, 13.17f); lineTo(9.41f, 9.17f)
    lineTo(2f, 16.59f); lineTo(3.41f, 18f); lineTo(9.41f, 12f); lineTo(13.41f, 16f)
    lineTo(19.71f, 9.71f); lineTo(22f, 12f); verticalLineTo(6f); horizontalLineTo(16f); close()
  }
}
