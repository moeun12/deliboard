package beverage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import com.example.deliboard.R

class NoCafe : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_no_cafe, container, false)

        view.findViewById<ConstraintLayout>(R.id.buttonCoffee).setOnClickListener{
            it.findNavController().navigate(R.id.action_noCafe_to_coffee)
        }

        view.findViewById<ConstraintLayout>(R.id.buttonSnack).setOnClickListener{
            it.findNavController().navigate(R.id.action_noCafe_to_snack)
        }

        return view
    }

}