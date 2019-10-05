package candidfella.ouchatbot;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class ChatActivity extends AppCompatActivity {


    private static final String TAG = "TAG";
    RecyclerView recyclerView;
    EditText editText;
    ImageView sendButton;

    DatabaseReference ref;

    CustomAdapter adapter;
    List<ChatModel> chatModelList;

    //FirebaseRecyclerAdapter<ChatModel, ChatViewHolder> adapter;
    //FirebaseRecyclerOptions<ChatModel> options;

    private AIService aiService;

    @NonNull
    protected static final Query sChatQuery =
            FirebaseDatabase.getInstance().getReference().child("chats").limitToLast(50);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerView);
        editText = findViewById(R.id.editText);
        sendButton = findViewById(R.id.send_btn);


        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        chatModelList = new ArrayList<>();
        chatModelList.add(new ChatModel("abcd","user"));

        adapter = new CustomAdapter(this,chatModelList);

        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);

        ref.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatModel model = dataSnapshot.getValue(ChatModel.class);
                Log.d(TAG, "onChildAdded: "+model.msgUser);
                Log.d(TAG, "onChildAdded: "+model.msgText);

                chatModelList.add(model);
                adapter.updateList(chatModelList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final AIConfiguration config = new AIConfiguration("3d5f701bdc764b4a87a888da5bdb337e",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        //aiService.setListener(this);

        final AIDataService aiDataService = new AIDataService(this,config);

        final AIRequest aiRequest = new AIRequest();



        sendButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                String message = editText.getText().toString().trim();

                if (!message.equals("")) {

                    ChatModel chatMessage = new ChatModel(message, "user");
                    ref.child("chat").push().setValue(chatMessage);

                    aiRequest.setQuery(message);
                    new AsyncTask<AIRequest,Void, AIResponse>(){

                        @Override
                        protected AIResponse doInBackground(AIRequest... aiRequests) {
                            final AIRequest request = aiRequests[0];
                            try {
                                final AIResponse response = aiDataService.request(aiRequest);
                                return response;
                            } catch (AIServiceException e) {
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(AIResponse response) {
                            if (response != null) {

                                Result result = response.getResult();
                                String reply = result.getFulfillment().getSpeech();
                                ChatModel chatMessage = new ChatModel(reply, "bot");
                                ref.child("chat").push().setValue(chatMessage);
                            }
                        }
                    }.execute(aiRequest);
                }


                editText.setText("");


            }
        });










    }



}






