package com.example.socialappbynavgraph

import android.app.AlertDialog
import android.icu.text.Normalizer.NO
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.socialappbynavgraph.ui.main.ListViewModel

@Suppress("DEPRECATION")
class DetailsFragment : Fragment() {

    private val args by navArgs<DetailsFragmentArgs>()

    private lateinit var viewModel: ListViewModel
    private var mail: TextView? = null
    private var name: TextView? = null
    private var id: TextView? = null
    private var gender: TextView? = null
    private var status: TextView? = null
    // private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.detail_toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        toolbar.title = ""
        (activity as AppCompatActivity).supportActionBar?.apply {
            title=""
        }
        name = view.findViewById(R.id.tvd_name)
        mail = view.findViewById(R.id.tvd_mail)
        id = view.findViewById(R.id.tvd_id)
        gender = view.findViewById(R.id.tvd_gender)
        status = view.findViewById(R.id.tvd_status)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ListViewModel::class.java]

        val back = view.findViewById<ImageView>(R.id.back)
        name?.text = args.user.name
        mail?.text = args.user.email
        id?.text = args.user.id.toString()
        gender?.text = args.user.gender
        status?.text = args.user.status

        back.setOnClickListener {
            activity?.onBackPressed()
        }
    }


    @Deprecated("Deprecated in Java", ReplaceWith("inflater.inflate(R.menu.detail_menu, menu)"))
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onOptionsItemSelected(item)",
            "androidx.fragment.app.Fragment"
        )
    )
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mid_edit -> {
                val action: NavDirections = DetailsFragmentDirections
                    .actionDetailsFragmentToEditProfileFragment(usersDetails = args.user)
                findNavController().navigate(action)
            }
            R.id.mid_delete -> {
                deleteDialog().show()
            }
        }
        return true
    }

    private fun deleteDialog() =
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Profile")
            .setMessage("Do you want to delete this profile?")
            .setIcon(R.drawable.ic_baseline_delete_24)
            .setPositiveButton("YES") { _, _ ->
                viewModel.deleteProfile(args.user.id)
                viewModel.deleteResponse.observe(viewLifecycleOwner) {
                    if ((it > 199) && (it < 300)) {
                        val action: NavDirections =
                            DetailsFragmentDirections.actionDetailsFragmentToUserFragment()
                        findNavController().navigate(action)
                        Toast.makeText(
                            requireContext(),
                            "${args.user.name} is deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(requireContext(), "$it is the response ", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            .setNegativeButton("NO") { _, _ ->
            }.create()
}