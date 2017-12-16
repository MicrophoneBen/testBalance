package server;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-12-15 0015.
 */
public class ServerData  implements Serializable,Comparable<ServerData>{
    private Integer balance; //服务器负载

    private String host; //服务器地址

    private Integer port; //服务器端口号

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }


    @Override
    public int compareTo(ServerData o) {
        return this.getBalance().compareTo(o.getBalance());
    }
}
