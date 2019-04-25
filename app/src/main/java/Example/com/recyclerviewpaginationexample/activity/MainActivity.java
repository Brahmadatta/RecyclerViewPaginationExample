package Example.com.recyclerviewpaginationexample.activity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Example.com.recyclerviewpaginationexample.adpater.GithubAdapter;
import Example.com.recyclerviewpaginationexample.model.GithubDataClass;
import Example.com.recyclerviewpaginationexample.R;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://api.github.com/repositories?since=1";
    RecyclerView recyclerView;
    ArrayList<HashMap<String,String>> githubList;
    LinearLayoutManager layoutManager;
    private ProgressDialog progressDialog;

    private static int current_page = 1;

    private int ival = 0;
    private int loadLimit = 10;
    private List<GithubDataClass> githubDataClassList;
    private RecyclerView.Adapter adapter;


    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        githubDataClassList = new ArrayList<GithubDataClass>();
        loadData(current_page);


        recyclerView = findViewById(R.id.recyclerView);

        progressDialog = new ProgressDialog(this);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new GithubAdapter(githubDataClassList);

        recyclerView.setAdapter(adapter);


        progressDialog.hide();


        /*recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                progressDialog.show();
                progressDialog.setTitle("please wait...");
                loadMoreData(current_page);
            }
        });*/


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading
                        && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached

                    // Do something
                    current_page++;

                    /*onLoadMore(current_page);*/
                    progressDialog.show();
                    progressDialog.setTitle("please wait...");
                    loadMoreData(current_page);

                    loading = true;
                }
            }
        });
    }



    // By default, we add 10 objects for first time
    private void loadData(int current_page) {


        StringRequest stringRequest = new StringRequest(BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.hide();
                /*[{"id":26,"node_id":"MDEwOlJlcG9zaXRvcnkyNg==","name":"merb-core","full_name":"wycats/merb-core","private":false,"owner":{"login":"wycats","id":4,"node_id":"MDQ6VXNlcjQ=","avatar_url":"https://avatars0.githubusercontent.com/u/4?v=4","gravatar_id":"","url":"https://api.github.com/users/wycats","html_url":"https://github.com/wycats","followers_url":"https://api.github.com/users/wycats/followers","following_url":"https://api.github.com/users/wycats/following{/other_user}","gists_url":"https://api.github.com/users/wycats/gists{/gist_id}","starred_url":"https://api.github.com/users/wycats/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/wycats/subscriptions","organizations_url":"https://api.github.com/users/wycats/orgs","repos_url":"https://api.github.com/users/wycats/repos","events_url":"https://api.github.com/users/wycats/events{/privacy}","received_events_url":"https://api.github.com/users/wycats/received_events","type":"User","site_admin":false}*/

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    githubList = new ArrayList<>();
                    for (int i = ival ;i <= loadLimit ; i++){

                        HashMap<String,String> hashMap = new HashMap<>();




                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        GithubDataClass githubDataClass = new GithubDataClass(jsonObject.getString("name"),jsonObject.getString("full_name"),jsonObject.getString("node_id"));
                        //GithubDataClass githubDataClass = new GithubDataClass(githubDataClassList.get(i).getName(),githubDataClassList.get(i).getFull_name(),githubDataClassList.get(i).getNode_id());

                        githubDataClassList.add(githubDataClass);

                        ival++;

                        /*String id = jsonObject.getString("id");
                        String node_id = jsonObject.getString("node_id");
                        String name = jsonObject.getString("name");
                        String full_name = jsonObject.getString("full_name");


                        hashMap.put("id",id);
                        hashMap.put("node_id",node_id);
                        hashMap.put("name",name);
                        hashMap.put("full_name",full_name);

                        githubList.add(hashMap);*/

                    }

                    adapter = new GithubAdapter(githubDataClassList);

                    recyclerView.setAdapter(adapter);

                    //attachAdapter(githubList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("response",response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

        /*for (int i = ival; i <= loadLimit; i++) {


            studentList.add(st);
            ival++;

        }*/
    }


    private void loadMoreData(int current_page) {

        loadLimit = ival + 10;




        StringRequest stringRequest = new StringRequest(BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.hide();


                /*[{"id":26,"node_id":"MDEwOlJlcG9zaXRvcnkyNg==","name":"merb-core","full_name":"wycats/merb-core","private":false,"owner":{"login":"wycats","id":4,"node_id":"MDQ6VXNlcjQ=","avatar_url":"https://avatars0.githubusercontent.com/u/4?v=4","gravatar_id":"","url":"https://api.github.com/users/wycats","html_url":"https://github.com/wycats","followers_url":"https://api.github.com/users/wycats/followers","following_url":"https://api.github.com/users/wycats/following{/other_user}","gists_url":"https://api.github.com/users/wycats/gists{/gist_id}","starred_url":"https://api.github.com/users/wycats/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/wycats/subscriptions","organizations_url":"https://api.github.com/users/wycats/orgs","repos_url":"https://api.github.com/users/wycats/repos","events_url":"https://api.github.com/users/wycats/events{/privacy}","received_events_url":"https://api.github.com/users/wycats/received_events","type":"User","site_admin":false}*/

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    githubList = new ArrayList<>();
                    for (int i = ival ;i <= loadLimit ; i++){

                        HashMap<String,String> hashMap = new HashMap<>();




                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        GithubDataClass githubDataClass = new GithubDataClass(jsonObject.getString("name"),jsonObject.getString("full_name"),jsonObject.getString("node_id"));

                        githubDataClassList.add(githubDataClass);







                        ival++;

                        /*String id = jsonObject.getString("id");
                        String node_id = jsonObject.getString("node_id");
                        String name = jsonObject.getString("name");
                        String full_name = jsonObject.getString("full_name");


                        hashMap.put("id",id);
                        hashMap.put("node_id",node_id);
                        hashMap.put("name",name);
                        hashMap.put("full_name",full_name);

                        githubList.add(hashMap);*/

                    }


                    adapter.notifyDataSetChanged();

                    //attachAdapter(githubList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("response",response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }


    private void getRecyclerViewData() {


        StringRequest stringRequest = new StringRequest(BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.hide();

                /*[{"id":26,"node_id":"MDEwOlJlcG9zaXRvcnkyNg==","name":"merb-core","full_name":"wycats/merb-core","private":false,"owner":{"login":"wycats","id":4,"node_id":"MDQ6VXNlcjQ=","avatar_url":"https://avatars0.githubusercontent.com/u/4?v=4","gravatar_id":"","url":"https://api.github.com/users/wycats","html_url":"https://github.com/wycats","followers_url":"https://api.github.com/users/wycats/followers","following_url":"https://api.github.com/users/wycats/following{/other_user}","gists_url":"https://api.github.com/users/wycats/gists{/gist_id}","starred_url":"https://api.github.com/users/wycats/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/wycats/subscriptions","organizations_url":"https://api.github.com/users/wycats/orgs","repos_url":"https://api.github.com/users/wycats/repos","events_url":"https://api.github.com/users/wycats/events{/privacy}","received_events_url":"https://api.github.com/users/wycats/received_events","type":"User","site_admin":false}*/

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    githubList = new ArrayList<>();
                    for (int i = 0;i < jsonArray.length() ; i++){
                        HashMap<String,String> hashMap = new HashMap<>();




                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String node_id = jsonObject.getString("node_id");
                        String name = jsonObject.getString("name");
                        String full_name = jsonObject.getString("full_name");


                        hashMap.put("id",id);
                        hashMap.put("node_id",node_id);
                        hashMap.put("name",name);
                        hashMap.put("full_name",full_name);

                        githubList.add(hashMap);

                    }

                    //attachAdapter(githubList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("response",response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private void hideDialog() {

        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    private void showDialog() {

        if (!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    /*private void attachAdapter(ArrayList<HashMap<String, String>> githubList) {

        GithubAdapter githubAdapter = new GithubAdapter(githubList,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(githubAdapter);
    }*/
}
