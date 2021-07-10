package com.ilaftalkful.mobileonthego.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.databinding.DashboardFragmentBinding
import com.ilaftalkful.mobileonthego.utilities.Constants
import com.ilaftalkful.mobileonthego.viewmodel.DashboardViewModel


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DashBoardFragment : IlafBaseFragment() {

    val viewModel: DashboardViewModel by viewModels()
    lateinit var   dashboardBinding :DashboardFragmentBinding
     var screenId:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments!=null){
            screenId = arguments?.getInt(Constants.SCREEN_MYLOGS)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       dashboardBinding = DataBindingUtil.inflate<DashboardFragmentBinding>(
           inflater,
           R.layout.dashboard_fragment,
           container,
           false
       )
        dashboardBinding.viewModel=viewModel
        dashboardBinding.lifecycleOwner=this
        return dashboardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val radius = resources.getDimension(R.dimen.radius_small)
        val bottomNavigationViewBackground = dashboardBinding.bottomNavigation.background as MaterialShapeDrawable
        bottomNavigationViewBackground.shapeAppearanceModel =
            bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build()
        dashboardBinding?.bottomNavigation.setItemIconTintList(null);

        if(screenId!=null){
            if(screenId == Constants.MYLOGS){
                dashboardBinding.bottomNavigation.selectedItemId=R.id.mylogs_fragment
            }
        }
      val  navController:NavController = Navigation.findNavController(view.findViewById(R.id.home_fragment_nav_host))
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.products_fragment -> {
                }
            }
        }
        NavigationUI.setupWithNavController(dashboardBinding.bottomNavigation, navController)
    }


   open fun showHome() {
        dashboardBinding.bottomNavigation.selectedItemId=R.id.products_fragment
    }

    open fun showProfile() {
        dashboardBinding.bottomNavigation.selectedItemId=R.id.profile_fragment
    }


}