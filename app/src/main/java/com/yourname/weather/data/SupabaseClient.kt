package com.yourname.weather.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.gotrue.Auth

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://mowmbbrjvwqmpcjlbevo.supabase.co",
        supabaseKey = "sb_publishable_cpuY59Ly7jsUgyTuTvZOZQ_XYRjUcUv"
    ) {
        install(Postgrest)
        install(Auth)
    }
}
