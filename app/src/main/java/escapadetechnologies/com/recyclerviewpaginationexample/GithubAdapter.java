package escapadetechnologies.com.recyclerviewpaginationexample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GithubAdapter extends RecyclerView.Adapter<GithubAdapter.GithubViewHolder>{

    ArrayList<HashMap<String,String>> hashMapArrayList;
    Context context;
    private List<GithubDataClass> dataClassList;

    public GithubAdapter(List<GithubDataClass> dataClassList) {
        this.dataClassList = dataClassList;
    }

    /*public GithubAdapter(ArrayList<HashMap<String, String>> hashMapArrayList, Context context) {
        this.hashMapArrayList = hashMapArrayList;
        this.context = context;
    }*/

    @NonNull
    @Override
    public GithubViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.github_card_layout,null,false);
        return new GithubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GithubViewHolder githubViewHolder, int i) {

        /*HashMap<String,String> hashMap = hashMapArrayList.get(i);

        githubViewHolder.name.setText(hashMap.get("name"));
        githubViewHolder.full_name.setText(hashMap.get("full_name"));
        githubViewHolder.node_id.setText(hashMap.get("node_id"));*/

        githubViewHolder.name.setText(dataClassList.get(i).getName());
        githubViewHolder.full_name.setText(dataClassList.get(i).getFull_name());
        githubViewHolder.node_id.setText(dataClassList.get(i).getNode_id());


    }

    @Override
    public int getItemCount() {
        return dataClassList.size();
    }

    public class GithubViewHolder extends RecyclerView.ViewHolder{

        TextView name,full_name,node_id;

        public GithubViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            full_name = itemView.findViewById(R.id.full_name);
            node_id = itemView.findViewById(R.id.node_id);
        }
    }
}
