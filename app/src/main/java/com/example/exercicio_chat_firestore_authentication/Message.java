package com.example.exercicio_chat_firestore_authentication;

import java.util.Date;

public class Message implements Comparable <Message>{

    @Override
    public int compareTo(Message message) {
        return this.date.compareTo(message.date);
    }

    private String text;
    private String email;
    private Date date;
    private boolean msg = false;

    public Message(){

    }

    public Message(String text, String email, Date date, boolean msg){

        this.text=text;
        this.email=email;
        this.date=date;
        this.msg = msg;
    }

    public String getText(){

        return text;
    }

    public void setText(String text){

        this.text=text;
    }

    public String getEmail(){

        return email;
    }

    public void setEmail(String email){

        this.email=email;
    }

    public Date getDate(){

        return date;
    }

    public void setDate(Date date){

        this.date=date;
    }

    public boolean getMsg(){

        return msg;
    }

    public void setMsg(boolean msg){

        this.msg=msg;
    }



}
