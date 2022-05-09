package com.iir4.g2.gmachine.ui.update;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UpdateViewModel extends ViewModel {

    private MutableLiveData<Integer> id;
    private  MutableLiveData<Float> prix;
    private  MutableLiveData<String> dateAchat;
    private  MutableLiveData<String> marque;
    private  MutableLiveData<String> ref;


    public UpdateViewModel() {
        id = new MutableLiveData<>();
        prix = new MutableLiveData<>();
        dateAchat = new MutableLiveData<>();
        marque = new MutableLiveData<>();
        ref = new MutableLiveData<>();
    }

    public void selectId(Integer item) {
        id.setValue(item);
    }
    public void selectMarque( String item) {
        marque.setValue(item);
    }    public void selectRef(String item) {
        ref.setValue(item);
    }
    public void selectPrix(Float item) {
        prix.setValue(item);
    }
    public void selectDate(String item) {
        dateAchat.setValue(item);
    }
    public LiveData<String> getmarque() {
        return marque;
    }
    public LiveData<Integer> getidd() {
        return id;
    }
    public LiveData<String> getdate() {
        return dateAchat;
    }
    public LiveData<Float> getprix() {
        return prix;
    }
    public LiveData<String> getRef() {
        return ref;
    }
}