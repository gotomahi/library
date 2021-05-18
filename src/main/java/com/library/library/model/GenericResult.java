package com.library.library.model;

import java.util.ArrayList;
import java.util.List;

public class GenericResult<B> {
    private B result;
    private boolean success;
    private List<String> errors = new ArrayList<>();

    public GenericResult(B result, boolean success){
        this.result = result;
        this.success = success;
    }

    public GenericResult(boolean success){
        this.success = success;
    }

    public GenericResult(){
    }

    public void addError(String error){
        this.errors.add(error);
    }

    public boolean hasError(String error){
        return this.errors.contains(error);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success){
        this.success = success;
    }

    public B getResult(){
        return result;
    }

    public void setResult(B result){
        this.result = result;
    }
}
