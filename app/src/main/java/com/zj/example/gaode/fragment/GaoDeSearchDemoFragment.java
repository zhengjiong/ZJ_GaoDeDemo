package com.zj.example.gaode.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zj.example.gaode.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Title: GaoDeSearchDemoFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/3/9  10:47
 *
 * @author 郑炯
 * @version 1.0
 */
public class GaoDeSearchDemoFragment extends Fragment implements Inputtips.InputtipsListener {
    private TextView mTextView;
    private TextInputEditText mTextInputEditText;
    private TextInputEditText mCityName;

    Inputtips inputTips;

    public static GaoDeSearchDemoFragment newInstance() {

        Bundle args = new Bundle();

        GaoDeSearchDemoFragment fragment = new GaoDeSearchDemoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gaode_layout, container, false);
        mTextView = (TextView) view.findViewById(R.id.textView);
        mTextInputEditText = (TextInputEditText) view.findViewById(R.id.edittext);
        mCityName = (TextInputEditText) view.findViewById(R.id.cityname);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InputtipsQuery inputquery = new InputtipsQuery("徐汇", "上海市");
        inputquery.setCityLimit(true);//限制在当前城市


        inputTips = new Inputtips(getContext(), inputquery);
        inputTips.setInputtipsListener(this);



        RxTextView.textChanges(mTextInputEditText).debounce(2000, TimeUnit.MILLISECONDS).subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                String key = charSequence.toString();
                System.out.println("key=" + key);
                inputTips.setQuery(new InputtipsQuery(key, mCityName.getText().toString()));
                inputTips.requestInputtipsAsyn();
            }
        });
    }

    @Override

    public void onGetInputtips(List<Tip> list, int i) {
        if (list == null) {
            return;
        }
        mTextView.setText("");
        for (Tip tip : list) {

            System.out.println("name="+tip.getName() + " ,address=" + tip.getAddress()
            + " ,poiID" + tip.getPoiID());

            mTextView.append("name="+tip.getName() + " ,address=" + tip.getAddress()
                    + " ,Adcode=" + tip.getAdcode()
                    + " ,district=" + tip.getDistrict()
                    + " ,typeCode=" + tip.getTypeCode()
                    + " ,poiID" + tip.getPoiID());
            if (tip.getPoint() != null) {
                mTextView.append(" ,经纬度=" + tip.getPoint().getLatitude());
                //+ " ,经纬度=" + tip.getPoint() != null ? tip.getPoint().getLatitude() + "" : "空"
            } else {
                mTextView.append(" ,经纬度=null");
            }
            mTextView.append("\n\n\n");
        }
    }
}
