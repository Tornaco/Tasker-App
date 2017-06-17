package dev.tornaco.tasker.common;

/**
 * Created by Nick@NewStand.org on 2017/4/15 13:39
 * E-Mail: NewStand@163.com
 * All right reserved.
 */
public class Holder<T> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
