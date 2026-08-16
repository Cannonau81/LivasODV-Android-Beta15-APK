LÌ.V.A.S. ANDROID BETA 1.0 — PARITÀ BUILD 27
Aprire la cartella con Android Studio e attendere Gradle Sync.

La home e i moduli coprono: Soci, Comunicazioni, Mezzi, Turni, Servizi, Magazzino, Presidi,
Servizio Civile, Cittadini, Emergenze, Monitor PS 118, Protezione Civile, Passatempo,
Ricerca globale, Backup, Scadenze, Notifiche, Registro attività e Operativo.

SICUREZZA:
- backup Android automatico disabilitato;
- cleartext HTTP disabilitato;
- Biometric AndroidX predisposto;
- auto logout policy 3 minuti;
- service_role Supabase non va mai inserita nell'APK.

SINCRONIZZAZIONE:
Inserire Project URL + publishable/anon key in SupabaseProvider.kt.
Le policy RLS del progetto Supabase devono autorizzare gli stessi ruoli della versione iPhone.
