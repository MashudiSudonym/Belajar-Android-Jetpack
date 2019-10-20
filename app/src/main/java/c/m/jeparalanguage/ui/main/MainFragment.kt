package c.m.jeparalanguage.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import c.m.jeparalanguage.R
import c.m.jeparalanguage.ui.detail.DetailFragment
import c.m.jeparalanguage.ui.setting.SettingsActivity
import c.m.jeparalanguage.util.gone
import c.m.jeparalanguage.util.toast
import c.m.jeparalanguage.util.visible
import c.m.jeparalanguage.vo.Status
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var mainAdapter: MainAdapter
    private val detailFragment = DetailFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar_main)
        setHasOptionsMenu(true)

        // Setup Main RecyclerView
        setupRecyclerView()

        // Setup Searching RecyclerView
        searchContent()

        // Refresh Data with SwipeRefresh
        swipe_refresh_main.setOnRefreshListener {
            swipe_refresh_main.isRefreshing = false
            observeMainDataViewModel()
        }
    }

    private fun searchContent() {
        // unfocused search view state
        if (!sv_main.isFocused) searchNotFocus()

        // search view main listener
        sv_main.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(searchKeyword: String?): Boolean {
                when (sv_main.isFocused) {
                    true -> searchNotFocus()
                    false -> {
                        mainViewModel.getSearchKeyword(searchKeyword.toString())
                        mainViewModel.searchContent.observe(this@MainFragment, Observer {
                            if (it != null) {
                                when (it.status) {
                                    Status.LOADING -> {
                                        // View Status
                                        shimmerStart()
                                        rv_main.gone()
                                        rv_search.gone()
                                        tv_no_data.gone()
                                    }
                                    Status.SUCCESS -> {
                                        // View Status
                                        shimmerStop()
                                        rv_main.gone()
                                        rv_search.visible()
                                        tv_no_data.gone()
                                        swipe_refresh_main.isRefreshing = false

                                        // Submit data to adapter
                                        mainAdapter.submitList(it.data)

                                        // If data from Repository is Null or Empty Array show "No Data" Page
                                        if (it.data.isNullOrEmpty()) {
                                            // View Status
                                            rv_main.gone()
                                            rv_search.gone()
                                            tv_no_data.visible()
                                            toast(activity as Activity, getString(R.string.no_data))
                                        }
                                    }
                                    Status.ERROR -> {
                                        // View Status
                                        shimmerStop()
                                        rv_main.visible()
                                        rv_search.gone()
                                        tv_no_data.gone()
                                    }
                                }
                            }
                        })
                    }
                }

                return true
            }
        })
    }

    private fun setupRecyclerView() {
        // Adapter Main RecyclerView
        mainAdapter = MainAdapter {
            DetailFragment.newInstance(it).show(childFragmentManager, detailFragment.tag)
        }

        // Observe main data from ViewModel
        observeMainDataViewModel()

        // Initiate Main RecyclerView
        rv_main.setHasFixedSize(true)
        rv_main.adapter = mainAdapter

        // Initiate Searching RecyclerView
        rv_search.setHasFixedSize(true)
        rv_search.adapter = mainAdapter
    }

    private fun observeMainDataViewModel() {
        mainViewModel.getContent().observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        // View Status
                        shimmerStart()
                        rv_main.gone()
                        rv_search.gone()
                        tv_no_data.gone()
                    }
                    Status.SUCCESS -> {
                        // View Status
                        shimmerStop()
                        rv_main.visible()
                        rv_search.gone()
                        tv_no_data.gone()
                        swipe_refresh_main.isRefreshing = false

                        // Submit data to adapter
                        mainAdapter.submitList(it.data)

                        // If data from Repository is Null or Empty Array show "No Data" Page
                        if (it.data.isNullOrEmpty()) {
                            // View Status
                            rv_main.gone()
                            rv_search.gone()
                            tv_no_data.visible()
                            toast(activity as Activity, getString(R.string.no_data))
                        }
                    }
                    Status.ERROR -> {
                        // View Status
                        shimmerStop()
                        rv_main.gone()
                        rv_search.gone()
                        tv_no_data.visible()
                    }
                }
            }
        })
    }

    // this function for if search view not active show main recycler view
    private fun searchNotFocus() {
        shimmerStop()
        rv_main.visible()
        rv_search.gone()
        tv_no_data.gone()
    }

    // shimmer loading animation start
    private fun shimmerStart() {
        shimmer_frame.visible()
        shimmer_frame.startShimmer()
    }

    // shimmer loading animation stop
    private fun shimmerStop() {
        shimmer_frame.gone()
        shimmer_frame.stopShimmer()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> startActivity(Intent(activity, SettingsActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

}
