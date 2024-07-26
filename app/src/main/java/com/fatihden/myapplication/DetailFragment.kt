package com.fatihden.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fatihden.myapplication.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {
    private var _binding:FragmentDetailBinding? = null
    private val binding = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        /*return inflater.inflate(R.layout.fragment_detail, container, false)*/
        _binding = FragmentDetailBinding.inflate(inflater,container,false)

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val bilgi = DetailFragmentArgs.fromBundle(it).bilgi
            if ( bilgi == "yeni"){
                //Yeni Eklenilecek :
                binding.deleteDetailBtn.isEnabled = false
                binding.saveDetailBtn.isEnabled = true
                binding.nameETxt.setText("")
                binding.detailETxt.setText("")

            }else{
                // Eskini getir-göster :
                binding.deleteDetailBtn.isEnabled = true
                binding.saveDetailBtn.isEnabled = false
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }


}