package Game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliboard.R
import com.example.deliboard.databinding.FragmentGameListBinding
import kotlinx.coroutines.launch

class GameList : Fragment() {

    private lateinit var binding: FragmentGameListBinding

    private val viewModel: GameViewModel by viewModels()

    private lateinit var gameAdapter: GameAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGameListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameAdapter = GameAdapter(requireContext(), emptyList()) // 초기에 빈 리스트로 어댑터 생성
        binding.gameListRV.adapter = gameAdapter
        binding.gameListRV.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.getCurrentGameList()

        lifecycleScope.launch {
            viewModel.gameList.observe(viewLifecycleOwner) { gamelist ->
                gameAdapter.updateData(gamelist) // 데이터 업데이트
            }
        }
    }
}