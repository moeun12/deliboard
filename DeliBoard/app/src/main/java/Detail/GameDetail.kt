package Detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.deliboard.BaseApi
import com.example.deliboard.R
import com.example.deliboard.databinding.FragmentGameDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameDetail : Fragment() {

    private lateinit var binding: FragmentGameDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_game_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("StoreInfo", Context.MODE_PRIVATE)
        val backButton = view.findViewById<ImageView>(R.id.detailBack)

        backButton.setOnClickListener{
            it.findNavController().navigate(R.id.action_gameDetail_to_GameList)
        }

        // Argument에서 클릭된 항목의 정보를 받아와서 UI에 표시
        val gameId = arguments?.getString("gameId")
        val title = arguments?.getString("title")
        val description = arguments?.getString("description")
        val thumbnailUrl = arguments?.getString("thumbnailUrl")
//        val stock = arguments?.getString("stock")!!.toInt()
        val minPlayer = arguments?.getString("minPlayer")
        val maxPlayer = arguments?.getString("maxPlayer")
        val minPlaytime = arguments?.getString("minPlaytime")
        val maxPlaytime = arguments?.getString("maxPlaytime")
        val difficulty = arguments?.getString("difficulty")!!.toInt()
        val theme = arguments?.getString("theme")

        // UI 요소에 정보 표시
        view.findViewById<TextView>(R.id.detailTitle).text = title
        view.findViewById<TextView>(R.id.detailDescription).text = description
//        view.findViewById<TextView>(R.id.detailDescription).text = thumbnailUrl
//        view.findViewById<TextView>(R.id.detailStock).text = if (stock > 0) "O" else "X"
        view.findViewById<TextView>(R.id.detailPlayer).text = if (minPlayer == maxPlayer) {
            "인원 : ${maxPlayer}명"
        } else {
            "인원 : $minPlayer ~ ${maxPlayer}명"
        }
//        view.findViewById<TextView>(R.id.detailMaxPlayer).text = maxPlayer
        view.findViewById<TextView>(R.id.detailPlaytime).text = if (minPlaytime == maxPlaytime) {
            "플레이타임 : ${maxPlaytime}분"
        } else {
            "플레이타임 : $minPlaytime ~ ${maxPlaytime}분"
        }
//        view.findViewById<TextView>(R.id.detailMaxPlaytime).text = maxPlaytime
        view.findViewById<TextView>(R.id.detailDifficulty).text = "난이도 : ${"★".repeat(difficulty)}"
        view.findViewById<TextView>(R.id.detailTheme).text = "장르 : $theme"

        thumbnailUrl?.let {
            Glide.with(requireContext())
                .load(it)
                .placeholder(R.drawable.ipad) // 로딩 중에 표시할 이미지
                .error(R.drawable.chess_black) // 로딩 실패 시 표시할 이미지
                .into(binding.detailImage)
        }

        binding.startButton.setOnClickListener{
            val storeId = sharedPreferences.getString("storeId", "")
            val roomNum = sharedPreferences.getString("roomNum", "")
            gameId?.let{startGame(it, roomNum!!, storeId!!)}
        }
    }

    private fun startGame(gameId: String, roomNum: String, storeId: String) {
        val service = BaseApi.getInstance().create((GameOrderApi::class.java))

        val call = service.orderGame(gameId, roomNum, storeId)
//        println("$gameId, $roomId, $storeId")

        call.enqueue(object : Callback<Void> {
            override  fun onResponse(call: Call<Void>, response: Response<Void>) {
                println(response)

                if (response.isSuccessful) {
                    Log.d("Order", "${response.body()}")
                    Log.d("Order", "성공")
                    saveGameId(gameId)
                } else {
                    Log.d("Order", "fail: ${response.errorBody()}")
                }
            }
            override fun onFailure(call: Call<Void>, t:Throwable) {
                Log.d("Order", "$t")
            }
        })
    }

    private fun saveGameId(gameId: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("RoomInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("gameId", gameId)
        editor.apply()
    }
}