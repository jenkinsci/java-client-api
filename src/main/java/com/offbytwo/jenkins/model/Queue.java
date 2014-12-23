// Copyright 2014 Vladimir Alyamkin. All Rights Reserved.

package com.offbytwo.jenkins.model;

import java.io.IOException;

public class Queue extends BaseModel {

    /** Queue Id */
    int id;
    String url;

    public Queue() {}

    public Queue(Queue from) {
        this(from.getId(), from.getUrl());
    }

    public Queue(int id, String url) {
        this.id = id;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public QueueWithDetails details() throws IOException {
        return client.get(url, QueueWithDetails.class);
    }
}
