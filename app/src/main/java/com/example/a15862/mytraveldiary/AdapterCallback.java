package com.example.a15862.mytraveldiary;

import com.example.a15862.mytraveldiary.Entity.Comment;

// define interface for click event in comment(add like)
public interface AdapterCallback {
    void onItemClick(Comment comment);
}
