package bader.cutShort.badrjobs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.LocaleChangerAppCompatDelegate;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.badrjobs.R;
import bader.cutShort.badrjobs.Utils.ToolbarClass;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlockedUser extends ToolbarClass {

    String image = "",name="",fixName="";

    CircleImageView circleImageView;
    TextView textViewName,textViewFix;

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_blocked_user, "");
        Bundle args = getIntent().getExtras();
        if (args!=null){
            image = args.getString("image");
            name = args.getString("name");
            fixName = args.getString("fixName");
        }
        init();
        Glide.with(BlockedUser.this).
                load(image).into(circleImageView);
        textViewName.setText(name);
        textViewFix.setText(fixName);
    }

    private void init() {
        circleImageView = findViewById(R.id.img);
        textViewName = findViewById(R.id.name);
        textViewFix = findViewById(R.id.fixName);
    }


    //language controller
    private LocaleChangerAppCompatDelegate localeChangerAppCompatDelegate;
    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        if (localeChangerAppCompatDelegate == null) {
            localeChangerAppCompatDelegate = new LocaleChangerAppCompatDelegate(super.getDelegate());
        }

        return localeChangerAppCompatDelegate;
    }
    @Override
    protected void onResume() {
        super.onResume();
        ActivityRecreationHelper.onResume(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityRecreationHelper.onDestroy(this);
    }
}