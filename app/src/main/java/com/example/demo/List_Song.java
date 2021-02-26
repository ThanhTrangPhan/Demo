package com.example.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link List_Song#newInstance} factory method to
 * create an instance of this fragment.
 */
public class List_Song extends Fragment {
    static ArrayList<modelAudio> list;
    RecyclerView recyclerView;

    public List_Song() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    protected static List_Song newInstance() {
        List_Song fragment = new List_Song();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        if (getArguments() != null) {
            list = getArguments().getParcelableArrayList("listSong");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_song, container, false);

        final AudioAdapter adapter = new AudioAdapter(this.getActivity(),list);
        RecyclerView recyclerView= root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                MainActivity main = new MainActivity();
                ((MainActivity) main).playAudio(pos);
            }
        });
        // Inflate the layout for this fragment
        return root;
    }
}