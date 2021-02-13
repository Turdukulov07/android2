package com.geektech.noteapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.geektech.noteapp.models.Note;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormFragment extends Fragment {

    private Note note;
    private EditText editText;
    private static final String NOTE_KEY = "NOTE";
    private static final String TAG2 = "InspiringQuote";
    private DocumentReference docRef = FirebaseFirestore.getInstance().document("sampleData/inspiration");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.edit_text);
        view.findViewById(R.id.button_save).setOnClickListener(v ->
                save());

        note = (Note) requireArguments().getSerializable("note");
        if (note != null) {
            editText.setText(note.getTitle());
        }
    }

    private void save() {
        String text = editText.getText().toString().trim();
        String date = DateFormat.getDateTimeInstance().format(new Date());
        if (note == null) {
            note = new Note(text, date);
            App.getAppDatabase().noteDao().insert(note);
            //    TODO: 7th Home Work - adding notes to FireStore
            if (text.isEmpty()) {
                return;
            }
            insertToFireStore(text);
        } else {
            note.setTitle(text);
            App.getAppDatabase().noteDao().update(note);
            //      TODO: 7th Home Work - updating notes from FireStore
            updateFireStoreData(text);
        }
        Note note = new Note(text, date);
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        getParentFragmentManager().setFragmentResult("rk_form", bundle);
        close();
    }

    private void insertToFireStore(String text) {
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put(NOTE_KEY, text);
        docRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG2, "Document has been saved!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG2, "Document was not saved");
            }
        });
    }

    private void updateFireStoreData(String text) {
        Map<String, Object> map = new HashMap<>();
        map.put(NOTE_KEY, text);
        docRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG2, "Document updated!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG2, "Haven`t updated yet: ", e);
            }
        });
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigateUp();
    }
}