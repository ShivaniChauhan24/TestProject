//package com.shipgig.thegun.pos.fragment;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.shipgig.thegun.pos.R;
//import com.shipgig.thegun.pos.adapter.ManageInventoryDetailsAdapter;
//import com.shipgig.thegun.pos.inventoryAPI.Data;
//import com.shipgig.thegun.pos.inventoryAPI.Result;
//import com.shipgig.thegun.pos.inventoryAPI.SendInventoryData;
//import com.shipgig.thegun.pos.inventoryAPI.StockResponse;
//import com.shipgig.thegun.pos.model.ManageInventoryModel;
//import com.shipgig.thegun.pos.utilities.RetrofitClient;
//import com.shipgig.thegun.pos.utilities.Sharepreference;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import static com.shipgig.thegun.pos.MainActivity.drawer;
//
///**
// * Created by Afroz on 18/07/19.
// */
//public class InventoryFT extends Fragment implements View.OnClickListener, ManageInventoryDetailsAdapter.CallBackUs {
//
//    View myView;
//    public JSONArray jsonArrayInventory = new JSONArray();
//    RecyclerView recyclerView;
//    ManageInventoryDetailsAdapter inventroyDetailsAdpter;
//    private ArrayList<ManageInventoryModel> searchData = new ArrayList<>();
//    ManageInventoryModel manageInventoryModel;
//    boolean isLoading = false;
//    Context  context;
//    EditText search;
//    ImageView open_drawer;
//    TextView nextBtn, prevBtn;
//    TextView pageNumber;
//    ProgressDialog dialog;
//    String token;
//    ArrayList<ManageInventoryModel> myArray;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
//
//        myView = inflater.inflate(R.layout.manage_inventory, container, false);
//
//        token  =  Sharepreference.getSharedPreferenceString(getActivity(),"usertoken",null);
//        dialog = new ProgressDialog(getActivity());
//        init();
//        open_drawer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawer.openDrawer(Gravity.START);
//            }
//        });
//
//        seeInventorydata(20,0);
//
////        populateData();
////        initScrollListener();
////        searchView();
//
//        return myView;
//    }
//
//    private void initScrollListener() {
//
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//                if (!isLoading) {
//                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == searchData.size() - 1) {
//                        //bottom of list!
//                        loadMore();
//                        isLoading = true;
//                    }
//                }
//            }
//        });
//    }
//
//    private void loadMore() {
//
//        searchData.add(null);
//        inventroyDetailsAdpter.notifyItemInserted(searchData.size() - 1);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                searchData.remove(searchData.size() - 1);
//                int scrollPosition = searchData.size();
//                inventroyDetailsAdpter.notifyItemRemoved(scrollPosition);
//                int currentSize = scrollPosition -1;
//                seeInventorydata(20,currentSize);
//                int nextLimit = currentSize + 20;
//                while (currentSize - 1 < nextLimit) {
//                    JSONObject jsonobject = null;
//                    try {
//                        searchData.add(myArray);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    searchData.add(manageInventoryModel);
//                    currentSize++;
//                }
//                inventroyDetailsAdpter.notifyDataSetChanged();
//                isLoading = false;
//            }
//        }, 2000);
//
//    }
//
//    private void initAdapter() {
//        inventroyDetailsAdpter = new ManageInventoryDetailsAdapter(searchData, context,this);
//        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(verticalLayoutManager);
//        recyclerView.setAdapter(inventroyDetailsAdpter);
//    }
//
//    private void populateData() {
//
//        int i = 0;
//        while (i < 20) {
//            JSONObject jsonobject = null;
//            try {
//                jsonobject = jsonArrayInventory.getJSONObject(i);
//                manageInventoryModel = new ManageInventoryModel();
//                ((ManageInventoryModel) manageInventoryModel).setSrno((String.valueOf(i)));
//                manageInventoryModel.setProdict(jsonobject.optString("productName"));
//                manageInventoryModel.setQuantity(jsonobject.optString("totalstock"));
//                manageInventoryModel.setProductId(jsonobject.optString("product_ID"));
//                manageInventoryModel.setDiscount(jsonobject.optString("discount"));
//                manageInventoryModel.setUnitPrice(jsonobject.optString("unitPrice"));
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            searchData.add(manageInventoryModel);
//            i++;
//        }
//
//    }
//
//    private void init() {
//
//        open_drawer = myView.findViewById(R.id.open_drawer);
//        recyclerView = myView.findViewById(R.id.v_recyclerView);
//        search = myView.findViewById(R.id.search);
//        pageNumber = myView.findViewById(R.id.number);
//        nextBtn =  myView.findViewById(R.id.next);
//        prevBtn =  myView.findViewById(R.id.previous);
//        prevBtn.setEnabled(false);
//    }
//
//    private void refresh() {
//        inventroyDetailsAdpter = new ManageInventoryDetailsAdapter(searchData, context,this);
//        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(verticalLayoutManager);
//        recyclerView.setAdapter(inventroyDetailsAdpter);
//
//    }
//
//    private void searchView() {
//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                filter(s.toString());
//            }
//        });
//
//    }
//
//    private void filter(String text) {
//        ArrayList<Result> filteredList = new ArrayList<>();
//        for (Result item : searchData) {
//
//            if (item.getProductName().toLowerCase().contains(text.toLowerCase())) {
//                filteredList.add(item);
//                Log.d("TA", "filter: "+filteredList.size());
//            }
//        }
//
//        recyclerView.setAdapter(new ManageInventoryDetailsAdapter(filteredList, context,this));
//        // inventroyDetailsAdpter.filterList(filteredList);
//    }
//
//    public void seeInventorydata(int limit, int skip ) {
//
//        dialog.setTitle("Data Showing....");
//        dialog.setMessage("Please wait while fetching data....");
//        dialog.show();
//        SendInventoryData sendInventoryData = new SendInventoryData();
//        sendInventoryData.setSki(skip);
//        sendInventoryData.setLim(limit);
//        String fTk = "\""+token+"\"";
//        Call<StockResponse> call = RetrofitClient.getInstance().getApi().stockReport(fTk,sendInventoryData);
//        call.enqueue(new Callback<StockResponse>() {
//            @Override
//            public void onResponse(Call<StockResponse> call, Response<StockResponse> response) {
//
//                String msg = response.raw().message().toString();
//                if (response != null){
//                    String status = response.body().getStatus();
//                    int code = response.body().getCode();
//                    Data data = response.body().getData();
//                    myArray = response.body().getData().getResult();
//                    searchData = new ArrayList<>(myArray);
//                    initAdapter();
//                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<StockResponse> call, Throwable t) {
//                String error = t.getMessage().toString();
//                Toast.makeText(getActivity(), "FAILED:-"+error, Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//
//
//    }
//
//    public JSONArray cur2Json(Cursor cursor) {
//        JSONArray resultSet = new JSONArray();
//        cursor.moveToFirst();
//        while (cursor.isAfterLast() == false) {
//            int totalColumn = cursor.getColumnCount();
//            JSONObject rowObject = new JSONObject();
//            for (int i = 0; i < totalColumn; i++) {
//                if (cursor.getColumnName(i) != null) {
//                    try {
//                        rowObject.put(cursor.getColumnName(i),
//                                cursor.getString(i));
//                    } catch (Exception e) {
//                        Log.e("thegun", e.getMessage());
//                    }
//                }
//            }
//            resultSet.put(rowObject);
//            cursor.moveToNext();
//        }
//
//        cursor.close();
//        return resultSet;
//
//    }
//
//    @Override
//    public void onClick(View v) {
//    }
//
//    @Override
//    public void updateInventoryAfterDelete() {
////        refresh();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d("fragment","resume");
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
////        refresh();
////        inventroyDetailsAdpter.notifyDataSetChanged();
//        Log.d("fragment","start");
//    }
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        Log.d("fragment","restored");
//    }
//
//
//}