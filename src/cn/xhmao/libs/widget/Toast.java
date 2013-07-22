package cn.xhmao.libs.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.xhmao.libs.R;

/**
 * Created by xhmao on 7/22/13.
 */
public class Toast extends android.widget.Toast {
    public Toast(Context context) {
        super(context);
    }

    public static Toast makeCustomText(Context context, CharSequence text, int duration) {
        Toast toast = new Toast(context);
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.notification_message, null);
        TextView tv = (TextView) v.findViewById(R.id.message);
        tv.setText(text);
        toast.setView(v);
        toast.setDuration(duration);

        return toast;
    }

    public static Toast makeCustomText(Context context, int resId, int duration) {
        return makeCustomText(context, context.getText(resId), duration);
    }
}
