package com.example.onlineoj.model;

import lombok.Data;
//存储用户成就

public class achivement {
    int r1;//扬帆起航
    int r10;//小试牛刀
    int r100;//初出茅庐
    int r200;//渐入佳境
    int r500;//登堂入室
    int r1000;//神！
    int w1;//没事，再来
    int w20;//勇敢牛牛不怕困难
    int w50;//《菜，就多练》
    int acnumber;//正确提交
    int allnumber;//总提交数


    public achivement() {
    }

    public achivement(int r1, int r10, int r100, int r200, int r500, int r1000, int w1, int w20, int w50, int acnumber, int allnumber) {
        this.r1 = r1;
        this.r10 = r10;
        this.r100 = r100;
        this.r200 = r200;
        this.r500 = r500;
        this.r1000 = r1000;
        this.w1 = w1;
        this.w20 = w20;
        this.w50 = w50;
        this.acnumber = acnumber;
        this.allnumber = allnumber;
    }

    /**
     * 获取
     * @return r1
     */
    public int getR1() {
        return r1;
    }

    /**
     * 设置
     * @param r1
     */
    public void setR1(int r1) {
        this.r1 = r1;
    }

    /**
     * 获取
     * @return r10
     */
    public int getR10() {
        return r10;
    }

    /**
     * 设置
     * @param r10
     */
    public void setR10(int r10) {
        this.r10 = r10;
    }

    /**
     * 获取
     * @return r100
     */
    public int getR100() {
        return r100;
    }

    /**
     * 设置
     * @param r100
     */
    public void setR100(int r100) {
        this.r100 = r100;
    }

    /**
     * 获取
     * @return r200
     */
    public int getR200() {
        return r200;
    }

    /**
     * 设置
     * @param r200
     */
    public void setR200(int r200) {
        this.r200 = r200;
    }

    /**
     * 获取
     * @return r500
     */
    public int getR500() {
        return r500;
    }

    /**
     * 设置
     * @param r500
     */
    public void setR500(int r500) {
        this.r500 = r500;
    }

    /**
     * 获取
     * @return r1000
     */
    public int getR1000() {
        return r1000;
    }

    /**
     * 设置
     * @param r1000
     */
    public void setR1000(int r1000) {
        this.r1000 = r1000;
    }

    /**
     * 获取
     * @return w1
     */
    public int getW1() {
        return w1;
    }

    /**
     * 设置
     * @param w1
     */
    public void setW1(int w1) {
        this.w1 = w1;
    }

    /**
     * 获取
     * @return w20
     */
    public int getW20() {
        return w20;
    }

    /**
     * 设置
     * @param w20
     */
    public void setW20(int w20) {
        this.w20 = w20;
    }

    /**
     * 获取
     * @return w50
     */
    public int getW50() {
        return w50;
    }

    /**
     * 设置
     * @param w50
     */
    public void setW50(int w50) {
        this.w50 = w50;
    }

    /**
     * 获取
     * @return acnumber
     */
    public int getAcnumber() {
        return acnumber;
    }

    /**
     * 设置
     * @param acnumber
     */
    public void setAcnumber(int acnumber) {
        this.acnumber = acnumber;
    }

    /**
     * 获取
     * @return allnumber
     */
    public int getAllnumber() {
        return allnumber;
    }

    /**
     * 设置
     * @param allnumber
     */
    public void setAllnumber(int allnumber) {
        this.allnumber = allnumber;
    }

    public String toString() {
        return "achivement{r1 = " + r1 + ", r10 = " + r10 + ", r100 = " + r100 + ", r200 = " + r200 + ", r500 = " + r500 + ", r1000 = " + r1000 + ", w1 = " + w1 + ", w20 = " + w20 + ", w50 = " + w50 + ", acnumber = " + acnumber + ", allnumber = " + allnumber + "}";
    }
}
