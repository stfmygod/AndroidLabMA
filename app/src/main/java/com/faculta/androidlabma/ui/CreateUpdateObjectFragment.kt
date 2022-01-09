package com.faculta.androidlabma.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.faculta.androidlabma.R
import com.faculta.androidlabma.data.db.AppDatabase
import com.faculta.androidlabma.data.db.model.MovieDB
import com.faculta.androidlabma.data.models.Movie
import com.faculta.androidlabma.databinding.FragmentCreateUpdateObjectBinding
import com.faculta.androidlabma.helpers.showToast
import com.faculta.androidlabma.ui.viewmodel.CreateUpdateObjectViewModel
import java.util.*

class CreateUpdateObjectFragment: Fragment() {
    private lateinit var binding: FragmentCreateUpdateObjectBinding
    private val args:  CreateUpdateObjectFragmentArgs by navArgs()

    private val viewModel = CreateUpdateObjectViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateUpdateObjectBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.setDb((requireActivity() as MainActivity).getDb())

        binding.saveButton.setOnClickListener {
            val nameString = binding.nameInputTIET.text.toString()
            val rating = binding.ratingTIET.text.toString().toInt()
            val isSeenAtCinema = binding.seenAtCinemaCheckBox.isChecked
            val photoPath = binding.photoPathTIET.text.toString()
            if (args.isCreate) {
                viewModel.createObject((requireActivity() as MainActivity).getToken()?: "", MovieDB(name = nameString, date = Date(System.currentTimeMillis()), rating = rating, isSeenAtCinema = isSeenAtCinema, photoPath = photoPath))
            } else {
                viewModel.updateObject((requireActivity() as MainActivity).getToken()?: "", MovieDB(id = args.objectValue?.id?: 0, _id = args.objectValue?._id?: "", name = nameString, date = Date(System.currentTimeMillis()), rating = rating, isSeenAtCinema = isSeenAtCinema, photoPath = photoPath), args.objectValue?._id?: "")
            }
        }


        if (!args.isCreate) {
            val movie = args.objectValue?: MovieDB()
            binding.nameInputTIET.setText(movie.name?: "")
            binding.ratingTIET.setText(movie.rating?.toString()?: "")
            binding.seenAtCinemaCheckBox.isChecked = movie.isSeenAtCinema?: false
            binding.photoPathTIET.setText(movie.photoPath?: "")
            Glide.with(requireContext())
                .load(movie.photoPath.toString())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .fallback(R.drawable.image_placeholder)
                .into(binding.photoView)


            binding.deleteButton.visibility = View.VISIBLE
            binding.deleteButton.setOnClickListener {
                viewModel.deleteMovie((requireActivity() as MainActivity).getToken()?: "", movie._id?: "")
            }
        } else {
            binding.deleteButton.visibility = View.GONE
        }

        binding.photoPathTIET.doAfterTextChanged {
            Glide.with(requireContext())
                .load(it.toString())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .fallback(R.drawable.image_placeholder)
                .into(binding.photoView)
        }

        viewModel.canNavigateToNextDestination.observe(viewLifecycleOwner) {
            requireActivity().onBackPressed()
        }

        viewModel.preNavigationMessage.observe(viewLifecycleOwner) {
            requireContext().showToast(it)
        }

        viewModel.errorMessageLiveData.observe(viewLifecycleOwner) {
            requireContext().showToast(it)
        }
    }
}