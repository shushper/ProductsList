package ru.apress.productslist;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Этот фрагмент не имеет графического интерфейса и служит для
 * хранениния AsyncTask'a и массива продуктов. Сохранет своё состояние
 * при изменении конфигурации устройства (например, при смене ориентации).
 */
public class DownloadTaskFragment extends Fragment {
    private final String TAG = "DownloadTaskFragment";
    private final boolean D = false;

    private final String FILE_URL = "http://railsc.ru/resp.txt";
    private final String FILE_NAME = "resp.txt";

    public final static int STATE_IDLE              = 0;
    public final static int STATE_DOWNLOADING       = 1;
    public final static int STATE_DOWNLOAD_COMPLETE = 2;

    private int mState; //состояние загрузки

    private DownloadTaskFragmentListener mListener;
    private DownloadTask mTask;

    /* Список продуктов храниться даже при изменении
       конфигурации устройста */
    private Product[] mProducts;

    public DownloadTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        if(D) Log.v(TAG, "onAttach");
        super.onAttach(activity);
        try {
            mListener = (DownloadTaskFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DownloadTaskFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(D) Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mState = STATE_IDLE;
    }

    @Override
    public void onDetach() {
        if(D) Log.v(TAG, "onDetach");
        super.onDetach();
        if(mListener != null) mListener = null;
    }

    synchronized public void setState(int state) {
        if(D) Log.v(TAG, "setState");
        this.mState = state;
    }

    synchronized public int getState() {
        if(D) Log.v(TAG, "getState");
        return mState;
    }

    public Product[] getProducts() {
        if(D) Log.v(TAG, "getProduct");
        return mProducts;
    }

    public void setProducts(Product[] products) {
        if(D) Log.v(TAG, "setProducts");
        this.mProducts = products;
    }

    public void starDownload() {
        if(D) Log.v(TAG, "starDownload");
        if (mState == STATE_IDLE) {
            mTask = new DownloadTask();
            mTask.execute();
        }
    }

    /**
     * Проверяет наличие ненулевого массива с продуктами.
     *
     * @return <b>true</b> если массив присутствует и <b>false</b> если он равен null.
     */
    public boolean wereProductsDownloaded() {
        return mProducts != null;
    }

    /**
     * Скачивает файл с сервера, сохраняет его во внутреннюю память и парсит этот
     * файл в массив продуктов.
     */
    private class DownloadTask extends AsyncTask<Void, Void, Product[] > {

        @Override
        protected void onPreExecute() {
            if(D) Log.v(TAG, "onPreExecute");
            if(mListener != null) {
                mListener.onPreExecute();
            }
        }

        @Override
        protected Product[] doInBackground(Void... params) {
            if(D) Log.v(TAG, "doInBackground");

            setState(STATE_DOWNLOADING);

            Product[] products = null;
            File file = new File(getActivity().getCacheDir(), FILE_NAME);

            try {
                URL url = new URL(FILE_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = new BufferedInputStream(connection.getInputStream());
                Utils.saveInputStreamToFile(is, file);
                mListener.onFileDownloaded(file);
                String jsonStr = Utils.readFile(file);
                products = Utils.parseJson(jsonStr);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return products;
        }

        @Override
        protected void onPostExecute(Product[] productObjs) {
            if(D) Log.v(TAG, "onPostExecute");

            setState(STATE_DOWNLOAD_COMPLETE);
            mProducts = productObjs;
            mListener.onPostExecute();
        }

        @Override
        protected void onCancelled() {
            if(D) Log.v(TAG, "onCancelled");
            super.onCancelled();

            if(mListener != null) {
                mListener.onCancelled();
            }
        }
    }

    public interface DownloadTaskFragmentListener{
        public void onFileDownloaded(File file);
        public void onPreExecute();
        public void onPostExecute();
        public void onCancelled();
    }

}
