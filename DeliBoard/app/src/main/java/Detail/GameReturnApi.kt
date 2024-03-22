package Detail

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GameReturnApi {
    @GET("game_return?storeId={storeId}&roomId={roomNum}")
    fun returnGame(
        @Path("roomNum") roomNum: String,
        @Path("storeId") storeId: String,
    ): Call<Void>
}