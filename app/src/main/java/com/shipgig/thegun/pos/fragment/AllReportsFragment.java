package com.shipgig.thegun.pos.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.activities.AboutOutOfStockReportsActivity;
import com.shipgig.thegun.pos.activities.AgedInventoryReportsActivity;
import com.shipgig.thegun.pos.activities.CustomerWiseSalesReportsActivity;
import com.shipgig.thegun.pos.activities.ExpenseReportsActivity;
import com.shipgig.thegun.pos.activities.GstReportsActivity;
import com.shipgig.thegun.pos.activities.ItemWiseSalesReportsActivity;
import com.shipgig.thegun.pos.activities.OutOfStockReportsActivity;
import com.shipgig.thegun.pos.activities.PaymentMethodReportsActivity;
import com.shipgig.thegun.pos.activities.ReturnItemReportsActivity;
import com.shipgig.thegun.pos.activities.StockReportsActivity;
import com.shipgig.thegun.pos.activities.TransactionReportsActivity;
import com.shipgig.thegun.pos.activities.VoidBillReportsActivity;

import static com.shipgig.thegun.pos.MainActivity.drawer;


public class AllReportsFragment extends Fragment  implements View.OnClickListener{
    View myView;
    LinearLayout transactionReports,itemWiseReports ,customerWiseReports,aboutOutStock,agedInvewntoryReports,voidbillreports,returnItemReports,
            stockReports,outOfStockReports,expenseReports,paymentMethodReports,gstReports;
    ImageView open_drawer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.all_reports, container, false);

        init();

        open_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

        return myView;

    }

    private void init() {
        transactionReports = myView.findViewById(R.id.ll_transaction_reports);
        itemWiseReports = myView.findViewById(R.id.ll_itemwisereports);
        customerWiseReports = myView.findViewById(R.id.ll_customerwisereports);
        aboutOutStock = myView.findViewById(R.id.ll_abouttooutofstock);
        agedInvewntoryReports = myView.findViewById(R.id.ll_agedInventoryreports);
        voidbillreports = myView.findViewById(R.id.ll_voidbillreports);
        returnItemReports = myView.findViewById(R.id.ll_returnitemreports);
        stockReports = myView.findViewById(R.id.ll_stockreports);
        outOfStockReports = myView.findViewById(R.id.ll_outofstockreports);
        expenseReports = myView.findViewById(R.id.ll_expensereports);
        paymentMethodReports = myView.findViewById(R.id.ll_paymentmethodreports);
        gstReports = myView.findViewById(R.id.ll_gstreports);
        open_drawer = myView.findViewById(R.id.open_drawer);

        transactionReports.setOnClickListener(this);
        itemWiseReports.setOnClickListener(this);
        customerWiseReports.setOnClickListener(this);
        aboutOutStock.setOnClickListener(this);
        agedInvewntoryReports.setOnClickListener(this);
        voidbillreports.setOnClickListener(this);
        returnItemReports.setOnClickListener(this);
        stockReports.setOnClickListener(this);
        outOfStockReports.setOnClickListener(this);
        expenseReports.setOnClickListener(this);
        paymentMethodReports.setOnClickListener(this);
        gstReports.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_transaction_reports:
                Intent intent = new Intent(getActivity(),TransactionReportsActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_itemwisereports:
                Intent intentItemWiseReports = new Intent(getActivity(), ItemWiseSalesReportsActivity.class);
                startActivity(intentItemWiseReports);
                break;

            case R.id.ll_customerwisereports:
                Intent intentCustomerWiseReports = new Intent(getActivity(), CustomerWiseSalesReportsActivity.class);
                startActivity(intentCustomerWiseReports);
                break;

            case R.id.ll_abouttooutofstock:
                Intent intentAboutToOutStockReports = new Intent(getActivity(), AboutOutOfStockReportsActivity.class);
                startActivity(intentAboutToOutStockReports);
                break;

            case R.id.ll_agedInventoryreports:
                Intent intentAgedInvemtory = new Intent(getActivity(), AgedInventoryReportsActivity.class);
                startActivity(intentAgedInvemtory);
                break;

            case R.id.ll_voidbillreports:
                Intent intentVoidbillreports = new Intent(getActivity(),VoidBillReportsActivity.class);
                startActivity(intentVoidbillreports);
                break;

            case R.id.ll_returnitemreports:
                Intent intentReturnitemreports = new Intent(getActivity(), ReturnItemReportsActivity.class);
                startActivity(intentReturnitemreports);
                break;

            case R.id.ll_stockreports:
                Intent intentStockReports = new Intent(getActivity(), StockReportsActivity.class);
                startActivity(intentStockReports);
                break;

            case R.id.ll_outofstockreports:
                Intent intentOutOfStockReports = new Intent(getActivity(), OutOfStockReportsActivity.class);
                startActivity(intentOutOfStockReports);
                break;

            case R.id.ll_expensereports:
                Intent intentExpenseReports = new Intent(getActivity(), ExpenseReportsActivity.class);
                startActivity(intentExpenseReports);
                break;

            case R.id.ll_paymentmethodreports:
                Intent intentPaymentMethod = new Intent(getActivity(),PaymentMethodReportsActivity.class);
                startActivity(intentPaymentMethod);
                break;

            case R.id.ll_gstreports:
                Intent intentGst = new Intent(getActivity(), GstReportsActivity.class);
                startActivity(intentGst);
                break;


        }
    }


}
