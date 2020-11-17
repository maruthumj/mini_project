package com.example.project_management;

public class projectlists {

   private String name,desc,role,starttime,finishingtime,colleaguename,task,status,update;
private projectlists(){

}
private projectlists(String update,String name,String desc,String role,String starttime,String finishingtime,String colleaguename,String task,String status)
{
    this.name=name;
    this.desc=desc;
    this.role=role;
    this.starttime=starttime;
    this.finishingtime=finishingtime;
    this.colleaguename=colleaguename;
    this.task=task;
    this.status=status;
    this.update=update;
    
}
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
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

    public String getfinishingtime() {
        return finishingtime;
    }

    public void setfinishingtime(String finishingtime) {
        this.finishingtime = finishingtime;
    }

    public String getColleaguename() {
        return colleaguename;
    }

    public void setColleaguename(String colleaguename) {
        this.colleaguename = colleaguename;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}
