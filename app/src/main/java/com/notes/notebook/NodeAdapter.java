package com.notes.notebook;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.NotesViewHolder> {

    LayoutInflater inflater;
    private List<NodeModel> nodeModelList;
    private Context context;
    private OnRecyclerItemClick onRecyclerItemClick;

    public NodeAdapter(List<NodeModel> nodeModelList, Context context) {
        this.nodeModelList = nodeModelList;
        this.context= context;
    }

    public void setOnRecyclerItemClick(OnRecyclerItemClick onRecyclerItemClick){
        this.onRecyclerItemClick = onRecyclerItemClick;
    }

    interface OnRecyclerItemClick{
        void onClick(int pos);
    }
    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {


        int colorHolder = nodeModelList.get(position).getColor();
        holder.headText.setText(nodeModelList.get(position).getHead());
        holder.timeText.setText(timePad(nodeModelList.get(position).getTime()));
        holder.descText.setText(nodeModelList.get(position).getDesc());
        holder.dateText.setText(nodeModelList.get(position).getDate());

        if(nodeModelList.get(position).getFav()==1)
            holder.favourite.setVisibility(View.VISIBLE);
        else
            holder.favourite.setVisibility(View.INVISIBLE);

        //holder.color.setBackgroundColor(context.getResources().getColor(R.color.colorThree));

       switch (colorHolder){
            case 1:
                holder.color.setBackgroundColor(context.getResources().getColor(R.color.colorOne));
                holder.noteHeader.setBackgroundColor(context.getResources().getColor(R.color.colorOne));
                break;
            case 2:
                holder.color.setBackgroundColor(context.getResources().getColor(R.color.colorTwo));
                holder.noteHeader.setBackgroundColor(context.getResources().getColor(R.color.colorTwo));
                break;
            case 3:
                holder.color.setBackgroundColor(context.getResources().getColor(R.color.colorThree));
                holder.noteHeader.setBackgroundColor(context.getResources().getColor(R.color.colorThree));
                break;
            case 4:
                holder.color.setBackgroundColor(context.getResources().getColor(R.color.colorFour));
                holder.noteHeader.setBackgroundColor(context.getResources().getColor(R.color.colorFour));
                break;

            case 5:
                holder.color.setBackgroundColor(context.getResources().getColor(R.color.colorFive));
                holder.noteHeader.setBackgroundColor(context.getResources().getColor(R.color.colorFive));
                break;
           case 6:
               holder.color.setBackgroundColor(context.getResources().getColor(R.color.colorSix));
               holder.noteHeader.setBackgroundColor(context.getResources().getColor(R.color.colorSix));
               break;
           case 7:
               holder.color.setBackgroundColor(context.getResources().getColor(R.color.colorSeven));
               holder.noteHeader.setBackgroundColor(context.getResources().getColor(R.color.colorSeven));
               break;
           case 8:
               holder.color.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
               holder.noteHeader.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
               break;
           case 9:
               holder.color.setBackgroundColor(context.getResources().getColor(R.color.colorNine));
               holder.noteHeader.setBackgroundColor(context.getResources().getColor(R.color.colorNine));
               break;
           case 10:
               holder.color.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
               holder.noteHeader.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
               break;

            default:
                holder.color.setBackgroundColor(context.getResources().getColor(R.color.backgroundColor));
                break;


        }
    }

    private String timePad(String time) {

        String [] arry = time.split(":",3);
        if(Integer.valueOf(arry[0]) > 12){
           return String.valueOf(Integer.valueOf(arry[0]) -12) + ":"+ arry[1]+ " pm";
        }
        else{
            return arry[0]+ ":"+ arry[1] + " am";
        }


    }

    @Override
    public int getItemCount() {
        return nodeModelList.size();
    }


    public class NotesViewHolder extends RecyclerView.ViewHolder {


        private TextView headText,descText,timeText,dateText, nID;
        private View color;
        private LinearLayout favourite,noteHeader;
        ImageButton colorIcon;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            headText= itemView.findViewById(R.id.nTitle);
            descText = itemView.findViewById(R.id.ndesc);
            timeText= itemView.findViewById(R.id.nTime);
            dateText= itemView.findViewById(R.id.nDate);
            nID = itemView.findViewById(R.id.listId);
            color= itemView.findViewById(R.id.color);
            noteHeader =  itemView.findViewById(R.id.note_item_header);
            colorIcon= itemView.findViewById(R.id.colorBtnEdit);
            favourite = itemView.findViewById(R.id.fav_icon);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),DetailsActivity.class);

                    long id = nodeModelList.get(getAdapterPosition()).getID();
                    intent.putExtra("ID",id);
                    Log.d("NewId","id-> "+ id);
                    view.getContext().startActivity(intent);
                }
            });
        }

    }
}
