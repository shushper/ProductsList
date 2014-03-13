package ru.apress.productslist;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class ListFragment extends Fragment {
    private final String TAG = "ListFragment";
    private final boolean D = false;

    private ListView mProductsList;
    private Button mClearBtn;

    private Product[] mProducts;
    private ProductsAdapter mAdapter;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.v(TAG, "onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        if(D) Log.v(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mProductsList = (ListView) view.findViewById(R.id.lv_products);
        mClearBtn = (Button) view.findViewById(R.id.btn_clear);

        mAdapter = new ProductsAdapter();
        mProductsList.setAdapter(mAdapter);

        return view;
    }


    public void updateWithNewData(Product[] productObjs) {
        if(D) Log.v(TAG, "updateWithNewData");
        mProducts = productObjs;
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
    }

    private class ProductsAdapter extends BaseAdapter {
        private final String TAG = "ProductsAdapter";

        private LayoutInflater mInflater;
        private ImageLoader mImageLoader;

        private ProductsAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mImageLoader = ImageLoader.getInstance();
        }

        @Override
        public int getCount() {
            if (mProducts != null) {
                return mProducts.length;
            } else {
                return 0;
            }
        }

        @Override
        public Product getItem(int position) {
            if (mProducts != null && mProducts.length > 0) {
                return mProducts[position];
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(D) Log.v(TAG, "getView");
//            if(D) Log.d(TAG, "position = " + position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_products, null);

                holder = new ViewHolder();

                holder.image     = (ImageView) convertView.findViewById(R.id.iv_product_image);
                holder.name      = (TextView) convertView.findViewById(R.id.tv_product_name);
                holder.imagesCnt = (TextView) convertView.findViewById(R.id.tv_images_cnt);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Product productObj = getItem(position);
            int imagesCnt = productObj.getImagesCnt();
            String imagesCntStr = String.format(getString(R.string.images_cnt), imagesCnt);
            String imageUri = productObj.getImages()[0].getPathThumb();

            holder.name.setText(productObj.getName());
            holder.imagesCnt.setText(imagesCntStr);

            mImageLoader.displayImage(imageUri, holder.image);

            return convertView;
        }

        private class ViewHolder{
            private ImageView image;
            private TextView name;
            private TextView imagesCnt;
        }
    }
}
