package me.qping.utils.codegen.bean.constant;

public enum WorkerType {

    ConnectionId(1), CodeDownloadId(2), ;

    int intValue;

    public int intValue(){
        return intValue;
    }

    private WorkerType(int i){
        this.intValue = i;
    }

}
