package com.fatihden.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fatihden.myapplication.databinding.FragmentDetailBinding
import com.google.android.material.snackbar.Snackbar


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

        binding.imageView.setOnClickListener {
            if (
                    ContextCompat.checkSelfPermission
                        (
                            requireContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) !=PackageManager.PERMISSION_GRANTED
                )
            {
                // İzin Verilmemiş, izin istememiz gerekli
                if(ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ){
                    //SnackBar göstermemiz lazım , kullanıcıdan neden izin istediğimizi bir kez
                    // daha söylerek izin istemeiz lazım
                    Snackbar.make(view,"Galeriye ulaşıp görsel seçmemiz lazım!",Snackbar.LENGTH_INDEFINITE).setAction(
                        "İzin Ver",
                        View.OnClickListener {
                            // İzin İstenilecek :

                        }
                    ).show()

                }else{
                    // İzin istenilecek
                }



            }else{
                //izin Verilmiş , galeriye erişim sağlanır


            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }


}