package com.shipgig.thegun.pos.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn769.Numpad;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.fragment.CartFragment;
import com.shipgig.thegun.pos.model.AddItemCartModel;
import com.shipgig.thegun.pos.model.Products;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import static com.shipgig.thegun.pos.fragment.CartFragment.addItemCartArray;
import static com.shipgig.thegun.pos.fragment.NewHomeFragment.etSearchCustomer;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder> {

    public static ArrayList<Products> productsArray ;//= new ArrayList<>();
    Context context;
    private CallBackUs mCallBackus;
    StringBuffer sb;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public HomeProductAdapter(ArrayList<Products> productsArray, Context context, CallBackUs mCallBackus) {
        this.context = context;
        this.productsArray = productsArray;
        this.mCallBackus = mCallBackus;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        String encodedString = productsArray.get(position).getImagePath();
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,encodeByte.length);
            if (bitmap != null){
                holder.productImage.setImageBitmap(bitmap);
            }
            else {
                holder.productImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.samosa));
            }
        }catch (Exception e){
            e.printStackTrace();
//            holder.productImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.samosa));
        }

//        if (encodedString.equalsIgnoreCase("")){
//            holder.productImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.samosa));
//        }
//        else {
//            try {
//                byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
//                        encodeByte.length);
//                if (!bitmap.equals("")){
//                    holder.productImage.setImageBitmap(bitmap);
//                }
//                else {
//                    Toast.makeText(context, "Wrong", Toast.LENGTH_SHORT).show();
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }

        holder.productName.setText(productsArray.get(position).getProductName());
        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!etSearchCustomer.getText().toString().equals("")){

                    final Dialog dialog12 = new Dialog(context);
                    dialog12.setContentView(R.layout.dialog_custom_keyboard);
                    dialog12.setTitle("Title...");

                    TextView edtTextQuantity= dialog12.findViewById(R.id.edt_txt_quantity);
                    int max = Integer.parseInt(productsArray.get(position).getTotalQty());
                    TextView availableQty = dialog12.findViewById(R.id.availableQty);
                    availableQty.setVisibility(View.VISIBLE);
                    availableQty.setText("Available Qty :- " + max);
                    Numpad numpad = dialog12.findViewById(R.id.num);
                    numpad.setTextLengthLimit(15);
                    numpad.setBackgroundRes(R.drawable.custome_keyboard_grid_border);
                    numpad.setOnTextChangeListner((String text, int digits_remaining) -> {
                        Log.d("input",(text)+"  "+digits_remaining);

                        removeZero(text);

                        edtTextQuantity.setText((sb));


                    });
                    ImageView dialogQuantityClose= dialog12.findViewById(R.id.quantity_dialog_close);
                    dialogQuantityClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog12.dismiss();
                        }
                    });
                    TextView dialogAddButton= dialog12.findViewById(R.id.dialog_add_quantity);
                    dialogAddButton.setVisibility(View.VISIBLE);
                    dialogAddButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (edtTextQuantity.getText().toString().equals("") || Integer.parseInt(edtTextQuantity.getText().toString()) < 1) {
                                Toast.makeText(context, "Add some quantity", Toast.LENGTH_SHORT).show();
                            } else {

                                if (Integer.parseInt(edtTextQuantity.getText().toString()) <= max){
                                    mCallBackus.addCartItemView();
                                    productsArray.get(position).setQuantity(edtTextQuantity.getText().toString());
                                    AddItemCartModel addItemCartModel = new AddItemCartModel();
                                    addItemCartModel.setProductName(productsArray.get(position).getProductName());
                                    addItemCartModel.setProduct_Id(productsArray.get(position).getProduct_Id());
                                    addItemCartModel.setDiscount(productsArray.get(position).getDiscount());
                                    addItemCartModel.setSrNo(String.valueOf("SR."));
                                    addItemCartModel.setProductPrice(productsArray.get(position).getPrice());
                                    addItemCartModel.setTotalQty(productsArray.get(position).getTotalQty());
                                    addItemCartModel.setQuantity(productsArray.get(position).getQuantity());
                                    addItemCartModel.setHsn_Number(productsArray.get(position).getHsn_No());
                                    addItemCartModel.setGst_Percent(productsArray.get(position).gst_Percent);
                                    addItemCartArray.add(addItemCartModel);
                                    CartFragment fragment2 = new CartFragment();
                                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.cart_fragment, fragment2);
                                    fragmentTransaction.commit();
                                }
                                else {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                                    dialog.setTitle(Html.fromHtml("<font color='#FF7F27'> Alert..!!! </font>"));
                                    dialog.setCancelable(false);
                                    dialog.setMessage("Sorry, you have exceed limit. Your stock is.. "+max +"  Thank you");
                                    dialog.setPositiveButton(
                                            "Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alertDialog = dialog.create();
                                    alertDialog.show();
                                }
                                dialog12.dismiss();
                            }
                        }
                    });
                    dialog12.show();

                }
                else {
                    Toast.makeText(context, "First select or add customer", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        Log.d("sizeproduct", String.valueOf(productsArray.size()));
        return productsArray.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;

        ViewHolder(View itemView) {
            super(itemView);
            productImage= itemView.findViewById(R.id.android_gridview_image);
            productName=itemView.findViewById(R.id.android_gridview_text);
        }
    }
    public interface CallBackUs{
        void addCartItemView();
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
