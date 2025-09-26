
package tech.treeentertainment.camera.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;

import tech.treeentertainment.camera.R;

public class WebViewDialogFragment extends DialogFragment {

    public static WebViewDialogFragment newInstance(int title, String url) {
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("url", url);
        WebViewDialogFragment f = new WebViewDialogFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getArguments().getInt("title"));
        View view = inflater.inflate(R.layout.webview_dialog, container, false);
        WebView webview = (WebView) view.findViewById(R.id.webview1);
        webview.loadUrl(getArguments().getString("url"));
        view.findViewById(android.R.id.button1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
