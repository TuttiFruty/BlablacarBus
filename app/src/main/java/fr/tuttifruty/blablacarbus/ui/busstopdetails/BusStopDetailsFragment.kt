package fr.tuttifruty.blablacarbus.ui.busstopdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import fr.tuttifruty.blablacarbus.R
import fr.tuttifruty.blablacarbus.common.mvi.IView
import fr.tuttifruty.blablacarbus.databinding.FragmentBusStopDetailsBinding
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class BusStopDetailsFragment : Fragment(),
    IView<BusStopDetailsState, BusStopDetailsIntent, BusStopsNavigation> {
    private lateinit var binding: FragmentBusStopDetailsBinding
    val viewModel: BusStopDetailsViewModel by viewModel()
    private lateinit var adapterDestinations: DestinationsAdapter
    private val args: BusStopDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bus_stop_details, container, false)

        adapterDestinations =
            DestinationsAdapter(viewModel) { busStop ->
                sendIntent(BusStopDetailsIntent.ShowFaresForDestination(busStop))
            }
        binding.rvBusStopDestinations.adapter = adapterDestinations
        binding.rvBusStopDestinations.layoutManager = GridLayoutManager(requireContext(), 2)

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
        sendIntent(BusStopDetailsIntent.ShowDetails(args.busStopId))
    }

    override fun render(state: BusStopDetailsState) {
        when (state) {
            BusStopDetailsState.Loading -> showProgress(true)
            is BusStopDetailsState.ShowBusStopDetails -> initView(state.busStop, state.listDestinations)
            is BusStopDetailsState.ShowError -> showErrorMessage(state.message)
        }
    }

    override fun sendIntent(intent: BusStopDetailsIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    override fun navigateTo(navigation: BusStopsNavigation) {
        //No-op
    }

    private fun initView(busStop: BusStopDomainModel, destinations: List<BusStopDomainModel>) {
        binding.apply {
            tvIdBusStop.text = "${busStop.id}"
            tvShortNameBusStop.text = busStop.shortName
            tvLongNameBusStop.text = busStop.longName
            tvTimeZoneBusStop.text = busStop.timeZone
            tvLatitudeValueBusStop.text = busStop.location.latitude.toString()
            tvLongitudeValueBusStop.text = busStop.location.longitude.toString()
            tvAdressValueBusStop.text = busStop.address
        }

        adapterDestinations.submitList(destinations)
    }

    private fun showProgress(isLoading: Boolean) {
        //TODO when Fares use case implemented
    }


    private fun showErrorMessage(message: Int) {
        //TODO when Fares use case implemented
    }
}