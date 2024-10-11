package com.example.socialappbynavgraph.ui.main

import android.content.ContentValues.TAG
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.socialappbynavgraph.DetailsFragment
import com.example.socialappbynavgraph.R
import com.example.socialappbynavgraph.adapter.ListAdapter

@Suppress("DEPRECATION")
class UserFragment : Fragment() {

    private var progressBar: ProgressBar? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_users, container, false)
        recyclerView = view.findViewById(R.id.users_recycle_view)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.detail_toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        toolbar.title = ""
        (activity as AppCompatActivity).supportActionBar?.apply {
            title=""
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipe)
//        swipe.setProgressBackgroundColorSchemeColor(
//            resources.getColor(
//                R.color.gray,
//                resources.newTheme()
//            )
//        )
//        swipe.setColorSchemeResources(R.color.white)

        viewModel = ViewModelProvider(this)[ListViewModel::class.java]
        progressBar = view.findViewById(R.id.progress_bar)
        progressBar?.isVisible = true
        viewModel.getDetails()
        viewModel.listData.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
//                swipe.setOnRefreshListener {
//                    viewModel.getDetails()
//                    if (list.isNotEmpty()) {
//                        swipe.isRefreshing = false
//                    }
               // }
                progressBar?.isVisible = false
                val adapter = ListAdapter(list){
                    val action:NavDirections = UserFragmentDirections.actionUserFragmentToDetailsFragment(it)
                    findNavController().navigate(action)
                }
                recyclerView?.adapter = adapter
            }else{
                Log.e(TAG, "list is Empty")
            }
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onCreateOptionsMenu(menu, inflater)",
        "androidx.fragment.app.Fragment"
    )
    )
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_user_menu,menu)
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onOptionsItemSelected(item)",
        "androidx.fragment.app.Fragment"
    )
    )
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_add -> {
                val action:NavDirections=UserFragmentDirections.actionUserFragmentToAddProfileFragment()
                findNavController().navigate(action)
            }
        }
        return true
    }
}