package com.example.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class FragmentMypage : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mapage, container, false)

        val personalButton: Button = view.findViewById(R.id.personal_button)
        val myWritingButton: Button = view.findViewById(R.id.mywriting_button)
        val myReviewButton: Button = view.findViewById(R.id.myreview_button)
        val myFestivalButton: Button = view.findViewById(R.id.myfestival_button)

        val personalView: LinearLayout = view.findViewById(R.id.personal_view)
        val myWritingView: LinearLayout = view.findViewById(R.id.mywriting_view)
        val myReviewView: LinearLayout = view.findViewById(R.id.myreview_view)
        val myFestivalView: LinearLayout = view.findViewById(R.id.myfestival_view)

        personalButton.setOnClickListener {
            personalView.visibility = View.VISIBLE
            myWritingView.visibility = View.GONE
            myReviewView.visibility = View.GONE
            myFestivalView.visibility = View.GONE
        }

        myWritingButton.setOnClickListener {
            personalView.visibility = View.GONE
            myWritingView.visibility = View.VISIBLE
            myReviewView.visibility = View.GONE
            myFestivalView.visibility = View.GONE
        }

        myReviewButton.setOnClickListener {
            personalView.visibility = View.GONE
            myWritingView.visibility = View.GONE
            myReviewView.visibility = View.VISIBLE
            myFestivalView.visibility = View.GONE
        }

        myFestivalButton.setOnClickListener {
            personalView.visibility = View.GONE
            myWritingView.visibility = View.GONE
            myReviewView.visibility = View.GONE
            myFestivalView.visibility = View.VISIBLE
        }

        return view
    }
}
