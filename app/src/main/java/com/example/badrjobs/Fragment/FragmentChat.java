package com.example.badrjobs.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badrjobs.Adapter.AdapterChatList;
import com.example.badrjobs.Model.ModelChatList;
import com.example.badrjobs.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import sdk.chat.app.firebase.ChatSDKFirebase;
import sdk.chat.core.dao.Thread;
import sdk.chat.core.events.NetworkEvent;
import sdk.chat.core.interfaces.ThreadType;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.ui.activities.ChatActivity;
import sdk.chat.ui.fragments.ChatFragment;
import sdk.chat.ui.fragments.PrivateThreadsFragment;
import sdk.chat.ui.utils.DialogUtils;
import sdk.chat.ui.utils.ToastHelper;


public class FragmentChat extends PrivateThreadsFragment {



    public FragmentChat() {
        // Required empty public constructor
    }



}