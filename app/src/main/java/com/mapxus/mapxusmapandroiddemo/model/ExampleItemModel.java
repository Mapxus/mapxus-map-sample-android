package com.mapxus.mapxusmapandroiddemo.model;

import android.content.Intent;

import com.mapxus.mapxusmapandroiddemo.BuildConfig;

public class ExampleItemModel {
  // Just a model for the detailed item recycler

  public int title;
  public int description;
  public int img;
  public Intent activity;

  public int getTitle() {
    return title;
  }

  public void setTitle(int title) {
    this.title = title;
  }

  public int getDescription() {
    return description;
  }

  public void setDescription(int description) {
    this.description = description;
  }

  public int getImg() {
    return img;
  }

  public void setImg(int img) {
    this.img = img;
  }

  public Intent getActivity() {
    return activity;
  }

  public void setActivity(Intent activity) {
    this.activity = activity;
  }

  public ExampleItemModel(int title, int description, Intent activity, int img) {
    this.title = title;
    this.description = description;
    this.img = img;
    this.activity = activity;
  }
}