package com.voltaire.rickandmortyapi.ui.details


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.voltaire.rickandmortyapi.R
import com.voltaire.rickandmortyapi.databinding.CharacterDetailFragmentBinding
import com.voltaire.rickandmortyapi.ui.characters.CharactersFragmentDirections

private lateinit var binding: CharacterDetailFragmentBinding
var filterOn : Boolean = false

class CharactersDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CharacterDetailFragmentBinding.inflate(inflater)
        return binding.root
    }

    private val args : CharactersDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val character = args.character
        val filterGender = args.filterGender
        val filterStatus = args.filterStatus

        Glide
            .with(requireContext())
            .load(character.image)
            .into(binding.detailImageCharacter)

        setInfo(binding.detailName, R.string.characterName, character.name)
        setInfo(binding.detailId, R.string.cpf, character.id.toString())
        setInfo(binding.detailSex, R.string.sexo, character.gender)
        setInfo(binding.detailSpecie, R.string.specie, character.species)
        setInfo(binding.detailStatus, R.string.status, character.status)
        setInfo(binding.detailEpisodes, R.string.episodes, character.episode.size.toString())
        setInfo(binding.detailLocalization, R.string.localization, character.location.name)

        binding.backCharatersFragment.setOnClickListener {
             findNavController().popBackStack(R.id.characters, false)
            }
    }
    private fun setInfo (textview : TextView, subsString : Int, newString : String) {
        textview.text = getString(subsString,newString)
    }
}
