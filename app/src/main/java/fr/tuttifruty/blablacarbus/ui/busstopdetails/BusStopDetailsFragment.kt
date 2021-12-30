package fr.tuttifruty.blablacarbus.ui.busstopdetails

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import fr.tuttifruty.blablacarbus.R
import fr.tuttifruty.blablacarbus.common.DelayedQueryTextListener
import fr.tuttifruty.blablacarbus.common.mvi.IView
import fr.tuttifruty.blablacarbus.databinding.FragmentBusStopDetailsBinding
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf


class BusStopDetailsFragment : Fragment(),
    IView<BusStopDetailsState, BusStopDetailsIntent, BusStopDetailsNavigation> {
    private lateinit var binding: FragmentBusStopDetailsBinding
    private lateinit var viewModel: BusStopDetailsViewModel
    private lateinit var adapterDestinations: DestinationsAdapter
    private val args: BusStopDetailsFragmentArgs by navArgs()
    private var lastName: String? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bottom_navigation_menu_details, menu)

        initSearch(menu)

        super.onCreateOptionsMenu(menu, inflater)

    }

    private fun initSearch(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(
            DelayedQueryTextListener(
                this@BusStopDetailsFragment.lifecycle,
                onDefaultQueryTextSubmit = {
                    sendIntent(
                        BusStopDetailsIntent.FilterDestination(
                            query = it.toString(),
                        )
                    )
                },
                onDelayedQueryTextChange = {
                    if (it.isNullOrEmpty()) {
                        sendIntent(
                            BusStopDetailsIntent.FilterDestination(
                                query = null,
                            )
                        )
                    } else {
                        sendIntent(
                            BusStopDetailsIntent.FilterDestination(
                                query = it.toString(),
                            )
                        )
                    }
                })
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bus_stop_details, container, false)

        setHasOptionsMenu(true)

        viewModel = getViewModel {
            parametersOf(args.busStopId)
        }

        adapterDestinations =
            DestinationsAdapter(viewModel) { busStop ->
                sendIntent(BusStopDetailsIntent.ShowFaresForDestination(busStop))
            }
        binding.rvBusStopDestinations.adapter = adapterDestinations
        binding.rvBusStopDestinations.layoutManager = LinearLayoutManager(requireContext())

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

        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.busStopDetailsFragment) {
                initActionBar()
            }
        }

        return binding.root
    }

    private fun initActionBar() {
        activity?.let {
            if (it is AppCompatActivity) {
                val actionBar = it.supportActionBar
                actionBar?.title = lastName
            }
        }
    }

    override fun render(state: BusStopDetailsState) {
        when (state) {
            BusStopDetailsState.Loading -> showProgress(true)
            is BusStopDetailsState.ShowBusStopDetails -> initView(
                state.busStop,
                state.listDestinations
            )
            is BusStopDetailsState.ShowError -> showErrorMessage(state.message)
        }
    }

    override fun sendIntent(intent: BusStopDetailsIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    override fun navigateTo(navigation: BusStopDetailsNavigation) {
        showProgress(false)
        when (navigation) {
            is BusStopDetailsNavigation.GoToFaresForDestination -> {
                findNavController().navigate(
                    BusStopDetailsFragmentDirections.actionBusStopDetailsFragmentToFaresDialogFragment(
                        navigation.fares.toTypedArray()
                    )
                )
            }
        }
    }

    private fun initView(busStop: BusStopDomainModel, destinations: List<BusStopDomainModel>) {
        showProgress(false)
        binding.apply {

            lastName = busStop.longName
            initActionBar()
            tvIdBusStop.text = "${busStop.id}"
            tvLongNameBusStop.text = busStop.longName
            tvTimeZoneBusStop.text = busStop.timeZone
            tvLatitudeValueBusStop.text = busStop.location.latitude.toString()
            tvLongitudeValueBusStop.text = busStop.location.longitude.toString()
            tvAdressValueBusStop.text = busStop.address
        }

        adapterDestinations.submitList(destinations)
    }

    private fun showProgress(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                loading.vLoading.visibility = View.VISIBLE
                loading.progressBar.visibility = View.VISIBLE
            } else {
                loading.vLoading.visibility = View.GONE
                loading.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showErrorMessage(message: Int) {
        showProgress(false)
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}