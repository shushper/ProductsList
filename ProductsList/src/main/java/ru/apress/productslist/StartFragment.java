package ru.apress.productslist;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class StartFragment extends Fragment implements View.OnClickListener {

    private StartFragmentListener mListener;
    private Button mDownloadBtn;
    private ProgressBar mProgressBar;

    private final String FILE_URL = "http://railsc.ru/resp.txt";
    private final String FILE_NAME = "resp.txt";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (StartFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement StartFragmentListener");
        }
    }

    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_start, container, false);

        mDownloadBtn = (Button) view.findViewById(R.id.btn_download);
        mDownloadBtn.setOnClickListener(this);

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_download);

        return view;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
                startDownloadFile();
                break;
            default:

                break;
        }
    }

    private void startDownloadFile() {

        new DownloadTask().execute();
    }

    private class DownloadTask extends AsyncTask<Void, Void, ProductObj[] > {

        @Override
        protected void onPreExecute() {
            mDownloadBtn.setEnabled(false);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ProductObj[] doInBackground(Void... params) {
            ProductObj[] products = null;
            File file = new File(getActivity().getCacheDir(), FILE_NAME);

            try {
                URL url = new URL(FILE_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                InputStream is = new BufferedInputStream(connection.getInputStream());
                Utils.saveInputStreamToFile(is, file);
                String jsonStr = Utils.readFile(file);
                products = parseJson(jsonStr);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return products;
        }

        @Override
        protected void onPostExecute(ProductObj[] productObjs) {
            mDownloadBtn.setEnabled(true);
            mProgressBar.setVisibility(View.INVISIBLE);

            if(productObjs != null) {
                mListener.onProductsListDownloaded(productObjs);
            }
        }
    }



    private ProductObj[] parseJson(String jsonStr){
        ProductObj[] productObjs = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONObject content = jsonObject.getJSONObject("content");
            JSONArray productsArr = content.getJSONArray("products");
            productObjs = new ProductObj[productsArr.length()];

            for(int i = 0; i < productsArr.length(); i++) {
                ProductObj productObj = new ProductObj();

                JSONObject product = productsArr.getJSONObject(i);
                productObj.setId(product.getInt("id"));
                productObj.setName(product.getString("name"));
                productObj.setImagesCnt(product.getInt("images_cnt"));

                JSONArray imagesArr = product.getJSONArray("images");
                ImageObj[] imageObjs = new ImageObj[imagesArr.length()];

                for (int k = 0; k < imagesArr.length(); k++) {
                    ImageObj imageObj = new ImageObj();

                    JSONObject image = imagesArr.getJSONObject(k);
                    imageObj.setId(image.getInt("id"));
                    imageObj.setPos(image.getInt("position"));
                    imageObj.setPathThumb(image.getString("path_thumb"));
                    imageObj.setPathBig(image.getString("path_big"));

                    imageObjs[k] = imageObj;
                }

                productObj.setImages(imageObjs);
                productObjs[i] = productObj;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            productObjs = null;
        }
        return productObjs;
    }

    public interface StartFragmentListener{
        public void onProductsListDownloaded(ProductObj[] productObjs);
    }
}
