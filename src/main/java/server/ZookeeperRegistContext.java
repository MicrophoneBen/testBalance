package server;

import org.I0Itec.zkclient.ZkClient;

/**
 * Created by Administrator on 2017-12-15 0015.
 * zookeeper注册上下文类
 */
public class ZookeeperRegistContext {
    private String path;  //server节点路径
    private ZkClient zkClient ;//连接客户端
    private Object data; //数据

    public ZookeeperRegistContext(String path,ZkClient zkClient,Object data){
        this.path = path;
        this.zkClient = zkClient;
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ZookeeperRegistContext{" +
                "path='" + path + '\'' +
                ", zkClient=" + zkClient +
                ", data=" + data +
                '}';
    }
}
