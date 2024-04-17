package uz.uzkassa.smartpos.core.presentation.utils.app

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun DialogFragment.show(fragmentManager: FragmentManager) =
    show(fragmentManager, this::class.java.name)

fun DialogFragment.showNow(fragmentManager: FragmentManager) =
    showNow(fragmentManager, this::class.java.name)

fun DialogFragment.show(transaction: FragmentTransaction) =
    show(transaction, this::class.java.name)