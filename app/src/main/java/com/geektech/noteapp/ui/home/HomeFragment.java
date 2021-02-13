package com.geektech.noteapp.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.geektech.noteapp.App;
import com.geektech.noteapp.NoteAdapter;
import com.geektech.noteapp.OnItemClickListener;
import com.geektech.noteapp.R;
import com.geektech.noteapp.models.Note;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private ArrayList<Note> list;
    private boolean update = false;
    private int pos;
    private DocumentReference docRef = FirebaseFirestore.getInstance().document("sampleData/inspiration");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        adapter = new NoteAdapter(this.getContext());
        loadData();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        view.findViewById(R.id.fab).setOnClickListener(v -> {
            update = false;
            openForm(null);
        });
        setFragmentListener();
        initList();
    }

    //   TODO: 4th Home Work - sorting
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_sort, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sort) {
            showSortDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        String[] options = {"By title", "By time"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Sort by");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    list = (ArrayList<Note>) App.getAppDatabase().noteDao().getAll();

                    Collections.sort(list, new Comparator<Note>() {
                        @Override
                        public int compare(Note o1, Note o2) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        }
                    });
                    adapter.setList(list);
                }
                if (which == 1) {
                    list = (ArrayList<Note>) App.getAppDatabase().noteDao().getAll();

                    Collections.sort(list, new Comparator<Note>() {
                        @Override
                        public int compare(Note o1, Note o2) {
                            return o1.getDate().compareTo(o2.getDate());
                        }
                    });
                    adapter.setList(list);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setIcon(R.drawable.ic_sort_24);
        dialog.show();
    }

    private void loadData() {
        list = (ArrayList<Note>) App.getAppDatabase().noteDao().getAll();
        adapter.setList(list);
    }

    private void initList() {
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onLongClick(int position) {
                Note note = adapter.getItem(position);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Remove this item?");
                alertDialog.setPositiveButton("NO, CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //          TODO: 7th Home Work - removing from FireStore
                        docRef.delete();
                        //          TODO:       4th Home Work - removing from DB
                        App.getAppDatabase().noteDao().delete(note);
                        adapter.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                AlertDialog dialog = alertDialog.create();
                dialog.setIcon(R.drawable.ic_delete_forever_24);
                dialog.show();
            }

            @Override
            public void onClick(int position) {
                pos = position;
                update = true;
                Note note = adapter.getItem(position);
                openForm(note);
                Toast.makeText(requireContext(), note.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }

    private void setFragmentListener() {
        getParentFragmentManager().setFragmentResultListener("rk_form",
                getViewLifecycleOwner(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Note note = (Note) result.getSerializable("note");
                        if (update) {
                            adapter.updateItem(pos, note);
                        } else {
                            adapter.addItem(note);
                        }
                    }
                });
    }

    private void openForm(Note note) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.form_fragment, bundle);
    }
}