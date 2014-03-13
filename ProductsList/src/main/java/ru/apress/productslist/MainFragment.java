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
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "MainFragment";
    private final boolean D = true;


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

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(D) Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);

//        FragmentManager fm = getFragmentManager();
//        mDownloadFragment = (DownloadTaskFragment) fm.findFragmentByTag("DownloadTaskFragment");

//        if (mDownloadFragment == null) {
//            if(D) Log.i(TAG, "DownloadFragment doesn't exist. Create new one.");
//            mDownloadFragment = new DownloadTaskFragment();
//            mDownloadFragment.setDownloadTaskFragmentListener(this);
//            fm.beginTransaction().add(mDownloadFragment, "DownloadTaskFragment").commit();
//        } else {
//            if(D) Log.i(TAG, "DownloadFragment exist");
//            mDownloadFragment.setDownloadTaskFragmentListener(this);
//            if (mDownloadFragment.getState() == DownloadTaskFragment.STATE_DOWNLOAD_COMPLETE) {
//                if(D) Log.i(TAG, "ProductObjs have been downloaded. Take it!");
//                takeProductObjsFromTaskFragment();
//            }
//        }
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
        mDownloadBtn.setEnabled(enable);
    }

    public void setProgressVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;
        mProgressBar.setVisibility(visibility);
    }

//    private void takeProductObjsFromTaskFragment () {
//        if(D) Log.v(TAG, "takeProductObjsFromTaskFragment");
//        Product[] objs = mDownloadFragment.getProductObjs();
//        mDownloadFragment.setState(DownloadTaskFragment.STATE_IDLE);
//
//        if(objs != null) {
//            mListener.onProductsListDownloaded(objs);
//        }
//    }
//
//    private void startDownloadFile() {
//        if(D) Log.v(TAG, "startDownloadFile");
//        if (mDownloadFragment != null) {
//            mDownloadFragment.starDownload();
//        }
//    }

//    @Override
//    public void onPreExecute() {
//        if(D) Log.v(TAG, "onPreExecute");
//        mDownloadBtn.setEnabled(false);
//        mProgressBar.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onPostExecute() {
//        if(D) Log.v(TAG, "onPostExecute");
//        mDownloadBtn.setEnabled(true);
//        mProgressBar.setVisibility(View.INVISIBLE);
//
//        takeProductObjsFromTaskFragment();
//    }
//
//    @Override
//    public void onCancelled() {
//        if(D) Log.v(TAG, "onPostExecute");
//        mDownloadBtn.setEnabled(true);
//        mProgressBar.setVisibility(View.INVISIBLE);
//
//    }
//
//
    public interface MainFragmentListener {
        public void onDownloadBtnClick();
    }
}
