package com.voltaire.rickandmortyapi.ui.characters


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.voltaire.rickandmortyapi.R
import com.voltaire.rickandmortyapi.adapters.CharactersAdapter
import com.voltaire.rickandmortyapi.api.RetrofitService
import com.voltaire.rickandmortyapi.databinding.FragmentCharactersBinding
import com.voltaire.rickandmortyapi.repositories.CharactersRepository
import com.voltaire.rickandmortyapi.ui.characters.viewmodel.CharactersViewModel
import com.voltaire.rickandmortyapi.ui.characters.viewmodel.CharactersViewModelFactory
import com.voltaire.rickandmortyapi.ui.details.CharactersDetailsFragmentArgs
import retrofit2.Call
import retrofit2.Response


class CharactersFragment : Fragment() {

    private val viewModel: CharactersViewModel by activityViewModels {
        CharactersViewModelFactory(
            CharactersRepository(RetrofitService.getInstance())
        )
    }

    private lateinit var binding: FragmentCharactersBinding
    private lateinit var adapter: CharactersAdapter
    private lateinit var recyclerView: RecyclerView

    private var filter_status: String = ""
    private var filter_gender: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharactersBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.listCharacters.observe(viewLifecycleOwner) {
            adapter.setCharacters(it)
        }

        recyclerView = binding.charactersRecyclerView
        adapter = CharactersAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.btnSearch.setOnClickListener {

            val view = View.inflate(requireContext(), R.layout.dialog_filter, null)
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            view.findViewById<Button>(R.id.applyfilter).setOnClickListener {

                val liveStatus = view.findViewById<RadioButton>(R.id.radio_live).isChecked
                val deadStatus = view.findViewById<RadioButton>(R.id.radio_dead).isChecked
                val unknownStatus =
                    view.findViewById<RadioButton>(R.id.radio_status_unknown).isChecked
                val maleGender = view.findViewById<RadioButton>(R.id.radio_male).isChecked
                val femaleGender = view.findViewById<RadioButton>(R.id.radio_female).isChecked
                val unknownGender =
                    view.findViewById<RadioButton>(R.id.radio_gender_unknown).isChecked

                val radio_group_gender =
                    view.findViewById<RadioGroup>(R.id.radio_group_gender).checkedRadioButtonId
                val radio_group_status =
                    view.findViewById<RadioGroup>(R.id.radio_group_status).checkedRadioButtonId

                if (radio_group_gender != -1) {
                    if (maleGender) {
                        filter_gender = "male"
                    }
                    filter_gender = if (femaleGender) {
                        "female"
                    } else {
                        "unknown"
                    }
                }
                if (radio_group_status != -1) {
                    if (liveStatus) {
                        filter_status = "live"
                    }

                    if (deadStatus) {
                        filter_status = "dead"
                    } else {
                        filter_status = "unknown"
                    }
                }

                if (filter_gender != "-1" && filter_status != "-1") {
                    viewModel.getFilterCharacterStatusGender(filter_status, filter_gender, 1)
                    dialog.dismiss()

                } else if (filter_status != "-1") {
                    viewModel.getCharacterStatus(filter_status, 1)
                    dialog.dismiss()
                } else {
                    viewModel.getCharacterGender(filter_gender, 1)
                    dialog.dismiss()
                }
                dialog.dismiss()
                if (filter_gender != "" || filter_status != "") {
                    binding.txtClearFilters.visibility = View.VISIBLE
                }
            }
        }

        getName()

        binding.txtClearFilters.setOnClickListener {
            viewModel.getCharacters(1, 2)
            filter_status = ""
            filter_gender = ""
        }


    }

    private fun getName() {

        binding.searchCharacters.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getCharacterName(query.toString())


                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.getCharacters(1, 2)
    }
}



