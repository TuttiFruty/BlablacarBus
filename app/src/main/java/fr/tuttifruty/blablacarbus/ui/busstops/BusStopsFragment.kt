package fr.tuttifruty.blablacarbus.ui.busstops

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import fr.tuttifruty.blablacarbus.R
import fr.tuttifruty.blablacarbus.common.DelayedQueryTextListener
import fr.tuttifruty.blablacarbus.common.Permission
import fr.tuttifruty.blablacarbus.common.PermissionManager
import fr.tuttifruty.blablacarbus.common.mvi.IView
import fr.tuttifruty.blablacarbus.databinding.FragmentBusStopsBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class BusStopsFragment : Fragment(), IView<BusStopsState, BusStopsIntent, BusStopsNavigation> {

    private lateinit var binding: FragmentBusStopsBinding
    val viewModel: BusStopsViewModel by viewModel()
    private lateinit var adapterBusStops: BusStopsAdapter
    private val permissionManager = PermissionManager.from(this)
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var lastSearch: String? = null
    private var lastFilter: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bus_stops, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        adapterBusStops = BusStopsAdapter(viewModel) { busStop ->
            sendIntent(BusStopsIntent.Navigation.GoToDetailsOfBusStop(busStop.id))
        }
        binding.rvBusStops.adapter = adapterBusStops
        binding.rvBusStops.layoutManager = LinearLayoutManager(requireContext())

        binding.svSearchBus.setOnQueryTextListener(
            DelayedQueryTextListener(
                this@BusStopsFragment.lifecycle,
                onDefaultQueryTextSubmit = {
                    sendIntent(BusStopsIntent.RefreshBusStops(query = it, coordinates = lastFilter))
                },
                onDelayedQueryTextChange = {
                    if (it.isNullOrEmpty()) {
                        sendIntent(
                            BusStopsIntent.RefreshBusStops(
                                query = null,
                                coordinates = lastFilter
                            )
                        )
                    } else {
                        sendIntent(
                            BusStopsIntent.RefreshBusStops(
                                query = it,
                                coordinates = lastFilter
                            )
                        )
                    }
                })
        )

        binding.cbFilterGps.setOnCheckedChangeListener { compoundButton, isFilteredByGps ->
            if (compoundButton.isPressed) {
                if (isFilteredByGps) {
                    handleLocation(isFilteredByGps)
                } else {
                    sendIntent(
                        BusStopsIntent.RefreshBusStops(
                            coordinates = null,
                            query = lastSearch
                        )
                    )
                }
            }
        }

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

    private fun handleLocation(isFilteredByGps: Boolean) {
        permissionManager
            .request(Permission.Location)
            .explicationMessage(getString(R.string.location_permission_needed))
            .checkPermission { result: Boolean ->
                if (result) {
                    //Location permission OK, we ask for location availability
                    fusedLocationClient.locationAvailability.addOnSuccessListener { locationAvailability ->
                        if (!locationAvailability.isLocationAvailable) {
                            //Location not available, we reroute the user to the settings
                            binding.cbFilterGps.isChecked = !isFilteredByGps
                            val intent =
                                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivity(intent)
                        } else {
                            //Location available we ask for the last known location could be null if no app prepare it
                            //We could prepare it ourself but we won't to stay simple for now
                            //One way to prepare it is to launch Google Map
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                if (location != null) {
                                    sendIntent(
                                        BusStopsIntent.RefreshBusStops(
                                            coordinates = location,
                                            query = lastSearch
                                        )
                                    )
                                } else {
                                    binding.cbFilterGps.isChecked = !isFilteredByGps
                                }
                            }
                        }
                    }
                } else {
                    binding.cbFilterGps.isChecked = !isFilteredByGps
                }
            }
    }

    override fun render(state: BusStopsState) {
        when (state) {
            BusStopsState.Loading -> showProgress(true)
            is BusStopsState.ShowBusStops -> initView(state)
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
                        navigation.busStopId
                    )
                )
            }
        }
    }

    private fun initView(state: BusStopsState.ShowBusStops) {
        showProgress(false)
        this.lastFilter = state.lastFilter
        this.lastSearch = state.lastSearch

        adapterBusStops.submitList(state.busStopsList)
    }

    private fun showProgress(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                vLoading.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
            } else {
                vLoading.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showErrorMessage(message: Int) {
        showProgress(false)
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction(R.string.retry) {
                sendIntent(BusStopsIntent.RefreshBusStops(lastSearch, lastFilter))
            }.show()
    }
}