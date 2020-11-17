package com.example.project_management;


public class ongoingprojectlists {
    private String name,desc,role,starttime,finishingtime,creator,task,status,colleaguename,filename;
    private ongoingprojectlists()
    {

    }
    private ongoingprojectlists(String name,String desc,String role,String filename,String starttime,String finishingtime,String creator,String task,String status,String colleaguename)
    {
        this.name=name;
        this.desc=desc;
        this.role=role;
        this.starttime=starttime;
        this.finishingtime=finishingtime;
        this.creator=creator;
        this.task=task;
        this.status=status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getFinishingtime() {
        return finishingtime;
    }

    public void setFinishingtime(String finishingtime) {
        this.finishingtime = finishingtime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getColleaguename() {
        return colleaguename;
    }

    public void setColleaguename(String colleaguename) {
        this.colleaguename = colleaguename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
