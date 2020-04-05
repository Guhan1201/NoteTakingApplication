package com.example.roompracticeactivity.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.roompracticeactivity.R
import com.example.roompracticeactivity.adapter.NotesListAdapter
import com.example.roompracticeactivity.viewmodel.NotesListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.notes_list_fragment.*
import kotlin.properties.Delegates


class NotesListFragment : Fragment() {

    private lateinit var notesViewModel: NotesListViewModel
    private var toggleSwitcher by Delegates.notNull<Boolean>()
    private lateinit var fab: FloatingActionButton
    private lateinit var back: LinearLayout
    private lateinit var changeView: LinearLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notes_list_fragment, container, false)
    }

    override fun onStart() {
        setOnclickListener()
        super.onStart()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        notesViewModel = ViewModelProvider(this).get(NotesListViewModel::class.java)

        recyclerView = view.findViewById(R.id.recyclerview)
        adapter = NotesListAdapter(context)

        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter
        toggleSwitcher = false

        notesViewModel.allNotes.observe(viewLifecycleOwner, Observer { words ->
            words?.let { adapter.setWords(it) }
        })

        notesViewModel.reversedLiveData.observe(viewLifecycleOwner, Observer {
            adapter.setWords(it)
        })

        fab = view.findViewById(R.id.fab)
        back = view.findViewById(R.id.back)
        changeView = view.findViewById(R.id.changeFormatView)
        toolbar = view.findViewById(R.id.toolbar)
        toolbar.inflateMenu(R.menu.order_menu)

        toolbar.setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.e("test ", "test")
        when (item.itemId) {
            R.id.newest_on_top -> {
                notesViewModel.setOrderAsNewestOnTop()
            }
            R.id.oldest_on_top -> {
                notesViewModel.setOrderAsOldestOnTop()
            }
        }
        return true
    }

    override fun onStop() {
        setOnclickListenerWithNull()
        super.onStop()
    }


    private fun setOnclickListener() {
        fab.setOnClickListener {
            activity?.let {
                Navigation.findNavController(
                    it,
                    R.id.hostFragment
                )
            }?.navigate(R.id.action_firstFragment_to_secondFragment)
        }

        back.setOnClickListener {
            activity?.onBackPressed()
        }

        changeView.setOnClickListener {
            if (!toggleSwitcher) {
                changeFormatImageView.setImageResource(R.drawable.ic_resize)
                recyclerView.layoutManager =
                    LinearLayoutManager(requireContext())
                toggleSwitcher = true

            } else {
                changeFormatImageView.setImageResource(R.drawable.ic_up_arrow)
                recyclerView.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                toggleSwitcher = false
            }
            recyclerView.adapter = adapter
            adapter.callNotifyDataSetChanged()
        }
    }

    private fun setOnclickListenerWithNull() {
        fab.setOnClickListener(null)
        back.setOnClickListener(null)
        changeView.setOnClickListener(null)
    }
}
