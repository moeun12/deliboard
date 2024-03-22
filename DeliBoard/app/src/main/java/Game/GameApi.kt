package Game

import retrofit2.http.GET

interface GameApi {

    @GET("stock/games?storeId=1")
    suspend fun getGameList() : List<GameClass>
}