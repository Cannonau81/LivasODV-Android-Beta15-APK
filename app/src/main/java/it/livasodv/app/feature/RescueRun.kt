package it.livasodv.app.feature
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable fun RescueRunGame(){
 var lane by remember{mutableIntStateOf(1)};var obstacle by remember{mutableIntStateOf(0)};var stretcher by remember{mutableIntStateOf(2)};var score by remember{mutableIntStateOf(0)}
 LaunchedEffect(Unit){while(true){delay(900);obstacle=Random.nextInt(3);stretcher=Random.nextInt(3);if(lane==stretcher)score+=10;if(lane==obstacle)score=(score-5).coerceAtLeast(0)}}
 Column(Modifier.fillMaxSize().padding(18.dp),horizontalAlignment=Alignment.CenterHorizontally){
  Text("RESCUE RUN 🚑",style=MaterialTheme.typography.headlineSmall);Text("Punti: $score")
  Spacer(Modifier.height(16.dp))
  Row(Modifier.fillMaxWidth().weight(1f)){
   repeat(3){i->Box(Modifier.weight(1f).fillMaxHeight().border(1.dp,Color.DarkGray).clickable{lane=i},contentAlignment=Alignment.Center){
    Column(horizontalAlignment=Alignment.CenterHorizontally){if(i==obstacle)Text("🚧");if(i==stretcher)Text("🛏️");Spacer(Modifier.weight(1f));if(i==lane)Text("🚑")}
   }}
  }
  Text("Tocca una corsia: evita 🚧 e raccogli 🛏️")
 }
}
