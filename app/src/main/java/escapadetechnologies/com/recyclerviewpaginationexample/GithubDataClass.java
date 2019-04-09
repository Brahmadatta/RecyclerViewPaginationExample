package escapadetechnologies.com.recyclerviewpaginationexample;

import java.io.Serializable;

public class GithubDataClass implements Serializable {


    private String name;

    private String full_name;

    private String node_id;

    public GithubDataClass(String name, String full_name, String node_id) {
        this.name = name;
        this.full_name = full_name;
        this.node_id = node_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }
}
