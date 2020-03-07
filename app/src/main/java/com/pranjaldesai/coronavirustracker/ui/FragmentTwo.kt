package com.pranjaldesai.coronavirustracker.ui

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.data.models.CovidStats
import com.pranjaldesai.coronavirustracker.databinding.FragmentTwoBinding
import com.pranjaldesai.coronavirustracker.extension.LogExt
import com.pranjaldesai.coronavirustracker.ui.shared.CoreFragment
import com.pranjaldesai.coronavirustracker.ui.shared.IPrimaryFragment

class FragmentTwo : CoreFragment<FragmentTwoBinding>(), IPrimaryFragment {
    override val layoutResourceId: Int = R.layout.fragment_two

    private val bottomNavOptionId: Int = R.id.fragmentTwo
    private val databaseRef = FirebaseDatabase.getInstance().reference

    override fun bindData() {
        super.bindData()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue(CovidStats::class.java)
                binding.text.text = post.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                LogExt.loge(databaseError.toException())
            }
        }
        databaseRef.addValueEventListener(postListener)
    }

    override fun onResume() {
        super.onResume()
        subscribeToNavigationHost()
        updateBottomNavigationSelection(bottomNavOptionId)
    }

    override fun onPause() {
        super.onPause()
        unsubscribeFromNavigationHost()
    }
}