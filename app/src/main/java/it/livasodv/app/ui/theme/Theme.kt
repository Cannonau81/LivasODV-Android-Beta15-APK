package it.livasodv.app.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val scheme=darkColorScheme(primary=Color(0xFFFF3434),background=Color(0xFF070707),surface=Color(0xFF111111),onPrimary=Color.White,onBackground=Color.White,onSurface=Color.White)
@Composable fun LivasTheme(content:@Composable ()->Unit){ MaterialTheme(colorScheme=scheme,content=content) }
