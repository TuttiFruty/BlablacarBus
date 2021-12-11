package fr.tuttifruty.blablacarbus.ui.fares

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import fr.tuttifruty.blablacarbus.databinding.FragmentFaresDialogBinding

class FaresDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentFaresDialogBinding
    private val args: FaresDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFaresDialogBinding.inflate(inflater, container, false)

        val faresAdapter = FaresAdapter()

        binding.rvFaresList.apply {
            adapter = faresAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        faresAdapter.submitList(args.fares.asList())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.viewTreeObserver.addOnGlobalLayoutListener {
            (dialog as? BottomSheetDialog)
                ?.let { it.findViewById(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout }
                ?.let { BottomSheetBehavior.from(it) }
                ?.let {
                    it.peekHeight =
                        (Resources.getSystem().displayMetrics.heightPixels * MAX_PEEK_HEIGHT).toInt()
                }
        }
    }

    companion object {
        const val MAX_PEEK_HEIGHT = 0.6
    }
}