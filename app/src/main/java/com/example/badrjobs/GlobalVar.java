package com.example.badrjobs;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

public class GlobalVar {


    public SpannableString underLinerTextView(String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }


}
