package com.example.aggoetey.myapplication.error_screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aggoetey.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ErrorScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ErrorScreenFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TEXT = "text";

    private String text;

    private TextView mTextView;

    public ErrorScreenFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param text De tekst die getoond moet worden op het errorscherm
     * @return
     */
    public static ErrorScreenFragment newInstance(String text) {
        ErrorScreenFragment fragment = new ErrorScreenFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            text = getArguments().getString(TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_error_screen, container, false);
        mTextView = v.findViewById(R.id.error_text);
        mTextView.setText(this.text);
        return v;
    }

}
