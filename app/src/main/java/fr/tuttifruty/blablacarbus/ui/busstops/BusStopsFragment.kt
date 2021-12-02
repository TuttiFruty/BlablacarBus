package fr.tuttifruty.blablacarbus.ui.busstops

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fr.tuttifruty.blablacarbus.R
import fr.tuttifruty.blablacarbus.common.mvi.IView
import fr.tuttifruty.blablacarbus.databinding.FragmentBusStopsBinding
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class BusStopsFragment : Fragment(), IView<BusStopsState, BusStopsIntent, BusStopsNavigation> {
    private lateinit var binding: FragmentBusStopsBinding
    val viewModel: BusStopsViewModel by viewModel()
    private lateinit var adapterBusStops: BusStopsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bus_stops, container, false)

        adapterBusStops = BusStopsAdapter(viewModel) { busStop ->
            sendIntent(BusStopsIntent.Navigation.GoToDetailsOfBusStop(busStop))
        }
        binding.rvBusStops.adapter = adapterBusStops
        binding.rvBusStops.layoutManager = LinearLayoutManager(requireContext())

        viewModel.state.observe(
            viewLifecycleOwner, { busStopsState ->
                render(busStopsState)
            })

        viewModel.navigation.observe(
            viewLifecycleOwner, { navigationEvent ->
                navigationEvent.getContentIfNotHandled()?.let {
                    navigateTo(it)
                }
            }
        )


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        sendIntent(BusStopsIntent.RefreshBusStops)
    }

    override fun render(state: BusStopsState) {
        when (state) {
            BusStopsState.Loading -> showProgress(true)
            is BusStopsState.ShowBusStops -> initView(state.busStopsList)
            is BusStopsState.ShowError -> showErrorMessage(state.message)
        }
    }

    override fun sendIntent(intent: BusStopsIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    override fun navigateTo(navigation: BusStopsNavigation) {
        when (navigation) {
            is BusStopsNavigation.GoToDetailsOfBusStop -> {
                findNavController().navigate(
                    BusStopsFragmentDirections.actionBusStopsFragmentToBusStopDetailsFragment(
                        navigation.busStop
                    )
                )
            }
        }
    }

    private fun initView(busStopsList: List<BusStopDomainModel>) {
        adapterBusStops.submitList(busStopsList)
    }

    private fun showProgress(isLoading: Boolean) {

    }


    private fun showErrorMessage(message: Int) {

    }
}