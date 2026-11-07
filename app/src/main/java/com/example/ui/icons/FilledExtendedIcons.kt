package androidx.compose.material.icons.filled

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private fun buildIcon(name: String, pathData: androidx.compose.ui.graphics.vector.PathBuilder.() -> Unit): ImageVector {
  return ImageVector.Builder(
    name = name,
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
  ).apply {
    path(fill = SolidColor(Color.Black), pathBuilder = pathData)
  }.build()
}

val Icons.Filled.AccountBalanceWallet: ImageVector by lazy {
  buildIcon("Filled.AccountBalanceWallet") {
    moveTo(21f, 18f); verticalLineTo(19f); curveTo(21f, 20.1f, 20.1f, 21f, 19f, 21f)
    horizontalLineTo(5f); curveTo(3.89f, 21f, 3f, 20.1f, 3f, 19f); verticalLineTo(5f)
    curveTo(3f, 3.9f, 3.89f, 3f, 5f, 3f); horizontalLineTo(19f); curveTo(20.1f, 3f, 21f, 3.9f, 21f, 5f)
    verticalLineTo(6f); horizontalLineTo(12f); curveTo(10.89f, 6f, 10f, 6.9f, 10f, 8f); verticalLineTo(16f)
    curveTo(10f, 17.1f, 10.89f, 18f, 12f, 18f); horizontalLineTo(21f); close()
    moveTo(12f, 16f); horizontalLineTo(22f); verticalLineTo(8f); horizontalLineTo(12f); verticalLineTo(16f); close()
    moveTo(16f, 13.5f); curveTo(15.17f, 13.5f, 14.5f, 12.83f, 14.5f, 12f)
    curveTo(14.5f, 11.17f, 15.17f, 10.5f, 16f, 10.5f); curveTo(16.83f, 10.5f, 17.5f, 11.17f, 17.5f, 12f)
    curveTo(17.5f, 12.83f, 16.83f, 13.5f, 16f, 13.5f); close()
  }
}

val Icons.Filled.AddAlert: ImageVector by lazy {
  buildIcon("Filled.AddAlert") {
    moveTo(12f, 22f); curveTo(13.1f, 22f, 14f, 21.1f, 14f, 20f); horizontalLineTo(10f)
    curveTo(10f, 21.1f, 10.9f, 22f, 12f, 22f); close()
    moveTo(18f, 16f); verticalLineTo(11f); curveTo(18f, 7.93f, 16.37f, 5.36f, 13.5f, 4.68f)
    verticalLineTo(4f); curveTo(13.5f, 3.17f, 12.83f, 2.5f, 12f, 2.5f)
    curveTo(11.17f, 2.5f, 10.5f, 3.17f, 10.5f, 4f); verticalLineTo(4.68f)
    curveTo(7.64f, 5.36f, 6f, 7.92f, 6f, 11f); verticalLineTo(16f); lineTo(4f, 18f)
    verticalLineTo(19f); horizontalLineTo(20f); verticalLineTo(18f); lineTo(18f, 16f); close()
    moveTo(13f, 14f); horizontalLineTo(11f); verticalLineTo(11f); horizontalLineTo(8f)
    verticalLineTo(9f); horizontalLineTo(11f); verticalLineTo(6f); horizontalLineTo(13f)
    verticalLineTo(9f); horizontalLineTo(16f); verticalLineTo(11f); horizontalLineTo(13f)
    verticalLineTo(14f); close()
  }
}

val Icons.Filled.ArrowDownward: ImageVector by lazy {
  buildIcon("Filled.ArrowDownward") {
    moveTo(20f, 12f); lineTo(18.59f, 10.59f); lineTo(13f, 16.17f); verticalLineTo(4f)
    horizontalLineTo(11f); verticalLineTo(16.17f); lineTo(5.42f, 10.58f); lineTo(4f, 12f)
    lineTo(12f, 20f); lineTo(20f, 12f); close()
  }
}

val Icons.Filled.ArrowUpward: ImageVector by lazy {
  buildIcon("Filled.ArrowUpward") {
    moveTo(4f, 12f); lineTo(5.41f, 13.41f); lineTo(11f, 7.83f); verticalLineTo(20f)
    horizontalLineTo(13f); verticalLineTo(7.83f); lineTo(18.58f, 13.42f); lineTo(20f, 12f)
    lineTo(12f, 4f); lineTo(4f, 12f); close()
  }
}

val Icons.Filled.BarChart: ImageVector by lazy {
  buildIcon("Filled.BarChart") {
    moveTo(4f, 9f); horizontalLineTo(8f); verticalLineTo(20f); horizontalLineTo(4f); close()
    moveTo(10f, 4f); horizontalLineTo(14f); verticalLineTo(20f); horizontalLineTo(10f); close()
    moveTo(16f, 13f); horizontalLineTo(20f); verticalLineTo(20f); horizontalLineTo(16f); close()
  }
}

val Icons.Filled.BeachAccess: ImageVector by lazy {
  buildIcon("Filled.BeachAccess") {
    moveTo(13.13f, 14.56f); lineTo(14.56f, 13.13f); lineTo(21f, 19.57f); lineTo(19.57f, 21f); close()
    moveTo(17.42f, 8.83f); curveTo(14.5f, 5.9f, 9.9f, 5.6f, 6.6f, 8.1f)
    lineTo(15.9f, 17.4f); curveTo(18.4f, 14.1f, 18.1f, 9.5f, 17.42f, 8.83f); close()
  }
}

val Icons.Filled.CalendarMonth: ImageVector by lazy {
  buildIcon("Filled.CalendarMonth") {
    moveTo(19f, 4f); horizontalLineTo(18f); verticalLineTo(2f); horizontalLineTo(16f)
    verticalLineTo(4f); horizontalLineTo(8f); verticalLineTo(2f); horizontalLineTo(6f)
    verticalLineTo(4f); horizontalLineTo(5f); curveTo(3.89f, 4f, 3f, 4.9f, 3f, 6f)
    verticalLineTo(20f); curveTo(3f, 21.1f, 3.89f, 22f, 5f, 22f); horizontalLineTo(19f)
    curveTo(20.1f, 22f, 21f, 21.1f, 21f, 20f); verticalLineTo(6f); curveTo(21f, 4.9f, 20.1f, 4f, 19f, 4f); close()
    moveTo(19f, 20f); horizontalLineTo(5f); verticalLineTo(10f); horizontalLineTo(19f); verticalLineTo(20f); close()
  }
}

val Icons.Filled.CardGiftcard: ImageVector by lazy {
  buildIcon("Filled.CardGiftcard") {
    moveTo(20f, 6f); horizontalLineTo(17.82f); curveTo(18.2f, 4.8f, 17.3f, 3f, 15.5f, 3f)
    curveTo(14.2f, 3f, 13.2f, 3.8f, 12f, 5.5f); curveTo(10.8f, 3.8f, 9.8f, 3f, 8.5f, 3f)
    curveTo(6.7f, 3f, 5.8f, 4.8f, 6.18f, 6f); horizontalLineTo(4f); curveTo(2.9f, 6f, 2f, 6.9f, 2f, 8f)
    verticalLineTo(19f); curveTo(2f, 20.1f, 2.9f, 21f, 4f, 21f); horizontalLineTo(20f)
    curveTo(21.1f, 21f, 22f, 20.1f, 22f, 19f); verticalLineTo(8f); curveTo(22f, 6.9f, 21.1f, 6f, 20f, 6f); close()
  }
}

val Icons.Filled.Code: ImageVector by lazy {
  buildIcon("Filled.Code") {
    moveTo(9.4f, 16.6f); lineTo(4.8f, 12f); lineTo(9.4f, 7.4f); lineTo(8f, 6f); lineTo(2f, 12f); lineTo(8f, 18f); close()
    moveTo(14.6f, 16.6f); lineTo(19.2f, 12f); lineTo(14.6f, 7.4f); lineTo(16f, 6f); lineTo(22f, 12f); lineTo(16f, 18f); close()
  }
}

val Icons.Filled.Compress: ImageVector by lazy {
  buildIcon("Filled.Compress") {
    moveTo(8f, 19f); horizontalLineTo(11f); verticalLineTo(22f); horizontalLineTo(13f)
    verticalLineTo(19f); horizontalLineTo(16f); lineTo(12f, 15f); lineTo(8f, 19f); close()
    moveTo(16f, 4f); horizontalLineTo(13f); verticalLineTo(1f); horizontalLineTo(11f)
    verticalLineTo(4f); horizontalLineTo(8f); lineTo(12f, 8f); lineTo(16f, 4f); close()
    moveTo(4f, 9f); verticalLineTo(11f); horizontalLineTo(20f); verticalLineTo(9f); horizontalLineTo(4f); close()
    moveTo(4f, 12f); verticalLineTo(14f); horizontalLineTo(20f); verticalLineTo(12f); horizontalLineTo(4f); close()
  }
}

val Icons.Filled.Computer: ImageVector by lazy {
  buildIcon("Filled.Computer") {
    moveTo(20f, 18f); curveTo(21.1f, 18f, 22f, 17.1f, 22f, 16f); verticalLineTo(6f)
    curveTo(22f, 4.9f, 21.1f, 4f, 20f, 4f); horizontalLineTo(4f); curveTo(2.9f, 4f, 2f, 4.9f, 2f, 6f)
    verticalLineTo(16f); curveTo(2f, 17.1f, 2.9f, 18f, 4f, 18f); horizontalLineTo(0f)
    verticalLineTo(20f); horizontalLineTo(24f); verticalLineTo(18f); horizontalLineTo(20f); close()
    moveTo(4f, 6f); horizontalLineTo(20f); verticalLineTo(16f); horizontalLineTo(4f); verticalLineTo(6f); close()
  }
}

val Icons.Filled.DeleteOutline: ImageVector by lazy {
  buildIcon("Filled.DeleteOutline") {
    moveTo(6f, 19f); curveTo(6f, 20.1f, 6.9f, 21f, 8f, 21f); horizontalLineTo(16f)
    curveTo(17.1f, 21f, 18f, 20.1f, 18f, 19f); verticalLineTo(7f); horizontalLineTo(6f); verticalLineTo(19f); close()
    moveTo(8f, 9f); horizontalLineTo(16f); verticalLineTo(19f); horizontalLineTo(8f); verticalLineTo(9f); close()
    moveTo(15.5f, 4f); lineTo(14.5f, 3f); horizontalLineTo(9.5f); lineTo(8.5f, 4f)
    horizontalLineTo(5f); verticalLineTo(6f); horizontalLineTo(19f); verticalLineTo(4f); horizontalLineTo(15.5f); close()
  }
}

val Icons.Filled.Description: ImageVector by lazy {
  buildIcon("Filled.Description") {
    moveTo(14f, 2f); horizontalLineTo(6f); curveTo(4.9f, 2f, 4f, 2.9f, 4f, 4f)
    verticalLineTo(20f); curveTo(4f, 21.1f, 4.89f, 22f, 5.99f, 22f); horizontalLineTo(18f)
    curveTo(19.1f, 22f, 20f, 21.1f, 20f, 20f); verticalLineTo(8f); lineTo(14f, 2f); close()
    moveTo(16f, 18f); horizontalLineTo(8f); verticalLineTo(16f); horizontalLineTo(16f); verticalLineTo(18f); close()
    moveTo(16f, 14f); horizontalLineTo(8f); verticalLineTo(12f); horizontalLineTo(16f); verticalLineTo(14f); close()
    moveTo(13f, 9f); verticalLineTo(3.5f); lineTo(18.5f, 9f); horizontalLineTo(13f); close()
  }
}

val Icons.Filled.DirectionsCar: ImageVector by lazy {
  buildIcon("Filled.DirectionsCar") {
    moveTo(18.92f, 6.01f); curveTo(18.72f, 5.42f, 18.16f, 5f, 17.5f, 5f); horizontalLineTo(6.5f)
    curveTo(5.84f, 5f, 5.29f, 5.42f, 5.08f, 6.01f); lineTo(3f, 12f); verticalLineTo(20f)
    curveTo(3f, 20.55f, 3.45f, 21f, 4f, 21f); horizontalLineTo(5f); curveTo(5.55f, 21f, 6f, 20.55f, 6f, 20f)
    verticalLineTo(19f); horizontalLineTo(18f); verticalLineTo(20f); curveTo(18f, 20.55f, 18.45f, 21f, 19f, 21f)
    horizontalLineTo(20f); curveTo(20.55f, 21f, 21f, 20.55f, 21f, 20f); verticalLineTo(12f); lineTo(18.92f, 6.01f); close()
  }
}

val Icons.Filled.Download: ImageVector by lazy {
  buildIcon("Filled.Download") {
    moveTo(5f, 20f); horizontalLineTo(19f); verticalLineTo(18f); horizontalLineTo(5f); verticalLineTo(20f); close()
    moveTo(19f, 9f); horizontalLineTo(15f); verticalLineTo(3f); horizontalLineTo(9f)
    verticalLineTo(9f); horizontalLineTo(5f); lineTo(12f, 16f); lineTo(19f, 9f); close()
  }
}

val Icons.Filled.EmojiEvents: ImageVector by lazy {
  buildIcon("Filled.EmojiEvents") {
    moveTo(19f, 5f); horizontalLineTo(17f); verticalLineTo(3f); horizontalLineTo(7f)
    verticalLineTo(5f); horizontalLineTo(5f); curveTo(3.9f, 5f, 3f, 5.9f, 3f, 7f); verticalLineTo(8f)
    curveTo(3f, 10.55f, 4.92f, 12.63f, 7.39f, 12.94f); curveTo(8.02f, 14.44f, 9.37f, 15.57f, 11f, 15.9f)
    verticalLineTo(19f); horizontalLineTo(7f); verticalLineTo(21f); horizontalLineTo(17f)
    verticalLineTo(19f); horizontalLineTo(13f); verticalLineTo(15.9f)
    curveTo(14.63f, 15.57f, 15.98f, 14.44f, 16.61f, 12.94f); curveTo(19.08f, 12.63f, 21f, 10.55f, 21f, 8f)
    verticalLineTo(7f); curveTo(21f, 5.9f, 20.1f, 5f, 19f, 5f); close()
  }
}

val Icons.Filled.EvStation: ImageVector by lazy {
  buildIcon("Filled.EvStation") {
    moveTo(19.77f, 7.23f); lineTo(16.05f, 3.51f); lineTo(15f, 4.56f); lineTo(17.11f, 6.67f)
    curveTo(16.17f, 7.03f, 15.5f, 7.93f, 15.5f, 9f); curveTo(15.5f, 10.38f, 16.62f, 11.5f, 18f, 11.5f)
    verticalLineTo(18.5f); curveTo(18f, 19.05f, 17.55f, 19.5f, 17f, 19.5f)
    curveTo(16.45f, 19.5f, 16f, 19.05f, 16f, 18.5f); verticalLineTo(14f); curveTo(16f, 12.9f, 15.1f, 12f, 14f, 12f)
    horizontalLineTo(13f); verticalLineTo(5f); curveTo(13f, 3.9f, 12.1f, 3f, 11f, 3f); horizontalLineTo(6f)
    curveTo(4.9f, 3f, 4f, 3.9f, 4f, 5f); verticalLineTo(21f); horizontalLineTo(14f); verticalLineTo(13.5f); close()
  }
}

val Icons.Filled.Fastfood: ImageVector by lazy {
  buildIcon("Filled.Fastfood") {
    moveTo(18.06f, 22.99f); horizontalLineTo(19.72f); curveTo(20.56f, 22.99f, 21.17f, 22.34f, 21.35f, 21.52f)
    lineTo(23f, 5.05f); horizontalLineTo(18f); verticalLineTo(1f); horizontalLineTo(16.03f)
    verticalLineTo(5.05f); horizontalLineTo(11.06f); lineTo(11.36f, 7.39f); close()
    moveTo(1f, 15f); horizontalLineTo(15f); verticalLineTo(17f); horizontalLineTo(1f); close()
    moveTo(1f, 19f); horizontalLineTo(15f); verticalLineTo(21f); horizontalLineTo(1f); close()
    moveTo(8f, 9f); curveTo(4f, 9f, 1f, 11f, 1f, 13f); horizontalLineTo(15f); curveTo(15f, 11f, 12f, 9f, 8f, 9f); close()
  }
}

val Icons.Filled.FitnessCenter: ImageVector by lazy {
  buildIcon("Filled.FitnessCenter") {
    moveTo(20.57f, 14.86f); lineTo(22f, 13.43f); lineTo(20.57f, 12f); lineTo(17f, 15.57f)
    lineTo(8.43f, 7f); lineTo(12f, 3.43f); lineTo(10.57f, 2f); lineTo(9.14f, 3.43f)
    lineTo(7.71f, 2f); lineTo(5.57f, 4.14f); lineTo(4.14f, 2.71f); lineTo(2.71f, 4.14f)
    lineTo(4.14f, 5.57f); lineTo(2f, 7.71f); lineTo(3.43f, 9.14f); lineTo(2f, 10.57f)
    lineTo(3.43f, 12f); lineTo(7f, 8.43f); lineTo(15.57f, 17f); lineTo(12f, 20.57f)
    lineTo(13.43f, 22f); lineTo(14.86f, 20.57f); lineTo(16.29f, 22f); lineTo(18.43f, 19.86f)
    lineTo(19.86f, 21.29f); lineTo(21.29f, 19.86f); lineTo(19.86f, 18.43f); lineTo(22f, 16.29f); close()
  }
}

val Icons.Filled.FlightTakeoff: ImageVector by lazy {
  buildIcon("Filled.FlightTakeoff") {
    moveTo(2.5f, 19f); horizontalLineTo(21.5f); verticalLineTo(21f); horizontalLineTo(2.5f); verticalLineTo(19f); close()
    moveTo(22.07f, 9.64f); curveTo(21.86f, 8.84f, 21.03f, 8.36f, 20.23f, 8.58f)
    lineTo(14.92f, 10f); lineTo(8.02f, 3.57f); lineTo(6.09f, 4.08f); lineTo(10.23f, 11.25f)
    lineTo(5.26f, 12.58f); lineTo(3.29f, 11.04f); lineTo(1.84f, 11.43f); lineTo(4.43f, 15.92f)
    lineTo(21.0f, 11.48f); curveTo(21.81f, 11.26f, 22.28f, 10.44f, 22.07f, 9.64f); close()
  }
}

val Icons.Filled.FolderOpen: ImageVector by lazy {
  buildIcon("Filled.FolderOpen") {
    moveTo(20f, 6f); horizontalLineTo(12f); lineTo(10f, 4f); horizontalLineTo(4f)
    curveTo(2.9f, 4f, 2f, 4.9f, 2f, 6f); verticalLineTo(18f); curveTo(2f, 19.1f, 2.9f, 20f, 4f, 20f)
    horizontalLineTo(20f); curveTo(21.1f, 20f, 22f, 19.1f, 22f, 18f); verticalLineTo(8f)
    curveTo(22f, 6.9f, 21.1f, 6f, 20f, 6f); close()
    moveTo(20f, 18f); horizontalLineTo(4f); verticalLineTo(8f); horizontalLineTo(20f); verticalLineTo(18f); close()
  }
}

val Icons.Filled.FontDownload: ImageVector by lazy {
  buildIcon("Filled.FontDownload") {
    moveTo(9.93f, 13.5f); horizontalLineTo(14.07f); lineTo(12f, 7.98f); lineTo(9.93f, 13.5f); close()
    moveTo(20f, 2f); horizontalLineTo(4f); curveTo(2.9f, 2f, 2f, 2.9f, 2f, 4f); verticalLineTo(20f)
    curveTo(2f, 21.1f, 2.9f, 22f, 4f, 22f); horizontalLineTo(20f); curveTo(21.1f, 22f, 22f, 21.1f, 22f, 20f)
    verticalLineTo(4f); curveTo(22f, 2.9f, 21.1f, 2f, 20f, 2f); close()
    moveTo(15.95f, 18.5f); lineTo(14.81f, 15.5f); horizontalLineTo(9.17f); lineTo(8.05f, 18.5f)
    horizontalLineTo(5.96f); lineTo(11.07f, 5.5f); horizontalLineTo(12.93f); lineTo(18.04f, 18.5f); horizontalLineTo(15.95f); close()
  }
}

val Icons.Filled.GridView: ImageVector by lazy {
  buildIcon("Filled.GridView") {
    moveTo(3f, 3f); horizontalLineTo(11f); verticalLineTo(11f); horizontalLineTo(3f); close()
    moveTo(3f, 13f); horizontalLineTo(11f); verticalLineTo(21f); horizontalLineTo(3f); close()
    moveTo(13f, 3f); horizontalLineTo(21f); verticalLineTo(11f); horizontalLineTo(13f); close()
    moveTo(13f, 13f); horizontalLineTo(21f); verticalLineTo(21f); horizontalLineTo(13f); close()
  }
}

val Icons.Filled.HealthAndSafety: ImageVector by lazy {
  buildIcon("Filled.HealthAndSafety") {
    moveTo(10.5f, 13f); horizontalLineTo(8f); verticalLineTo(10f); horizontalLineTo(10.5f)
    verticalLineTo(7.5f); horizontalLineTo(13.5f); verticalLineTo(10f); horizontalLineTo(16f)
    verticalLineTo(13f); horizontalLineTo(13.5f); verticalLineTo(15.5f); horizontalLineTo(10.5f); verticalLineTo(13f); close()
    moveTo(12f, 2f); lineTo(4f, 5f); verticalLineTo(11.09f); curveTo(4f, 16.14f, 7.41f, 20.85f, 12f, 22f)
    curveTo(16.59f, 20.85f, 20f, 16.14f, 20f, 11.09f); verticalLineTo(5f); lineTo(12f, 2f); close()
  }
}

val Icons.Filled.LocalGroceryStore: ImageVector by lazy {
  buildIcon("Filled.LocalGroceryStore") {
    moveTo(7f, 18f); curveTo(5.9f, 18f, 5f, 18.9f, 5f, 20f); curveTo(5f, 21.1f, 5.9f, 22f, 7f, 22f)
    curveTo(8.1f, 22f, 9f, 21.1f, 9f, 20f); curveTo(9f, 18.9f, 8.1f, 18f, 7f, 18f); close()
    moveTo(1f, 2f); verticalLineTo(4f); horizontalLineTo(3f); lineTo(6.6f, 11.59f); lineTo(5.25f, 14.04f)
    curveTo(5.09f, 14.32f, 5f, 14.65f, 5f, 15f); curveTo(5f, 16.1f, 5.9f, 17f, 7f, 17f); horizontalLineTo(19f)
    verticalLineTo(15f); horizontalLineTo(7.42f); lineTo(8.1f, 13f); horizontalLineTo(15.55f)
    curveTo(16.3f, 13f, 16.96f, 12.59f, 17.3f, 11.97f); lineTo(20.88f, 5.48f); horizontalLineTo(5.21f); lineTo(4.27f, 2f); close()
  }
}

val Icons.Filled.Movie: ImageVector by lazy {
  buildIcon("Filled.Movie") {
    moveTo(18f, 4f); lineTo(20f, 8f); horizontalLineTo(17f); lineTo(15f, 4f); horizontalLineTo(13f)
    lineTo(15f, 8f); horizontalLineTo(12f); lineTo(10f, 4f); horizontalLineTo(8f); lineTo(10f, 8f)
    horizontalLineTo(7f); lineTo(5f, 4f); horizontalLineTo(4f); curveTo(2.9f, 4f, 2f, 4.9f, 2f, 6f)
    verticalLineTo(18f); curveTo(2f, 19.1f, 2.9f, 20f, 4f, 20f); horizontalLineTo(20f)
    curveTo(21.1f, 20f, 22f, 19.1f, 22f, 18f); verticalLineTo(4f); horizontalLineTo(18f); close()
  }
}

val Icons.Filled.NotificationsActive: ImageVector by lazy {
  buildIcon("Filled.NotificationsActive") {
    moveTo(12f, 22f); curveTo(13.1f, 22f, 14f, 21.1f, 14f, 20f); horizontalLineTo(10f)
    curveTo(10f, 21.1f, 10.89f, 22f, 12f, 22f); close()
    moveTo(18f, 16f); verticalLineTo(11f); curveTo(18f, 7.93f, 16.36f, 5.36f, 13.5f, 4.68f)
    verticalLineTo(4f); curveTo(13.5f, 3.17f, 12.83f, 2.5f, 12f, 2.5f)
    curveTo(11.17f, 2.5f, 10.5f, 3.17f, 10.5f, 4f); verticalLineTo(4.68f)
    curveTo(7.63f, 5.36f, 6f, 7.92f, 6f, 11f); verticalLineTo(16f); lineTo(4f, 18f)
    verticalLineTo(19f); horizontalLineTo(20f); verticalLineTo(18f); lineTo(18f, 16f); close()
  }
}

val Icons.Filled.NotificationsNone: ImageVector by lazy {
  buildIcon("Filled.NotificationsNone") {
    moveTo(12f, 22f); curveTo(13.1f, 22f, 14f, 21.1f, 14f, 20f); horizontalLineTo(10f)
    curveTo(10f, 21.1f, 10.9f, 22f, 12f, 22f); close()
    moveTo(18f, 16f); verticalLineTo(11f); curveTo(18f, 7.93f, 16.37f, 5.36f, 13.5f, 4.68f)
    verticalLineTo(4f); curveTo(13.5f, 3.17f, 12.83f, 2.5f, 12f, 2.5f)
    curveTo(11.17f, 2.5f, 10.5f, 3.17f, 10.5f, 4f); verticalLineTo(4.68f)
    curveTo(7.64f, 5.36f, 6f, 7.92f, 6f, 11f); verticalLineTo(16f); lineTo(4f, 18f)
    verticalLineTo(19f); horizontalLineTo(20f); verticalLineTo(18f); lineTo(18f, 16f); close()
    moveTo(16f, 17f); horizontalLineTo(8f); verticalLineTo(11f); curveTo(8f, 8.52f, 9.51f, 6.5f, 12f, 6.5f)
    curveTo(14.49f, 6.5f, 16f, 8.52f, 16f, 11f); verticalLineTo(17f); close()
  }
}

val Icons.Filled.Paid: ImageVector by lazy {
  buildIcon("Filled.Paid") {
    moveTo(12f, 2f); curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f); curveTo(2f, 17.52f, 6.48f, 22f, 12f, 22f)
    curveTo(17.52f, 22f, 22f, 17.52f, 22f, 12f); curveTo(22f, 6.48f, 17.52f, 2f, 12f, 2f); close()
    moveTo(13f, 17f); horizontalLineTo(11f); verticalLineTo(16f); horizontalLineTo(9f)
    verticalLineTo(14f); horizontalLineTo(13f); verticalLineTo(13f); horizontalLineTo(10f)
    curveTo(9.45f, 13f, 9f, 12.55f, 9f, 12f); verticalLineTo(9f); curveTo(9f, 8.45f, 9.45f, 8f, 10f, 8f)
    horizontalLineTo(11f); verticalLineTo(7f); horizontalLineTo(13f); verticalLineTo(8f); horizontalLineTo(15f)
    verticalLineTo(10f); horizontalLineTo(11f); verticalLineTo(11f); horizontalLineTo(14f)
    curveTo(14.55f, 11f, 15f, 11.45f, 15f, 12f); verticalLineTo(15f); curveTo(15f, 15.55f, 14.55f, 16f, 14f, 16f)
    horizontalLineTo(13f); verticalLineTo(17f); close()
  }
}

val Icons.Filled.Palette: ImageVector by lazy {
  buildIcon("Filled.Palette") {
    moveTo(12f, 3f); curveTo(7.03f, 3f, 3f, 7.03f, 3f, 12f); curveTo(3f, 16.97f, 7.03f, 21f, 12f, 21f)
    curveTo(12.83f, 21f, 13.5f, 20.33f, 13.5f, 19.5f); curveTo(13.5f, 19.11f, 13.35f, 18.76f, 13.11f, 18.49f)
    curveTo(12.88f, 18.23f, 12.73f, 17.88f, 12.73f, 17.5f); curveTo(12.73f, 16.67f, 13.4f, 16f, 14.23f, 16f)
    horizontalLineTo(16f); curveTo(18.76f, 16f, 21f, 13.76f, 21f, 11f); curveTo(21f, 6.58f, 16.97f, 3f, 12f, 3f); close()
  }
}

val Icons.Filled.Pets: ImageVector by lazy {
  buildIcon("Filled.Pets") {
    moveTo(12f, 10f); curveTo(9.5f, 10f, 7f, 12.5f, 7f, 15.5f); curveTo(7f, 18f, 9f, 20f, 12f, 20f)
    curveTo(15f, 20f, 17f, 18f, 17f, 15.5f); curveTo(17f, 12.5f, 14.5f, 10f, 12f, 10f); close()
  }
}

val Icons.Filled.PictureAsPdf: ImageVector by lazy {
  buildIcon("Filled.PictureAsPdf") {
    moveTo(20f, 2f); horizontalLineTo(8f); curveTo(6.9f, 2f, 6f, 2.9f, 6f, 4f); verticalLineTo(16f)
    curveTo(6f, 17.1f, 6.9f, 18f, 8f, 18f); horizontalLineTo(20f); curveTo(21.1f, 18f, 22f, 17.1f, 22f, 16f)
    verticalLineTo(4f); curveTo(22f, 2.9f, 21.1f, 2f, 20f, 2f); close()
    moveTo(4f, 6f); horizontalLineTo(2f); verticalLineTo(20f); curveTo(2f, 21.1f, 2.9f, 22f, 4f, 22f)
    horizontalLineTo(18f); verticalLineTo(20f); horizontalLineTo(4f); verticalLineTo(6f); close()
  }
}

val Icons.Filled.Restaurant: ImageVector by lazy {
  buildIcon("Filled.Restaurant") {
    moveTo(11f, 9f); horizontalLineTo(9f); verticalLineTo(2f); horizontalLineTo(7f); verticalLineTo(9f)
    horizontalLineTo(5f); verticalLineTo(2f); horizontalLineTo(3f); verticalLineTo(9f)
    curveTo(3f, 11.12f, 4.66f, 12.84f, 6.75f, 12.97f); verticalLineTo(22f); horizontalLineTo(9.25f)
    verticalLineTo(12.97f); curveTo(11.34f, 12.84f, 13f, 11.12f, 13f, 9f); verticalLineTo(2f)
    horizontalLineTo(11f); verticalLineTo(9f); close()
    moveTo(16f, 6f); verticalLineTo(14f); horizontalLineTo(18.5f); verticalLineTo(22f)
    horizontalLineTo(21f); verticalLineTo(2f); curveTo(18.24f, 2f, 16f, 4.24f, 16f, 6f); close()
  }
}

val Icons.Filled.Savings: ImageVector by lazy {
  buildIcon("Filled.Savings") {
    moveTo(19.83f, 7.5f); lineTo(17.5f, 5.17f); curveTo(16.1f, 4.4f, 14.5f, 4f, 12.5f, 4f)
    curveTo(7.8f, 4f, 4f, 7.5f, 4f, 12f); curveTo(4f, 15.1f, 5.8f, 17.8f, 8.5f, 19f)
    verticalLineTo(21f); horizontalLineTo(11.5f); verticalLineTo(19.5f); horizontalLineTo(13.5f)
    verticalLineTo(21f); horizontalLineTo(16.5f); verticalLineTo(18.8f)
    curveTo(19.2f, 17.4f, 21f, 14.9f, 21f, 12f); curveTo(21f, 10.3f, 20.5f, 8.8f, 19.83f, 7.5f); close()
  }
}

val Icons.Filled.School: ImageVector by lazy {
  buildIcon("Filled.School") {
    moveTo(5f, 13.18f); verticalLineTo(17.18f); lineTo(12f, 21f); lineTo(19f, 17.18f)
    verticalLineTo(13.18f); lineTo(12f, 17f); lineTo(5f, 13.18f); close()
    moveTo(12f, 3f); lineTo(1f, 9f); lineTo(12f, 15f); lineTo(21f, 10.09f); verticalLineTo(17f)
    horizontalLineTo(23f); verticalLineTo(9f); lineTo(12f, 3f); close()
  }
}

val Icons.Filled.Security: ImageVector by lazy {
  buildIcon("Filled.Security") {
    moveTo(12f, 1f); lineTo(3f, 5f); verticalLineTo(11f); curveTo(3f, 16.55f, 6.84f, 21.74f, 12f, 23f)
    curveTo(17.16f, 21.74f, 21f, 16.55f, 21f, 11f); verticalLineTo(5f); lineTo(12f, 1f); close()
    moveTo(12f, 11.99f); horizontalLineTo(19f); curveTo(18.47f, 16.11f, 15.72f, 19.78f, 12f, 20.93f)
    verticalLineTo(12f); horizontalLineTo(5f); verticalLineTo(6.3f); lineTo(12f, 3.19f); verticalLineTo(11.99f); close()
  }
}

val Icons.Filled.ShoppingBag: ImageVector by lazy {
  buildIcon("Filled.ShoppingBag") {
    moveTo(18f, 6f); horizontalLineTo(16f); curveTo(16f, 3.79f, 14.21f, 2f, 12f, 2f)
    curveTo(9.79f, 2f, 8f, 3.79f, 8f, 6f); horizontalLineTo(6f); curveTo(4.9f, 6f, 4f, 6.9f, 4f, 8f)
    verticalLineTo(20f); curveTo(4f, 21.1f, 4.9f, 22f, 6f, 22f); horizontalLineTo(18f)
    curveTo(19.1f, 22f, 20f, 21.1f, 20f, 20f); verticalLineTo(8f); curveTo(20f, 6.9f, 19.1f, 6f, 18f, 6f); close()
    moveTo(12f, 4f); curveTo(13.1f, 4f, 14f, 4.9f, 14f, 6f); horizontalLineTo(10f)
    curveTo(10f, 4.9f, 10.9f, 4f, 12f, 4f); close()
  }
}

val Icons.Filled.Spa: ImageVector by lazy {
  buildIcon("Filled.Spa") {
    moveTo(12f, 3f); curveTo(9f, 7f, 7f, 11f, 7f, 15f); curveTo(7f, 18.3f, 9.2f, 21f, 12f, 21f)
    curveTo(14.8f, 21f, 17f, 18.3f, 17f, 15f); curveTo(17f, 11f, 15f, 7f, 12f, 3f); close()
  }
}

val Icons.Filled.Subscriptions: ImageVector by lazy {
  buildIcon("Filled.Subscriptions") {
    moveTo(20f, 8f); horizontalLineTo(4f); verticalLineTo(6f); horizontalLineTo(20f); verticalLineTo(8f); close()
    moveTo(18f, 2f); horizontalLineTo(6f); verticalLineTo(4f); horizontalLineTo(18f); verticalLineTo(2f); close()
    moveTo(22f, 12f); verticalLineTo(20f); curveTo(22f, 21.1f, 21.1f, 22f, 20f, 22f); horizontalLineTo(4f)
    curveTo(2.9f, 22f, 2f, 21.1f, 2f, 20f); verticalLineTo(12f); curveTo(2f, 10.9f, 2.9f, 10f, 4f, 10f)
    horizontalLineTo(20f); curveTo(21.1f, 10f, 22f, 10.9f, 22f, 12f); close()
    moveTo(16f, 16f); lineTo(10f, 12.73f); verticalLineTo(19.26f); lineTo(16f, 16f); close()
  }
}

val Icons.Filled.SwapHoriz: ImageVector by lazy {
  buildIcon("Filled.SwapHoriz") {
    moveTo(6.99f, 11f); lineTo(3f, 15f); lineTo(6.99f, 19f); verticalLineTo(16f); horizontalLineTo(14f)
    verticalLineTo(14f); horizontalLineTo(6.99f); verticalLineTo(11f); close()
    moveTo(21f, 9f); lineTo(17.01f, 5f); verticalLineTo(8f); horizontalLineTo(10f)
    verticalLineTo(10f); horizontalLineTo(17.01f); verticalLineTo(13f); lineTo(21f, 9f); close()
  }
}

val Icons.Filled.TableChart: ImageVector by lazy {
  buildIcon("Filled.TableChart") {
    moveTo(10f, 10.02f); horizontalLineTo(15f); verticalLineTo(21f); horizontalLineTo(10f); close()
    moveTo(17f, 21f); horizontalLineTo(20f); curveTo(21.1f, 21f, 22f, 20.1f, 22f, 19f)
    verticalLineTo(10f); horizontalLineTo(17f); verticalLineTo(21f); close()
    moveTo(20f, 3f); horizontalLineTo(5f); curveTo(3.9f, 3f, 3f, 3.9f, 3f, 5f)
    verticalLineTo(8f); horizontalLineTo(22f); verticalLineTo(5f); curveTo(22f, 3.9f, 21.1f, 3f, 20f, 3f); close()
    moveTo(3f, 19f); curveTo(3f, 20.1f, 3.9f, 21f, 5f, 21f); horizontalLineTo(8f)
    verticalLineTo(10f); horizontalLineTo(3f); verticalLineTo(19f); close()
  }
}

val Icons.Filled.TrackChanges: ImageVector by lazy {
  buildIcon("Filled.TrackChanges") {
    moveTo(19.07f, 4.93f); lineTo(17.66f, 6.34f); curveTo(19.1f, 7.79f, 20f, 9.79f, 20f, 12f)
    curveTo(20f, 16.42f, 16.42f, 20f, 12f, 20f); curveTo(7.58f, 20f, 4f, 16.42f, 4f, 12f)
    curveTo(4f, 7.92f, 7.05f, 4.56f, 11f, 4.07f); verticalLineTo(6.09f)
    curveTo(8.16f, 6.57f, 6f, 9.03f, 6f, 12f); curveTo(6f, 15.31f, 8.69f, 18f, 12f, 18f)
    curveTo(15.31f, 18f, 18f, 15.31f, 18f, 12f); curveTo(18f, 10.34f, 17.33f, 8.84f, 16.24f, 7.76f)
    lineTo(14.83f, 9.17f); curveTo(15.55f, 9.9f, 16f, 10.9f, 16f, 12f)
    curveTo(16f, 14.21f, 14.21f, 16f, 12f, 16f); curveTo(9.79f, 16f, 8f, 14.21f, 8f, 12f)
    curveTo(8f, 10.14f, 9.28f, 8.59f, 11f, 8.14f); verticalLineTo(10.28f)
    curveTo(10.4f, 10.63f, 10f, 11.26f, 10f, 12f); curveTo(10f, 13.1f, 10.9f, 14f, 12f, 14f)
    curveTo(13.1f, 14f, 14f, 13.1f, 14f, 12f); curveTo(14f, 11.26f, 13.6f, 10.62f, 13f, 10.28f)
    verticalLineTo(2.05f); curveTo(12.67f, 2.02f, 12.34f, 2f, 12f, 2f)
    curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f); curveTo(2f, 17.52f, 6.48f, 22f, 12f, 22f)
    curveTo(17.52f, 22f, 22f, 17.52f, 22f, 12f); curveTo(22f, 9.24f, 20.88f, 6.74f, 19.07f, 4.93f); close()
  }
}

val Icons.Filled.Vibration: ImageVector by lazy {
  buildIcon("Filled.Vibration") {
    moveTo(0f, 15f); horizontalLineTo(2f); verticalLineTo(9f); horizontalLineTo(0f); verticalLineTo(15f); close()
    moveTo(3f, 17f); horizontalLineTo(5f); verticalLineTo(7f); horizontalLineTo(3f); verticalLineTo(17f); close()
    moveTo(22f, 9f); verticalLineTo(15f); horizontalLineTo(24f); verticalLineTo(9f); horizontalLineTo(22f); close()
    moveTo(19f, 17f); horizontalLineTo(21f); verticalLineTo(7f); horizontalLineTo(19f); verticalLineTo(17f); close()
    moveTo(16.5f, 3f); horizontalLineTo(7.5f); curveTo(6.67f, 3f, 6f, 3.67f, 6f, 4.5f)
    verticalLineTo(19.5f); curveTo(6f, 20.33f, 6.67f, 21f, 7.5f, 21f); horizontalLineTo(16.5f)
    curveTo(17.33f, 21f, 18f, 20.33f, 18f, 19.5f); verticalLineTo(4.5f); curveTo(18f, 3.67f, 17.33f, 3f, 16.5f, 3f); close()
    moveTo(16f, 19f); horizontalLineTo(8f); verticalLineTo(5f); horizontalLineTo(16f); verticalLineTo(19f); close()
  }
}

val Icons.Filled.Visibility: ImageVector by lazy {
  buildIcon("Filled.Visibility") {
    moveTo(12f, 4.5f); curveTo(7f, 4.5f, 2.73f, 7.61f, 1f, 12f); curveTo(2.73f, 16.39f, 7f, 19.5f, 12f, 19.5f)
    curveTo(17f, 19.5f, 21.27f, 16.39f, 23f, 12f); curveTo(21.27f, 7.61f, 17f, 4.5f, 12f, 4.5f); close()
    moveTo(12f, 17f); curveTo(9.24f, 17f, 7f, 14.76f, 7f, 12f); curveTo(7f, 9.24f, 9.24f, 7f, 12f, 7f)
    curveTo(14.76f, 7f, 17f, 9.24f, 17f, 12f); curveTo(17f, 14.76f, 14.76f, 17f, 12f, 17f); close()
    moveTo(12f, 9f); curveTo(10.34f, 9f, 9f, 10.34f, 9f, 12f); curveTo(9f, 13.66f, 10.34f, 15f, 12f, 15f)
    curveTo(13.66f, 15f, 15f, 13.66f, 15f, 12f); curveTo(15f, 10.34f, 13.66f, 9f, 12f, 9f); close()
  }
}

val Icons.Filled.VisibilityOff: ImageVector by lazy {
  buildIcon("Filled.VisibilityOff") {
    moveTo(12f, 7f); curveTo(14.76f, 7f, 17f, 9.24f, 17f, 12f); curveTo(17f, 12.65f, 16.87f, 13.26f, 16.64f, 13.83f)
    lineTo(19.56f, 16.75f); curveTo(21.07f, 15.49f, 22.26f, 13.86f, 22.99f, 12f)
    curveTo(21.26f, 7.61f, 16.99f, 4.5f, 11.99f, 4.5f); curveTo(10.59f, 4.5f, 9.25f, 4.75f, 8.01f, 5.2f)
    lineTo(10.17f, 7.36f); curveTo(10.74f, 7.13f, 11.35f, 7f, 12f, 7f); close()
    moveTo(2f, 4.27f); lineTo(4.28f, 6.55f); lineTo(4.74f, 7.01f); curveTo(3.08f, 8.3f, 1.78f, 10.02f, 1f, 12f)
    curveTo(2.73f, 16.39f, 7f, 19.5f, 12f, 19.5f); curveTo(13.55f, 19.5f, 15.03f, 19.2f, 16.38f, 18.66f)
    lineTo(16.8f, 19.08f); lineTo(19.73f, 22f); lineTo(21f, 20.73f); lineTo(3.27f, 3f); lineTo(2f, 4.27f); close()
  }
}

val Icons.Filled.Work: ImageVector by lazy {
  buildIcon("Filled.Work") {
    moveTo(20f, 6f); horizontalLineTo(16f); verticalLineTo(4f); curveTo(16f, 2.89f, 15.11f, 2f, 14f, 2f)
    horizontalLineTo(10f); curveTo(8.89f, 2f, 8f, 2.89f, 8f, 4f); verticalLineTo(6f); horizontalLineTo(4f)
    curveTo(2.89f, 6f, 2f, 6.89f, 2f, 8f); verticalLineTo(19f); curveTo(2f, 20.11f, 2.89f, 21f, 4f, 21f)
    horizontalLineTo(20f); curveTo(21.11f, 21f, 22f, 20.11f, 22f, 19f); verticalLineTo(8f)
    curveTo(22f, 6.89f, 21.11f, 6f, 20f, 6f); close()
    moveTo(14f, 6f); horizontalLineTo(10f); verticalLineTo(4f); horizontalLineTo(14f); verticalLineTo(6f); close()
  }
}
