package Detail

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GameOrderApi {
    @GET("game-order?")
    fun orderGame(
        @Query("gameId") gameId: String,
        @Query("roomNumber") roomId: String,
        @Query("storeId") storeId: String,
    ): Call<Void>
}