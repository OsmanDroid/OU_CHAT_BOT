package candidfella.ouchatbot;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ChatViewHolder> {


    List<ChatModel> chatModelList;
    Context context;

    public CustomAdapter(Context context,List<ChatModel> chatModelList) {
        this.context = context;
        this.chatModelList = chatModelList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout, parent, false);
        return new ChatViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {


        ChatModel model = chatModelList.get(position);
        if (chatModelList.get(position).getMsgUser().equals("user")) {

            holder.rightText.setText(model.getMsgText());

            holder.rightText.setVisibility(View.VISIBLE);
            holder.leftText.setVisibility(View.GONE);
        }
        else {
            holder.leftText.setText(model.getMsgText());
            holder.rightText.setVisibility(View.GONE);
            holder.leftText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }


    public void updateList(List<ChatModel> chatModelList)
    {
        this.chatModelList.clear();
        this.chatModelList.addAll(chatModelList);
        notifyDataSetChanged();
    }
}
