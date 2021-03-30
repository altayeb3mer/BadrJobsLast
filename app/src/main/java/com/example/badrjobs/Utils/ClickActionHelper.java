package com.example.badrjobs.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ClickActionHelper {

    public static void startActivity(String className, Bundle extras, Context context){
        Class cls;

        try {
            cls = Class.forName(className);
            Intent i = new Intent(context, cls);
            i.putExtras(extras);
            context.startActivity(i);


        }catch(ClassNotFoundException e){
            Log.e("NotificationError",e.getMessage());
            //means you made a wrong input in firebase console
        }

    }
}
