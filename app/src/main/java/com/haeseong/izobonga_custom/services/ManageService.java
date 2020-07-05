package com.haeseong.izobonga_custom.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.haeseong.izobonga_custom.FireBaseHelper;
import com.haeseong.izobonga_custom.interfaces.ManageActivityView;
import com.haeseong.izobonga_custom.models.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.haeseong.izobonga_custom.FireBaseHelper.COLLECTION_MANAGER;

public class ManageService {
    private final String TAG = "ManageService";
    private ManageActivityView mManageActivityView;

    public ManageService(final ManageActivityView manageActivityView) {
        this.mManageActivityView = manageActivityView;
    }

    public void setData() {
        final ArrayList<Customer> customers = new ArrayList<>();
        FireBaseHelper.getInstance().collection(COLLECTION_MANAGER).orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Customer customer = document.toObject(Customer.class);
                                customers.add(customer);
                            }
                            mManageActivityView.validateSuccess(customers);
                        } else {
                            Log.d("initWaitingCustomer", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void deleteData(String docID, final int position){
        FireBaseHelper.getInstance().collection(COLLECTION_MANAGER).document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        mManageActivityView.validateSuccessDelete("삭제되었습니다.",position);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
}
