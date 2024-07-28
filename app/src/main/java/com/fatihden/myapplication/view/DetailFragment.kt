package com.fatihden.myapplication.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.fatihden.myapplication.databinding.FragmentDetailBinding
import com.fatihden.myapplication.db.DetailDAO
import com.fatihden.myapplication.db.DetailDatabase
import com.fatihden.myapplication.model.Detail
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.internal.disposables.ArrayCompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.IOException


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding = _binding!!

    // İzin İstemek için :
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    // Galeriye Gitmek için :
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var secilenGorsel: Uri? = null  // gorselin dizinini verir
    private var secilenBitmap: Bitmap? = null // Gorsele çevirme işlemi

    // Database :
    private lateinit var db : DetailDatabase
    private lateinit var detailDAO: DetailDAO

    //RxJava3
    private val mDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dikkat !
        registerLauncher()
        // Database
        db = Room.databaseBuilder(requireContext(),DetailDatabase::class.java,"Details").build()
        detailDAO = db.detailDao()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        /*return inflater.inflate(R.layout.fragment_detail, container, false)*/
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val bilgi = DetailFragmentArgs.fromBundle(it).bilgi
            if (bilgi == "yeni") {
                //Yeni Eklenilecek :
                binding.deleteDetailBtn.isEnabled = false
                binding.saveDetailBtn.isEnabled = true
                binding.nameETxt.setText("")
                binding.detailETxt.setText("")

            } else {
                // Eskini getir-göster :
                binding.deleteDetailBtn.isEnabled = true
                binding.saveDetailBtn.isEnabled = false
            }
        }

        binding.imageView.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // SDK version 33'den büyükse :
                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.READ_MEDIA_IMAGES
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // İzin Verilmemiş, izin istememiz gerekli
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(), Manifest.permission.READ_MEDIA_IMAGES
                        )
                    ) {
                        //SnackBar göstermemiz lazım , kullanıcıdan neden izin istediğimizi bir kez
                        // daha söylerek izin istemeiz lazım
                        Snackbar.make(
                            view,
                            "Galeriye ulaşıp görsel seçmemiz lazım!",
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction("İzin Ver", View.OnClickListener {
                            // İzin İstenilecek :
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }).show()

                    } else {
                        // İzin istenilecek :
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }
                } else {
                    //izin Verilmiş , galeriye erişim sağlanır
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)

                }
            } else {
                // SDK version 33'den Küçükse :

                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // İzin Verilmemiş, izin istememiz gerekli
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                        //SnackBar göstermemiz lazım , kullanıcıdan neden izin istediğimizi bir kez
                        // daha söylerek izin istemeiz lazım
                        Snackbar.make(
                            view,
                            "Galeriye ulaşıp görsel seçmemiz lazım!",
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction("İzin Ver", View.OnClickListener {
                            // İzin İstenilecek :
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()

                    } else {
                        // İzin istenilecek :
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                } else {
                    //izin Verilmiş , galeriye erişim sağlanır
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)

                }
            }


        }


        binding.saveDetailBtn.setOnClickListener {
            val isim = binding.nameETxt.text.toString()
            val detay= binding.detailETxt.text.toString()


            if (secilenBitmap != null){
                val kucukBitMap = kucukBitmapOlustur(secilenBitmap!!,300)
                val outputStream = ByteArrayOutputStream()

                kucukBitMap.compress(Bitmap.CompressFormat.PNG , 50 , outputStream)
                val byteDizisi = outputStream.toByteArray()

                val detayInsert = Detail(isim,detay,byteDizisi)
                //detailDAO.insert(detayInsert)  // RxJava3 ile tekrar aşağıda yazıldı

                //RxJava

                mDisposable.add(
                    detailDAO.insert(detayInsert)
                        .subscribeOn(Schedulers.io()) // Arka planda işlemi yaptırır
                        .observeOn(AndroidSchedulers.mainThread()) // ön planda gösterir
                        .subscribe(this::handleResponseForInsert) // fund'unu tetikler

                )




            }

        }

    }

    private fun handleResponseForInsert(){
        // İnsert işlemden sonra burası çalışır. Buraki Fuck'ın yukarıda tetiklenir.
        val action  = DetailFragmentDirections.actionDetailFragmentToListeFragment()

    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    fun registerLauncher() {

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val intentFromResult = it.data
                if (intentFromResult != null) {
                    secilenGorsel =
                        intentFromResult.data // Kullanıcının seçtiği görselin nerede olduğunu verir. Dosya dizinini saklar

                    try {
                        if (Build.VERSION.SDK_INT >= 28) {
                            // Yeni Versiyonlar :
                            val source = ImageDecoder.createSource(
                                requireActivity().contentResolver, secilenGorsel!!
                            )
                            secilenBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageView.setImageBitmap(secilenBitmap)
                        } else {
                            // Eski Versiyonlar :
                            secilenBitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver, secilenGorsel
                            )
                            binding.imageView.setImageBitmap(secilenBitmap)
                        }
                    } catch (e: IOException) {
                        println(e.localizedMessage)
                    } catch (e1: Exception) {
                        e1.localizedMessage?.let { it1 -> Log.e("HataResim", it1) }
                    }

                }
            } else {

            }
        }



        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                //izin verildi
                //Galeriye git
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                //İzin verilmedi
                Toast.makeText(requireContext(), "İzin Verilmedi!", Toast.LENGTH_LONG).show()
            }
        }


    }

    fun kucukBitmapOlustur( kullanicininSectigiBitmap:Bitmap,maxBoyut:Int):Bitmap{
        var width = kullanicininSectigiBitmap.width
        var height = kullanicininSectigiBitmap.height
        var orani:Double = width.toDouble() / height.toDouble()
        var boyut:Bitmap


        if (orani >1 ){
            // img vertical
            width = maxBoyut
            val kisaltilmisYukseklik = width / orani
            height = kisaltilmisYukseklik.toInt()
            boyut =  Bitmap.createScaledBitmap(kullanicininSectigiBitmap, width,height,true)
        } else {
            // img horizantel
            height = maxBoyut
            val kisaltilmisGenislik = height * orani
            width = kisaltilmisGenislik.toInt()
            boyut =  Bitmap.createScaledBitmap(kullanicininSectigiBitmap, width,height,true)

        }


        return boyut
    }
}