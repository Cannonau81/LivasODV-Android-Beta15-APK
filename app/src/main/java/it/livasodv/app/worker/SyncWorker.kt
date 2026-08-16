package it.livasodv.app.worker
import android.content.Context
import androidx.work.*
class SyncWorker(ctx:Context,params:WorkerParameters):CoroutineWorker(ctx,params){
 override suspend fun doWork():Result = try {
  // Supabase delta sync adapter: mantenere la copia locale in caso di rete assente.
  Result.success()
 } catch(e:Exception){ Result.retry() }
}
