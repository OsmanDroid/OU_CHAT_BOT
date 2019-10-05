package candidfella.ouchatbot;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class ChatViewHolder extends RecyclerView.ViewHolder  {



    TextView leftText,rightText;
    Button btnIntent;

    public ChatViewHolder(View itemView){
        super(itemView);

        leftText = (TextView)itemView.findViewById(R.id.leftText);
        rightText = (TextView)itemView.findViewById(R.id.rightText);
        btnIntent = itemView.findViewById(R.id.extra);


    }
}