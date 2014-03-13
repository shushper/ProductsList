package ru.apress.productslist;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;


/**
 * Фрагмент отображет кноку "загрузить" и прогресс бар, отображающий
 * процесс загрузки файла.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "MainFragment";
    private final boolean D = false;


    private Button mDownloadBtn;
    private ProgressBar mProgressBar;
    private MainFragmentListener mListener;

    @Override
    public void onAttach(Activity activity) {
        if(D) Log.v(TAG, "onAttach");
        super.onAttach(activity);
        try {
            mListener = (MainFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement StartFragmentListener");
        }
    }

    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(D) Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(D) Log.v(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        mDownloadBtn = (Button) view.findViewById(R.id.btn_download);
        mDownloadBtn.setOnClickListener(this);

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_download);
        DownloadTaskFragment f = (DownloadTaskFragment) getFragmentManager().findFragmentByTag("DownloadTaskFragment");

        if(f.getState() == DownloadTaskFragment.STATE_DOWNLOADING){
            setDownloadBtnEnable(false);
            setProgressVisible(true);
        }

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
                mListener.onDownloadBtnClick();
                break;
            default:

                break;
        }
    }

    public void setDownloadBtnEnable(boolean enable) {
        if (mDownloadBtn != null) {
            mDownloadBtn.setEnabled(enable);
        }
    }

    public void setProgressVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;

        if (mProgressBar != null) {
            mProgressBar.setVisibility(visibility);
        }

    }

    public interface MainFragmentListener {
        public void onDownloadBtnClick();
    }
}
