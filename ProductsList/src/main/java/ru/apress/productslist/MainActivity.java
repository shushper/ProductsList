package ru.apress.productslist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity implements StartFragment.StartFragmentListener{

    private StartFragment mStartFragment;
    private ListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        mStartFragment = (StartFragment) fm.findFragmentById(R.id.start_fragment);
        mListFragment = (ListFragment) fm.findFragmentById(R.id.list_fragment);

        hideFragment(mListFragment);
    }

    @Override
    public void onProductsListDownloaded(ProductObj[] productObjs) {
        hideFragment(mStartFragment);
        showFragment(mListFragment);

        mListFragment.updateWithNewData(productObjs);
    }

    private void hideFragment(Fragment f) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(f);
        ft.commit();
    }

    private void showFragment(Fragment f) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(f);
        ft.commit();
    }
}
