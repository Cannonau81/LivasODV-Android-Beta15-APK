package it.livasodv.app.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import it.livasodv.app.R
import it.livasodv.app.data.SupabaseProvider
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFF030303), Color(0xFF1A0707), Color(0xFF050505)))
        )
    ) {
        Column(
            Modifier.fillMaxSize().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(R.drawable.livas_logo),
                contentDescription = "Logo Lì.v.a.s.",
                modifier = Modifier.size(190.dp).shadow(28.dp, CircleShape),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(12.dp))
            Text("LÌ.V.A.S. O.D.V.", color=Color.White, fontSize=28.sp, fontWeight=FontWeight.Black)
            Text("GONNOSFANADIGA", color=Color(0xFFFF3B30), fontWeight=FontWeight.Bold, letterSpacing=1.4.sp)
            Text("Insieme per aiutare, sempre.", color=Color(0xFFB0B0B0), fontSize=13.sp)
            Spacer(Modifier.height(24.dp))

            Card(
                colors=CardDefaults.cardColors(containerColor=Color(0xE6151515)),
                shape=RoundedCornerShape(24.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Row(verticalAlignment=Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock,null,tint=Color(0xFFFF3B30))
                        Spacer(Modifier.width(9.dp))
                        Column {
                            Text("Accesso area riservata",color=Color.White,fontWeight=FontWeight.Bold,fontSize=18.sp)
                            Text("Credenziali Lì.v.a.s.",color=Color.Gray,fontSize=12.sp)
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value=email,onValueChange={email=it.trim()},label={Text("Email")},
                        singleLine=true,modifier=Modifier.fillMaxWidth(),
                        colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=Color(0xFFFF3B30),focusedLabelColor=Color(0xFFFF3B30))
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value=password,onValueChange={password=it},label={Text("Password")},
                        singleLine=true,modifier=Modifier.fillMaxWidth(),
                        visualTransformation=if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon={
                            IconButton(onClick={showPassword=!showPassword}) {
                                Icon(if(showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    if(showPassword) "Nascondi password" else "Mostra password")
                            }
                        },
                        colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=Color(0xFFFF3B30),focusedLabelColor=Color(0xFFFF3B30))
                    )
                    error?.let {
                        Spacer(Modifier.height(10.dp))
                        Text(it,color=Color(0xFFFF6B6B),fontSize=12.sp,textAlign=TextAlign.Center,modifier=Modifier.fillMaxWidth())
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick={
                            scope.launch {
                                loading=true; error=null
                                try {
                                    SupabaseProvider.client.auth.signInWith(Email) {
                                        this.email=email; this.password=password
                                    }
                                    onSuccess()
                                } catch(e:Exception) {
                                    error="Accesso non riuscito. Controlla email e password."
                                } finally { loading=false }
                            }
                        },
                        enabled=!loading && email.isNotBlank() && password.isNotBlank(),
                        modifier=Modifier.fillMaxWidth().height(52.dp),
                        colors=ButtonDefaults.buttonColors(containerColor=Color(0xFFFF2D2D)),
                        shape=RoundedCornerShape(18.dp)
                    ) {
                        Icon(Icons.Default.Login,null); Spacer(Modifier.width(8.dp))
                        Text(if(loading) "Accesso…" else "ACCEDI",fontWeight=FontWeight.Black)
                    }
                }
            }
        }
    }
}
