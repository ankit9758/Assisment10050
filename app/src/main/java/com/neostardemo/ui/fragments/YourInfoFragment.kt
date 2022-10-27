package com.neostardemo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.neostardemo.R
import com.neostardemo.databinding.FragmentYourInfoBinding
import com.neostardemo.utils.educationDataList
import com.neostardemo.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class YourInfoFragment : Fragment() {
    private var _binding: FragmentYourInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentYourInfoBinding.inflate(inflater, container, false)
        binding.mainViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
        clickEvents()

    }

    private fun clickEvents() {
        binding.toolbar.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun setupViews() {
        with(binding) {
            //setting the title
            toolbar.tvTitle.text = getString(R.string.info)

            //add the adapter and attach
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, educationDataList)
            actEducation.setAdapter(adapter)
            //selecting the item
            actEducation.setOnItemClickListener { adapterView, view, i, l ->
                viewModel.qualification.set(educationDataList[i])
            }
        }


    }

    private fun setupObservers() {
        viewModel.errorChannel.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.asString(requireContext()), Toast.LENGTH_SHORT)
                .show()
        }

    }

}