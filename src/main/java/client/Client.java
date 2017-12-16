package client;

/**
 * Created by Administrator on 2017-12-15 0015.
 */
public interface Client {

    public void connect() throws Exception; //用于连接到服务器

    public void disConnect() throws Exception;//用于和服务器断开
}
