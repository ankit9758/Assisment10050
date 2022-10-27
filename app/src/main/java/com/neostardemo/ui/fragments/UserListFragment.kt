package com.neostardemo.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.neostardemo.R
import com.neostardemo.databinding.FragmentUserListBinding
import com.neostardemo.models.User
import com.neostardemo.ui.adapter.UserListAdapter
import com.neostardemo.utils.hide
import com.neostardemo.utils.show
import com.neostardemo.utils.showUserInfoDialog
import com.neostardemo.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : Fragment(), UserListAdapter.UserInteraction {
    private lateinit var userListAdapter: UserListAdapter
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentUserListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
        clickEvents()

    }

    private fun clickEvents() {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_userListFragment_to_registerFragment)
        }
    }


    //initialize the adapter and attach with recyclerview
    private fun setupViews() {
        userListAdapter = UserListAdapter(this)
        binding.rvUser.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayout.VERTICAL))
            adapter = userListAdapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.userList.observe(viewLifecycleOwner) {
                showNoDataFound(it)
                userListAdapter.setUserList(it)
            }
        }
    }

    private fun showNoDataFound(itemList: List<User>) {
        with(binding) {
            if (itemList.isEmpty()) {
                tvNoData.show()
                rvUser.hide()
            } else {
                tvNoData.hide()
                rvUser.show()
            }
        }

    }

    override fun onUserClicked(user: User) {
        requireContext().showUserInfoDialog(user)
    }
}