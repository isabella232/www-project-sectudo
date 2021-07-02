package com.example.synradar_sectudo.learningmodule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.MainNewActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.GeneralUtils;
import com.example.synradar_sectudo.insecure.InsAddkycActivity;
import com.example.synradar_sectudo.insecure.InsLogin2Activity;
import com.example.synradar_sectudo.insecure.InsLoginActivity;
import com.example.synradar_sectudo.insecure.InsShowDetailsActivity;
import com.example.synradar_sectudo.insecure.InsUpdateAddrActivity;

import static android.content.Context.MODE_PRIVATE;
import static com.example.synradar_sectudo.helper.Constants.learn;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpFragment extends Fragment {

    WebView webView;
    TextView topicName;
    Button exp;
    String topic;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpFragment newInstance(String param1, String param2) {
        ExpFragment fragment = new ExpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_exp, container, false);
        webView = (WebView) v.findViewById(R.id.exp);


        exp = (Button) v.findViewById(R.id.button42);

        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setWebViewClient(new WebViewClient()) ;
        webView.setWebChromeClient(new ChromeClient());

        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAppCacheEnabled(true);

        if(getActivity() != null) {
             topic = getActivity().getIntent().getExtras().getString("topic");
            switch (topic) {
                case "ia":

                    webView.loadUrl(learn+"ia/exploit.html");
                    break;
                case "ism":

                    webView.loadUrl(learn+"ism/exploit.html");
                    break;
                case "csua":

                    webView.loadUrl(learn+"csua/exploit.html");
                    break;
                case "ids":

                    webView.loadUrl(learn+"ids/exploit.html");
                    break;
                case "xss":

                    webView.loadUrl(learn+"xss/exploit.html");
                    break;
                case "sqli":

                    webView.loadUrl(learn+"sqli/exploit.html");
                    break;
                case "ida":

                    webView.loadUrl(learn+"ida/exploit.html");
                    break;
                case "dl":

                    webView.loadUrl(learn+"dl/exploit.html");
                    break;
                case "icp":

                    webView.loadUrl(learn+"icp/exploit.html");
                    break;
                case "sm":

                    webView.loadUrl(learn+"sm/exploit.html");
                    break;
            }
        }

        exp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               SharedPreferences prefs = getActivity().getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
                String sessionstr = GeneralUtils.readSharedPrefs(prefs, "session");
                if(sessionstr != null){
                    switch (topic) {
                        case "csua":
                        case "dl":
                        case "icp":
                        case "sm":

                            startActivity(new Intent(getActivity(), QuestionActivity.class).putExtra("topic", topic));
                            break;
                        case "xss":
                            startActivity(new Intent(getActivity(), InsUpdateAddrActivity.class).putExtra("topic", topic));
                            break;
                        case "ids":
                            startActivity(new Intent(getActivity(), InsAddkycActivity.class).putExtra("topic", topic));
                            break;
                        case "ida":
                        case "sqli":
                            startActivity(new Intent(getActivity(), InsShowDetailsActivity.class).putExtra("topic", topic));
                            break;
                        case "ism":
                            startActivity(new Intent(getActivity(), InsLoginActivity.class).putExtra("topic", topic));
                            break;
                        case "ia":
                          startActivity(new Intent(getActivity(), InsLogin2Activity.class).putExtra("topic", topic));
                        break;
                    }
                }


            }
        });

         return v;


    }
    private class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        ChromeClient() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getActivity().getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getActivity().getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            getActivity().setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getActivity().getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getActivity().getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getActivity().getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getActivity().getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}