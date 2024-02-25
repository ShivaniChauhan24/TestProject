package com.shipgig.thegun.pos.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fxn769.Numpad;
import com.shipgig.thegun.pos.Payment.ProceedtoPayActivity;
import com.shipgig.thegun.pos.activities.AddCustomer;
import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.activities.SummaryActivity;
import com.shipgig.thegun.pos.adapter.AddItemCartAdapter;
import com.shipgig.thegun.pos.adapter.AutoCompleteCustomerAdapter;
import com.shipgig.thegun.pos.adapter.HomeProductAdapter;
import com.shipgig.thegun.pos.model.AddItemCartModel;
import com.shipgig.thegun.pos.model.ManageCustomerDetailsModel;
import com.shipgig.thegun.pos.model.Products;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.shipgig.thegun.pos.MainActivity.drawer;



public class NewHomeFragment extends Fragment implements HomeProductAdapter.CallBackUs , ProceedtoPayActivity.CallBackUs {

    Context context;
    @BindView(R.id.txt_view_item_categories)
    Spinner categorySpinner;
    @BindView(R.id.search_product)
    EditText etSearchProduct;
//    @BindView(R.id.search_customer)
    public static AutoCompleteTextView etSearchCustomer;
    @BindView(R.id.remove_all_item)
    ImageView deleteCartItems;
    @BindView(R.id.recylegrid_view)
    RecyclerView productGridView;
    @BindView(R.id.addCartRecyclerView)
    RecyclerView addCartRecyclerView;
//    @BindView(R.id.add_customer)
    Button addCustomer;
    //@BindView(R.id.txt_item_in_cart)
    public static TextView itemInCart;
    public static TextView totalAmountInPayButton;
    public static TextView totalDiscount;
    public static TextView subTotal;
    public static TextView cgstAmount;
    public static String globalTakendiscountofPercent = "na";
    public static String globalTakendiscountofRupees = "na";

    @BindView(R.id.sign_totalPrice)
    TextView totalPrice;
    @BindView(R.id.ll_checkpoint)
    LinearLayout payButton;
    StringBuffer sb;
    Unbinder unbinder;
    HomeProductAdapter homeProductAdapter;
    AddItemCartAdapter addItemCartAdapter;
    ArrayList<AddItemCartModel> addItemCartArray = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();
    private List<ManageCustomerDetailsModel> customerArray= new ArrayList<>();
    private View myView;
    private ArrayList<Products> productArray = new ArrayList<>();
    ManageCustomerDetailsModel manageCustomerDetailsModel;
    private Products products;
    private double payMentDiscount;
    private Spinner categoryMenu;
    public static String category;
    public static String categoryDynamic;
    private JSONArray jsonArray = new JSONArray();
    public static String discountMode = "";
    private String paymentMode;
    private String discountInsertDB;
    private String customerIdForTransaction;
    TextView selected_user_show;
    ImageView search_imageview,open_main_drawer;
    public static int maxQuantity;
    private ProgressBar progressBar;
    Dialog dialog;
    Double discountPercent;
    String cutomer_mobile,firstname,customer_mail,customer_id,lastname;
    ProgressDialog dialogP;
    Double purDiscount;
    private static ImageView remove_discount_icon;
    private static ImageView discount_icon;
    private static TextView inputypeTxt,discountTxt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.home_exe, container, false);
        unbinder = ButterKnife.bind(this, myView);
        progressBar = myView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = getActivity();

        categoryMenu=myView.findViewById(R.id.txt_view_item_categories);
        etSearchCustomer=myView.findViewById(R.id.search_customer);
        itemInCart= myView.findViewById(R.id.txt_item_in_cart);
        subTotal= myView.findViewById(R.id.txt_view_subtotal);
        totalAmountInPayButton= myView.findViewById(R.id.total_amount);
        totalDiscount= myView.findViewById(R.id.txt_view_discount);
        cgstAmount= myView.findViewById(R.id.txt_view_cgst_amt);
        selected_user_show = myView.findViewById(R.id.selected_user_show);
        search_imageview = myView.findViewById(R.id.search_imageview);
        discountTxt = myView.findViewById(R.id.discount);
        discount_icon = myView.findViewById(R.id.discount_icon);
        open_main_drawer = myView.findViewById(R.id.open_drawer);
        addCustomer = myView.findViewById(R.id.add_customer);
        remove_discount_icon = myView.findViewById(R.id.remove_discount_icon);
        inputypeTxt = myView.findViewById(R.id.inputypeTxt);

        list.add(0,"All  Categories");
        initCategory();

        CartFragment.addItemCartArray.clear();

        search_imageview.setVisibility(View.GONE);
        selected_user_show.setVisibility(View.GONE);
        etSearchCustomer.setVisibility(View.VISIBLE);
        remove_discount_icon.setVisibility(View.GONE);
        inputypeTxt.setVisibility(View.GONE);

        search_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selected_user_show.setVisibility(View.GONE);
                search_imageview.setVisibility(View.GONE);
                etSearchCustomer.setVisibility(View.VISIBLE);
                etSearchCustomer.setText("");

            }
        });

        remove_discount_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeDisCall();

                try {
                    String subamount = subTotal.getText().toString();
                    totalAmountInPayButton.setText(subamount);
                    totalDiscount.setText("0.0");
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }catch (NullPointerException e1){
                    e1.printStackTrace();
                }

            }
        });

        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addcustomerintent = new Intent(getActivity(), AddCustomer.class);
                startActivity(addcustomerintent);
            }
        });

        discount_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedDiscount();
            }
        });

        open_main_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

        return myView;
    }

    public static void removeDisCall() {
        remove_discount_icon.setVisibility(View.GONE);
        discount_icon.setVisibility(View.VISIBLE);
        discountTxt.setVisibility(View.VISIBLE);
        discountTxt.setText(" Add discount");
        inputypeTxt.setVisibility(View.GONE);
        globalTakendiscountofPercent = null;
        globalTakendiscountofRupees = null;
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void addedDiscount() {

        if ((subTotal.getText().toString().equals(""))) {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle(Html.fromHtml("<font color='#FF7F27'>There is no item in cart</font>"));
            alert.setCancelable(false);
            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.create();
            alert.show();
        }
        else {

            if (Double.parseDouble(subTotal.getText().toString()) <1 ){
                Toast.makeText(context, "Add item in cart", Toast.LENGTH_SHORT).show();
            } else {
                final Dialog dialog12 = new Dialog(context);
                dialog12.setContentView(R.layout.dialog_custom_keyboard);
                dialog12.setTitle("Title...");
                dialog12.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                RadioGroup radioGroup = dialog12.findViewById(R.id.radio_group_special_discount);
                LinearLayout linearRadioBox = dialog12.findViewById(R.id.discount_radio_box);
                TextView quantityTextView = dialog12.findViewById(R.id.quantity_text_view);
                TextView discountTextView = dialog12.findViewById(R.id.discount_text_view);
                TextView applyDicountButton = dialog12.findViewById(R.id.dialog_apply_discount);
                TextView quantityUpdateButton = dialog12.findViewById(R.id.dialog_update_quantity);
                RadioButton percentDiscount = dialog12.findViewById(R.id.discount_in_percent);
                RadioButton rupeeDiscount = dialog12.findViewById(R.id.discount_in_money);
                linearRadioBox.setVisibility(View.VISIBLE);
                quantityTextView.setVisibility(View.GONE);
                discountTextView.setVisibility(View.VISIBLE);
                quantityUpdateButton.setVisibility(View.GONE);
                applyDicountButton.setVisibility(View.VISIBLE);
                TextView edtTextQuantity = dialog12.findViewById(R.id.edt_txt_quantity);
                Numpad numpad = dialog12.findViewById(R.id.num);
                numpad.setTextLengthLimit(3);
                numpad.setBackgroundRes(R.drawable.custome_keyboard_grid_border);
                numpad.setOnTextChangeListner((String text, int digits_remaining) -> {
                    Log.d("input", (text) + "  " + digits_remaining);

                    removeZero(text);

                    edtTextQuantity.setText((sb));
                });
                ImageView dialogQuantityClose = dialog12.findViewById(R.id.quantity_dialog_close);
                dialogQuantityClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog12.dismiss();
                    }
                });
                TextView dialogUpdateButton = dialog12.findViewById(R.id.dialog_apply_discount);
                dialogUpdateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtTextQuantity.getText().toString().equals("") || Integer.parseInt(edtTextQuantity.getText().toString()) < 1) {
                            Toast.makeText(context, "Add discount value", Toast.LENGTH_SHORT).show();
                        } else {

                                try {
                                    if (discountMode.equals("percent")) {

                                        if (Integer.parseInt(edtTextQuantity.getText().toString()) > 100){

                                            Toast.makeText(context, "Sorry, You can't enter more than 100%", Toast.LENGTH_SHORT).show();
                                        }
                                        else {

                                            try {
                                                Double am = Double.valueOf(payMentDiscount);
                                                String inputValue = edtTextQuantity.getText().toString();
                                                if (Double.valueOf(inputValue) <= 100){
                                                    globalTakendiscountofPercent = edtTextQuantity.getText().toString();
                                                    discountPercent = Double.valueOf(edtTextQuantity.getText().toString());
                                                    purDiscount = ((payMentDiscount)* Double.valueOf(edtTextQuantity.getText().toString()))/100;
//                                                    totalDiscount.setText(String.valueOf(purDiscount+" ("+inputValue+"%)"));
                                                    totalDiscount.setText(String.valueOf(purDiscount));
                                                    inputypeTxt.setVisibility(View.VISIBLE);
                                                    inputypeTxt.setText(inputValue+"( %)");
                                                    payMentDiscount = am - purDiscount;
                                                    remove_discount_icon.setVisibility(View.VISIBLE);
                                                    discount_icon.setVisibility(View.GONE);
                                                    discountTxt.setText("Remove discount");
                                                    subTotal.setText(String.valueOf(am));
                                                }
                                                else {
                                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                                    alert.setTitle(Html.fromHtml("<font color='#FF7F27'>You can't give discount is more than amount</font>"));
                                                    alert.setCancelable(false);

                                                    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });
                                                    alert.create();
                                                    alert.show();
                                                }
                                            } catch (NumberFormatException e) {
                                                Toast.makeText(context, "Enter the discount value ", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }

                                    else if (discountMode.equals("rupees")) {
                                        try {
                                            String inputValue = edtTextQuantity.getText().toString();
                                            if (payMentDiscount >= Double.valueOf(inputValue)){
                                                globalTakendiscountofRupees = edtTextQuantity.getText().toString();
                                                payMentDiscount -= Double.parseDouble(edtTextQuantity.getText().toString());
                                                discountInsertDB = String.valueOf(Double.parseDouble(edtTextQuantity.getText().toString()));
//                                                totalDiscount.setText(discountInsertDB + "( Rs.)");
                                                totalDiscount.setText(discountInsertDB);
                                                inputypeTxt.setVisibility(View.VISIBLE);
                                                inputypeTxt.setText("(Rs.)");
                                                remove_discount_icon.setVisibility(View.VISIBLE);
                                                discount_icon.setVisibility(View.GONE);
                                                discountTxt.setText("Remove discount");
                                            }else {
                                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                                alert.setTitle(Html.fromHtml("<font color='#FF7F27'>You can't give discount is more than amount</font>"));
                                                alert.setCancelable(false);

                                                alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                                alert.create();
                                                alert.show();
                                            }

                                        } catch (NumberFormatException e) {
                                            Toast.makeText(context, "Enter the discount value ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    totalAmountInPayButton.setText(String.valueOf(payMentDiscount));
                                    dialog12.dismiss();
                                } catch (NullPointerException e) {
                                    Toast.makeText(context, "Select discount mode", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                });
                payMentDiscount = Double.parseDouble(subTotal.getText().toString());

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // find which radio button is selected
                        if (checkedId == R.id.discount_in_percent) {
                            rupeeDiscount.setBackgroundColor(getResources().getColor(R.color.radio_button_color));
                            percentDiscount.setBackgroundColor(getResources().getColor(R.color.peripherals_button_color_blue));
                            discountMode = "percent";

                            Log.d("discountthegun", String.valueOf(payMentDiscount) + " " + subTotal.getText().toString() + " " + edtTextQuantity.getText().toString());
                        } else if (checkedId == R.id.discount_in_money) {
                            percentDiscount.setBackgroundColor(getResources().getColor(R.color.radio_button_color));
                            rupeeDiscount.setBackgroundColor(getResources().getColor(R.color.peripherals_button_color_blue));
                            discountMode = "rupees";
                        }
                    }

                });

                dialog12.show();

            }
        }

    }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~



    @OnClick(R.id.remove_all_item)
    public void removeAllItemFromCart(View view) {

        try {
            if (AddItemCartAdapter.reports.size() > 0 ) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(Html.fromHtml("<font color='#FF7F27'>Do you want to remove all items?</font>"));
                alert.setCancelable(false);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddItemCartAdapter.reports.clear();

                        ((ViewGroup) addCartRecyclerView.getParent()).removeAllViews();
                        itemInCart.setText("");
                        totalAmountInPayButton.setText("");
                        subTotal.setText("");
                        Log.d("cartsizeremovethegun", String.valueOf(AddItemCartAdapter.reports.size()));
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                        CartFragment.addItemCartArray.clear();

                    }
                });


                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
                // break;
            }
            else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setCancelable(false);
                alertDialog.setTitle(Html.fromHtml("<font color='#FF7F27'>There is no item for remove in cart!</font>"));
                alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.create();
                alertDialog.show();
            }

        }catch (NullPointerException e){
            e.printStackTrace();
            Snackbar snackbar = (Snackbar) Snackbar.make(view,"Please first add item in your cart",Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    private void initCategory() {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading_data);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        ImageView imageGif = dialog.findViewById(R.id.image_gif);
        Glide.with(getContext()).load(R.drawable.check1)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.check1)
                        .fitCenter())
                .into(imageGif);
        dialog.show();

        ArrayAdapter dataAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryMenu.setAdapter(dataAdapter);
        categoryMenu.setDropDownHorizontalOffset(10);

        categoryMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String categaryItem= categoryMenu.getSelectedItem().toString();
                category=categaryItem;
                if (categaryItem.equals(list.get(arg2))) {
                    Log.d("categoryitem",categaryItem+"\n"+list.get(arg2));

                    if (productArray!=null) {
                        productArray.clear();
                    }
                    setgridViewString(categaryItem);
                    init();

                    Toast.makeText(context, categaryItem, Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void filter(String text) {
        dialogP = new ProgressDialog(getActivity());
        dialogP.setTitle("Searching");
        dialogP.setMessage("Please wait,to get your search result");
        dialogP.show();
            ArrayList<Products> filteredList = new ArrayList<>();
            for (Products item : productArray) {

                if (item.getProductName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                    Log.d("TA", "filter: "+filteredList.size());
                }
            }

            productGridView.setAdapter(new HomeProductAdapter(filteredList, context,this));
            dialogP.dismiss();

    }

    private void init() {
        seeCustomerEditdata();
        Log.d("cuarraysizethrgun", String.valueOf(customerArray.size()));
        AutoCompleteCustomerAdapter adapter = new AutoCompleteCustomerAdapter(context, customerArray);
        etSearchCustomer.setAdapter(adapter);

        etSearchCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                firstname = customerArray.get(position).getFirstName();
                lastname = customerArray.get(position).getLastName();
                cutomer_mobile = customerArray.get(position).getMobileNumber();
                customer_mail = customerArray.get(position).getEmailAddress();
                customer_id = customerArray.get(position).getCustomerId();

                if (!firstname.equalsIgnoreCase("")){
                    etSearchCustomer.setVisibility(View.GONE);
                    selected_user_show.setVisibility(View.VISIBLE);
                    search_imageview.setVisibility(View.VISIBLE);
                    selected_user_show.setText(firstname);
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            }
        });


        etSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() >= 4){
                    filter(s.toString());
                    dialogP.dismiss();
                }
                else if (s.toString().length() == 0){
                    filter(s.toString());
                    dialogP.dismiss();
                }

            }
        });

        homeProductAdapter = new HomeProductAdapter(productArray, context, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 5, LinearLayoutManager.VERTICAL, false);
        productGridView.setLayoutManager(gridLayoutManager);
        productGridView.setAdapter(homeProductAdapter);


    }

    private void addItemToCartMethod() {
        addItemCartAdapter = new AddItemCartAdapter(addItemCartArray, context,1);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        addCartRecyclerView.setLayoutManager(verticalLayoutManager);
        addCartRecyclerView.setAdapter(addItemCartAdapter);
        addItemCartAdapter.notifyDataSetChanged();

    }

    private void setgridViewString(String category) {


        ((MainActivity) getActivity()).getData(category);
        Log.d("categ",category);
        for (int i = 0; i < ((MainActivity) getActivity()).jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = ((MainActivity) getActivity()).jsonArray.getJSONObject(i);
                products = new Products();

                products.setTotalQty(jsonobject.getString("totalstock"));
                products.product_Id = jsonobject.getString("product_ID");
                products.productName = jsonobject.getString("productName");
                products.price = jsonobject.getString("unitPrice");
                products.discount = jsonobject.getString("discount");
                products.amount = jsonobject.getString("unitPrice");
                products.totalQty = jsonobject.getString("totalstock");
                products.hsn_No = jsonobject.getString("HSN_ID");
                products.gst_Percent = jsonobject.getInt("gst_Cat_ID");
                maxQuantity = Integer.parseInt(products.quantity = jsonobject.getString("totalstock"));
                categoryDynamic = jsonobject.getString("category");
                products.imagePath = jsonobject.getString("image_path");
                productArray.add(products);
                list.add(categoryDynamic);
                Set<String> set = new LinkedHashSet<>(list);
                list.clear();
                list.addAll(set);
                Collections.sort(list);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NumberFormatException e){
                e.printStackTrace();
            }

        }
        dialog.dismiss();
    }


//Final Pay button********************************************************************************************************************************
    @OnClick(R.id.ll_checkpoint)
    public void onPayButtonClick() {

        if (Double.parseDouble(totalAmountInPayButton.getText().toString()) < 1 ) {
            try {
                if (discountPercent == 100){
                    Intent intent = new Intent(context, SummaryActivity.class);
                    intent.putExtra("noofitems",itemInCart.getText().toString().trim());
                    intent.putExtra("totalamt",totalAmountInPayButton.getText().toString().trim());
                    intent.putExtra("taxes",cgstAmount.getText().toString());
                    intent.putExtra("discountamt",totalDiscount.getText().toString());
                    intent.putExtra("subtotalamt",subTotal.getText().toString());
                    intent.putExtra("cus_name",firstname);
                    intent.putExtra("cus_email",customer_mail);
                    intent.putExtra("cus_mobile",cutomer_mobile);
                    intent.putExtra("cus_id",customer_id);
                    context.startActivity(intent);

                }
                else {

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle(Html.fromHtml("<font color='#FF7F27'>No items are selected!</font>"));
                    alertDialog.setCancelable(false);
                    alertDialog.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>Ok</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                }

            }catch (NullPointerException e){
                Toast.makeText(context, "Please select item and customer name then proceed", Toast.LENGTH_SHORT).show();
            }

        }

        else if (etSearchCustomer.getText().toString().equals("")) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle(Html.fromHtml("<font color='#FF7F27'>No customer is selected!</font>"));
            alertDialog.setCancelable(false);
            alertDialog.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>Ok</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.create();
            alertDialog.show();
        }

        else {

            Intent intent = new Intent(context, SummaryActivity.class);
            intent.putExtra("noofitems",itemInCart.getText().toString().trim());
            intent.putExtra("totalamt",totalAmountInPayButton.getText().toString().trim());
            intent.putExtra("taxes",cgstAmount.getText().toString());
            intent.putExtra("discountamt",totalDiscount.getText().toString());
            intent.putExtra("subtotalamt",subTotal.getText().toString());
            intent.putExtra("cus_name",firstname);
            intent.putExtra("cus_email",customer_mail);
            intent.putExtra("cus_mobile",cutomer_mobile);
            intent.putExtra("cus_id",customer_id);
            context.startActivity(intent);
        }

    }
//Final Pay button********************************************************************************************************************************

    @SuppressLint("WrongConstant")
    private void seeCustomerEditdata() {
        try {
            SQLiteDatabase mydatabase = SQLiteDatabase.openDatabase("/data/data/" + getContext().getPackageName() + "/newinventory.db", null, 0);
            mydatabase.beginTransaction();
            Cursor cursor = mydatabase.rawQuery("SELECT * from tbl_custuser", null);
            mydatabase.endTransaction();
            jsonArray = cur2Json(cursor);
            Log.e("customer data::", "customer data " + jsonArray);
            setDataAdptr(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setDataAdptr(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
                manageCustomerDetailsModel = new ManageCustomerDetailsModel();
                manageCustomerDetailsModel.setSrno(jsonobject.getString("custUser_ID"));
                manageCustomerDetailsModel.setCustomerId(jsonobject.getString("custUser_ID"));
                manageCustomerDetailsModel.setFirstName(jsonobject.getString("firstName"));
                manageCustomerDetailsModel.setLastName(jsonobject.getString("lastName"));
                manageCustomerDetailsModel.setMobileNumber(jsonobject.getString("mobile"));
                manageCustomerDetailsModel.setGednder(jsonobject.getString("gender"));
                manageCustomerDetailsModel.setEmailAddress(jsonobject.getString("email"));
                customerArray.add(manageCustomerDetailsModel);
                customerIdForTransaction= customerArray.get(i).getCustomerId();
                Log.d("cidthegun",customerIdForTransaction);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public JSONArray cur2Json(Cursor cursor) {
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                        Log.e("parveen112", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        CartFragment.addItemCartArray.clear();
    }

    @Override
    public void addCartItemView() {
        addItemToCartMethod();
    }

    @Override
    public void insertinTable() {
        Toast.makeText(context, "success insert", Toast.LENGTH_SHORT).show();
    }

    public void removeZero(String str) {
        int i = 0;
        while (i < str.length() && str.charAt(i) == '0')
            i++;

        sb = new StringBuffer(str);

        sb.replace(0, i, "");

        sb.toString();

    }

}