package com.pranjaldesai.coronavirustracker.ui

import android.text.method.ScrollingMovementMethod
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.data.models.CovidStats
import com.pranjaldesai.coronavirustracker.databinding.FragmentOneBinding
import com.pranjaldesai.coronavirustracker.extension.LogExt.loge
import com.pranjaldesai.coronavirustracker.ui.shared.CoreFragment
import com.pranjaldesai.coronavirustracker.ui.shared.IPrimaryFragment

class FragmentOne : CoreFragment<FragmentOneBinding>(), IPrimaryFragment {
    override val layoutResourceId: Int = R.layout.fragment_one

    private val bottomNavOptionId: Int = R.id.fragmentOne
    private val databaseRef = FirebaseDatabase.getInstance().reference

    override fun bindData() {
        super.bindData()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue(CovidStats::class.java)
                binding.text.text = post.toString()
                binding.text.movementMethod = ScrollingMovementMethod()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                loge(databaseError.toException())
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