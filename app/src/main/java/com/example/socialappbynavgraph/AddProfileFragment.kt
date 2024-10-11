package com.example.socialappbynavgraph

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.socialappbynavgraph.apiService.Users
import com.example.socialappbynavgraph.ui.main.ListViewModel
import com.google.android.material.textfield.TextInputLayout

@Suppress("DEPRECATION")
class AddProfileFragment : Fragment() {

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var nameTextField: TextInputLayout? = null
    private var emailTextField: TextInputLayout? = null
    private var progressBar: ProgressBar? = null
    private lateinit var viewModel: ListViewModel
    private var gen: String = ""
    private var stat: String = ""
    private var name: EditText? = null
    private var email: EditText? = null

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
        val view = inflater.inflate(R.layout.fragment_add_profile, container, false)
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.detail_toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        toolbar.title = ""
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ListViewModel::class.java]
        val back = view.findViewById<ImageView>(R.id.back)
        val title=view.findViewById<TextView>(R.id.tool_text)
        title.text="  Add User"
        name = view.findViewById(R.id.et_name)
        email = view.findViewById(R.id.et_Email)
        nameTextField = view.findViewById(R.id.nameTextInputLayout)
        emailTextField = view.findViewById(R.id.emailTextInputLayout)
        val gender: Spinner = view.findViewById(R.id.sp_gender)
        val status: Spinner = view.findViewById(R.id.sp_status)
        val good: kotlin.Array<String> = view.resources.getStringArray((R.array.Status))

        status.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                stat = good[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val genderList: Array<String> = view.resources.getStringArray((R.array.Gender))
        gender.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                gen = genderList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onCreateOptionsMenu(menu, inflater)",
            "androidx.fragment.app.Fragment"
        )
    )
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu,menu)
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onOptionsItemSelected(item)",
            "androidx.fragment.app.Fragment"
        )
    )
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_save -> {
                if (name?.text!!.isNotEmpty() && email?.text!!.isNotEmpty()) {
                    if (email?.text.toString().matches(emailPattern.toRegex())) {
                        emailTextField?.let { setErrorTextField(false, it) }
                        nameTextField?.let { setErrorTextField(false, it) }

                        progressBar = view?.findViewById(R.id.progressBar)
                        progressBar?.isVisible = true
                        viewModel.postData(
                            Users(
                                102,
                                name?.text.toString(),
                                email?.text.toString(),
                                gen,
                                stat
                            )
                        )
                        viewModel.postData.observe(viewLifecycleOwner) {
                            if (it.isSuccessful) {
                                progressBar?.isVisible = false
                                Log.i(
                                    "TAG",
                                    "Post person clicked name: ${name?.text.toString()} & ${email?.text.toString()} ,$gen ,$stat"
                                )
                                val action:NavDirections=AddProfileFragmentDirections.actionAddProfileFragmentToUserFragment()
                                findNavController().navigate(action)
                                Toast.makeText(
                                    requireContext(),
                                    "Profile Added Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Profile is not Added",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    } else {
                        setErrorTextField(true, emailTextField!!)
                        emailTextField?.error = "Invalid Email"
                    }
                } else if (name?.text!!.isEmpty()) {
                    nameTextField?.let { setErrorTextField(true, it) }
                    emailTextField?.let { setErrorTextField(false, it) }
                } else if (email?.text!!.isEmpty()) {
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
}