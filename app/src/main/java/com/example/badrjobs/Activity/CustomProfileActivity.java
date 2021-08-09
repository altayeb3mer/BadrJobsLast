package com.example.badrjobs.Activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.badrjobs.Fragment.FragmentChatProfile;
import com.example.badrjobs.R;

import de.hdodenhof.circleimageview.CircleImageView;
import sdk.chat.core.dao.User;
import sdk.chat.core.ui.ProfileFragmentProvider;
import sdk.chat.ui.ChatSDKUI;
import sdk.chat.ui.activities.ProfileActivity;
import sdk.chat.ui.appbar.ChatActionBar;
import sdk.chat.ui.appbar.ChatActionBar_ViewBinding;
import sdk.chat.ui.fragments.ProfileFragment;
import sdk.chat.ui.views.ChatView;

public class CustomProfileActivity extends ProfileActivity implements ProfileFragmentProvider{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public Fragment profileFragment(User user) {
        return new FragmentChatProfile();
    }
}
