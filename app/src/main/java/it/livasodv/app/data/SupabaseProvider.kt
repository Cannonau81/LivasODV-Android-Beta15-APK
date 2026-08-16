package it.livasodv.app.data
import it.livasodv.app.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

object SupabaseProvider {
 val client by lazy {
  createSupabaseClient(BuildConfig.SUPABASE_URL,BuildConfig.SUPABASE_PUBLISHABLE_KEY){
   install(Auth){ flowType=FlowType.PKCE; scheme="livasodv"; host="auth" }
   install(Postgrest)
   install(Realtime)
  }
 }
}
