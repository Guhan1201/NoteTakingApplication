package com.example.roompracticeactivity.fragment

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.roompracticeactivity.R
import com.example.roompracticeactivity.adapter.NotesItemClickListener
import com.example.roompracticeactivity.adapter.NotesListAdapter
import com.example.roompracticeactivity.database.entities.Notes
import com.example.roompracticeactivity.enumClass.Order
import com.example.roompracticeactivity.viewmodel.NotesListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.note_display.*
import kotlinx.android.synthetic.main.notes_list_fragment.*
import kotlin.properties.Delegates


class NotesListFragment : Fragment(), NotesItemClickListener {

    private lateinit var notesViewModel: NotesListViewModel
    private var toggleSwitcher by Delegates.notNull<Boolean>()
    private lateinit var fab: FloatingActionButton
    private lateinit var back: LinearLayout
    private lateinit var changeView: LinearLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotesListAdapter
    private var selectedOrder = Order.NONE
    private lateinit var notesList: List<Notes>

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
        adapter.setNotesItemClickListener(this)


        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val postion = viewHolder.adapterPosition
                val deletedNotes = notesList[postion]
                notesViewModel.deleteNotes(deletedNotes)
                Snackbar.make(parent, getString(R.string.notes_deleted), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)) {
                        notesViewModel.insertNotes(deletedNotes)
                    }
                    .show()
            }
        }

        val setItemTouchHelper = ItemTouchHelper(itemTouchHelper)
        setItemTouchHelper.attachToRecyclerView(recyclerView)


        toggleSwitcher = false


        with(notesViewModel) {
            allNotes.observe(viewLifecycleOwner, Observer { words ->
                notesList = words
                words?.let { adapter.setNotes(it) }
            })
            reversedLiveData.observe(viewLifecycleOwner, Observer {
                adapter.setNotes(it)
            })
            staggeredGridLayoutEnable.observe(viewLifecycleOwner, Observer {
                if (it) {
                    recyclerView.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    recyclerView.adapter = adapter
                } else {
                    recyclerView.layoutManager =
                        LinearLayoutManager(requireContext())
                }

            })
        }

        fab = view.findViewById(R.id.fab)
        back = view.findViewById(R.id.back)
        changeView = view.findViewById(R.id.changeFormatView)
        toolbar = view.findViewById(R.id.toolbar)
        toolbar.inflateMenu(R.menu.order_menu)
        toolbar.setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.newest_on_top -> {
                notesViewModel.setOrderAsNewestOnTop()
                item.isChecked = true
                selectedOrder = Order.NEWEST_ON_TOP
            }
            R.id.oldest_on_top -> {
                notesViewModel.setOrderAsOldestOnTop()
                item.isChecked = true
                selectedOrder = Order.OLDEST_ON_TOP
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.order_menu, menu)
        when (selectedOrder) {
            Order.OLDEST_ON_TOP -> menu.findItem(R.id.oldest_on_top).isChecked = true
            Order.NEWEST_ON_TOP -> menu.findItem(R.id.newest_on_top).isChecked = true
            Order.NONE -> {

            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onStop() {
        setOnclickListenerWithNull()
        super.onStop()
    }


    private fun setOnclickListener() {
        fab.setOnClickListener {
            findNavController().navigate(R.id.notes_list_to_add_notes_fragment)
        }
        back.setOnClickListener {
            activity?.onBackPressed()
        }

        changeView.setOnClickListener {
            if (notesViewModel.staggeredGridLayoutEnable.value!!) {
                changeFormatImageView.setImageResource(R.drawable.ic_resize)
                notesViewModel.setStaggeredGridLayoutEnable(false)
            } else {
                changeFormatImageView.setImageResource(R.drawable.ic_up_arrow)
                notesViewModel.setStaggeredGridLayoutEnable(true)
            }
        }

    }

    private fun setOnclickListenerWithNull() {
        fab.setOnClickListener(null)
        back.setOnClickListener(null)
        changeView.setOnClickListener(null)
    }

    override fun onClick(notes: Notes) {
        val bundle = Bundle().apply {
            putSerializable("notes", notes)
        }
        findNavController().navigate(R.id.notes_list_to_edit_notes_fragment, bundle)

    }
}
