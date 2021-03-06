package com.example.electricpower.view;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.electricpower.BaseActivity;
import com.example.electricpower.R;
import com.example.electricpower.adapter.FujinshebeiAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import butterknife.Bind;

public class FujinshebeilibiaoActivity extends BaseActivity implements View.OnClickListener {
    int x = R.layout.activity_fujinshebeiliebiao;

    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.list_fujinshebeiliebiao)
    ListView listFujinshebeiliebiao;
    BluetoothManager bluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    List<ScanResult> list = new ArrayList<>();
    FujinshebeiAdapter fujinshebeiAdapter;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.searchstate_tv)
    TextView searchstateTv;




    @Override
    public void bindListener() {
        backImg.setOnClickListener(this);
        //设备列表每个item点击监听
        listFujinshebeiliebiao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FujinshebeilibiaoActivity.this, ChartTest.class);
                intent.putExtra("name", list.get(position).getDevice().getName());
                startActivity(intent);
                mBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
                Log.d("search cancel", "取消搜索");
                searchstateTv.setText("设备列表");
            }
        });

    }

    @Override
    public void initData() {
        //权限获取
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        //广播注册
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, intentFilter);
        IntentFilter intentFilter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, intentFilter1);
        IntentFilter intentFilter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, intentFilter2);
        Log.d("开始搜索设备！------", "开始搜索设备");
        bluetoothManager= (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter= bluetoothManager.getAdapter();
        //蓝牙权限检测
        doBluetooth();
//        mBluetoothAdapter.startDiscovery();
//        mBluetoothAdapter.startLeScan(mLeScanCallback);
//        scan();
//        mBluetoothAdapter.startLeScan(leScanCallback);
    }

    //扫描
    private void scan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
            Log.d("扫描开始","扫描开始");
        }
//        else {
//            mBluetoothAdapter.startLeScan(leScanCallback);
//        }
    }

    //停止扫描
    private void stopScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
        }
//        else {
//            mBluetoothAdapter.stopLeScan(leScanCallback);
//        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallback=new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            String a = bytesToHexString(scanRecord);
            Log.d("解码",a);
        }
    };

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            String data = bytesToHexString(result.getScanRecord().getBytes());
            Log.d("设备名称：" + result.getDevice().getName(), data);

            if (result.getDevice().getType() == 2) {
                list.add(result);
            }

//            List<ScanResult> templist = new ArrayList<>();
//            for (ScanResult scanResult : list) {
//                if (!list.contains(scanResult)) {
//                    templist.add(scanResult);
//                }
//            }
//            FujinshebeiAdapter adapter = new FujinshebeiAdapter(FujinshebeilibiaoActivity.this, R.layout.item_fujinshebei, list);
//            listFujinshebeiliebiao.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onPause() {
//        unregisterReceiver(mReceiver);
//        mBluetoothAdapter.cancelDiscovery();
//        Log.d("search cancel","取消搜索");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 蓝牙相关权限检测
     */
    private boolean isOpen = false;

    private void doBluetooth() {
        if (mBluetoothAdapter == null) {
            showToast("该设备不支持蓝牙");
        }
        //检查当前是否已启用蓝牙。 如果此方法返回 false，则表示蓝牙处于停用状态。要请求启用蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);

        } else {
            isOpen = true;
            //已扫描过的设备
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    Log.d("已扫描过的设备", "invoked");

//                    list.add(device);
                }
//                FujinshebeiAdapter fujinshebeiAdapter = new FujinshebeiAdapter(mContext, R.layout.item_fujinshebei, list);
//                listFujinshebeiliebiao.setAdapter(fujinshebeiAdapter);
//                fujinshebeiAdapter.notifyDataSetChanged();
            } else {
                Log.d("===", "之前扫描设备为空");
            }
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_fujinshebeiliebiao;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img: {
                finish();
                break;
            }
            default:
                break;
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("onReceive method", "is invoked");
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if (!list.contains(device)) {
//                    list.add(device);
//                }
//
//                FujinshebeiAdapter adapter = new FujinshebeiAdapter(FujinshebeilibiaoActivity.this, R.layout.item_fujinshebei, list);
//                listFujinshebeiliebiao.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
                Log.d("搜索到设备", device.getName() + "==" + device.getAddress());
            } else if (action.equals(mBluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
//                if (Build.MODEL.equals("PADT00")){
//                    mBluetoothAdapter.cancelDiscovery();
//                    unregisterReceiver(mReceiver);
//                }
//                mBluetoothAdapter.cancelDiscovery();
                Log.d("搜索完成！", Build.MODEL + "");
                searchstateTv.setText("设备列表");
            } else if (action.equals(mBluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                Log.d("开始搜索", "start search!");
                searchstateTv.setText("搜索中...");
            }else if (action.equals(mBluetoothAdapter.STATE_ON)){
                Log.d("STATE_ON",action);
            }else if (action.equals(mBluetoothAdapter.STATE_OFF)){
                Log.d("STATE_OFF",action);
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        mBluetoothAdapter.cancelDiscovery();
        Log.d("search cancel", "取消搜索");
        mBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
        super.onDestroy();
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            String wendu = bytesToHexString(scanRecord).substring(1, 30);
            Log.d(device.getName() + "携带数据", bytesToHexString(scanRecord));
            Log.d("信号强度", device.getName() + rssi + "");
            if (!list.contains(device) && device.getType() == 2) {
//                list.add(device);
            }

//            FujinshebeiAdapter adapter = new FujinshebeiAdapter(FujinshebeilibiaoActivity.this, R.layout.item_fujinshebei, list);
//            listFujinshebeiliebiao.setAdapter(adapter);
//            adapter.notifyDataSetChanged();

        }
    };

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}