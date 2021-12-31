package fr.tuttifruty.blablacarbus.ui.busstops

import android.content.Intent
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.snackbar.Snackbar
import com.lriccardo.timelineview.TimelineDecorator
import fr.tuttifruty.blablacarbus.R
import fr.tuttifruty.blablacarbus.common.DelayedQueryTextListener
import fr.tuttifruty.blablacarbus.common.Permission
import fr.tuttifruty.blablacarbus.common.PermissionManager
import fr.tuttifruty.blablacarbus.common.mvi.IView
import fr.tuttifruty.blablacarbus.common.view.CustomDividerItemDecoration
import fr.tuttifruty.blablacarbus.databinding.FragmentBusStopsBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class BusStopsFragment : Fragment(), IView<BusStopsState, BusStopsIntent, BusStopsNavigation> {

    private val viewModel: BusStopsViewModel by viewModel()
    private val fusedLocationClient: FusedLocationProviderClient by inject()
    private val permissionManager = PermissionManager.from(this)

    private lateinit var binding: FragmentBusStopsBinding
    private lateinit var adapterBusStops: BusStopsAdapter

    private var lastSearch: String? = null
    private var lastFilter: Location? = null

    private var searchView: SearchView? = null
    private var filterView: MaterialCheckBox? = null


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bottom_navigation_menu, menu)

        initSearch(menu)

        initFilter(menu)

        super.onCreateOptionsMenu(menu, inflater)

    }

    private fun initSearch(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        if (lastSearch != null && lastSearch?.isNotBlank() == true) {
            searchItem.expandActionView()
            searchView?.setQuery(lastSearch, true)
        }

        searchView?.setOnQueryTextListener(
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
    }


    private fun initFilter(menu: Menu) {
        val filterItem = menu.findItem(R.id.filterByGps)
        filterView = filterItem.actionView as MaterialCheckBox
        filterView?.setButtonDrawable(R.drawable.checkbox_selector)
        filterView?.isChecked = lastFilter != null

        filterView?.setOnCheckedChangeListener { compoundButton, isFilteredByGps ->
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val colorPrimary = TypedValue()
        val theme: Resources.Theme? = activity?.theme
        theme?.resolveAttribute(R.attr.colorPrimary, colorPrimary, true)

        binding = FragmentBusStopsBinding.inflate(inflater, container, false).apply {
            adapterBusStops = BusStopsAdapter(viewModel) { busStop ->
                sendIntent(BusStopsIntent.Navigation.GoToDetailsOfBusStop(busStop.id))
            }
            rvBusStops.apply {
                adapter = adapterBusStops
                //holdersAdapter = ShimmerPlaceHolderAdapter()
                addItemDecoration(TimelineDecorator(
                    indicatorSize = 16f,
                    lineWidth = 8f,
                    padding = 16f,
                    position = TimelineDecorator.Position.Left,
                    indicatorColor = colorPrimary.data,
                    lineColor = colorPrimary.data
                ))
                //holdersItemDecoration = CustomDividerItemDecoration(requireContext(), R.drawable.divider)
            }

            setHasOptionsMenu(true)
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
                            filterView?.isChecked = !isFilteredByGps
                            val intent =
                                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivity(intent)
                        } else {
                            //Location available we ask for the last known location could be null if no app prepare it
                            //We could prepare it ourself but we won't to stay simple for now
                            //One way to prepare it is to launch Google Map
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                sendIntent(
                                    BusStopsIntent.RefreshBusStops(
                                        coordinates = location,
                                        query = lastSearch
                                    )
                                )
                            }
                        }
                    }
                } else {
                    filterView?.isChecked = !isFilteredByGps
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
        adapterBusStops.submitList(state.busStopsList)
        this.lastFilter = state.lastFilter
        this.lastSearch = state.lastSearch
        showProgress(false)
    }

    private fun showProgress(isLoading: Boolean) {
        binding.apply {
            //rvBusStops.toggleHoldersAdapter(isLoading)
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
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction(R.string.retry) {
                sendIntent(BusStopsIntent.RefreshBusStops(lastSearch, lastFilter))
            }.show()
    }
}