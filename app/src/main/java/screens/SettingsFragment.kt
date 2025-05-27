package screens

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.quickcart.MainActivity
import viewModel.ShoppingListViewModel
import com.example.quickcart.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShoppingListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        //Handles changing fragment colors to black since they dont auto change like composite
        val isDark = (requireActivity() as MainActivity).isDarkModeEnabled()
        binding.textDarkMode.setTextColor(
            if (isDark) Color.WHITE else Color.BLACK
        )

        //Handle dark mode toggle
        binding.switchDarkMode.isChecked =
            (requireActivity() as MainActivity).isDarkModeEnabled()

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            (requireActivity() as MainActivity).setDarkMode(isChecked)
            binding.textDarkMode.setTextColor(
                if (isChecked) Color.WHITE else Color.BLACK
            )
        }

        //Handle clear DB
        binding.buttonClearDatabase.setOnClickListener {
            viewModel.clearDatabase()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}