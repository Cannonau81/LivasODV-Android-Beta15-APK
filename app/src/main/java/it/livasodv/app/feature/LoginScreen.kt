package it.livasodv.app.feature
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import it.livasodv.app.data.SupabaseProvider
import kotlinx.coroutines.launch

@Composable fun LoginScreen(onSuccess:()->Unit){
 var email by remember { mutableStateOf("") };var password by remember { mutableStateOf("") };var error by remember{mutableStateOf<String?>(null)}
 val scope=rememberCoroutineScope()
 Column(Modifier.fillMaxSize().padding(28.dp),verticalArrangement=Arrangement.Center){
  Text("LÌ.V.A.S. O.D.V.",style=MaterialTheme.typography.headlineMedium)
  Text("Accesso area riservata")
  Spacer(Modifier.height(18.dp))
  OutlinedTextField(
      value=email,
      onValueChange={email=it.trim()},
      label={Text("Email account")},
      singleLine=true,
      keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Email,imeAction=ImeAction.Next),
      modifier=Modifier.fillMaxWidth()
    )
  OutlinedTextField(
      value=password,
      onValueChange={password=it},
      label={Text("Password")},
      singleLine=true,
      visualTransformation=PasswordVisualTransformation(),
      keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Password,imeAction=ImeAction.Done),
      modifier=Modifier.fillMaxWidth()
    )
  error?.let{Text(it,color=MaterialTheme.colorScheme.error)}
  Button(onClick={scope.launch{try{SupabaseProvider.client.auth.signInWith(Email){this.email=email;this.password=password};onSuccess()}catch(e:Exception){error="Accesso non riuscito: ${e.message ?: "verifica credenziali"}"}}},modifier=Modifier.fillMaxWidth()){Text("Accedi")}
 }
}
