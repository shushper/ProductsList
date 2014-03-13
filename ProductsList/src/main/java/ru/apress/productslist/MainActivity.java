package ru.apress.productslist;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import ru.apress.productslist.DownloadTaskFragment.DownloadTaskFragmentListener;
import ru.apress.productslist.MainFragment.MainFragmentListener;


public class MainActivity extends ActionBarActivity implements MainFragmentListener, DownloadTaskFragmentListener {
    private final String TAG = "MainActivity";
    private final boolean D = true;

    private MainFragment mMainFragment;
    private ListFragment mListFragment;
    private DownloadTaskFragment mDownloadFragment;

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        mMainFragment     = new MainFragment();
        mListFragment     = new ListFragment();
        mDownloadFragment = (DownloadTaskFragment) mFragmentManager.findFragmentByTag("DownloadTaskFragment");

        if (mDownloadFragment == null) {
            if(D) Log.i(TAG, "DownloadFragment doesn't exist. Create new one.");
            mDownloadFragment = new DownloadTaskFragment();
            mFragmentManager.beginTransaction().add(mDownloadFragment, "DownloadTaskFragment").commit();
        }

        boolean productsDownloaded = mDownloadFragment.productsDownloaded();

        if (!productsDownloaded) {
            showMainFragment();
        } else {
            showListFragment();
            mListFragment.updateWithNewData(mDownloadFragment.getProductObjs());
        }
    }

    private void showMainFragment(){
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, mMainFragment);
        ft.commit();
    }

    private void showListFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, mListFragment);
        ft.commit();
    }

    @Override
    public void onDownloadBtnClick() {
        startDownloadFile();
    }

    @Override
    public void onPreExecute() {
        mMainFragment.setProgressVisible(true);
        mMainFragment.setDownloadBtnEnable(false);
    }

    @Override
    public void onPostExecute() {
        mMainFragment.setProgressVisible(false);
        mMainFragment.setDownloadBtnEnable(true);

        boolean productsDownloaded = mDownloadFragment.productsDownloaded();

        if (productsDownloaded) {
            showListFragment();
            mListFragment.updateWithNewData(mDownloadFragment.getProductObjs());
        }
    }

    @Override
    public void onCancelled() {
        mMainFragment.setProgressVisible(false);
        mMainFragment.setDownloadBtnEnable(true);
    }

    private void startDownloadFile() {
        if(D) Log.v(TAG, "startDownloadFile");
        if (mDownloadFragment != null) {
            mDownloadFragment.starDownload();
        }
    }
}
