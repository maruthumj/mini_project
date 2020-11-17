package com.example.project_management;

public class filelists {
    private String link,sender,task;
  private filelists(){

    }
    private filelists(String link,String sender,String task){

    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
