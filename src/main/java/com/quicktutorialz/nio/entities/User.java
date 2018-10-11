package com.quicktutorialz.nio.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class User {
    @XmlElement
    private String id;
    @XmlElement
    private String name;
    @XmlElement
    private String date;

    public User(){}

    public User(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.date = LocalDateTime.now().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
