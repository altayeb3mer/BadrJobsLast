package bader.cutShort.badrjobs.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import bader.cutShort.badrjobs.Fragment.FragmentChatProfile;

import sdk.chat.core.dao.User;
import sdk.chat.core.ui.ProfileFragmentProvider;
import sdk.chat.ui.activities.ProfileActivity;

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
