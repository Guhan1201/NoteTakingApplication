package com.example.roompracticeactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class NotesListFragment : Fragment() {

    private lateinit var notesViewModel: NotesListViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        notesViewModel = ViewModelProvider(this).get(NotesListViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotesListAdapter(context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        notesViewModel.allNotes.observe(this.viewLifecycleOwner, Observer { words ->
            words?.let { adapter.setWords(it) }
        })

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)


        fab.setOnClickListener {
            activity?.let { Navigation.findNavController(it, R.id.hostFragment) }
                ?.navigate(R.id.action_firstFragment_to_secondFragment)
//            Intent(context, AddNoteFragment::class.java).apply(fun Intent.() {
//                startActivity(this)
//            })
        }
    }


}
