// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from group.proto

package com.dyuproject.protostuff.me;

import java.io.IOException;
import java.util.Vector;

import com.dyuproject.protostuff.me.ByteString;
import com.dyuproject.protostuff.me.GraphIOUtil;
import com.dyuproject.protostuff.me.Input;
import com.dyuproject.protostuff.me.Message;
import com.dyuproject.protostuff.me.Output;
import com.dyuproject.protostuff.me.Schema;

public final class Student implements Message, Schema
{

    public static Schema getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Student getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Student DEFAULT_INSTANCE = new Student();

    
    private String name;
    private Vector club;

    public Student()
    {
        
    }

    // getters and setters

    // name

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    // club

    public Vector getClubList()
    {
        return club;
    }

    public void setClubList(Vector club)
    {
        this.club = club;
    }

    public Club getClub(int index)
    {
        return club == null ? null : (Club)club.elementAt(index);
    }

    public int getClubCount()
    {
        return club == null ? 0 : club.size();
    }

    public void addClub(Club club)
    {
        if(this.club == null)
            this.club = new Vector();
        this.club.addElement(club);
    }

    // message method

    public Schema cachedSchema()
    {
        return this;
    }

    // schema methods

    public Object /*Student*/ newMessage()
    {
        return new Student();
    }

    public Class typeClass()
    {
        return Student.class;
    }

    public String messageName()
    {
        return Student.class.getName();
    }

    public String messageFullName()
    {
        return Student.class.getName();
    }

    public boolean isInitialized(Object /*Student*/ message)
    {
        return true;
    }

    public void mergeFrom(Input input, Object /*Student*/ messageObj) throws IOException
    {
        Student message = (Student)messageObj;
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 1:
                    message.name = ByteString.stringDefaultValue(input.readString());
                    break;
                case 2:
                    if(message.club == null)
                        message.club = new Vector();
                    message.club.addElement(input.mergeObject(null, Club.getSchema()));
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }

    public void mergeFrom(Object /*Student*/ messageObj)
    {
        Student message = (Student)messageObj;
        this.name = message.name;
        this.club = new Vector();
        if(message.club != null) {
            for(int i = 0; i < message.club.size(); i++) {
                Club origElt = (Club)message.club.elementAt(i);
                Club newElt = (Club)origElt.newMessage();
                newElt.mergeFrom((Club)message.club.elementAt(i));
                this.club.addElement(newElt);
            }
        }
    }


    public void writeTo(Output output, Object /*Student*/ messageObj) throws IOException
    {
        Student message = (Student)messageObj;
        if(message.name != null)
            output.writeString(1, message.name, false);

        if(message.club != null)
        {
            for(int i = 0; i < message.club.size(); i++)
            {
                Club club = (Club)message.club.elementAt(i);
                if(club != null)
                    output.writeObject(2, club, Club.getSchema(), true);
            }
        }

    }

    public String getFieldName(int number)
    {
        return Integer.toString(number);
    }

    public int getFieldNumber(String name)
    {
        return Integer.parseInt(name);
    }
    
}
