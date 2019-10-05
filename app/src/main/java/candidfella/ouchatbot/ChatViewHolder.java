package candidfella.ouchatbot;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class ChatViewHolder extends RecyclerView.ViewHolder  {



    TextView leftText,rightText;

    public ChatViewHolder(View itemView){
        super(itemView);

        leftText = (TextView)itemView.findViewById(R.id.leftText);
        rightText = (TextView)itemView.findViewById(R.id.rightText);


    }
}