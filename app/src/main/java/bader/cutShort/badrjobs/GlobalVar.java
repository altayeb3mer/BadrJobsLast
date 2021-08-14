package bader.cutShort.badrjobs;

import android.app.Activity;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Window;
import android.view.WindowManager;

public class GlobalVar {


    public SpannableString underLinerTextView(String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }
    public void changeStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

}
