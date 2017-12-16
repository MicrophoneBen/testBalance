package server;

/**
 * Created by Administrator on 2017-12-15 0015.
 * 服务器注册接口
 */
public interface RegistProvider {
    /**
     * 注册
     * @param context
     * @throws Exception
     */
    public void regist(Object context) throws Exception;

    /**
     * 取消注册
     * @param context
     * @throws Exception
     */
    public void unRegist(Object context) throws Exception;
}
