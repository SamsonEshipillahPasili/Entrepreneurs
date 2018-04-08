package com.entrepreneurs.entrepreneurs.entities;

import java.util.List;

/**
 * Created by eshipillah on 4/5/18.
 */

public class Group {
    private String amount;
    private String name;
    private String target;
    private List<Member> members;

    public Group(){
        // mandatory for saving data to firebase
    }

    public Group(String amount, String name, String target, List<Member> members) {
        this.amount = amount;
        this.name = name;
        this.target = target;
        this.members = members;
    }

    public String getAmount() {
        int tempAmount = 0;
        for(Member member : members){
            tempAmount += Integer.parseInt(member.getAmount());
        }
        return String.valueOf(tempAmount);
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    // get a member by name
    public Member getMemberByName(String name){
        Member member = null;
        for(Member m : this.members){
            if(m.getName().equals(name)){
                member = m;
            }
        }
        return member;
    }

    // get a percentage
    public String getPercentage(){
        int percentage = (Integer.parseInt(this.getAmount()) * 100) / Integer.parseInt(this.target);
        return String.valueOf(percentage);
    }
}
