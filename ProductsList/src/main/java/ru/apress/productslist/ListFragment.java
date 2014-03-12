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


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class ListFragment extends Fragment {
    private final String TAG = "ListFragment";
    private final boolean D = true;

    private ListView mProductsList;
    private Button mClearBtn;

    private ProductObj[] mProductObjs;
    private ProductsAdapter mAdapter;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.v(TAG, "onCreate");
        mAdapter = new ProductsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        if(D) Log.v(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mProductsList = (ListView) view.findViewById(R.id.lv_products);
        mClearBtn = (Button) view.findViewById(R.id.btn_clear);

        mProductsList.setAdapter(mAdapter);

        return view;
    }


    public void updateWithNewData(ProductObj[] productObjs) {
        if(D) Log.v(TAG, "updateWithNewData");
        mProductObjs = productObjs;
        mAdapter.notifyDataSetChanged();
    }

    private class ProductsAdapter extends BaseAdapter {
        private final String TAG = "ProductsAdapter";

        private LayoutInflater mInflater;

        private ProductsAdapter() {

            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (mProductObjs != null) {
                return mProductObjs.length;
            } else {
                return 0;
            }
        }

        @Override
        public ProductObj getItem(int position) {
            if (mProductObjs != null && mProductObjs.length > 0) {
                return mProductObjs[position];
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
            if(D) Log.d(TAG, "position = " + position);
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

            ProductObj productObj = getItem(position);

            holder.name.setText(productObj.getName());
            holder.imagesCnt.setText(String.valueOf(productObj.getImagesCnt()));

            return convertView;
        }

        private class ViewHolder{
            private ImageView image;
            private TextView name;
            private TextView imagesCnt;
        }
    }
}
