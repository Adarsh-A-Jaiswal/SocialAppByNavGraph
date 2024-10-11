package com.example.socialappbynavgraph

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.socialappbynavgraph.apiService.Users
import com.example.socialappbynavgraph.ui.main.ListViewModel
import com.google.android.material.textfield.TextInputLayout

@Suppress("DEPRECATION")
class EditProfileFragment : Fragment() {

    private val go: EditProfileFragmentArgs by navArgs()
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z-]+\\.+[a-z]+"
    private var nameTextField: TextInputLayout? = null
    private var emailTextField: TextInputLayout? = null
    lateinit var viewModel: ListViewModel
    private var mail: EditText? = null
    private var name: EditText? = null
    private var gender: Spinner? = null
    private var status: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
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
        viewModel = ViewModelProvider(this)[ListViewModel::class.java]

        val back = view.findViewById<ImageView>(R.id.back)
        val title=view.findViewById<TextView>(R.id.tool_text)
        title.text="  Edit Detail"
        nameTextField = view.findViewById(R.id.edit_nameTextInputLayout)
        emailTextField = view.findViewById(R.id.edit_emailTextInputLayout)
        name = view.findViewById(R.id.ete_name)
        mail = view.findViewById(R.id.ete_Email)
        name?.setText(go.usersDetails.name)
        mail?.setText(go.usersDetails.email)

        gender = view.findViewById(R.id.spe_gender)
        val genderList: Array<String> = view.resources.getStringArray((R.array.Gender))
        val position = genderList.indexOf(go.usersDetails.gender)
        gender?.setSelection(position)

        status = view.findViewById(R.id.spe_status)
        val statusList: Array<String> = view.resources.getStringArray((R.array.Status))
        val statusIndex = statusList.indexOf(go.usersDetails.status)
        status?.setSelection(statusIndex)

        back.setOnClickListener {
            backDialog().show()
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith("inflater.inflate(R.menu.save_menu, menu)"))
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_save -> {
                if (name?.text!!.isNotEmpty() && mail?.text!!.isNotEmpty()) {
                    if (mail?.text.toString().matches(emailPattern.toRegex())) {

                        emailTextField?.let { setErrorTextField(false, it) }
                        nameTextField?.let { setErrorTextField(false, it) }
                        viewModel.putData(
                            go.usersDetails.id,
                            Users(
                                go.usersDetails.id.toString().toInt(),
                                name?.text.toString(), mail?.text.toString(),
                                gender?.selectedItem as String,
                                status?.selectedItem as String
                            )
                        )
                        viewModel.putData.observe(viewLifecycleOwner) {
                            if (it.isSuccessful) {
                                val action: NavDirections = EditProfileFragmentDirections
                                    .actionEditProfileFragmentToDetailsFragment(go.usersDetails)
                                findNavController().navigate(action)
                                Toast.makeText(
                                    requireContext(),
                                    "Profile is Updated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        setErrorTextField(true, emailTextField!!)
                        emailTextField?.error = "Invalid Email"
                    }
                } else if (name?.text!!.isEmpty()) {
                    nameTextField?.let { setErrorTextField(true, it) }
                    emailTextField?.let { setErrorTextField(false, it) }
                } else if (mail?.text!!.isEmpty()) {
                    emailTextField?.let { setErrorTextField(true, it) }
                    nameTextField?.let { setErrorTextField(false, it) }
                }
            }
        }
        return true
    }

    private fun setErrorTextField(error: Boolean, emptyTextInputLayout: TextInputLayout) {
        if (error) {
            emptyTextInputLayout.isErrorEnabled = true
            emptyTextInputLayout.error = "Field Can't be empty"
        } else {
            emptyTextInputLayout.isErrorEnabled = false
            emptyTextInputLayout.error = null
        }
    }
    private fun backDialog() =
        AlertDialog.Builder(requireContext())
            .setTitle("Exit")
            .setMessage("Do you want to go back?")
            .setPositiveButton("YES") { _, _ ->
                activity?.onBackPressed()
            }
            .setNegativeButton("NO") { _, _ ->
            }.create()

}