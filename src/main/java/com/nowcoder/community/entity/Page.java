package com.nowcoder.community.entity;

import java.util.ArrayList;

public class Page {
    private int offset;
    private int rows;
    private int limit;
    private int current = 1;
    private int[] range;
    private int total;
    private String path;


    @Override
    public String toString() {
        return "Page{" +
                "offset=" + offset +
                ", rows=" + rows +
                ", limit=" + limit +
                ", current=" + current +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int[] getRange() {
        if (this.current<1||this.current>this.total){
            return new int[]{};
        }
        if (this.current==1){
            return new int[]{1, 2,3};
        }
        if (this.current==2){
            return new int[]{1, 2,3,4};
        }
        if (this.current==this.total-1){
            return new int[]{this.current-2, this.current-1,this.current,this.current+1};
        }

        if (this.current==this.total){
            return new int[]{this.current-2, this.current-1,this.current};
        }
        return new int[]{this.current-2, this.current-1, this.current,this.current+1,this.current+2};
    }

    public void setRange(int[] range) {
        this.range = range;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setRows(int rows) {
        this.rows = rows;
        this.setTotal(this.rows/this.getLimit()+1);
    }


    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getOffset() {
        return (this.getCurrent()-1)*this.getLimit();
    }

    public int getRows() {
        return rows;
    }

    public int getLimit() {
        return limit;
    }

    public int getCurrent() {
        return current;
    }
}
