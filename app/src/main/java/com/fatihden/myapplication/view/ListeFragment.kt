package com.fatihden.myapplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.room.Room
import com.fatihden.myapplication.databinding.FragmentListeBinding
import com.fatihden.myapplication.db.DetailDAO
import com.fatihden.myapplication.db.DetailDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class ListeFragment : Fragment() {

    private var _binding : FragmentListeBinding? = null

    private val binding get() = _binding!!


    // Database :
    private lateinit var db : DetailDatabase
    private lateinit var detailDAO: DetailDAO

    //RxJava3
    private val mDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Database
        db = Room.databaseBuilder(requireContext(),DetailDatabase::class.java,"Details").build()
        detailDAO = db.detailDao()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {
            val action = ListeFragmentDirections.actionListeFragmentToDetailFragment(bilgi = "yeni",id=0)
            Navigation.findNavController(it).navigate(action)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mDisposable.clear() // Ram'de yer kaplayan geçici verileri uygulama kapatılınca Ram'deki verileri temizlemek için tercih edilir .
    }
    private fun  verileriAl(){
        mDisposable.add(
            detailDAO.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

}