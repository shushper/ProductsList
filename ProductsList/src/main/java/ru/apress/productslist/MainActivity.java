package ru.apress.productslist;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.io.File;

import ru.apress.productslist.DownloadTaskFragment.DownloadTaskFragmentListener;
import ru.apress.productslist.MainFragment.MainFragmentListener;
import ru.apress.productslist.ListFragment.ListFragmentListener;


public class MainActivity extends ActionBarActivity implements MainFragmentListener, DownloadTaskFragmentListener, ListFragmentListener{
    private final String TAG = "MainActivity";
    private final boolean D = true;

    private MainFragment mMainFragment;
    private ListFragment mListFragment;
    private DownloadTaskFragment mDownloadFragment;

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(D) Log.v(TAG, "onCreate");
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

            boolean b = checkFile();
            if (!b) showMainFragment();

        } else {
            if(D) Log.i(TAG, "DownloadFragment exist. Check state.");
            checkDownloadFragmentState();
        }
    }

    private void checkDownloadFragmentState() {
        switch (mDownloadFragment.getState()) {
            case DownloadTaskFragment.STATE_DOWNLOAD_COMPLETE:
                if(D) Log.i(TAG, "STATE_DOWNLOAD_COMPLETE");
                mDownloadFragment.setState(DownloadTaskFragment.STATE_IDLE);
                boolean b = checkProducts();
                if (!b) showMainFragment();
                break;

            case DownloadTaskFragment.STATE_DOWNLOADING:
                if(D) Log.i(TAG, "STATE_DOWNLOADING");
                //Do nothing. Wating for callback.
                break;

            case DownloadTaskFragment.STATE_IDLE:
                if(D) Log.i(TAG, "STATE_IDLE");
                boolean bo = checkProducts();
                if (!bo) showMainFragment();
                break;
        }
    }

    private boolean checkFile() {
        if(D) Log.v(TAG, "checkFile");
        boolean fileWasDownloaded = Prefs.wasFileDownloaded();

        if (fileWasDownloaded) {
            if(D) Log.i(TAG, "File was downloaded. Parse it!");
            File file = new File(Prefs.getFilePath());
            String jsonStr = Utils.readFile(file);
            Product[] products = Utils.parseJson(jsonStr);
            mDownloadFragment.setProducts(products);
            showListFragment();
            mListFragment.updateWithNewData(products);
            return true;
        } else {
            return false;
        }
    }

    private boolean checkProducts() {
        if(D) Log.v(TAG, "checkProducts");
        boolean productsWereDownloaded = mDownloadFragment.wereProductsDownloaded();

        if (productsWereDownloaded) {
            if(D) Log.i(TAG, "Products were downloaded. Show it!");
            showListFragment();
            mListFragment.updateWithNewData(mDownloadFragment.getProducts());
            return true;
        } else {
            return false;
        }
    }

    private void showMainFragment(){
        if(D) Log.v(TAG, "showMainFragment");
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, mMainFragment);
        ft.commit();
    }

    private void showListFragment() {
        if(D) Log.v(TAG, "showListFragment");
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, mListFragment);
        ft.commit();
    }

    @Override
    public void onDownloadBtnClick() {
        startDownloadFile();
    }

    @Override
    public void onClearBtnClick() {
        clearAllProductsInfo();
    }

    @Override
    public void onFileDownloaded(File file) {
        Prefs.setFileWasDownloaded(true);
        Prefs.setFilePath(file.getPath());
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

        mDownloadFragment.setState(DownloadTaskFragment.STATE_IDLE);
        checkProducts();
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

    private void clearAllProductsInfo() {
        if(D) Log.v(TAG, "startDownloadFile");
        mDownloadFragment.setProducts(null);
        Prefs.setFilePath("");
        Prefs.setFileWasDownloaded(false);
        showMainFragment();
    }

}