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
    private final boolean D = false;

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
            /*
             * При первом запуске приложения создадим объект фрагмента DownloadTaskFragment.
             * Он будет служить для сохранения AsyncTask'a и массива от воздействия
             * изменения конфигурации устройства (смена ориентации дисплея)
             */


            if(D) Log.i(TAG, "DownloadFragment doesn't exist. Create new one.");
            mDownloadFragment = new DownloadTaskFragment();
            mFragmentManager.beginTransaction().add(mDownloadFragment, "DownloadTaskFragment").commit();

            boolean b = checkFile();
            if (!b) showMainFragment();

        } else {
            /*
             * При изменении конфигурации устройства находим DownloadTaskFragment и провереям состояние
             * загрузки списка продуктов.
             */
            if(D) Log.i(TAG, "DownloadFragment exist. Check state.");
            checkDownloadFragmentState();
        }
    }

    /**
     * Проверяет состояние загрузки списка продуктов и предпринимает
     * различные пути поведения в зависимости от этого состояния.
     */
    private void checkDownloadFragmentState() {
        switch (mDownloadFragment.getState()) {
            case DownloadTaskFragment.STATE_DOWNLOAD_COMPLETE:
                if(D) Log.i(TAG, "STATE_DOWNLOAD_COMPLETE");
                mDownloadFragment.setState(DownloadTaskFragment.STATE_IDLE);
                /* Файл не грузится. Проверяем наличие массива продуктов и есил их нет
                 * то оображаем MainFragment */
                boolean b = checkProducts();
                if (!b) showMainFragment();
                break;

            case DownloadTaskFragment.STATE_DOWNLOADING:
                if(D) Log.i(TAG, "STATE_DOWNLOADING");
                /* Если файл ещё грузится, ничено не делаем. Ждём
                 * срабатывания колбэка */
                break;

            case DownloadTaskFragment.STATE_IDLE:
                if(D) Log.i(TAG, "STATE_IDLE");
                /* Файл не грузится. Проверяем наличие массива продуктов и есил их нет
                 * то оображаем MainFragment */
                boolean bo = checkProducts();
                if (!bo) showMainFragment();
                break;
        }
    }

    /**
     * Проверяет, не был ли скачан файл с продуктами в
     * предыдущие сессии приложения. Если файл был скачан,
     * парсим его и отображем список продуктов.
     * @return <b>true</b> если файл был скачан, иначе <b>false</b>
     */
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

    /**
     * Проверяет, не хранится ли в фрагменте {@link ru.apress.productslist.DownloadTaskFragment}
     * массив с продуктами. Если да, то просто отображаем список продуктов
     * из массива.
     * @return <b>true</b> если {@link ru.apress.productslist.DownloadTaskFragment} хранит в себе ненулевой массив продуктов, иначе <b>false</b>
     */
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


    /**
     * Очищаем всю информации о продуктах (массив продуктов, JSON файл с описанием
     * продуктов)
     */
    private void clearAllProductsInfo() {
        if(D) Log.v(TAG, "startDownloadFile");

        File file = new File(Prefs.getFilePath());
        if (file.exists()) file.delete();

        mDownloadFragment.setProducts(null);

        Prefs.setFilePath("");
        Prefs.setFileWasDownloaded(false);

        showMainFragment();
    }

}