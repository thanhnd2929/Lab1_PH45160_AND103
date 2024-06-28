package fpoly.ph45160.fbase.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import fpoly.ph45160.fbase.R;

public class MainActivity2 extends AppCompatActivity {

    String id = "";
    ToDo toDo = null;
    FirebaseFirestore database;
    Button btnInsert, btnUpdate, btnDelete;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseFirestore.getInstance(); // khoi tao database
        tvResult = findViewById(R.id.tv1);
        btnInsert = findViewById(R.id.btnInsert);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertFireBase(tvResult);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFireBase(tvResult);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFireBase(tvResult);
            }
        });

        setDataFromFireBase(tvResult);
    }

    public void insertFireBase(TextView tvResult) {
        id = UUID.randomUUID().toString(); // lay 1 id bat ky
        toDo = new ToDo(id, "title 30", "content 30"); // tao doi tuong insert
        // chuyen doi tuong sang co the thao tac voi firebase
        HashMap<String, Object> mapToDo = toDo.convertHashMap();
        // insert vao database
        database.collection("TODO").document(id)
                .set(mapToDo) // doi tuong can insert
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Thêm thành công");
                        setDataFromFireBase(tvResult); // Refresh data
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                })
        ;

    }

    public void updateFireBase(TextView tvResult) {
        id = "1288122d-7a51-4e7d-92eb-5d2ec12a695d";
        toDo = new ToDo(id, "sua title 1003", "sua contnent 1003");
        database.collection("TODO").document(toDo.getId())
                .update(toDo.convertHashMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Sửa thành công");
                        setDataFromFireBase(tvResult); // Refresh data
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
    }

    public void DeleteFireBase(TextView tvResult) {
        id = "764ca454-7c69-4d90-91f5-3b99f7280fac";
        database.collection("TODO").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Xoá thành công");
                        setDataFromFireBase(tvResult); // Refresh data
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });

    }

    String strResult = "";
    public ArrayList<ToDo> setDataFromFireBase(TextView tvResult) {
        ArrayList<ToDo> list = new ArrayList<>();
        database.collection("TODO")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) { // sau khi lấy dữ liệu thành công
                            strResult="";
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                ToDo toDo1 = documentSnapshot.toObject(ToDo.class);
                                strResult+="Id: "+toDo1.getId()+"\n";
                                list.add(toDo1); // them vao list
                            }
                            tvResult.setText(strResult);
                        } else {
                            tvResult.setText("Doc tai lieu that bai");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                })
                ;
        return  list;
    }
}