package com.example.firebase_group_chat_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private Button btnSend;
    private EditText edtMessage;
    private RecyclerView rvMessage;

    private AppPreference mAppPreference;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private FirebaseRecyclerAdapter<Message, ChatViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

        edtMessage = (EditText) findViewById(R.id.edt_message);
        rvMessage = (RecyclerView) findViewById(R.id.rv_chat);
        rvMessage.setHasFixedSize(true);
        rvMessage.setLayoutManager(new LinearLayoutManager(this));

        mAppPreference = new AppPreference(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        adapter = new FirebaseRecyclerAdapter<Message, ChatViewHolder>(
                Message.class,
                R.layout.item_row_chat,
                ChatViewHolder.class,
                mDatabaseReference.child("chat")
        ) {
            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, Message model, int position) {
                viewHolder.tvMessage.setText(model.message);
                viewHolder.tvEmail.setText(model.sender);
            }
        };
        rvMessage.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send){
            String message = edtMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(message)){
                Map<String, Object> param = new HashMap<>();
                param.put("sender", mAppPreference.getEmail());
                param.put("message", message);

                mDatabaseReference.child("chat")
                        .push()
                        .setValue(param)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                edtMessage.setText("");
                                if(task.isSuccessful()){
                                    Log.d("SendMessage", "Sukses");
                                }else{
                                    Log.d("SendMessage", "failed ");
                                }
                            }
                        });
            }
        }
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmail, tvMessage;

        public ChatViewHolder(View itemView) {
            super(itemView);

            tvEmail = (TextView) itemView.findViewById(R.id.tv_sender);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }
}
