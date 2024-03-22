package Game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val GameNetWork = GameNetwork()

    private val _gameList = MutableLiveData<List<GameClass>>()
    val gameList: LiveData<List<GameClass>> = _gameList

    fun getCurrentGameList() {
        viewModelScope.launch {
            try {
                val result = GameNetWork.getGameList()
                _gameList.value = result
            } catch (e: Exception) {
                // 에러 처리
                println(e)
            }
        }
    }
}