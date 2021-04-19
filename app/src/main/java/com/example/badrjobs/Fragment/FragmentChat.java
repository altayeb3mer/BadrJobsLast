package com.example.badrjobs.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badrjobs.Adapter.AdapterChatList;
import com.example.badrjobs.Model.ModelChatList;
import com.example.badrjobs.R;

import java.util.ArrayList;


public class FragmentChat extends Fragment {


    View view;
    Context context;
    //recycler
    RecyclerView recyclerView;
    AdapterChatList adapterChatList;
    ArrayList<ModelChatList> arrayList;
    GridLayoutManager gridLayoutManager;

    public FragmentChat() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        init();
        initAdapter();
        return view;
    }

    private void init() {

    }

    private void initAdapter(){
        arrayList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler);

        gridLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        for (int i = 0; i < 15; i++) {
            ModelChatList item = new ModelChatList();
            item.setId(i+"");

            arrayList.add(item);
        }
        adapterChatList = new AdapterChatList(getActivity(),arrayList);
        recyclerView.setAdapter(adapterChatList);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }


}