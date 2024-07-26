package com.fatihden.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fatihden.myapplication.databinding.FragmentListeBinding


class ListeFragment : Fragment() {

    private var _binding : FragmentListeBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        /*return inflater.inflate(R.layout.fragment_liste, container, false)*/

        _binding = FragmentListeBinding.inflate(inflater,container,false)


        val view = binding.root
        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}