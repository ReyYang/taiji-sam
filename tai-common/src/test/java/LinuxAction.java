//package org.database.neo4j.base.common;
//
//import ch.ethz.ssh2.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.experimental.Accessors;
//import org.database.neo4j.base.io.StreamListener;
//
//import java.io.*;
//import java.lang.reflect.Field;
//import java.net.Inet4Address;
//import java.net.InetAddress;
//import java.net.SocketException;
//import java.util.EventListener;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * Copyright © 2018 eSunny Info. Developer Stu. All rights reserved.
// * <p>
// * code is far away from bug with the animal protecting
// * <p>
// * ┏┓　　　┏┓
// * ┏┛┻━━━┛┻┓
// * ┃　　　　　　　┃
// * ┃　　　━　　　┃
// * ┃　┳┛　┗┳　┃
// * ┃　　　　　　　┃
// * ┃　　　┻　　　┃
// * ┃　　　　　　　┃
// * ┗━┓　　　┏━┛
// * 　　┃　　　┃神兽保佑
// * 　　┃　　　┃代码无BUG！
// * 　　┃　　　┗━━━┓
// * 　　┃　　　　　　　┣┓
// * 　　┃　　　　　　　┏┛
// * 　　┗┓┓┏━┳┓┏┛
// * 　　　┃┫┫　┃┫┫
// * 　　　┗┻┛　┗┻┛
// *
// * @author cl24
// * Build File @date: 2019/8/5
// * @description TODO
// * @version 1.0
// */
//@Getter
//@Setter
//@NoArgsConstructor
//@Accessors(chain = true)
//public class LinuxAction {
//
//    public final static String LINUX_SCRIPT_FILE_DIR = "/home/cldev/script";
//    public final static Integer DEFAULT_PORT = 22;
//    public final static String DEFAULT_CHART = "UTF-8";
//    public final static String AUTHENTICATION_CODE = "authentication";
//    public final static String CONNECT_CODE = "connect";
//
//    private Connection conn;
//    private String ip;
//    private String user;
//    private String password;
//    private int port;
//    private String charset;
//
//    public LinuxAction(String ip, String user, String password, int port, String charset) {
//        this.ip = ip;
//        this.user = user;
//        this.password = password;
//        this.port = port;
//        this.charset = charset;
//        this.conn = new Connection(ip, port);
//    }
//
//    /**
//     * 判断是否可以操作远端
//     *
//     * @return
//     * @throws IOException
//     */
//    private synchronized Boolean login() throws IOException {
//        if (!connectIp()) {
//            return false;
//        }
//        boolean flg;
//        connect();
//        flg = isAuthentication();
//        if (!flg) {
//            flg = conn.authenticateWithPassword(user, password);
//        }
//        return flg;
//    }
//
//    /**
//     * 创建连接
//     *
//     * @throws IOException
//     */
//    private synchronized void connect() throws IOException {
//        if (isConnection()) {
//            return;
//        }
//        conn.connect();
//    }
//
//    /**
//     * 判断远程ip是否能连接
//     *
//     * @return
//     */
//    private boolean connectIp() {
//        try {
//            InetAddress inetAddress = Inet4Address.getByName(ip);
//            if (inetAddress.isReachable(1500)) {
//                return true;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        throw new BusinessException("connect");
//    }
//
//    /**
//     * 判断是否需要连接
//     * 如果连接对象connectionClosed状态已经是未连接状态就重新实例化对象
//     *
//     * @return
//     */
//    private synchronized Boolean isConnection() {
//        try {
//            Field tm = conn.getClass().getDeclaredField("tm");
//            tm.setAccessible(true);
//            Object o = tm.get(conn);
//            if (o == null) {
//                return false;
//            }
//            Field connectionClosed = o.getClass().getSuperclass().getDeclaredField("connectionClosed");
//            connectionClosed.setAccessible(true);
//            boolean aBoolean = connectionClosed.getBoolean(o);
//            if (aBoolean) {
//                conn = new Connection(ip, port);
//            }
//            return !aBoolean;
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
//
//    /**
//     * 返回连接对象中身份校验的状态
//     *
//     * @return
//     */
//    private synchronized Boolean isAuthentication() {
//        try {
//            Field authenticated = conn.getClass().getDeclaredField("authenticated");
//            authenticated.setAccessible(true);
//            return authenticated.getBoolean(conn);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    /**
//     * 执行一条Linux语句
//     *
//     * @param cmd Linux语句
//     * @return 返回格式封装对象
//     */
//    public ResultApi<List<String>> executeSuccess(String cmd) {
//        return this.executeSuccess(cmd, null);
//    }
//
//    public ResultApi<List<String>> executeSuccess(String cmd, ResultApiCallInterface<List<String>> resultApiCallInterface) {
//        Session session = null;
//        try {
//            if (login()) {
//                session = conn.openSession();
//                session.execCommand(cmd);
//                return processStdout(session.getStdout(), resultApiCallInterface);
//            } else {
//                throw new BusinessException("authentication");
//            }
//        } catch (IOException e) {
//            if (e instanceof SocketException) {
//                return ResultApi.build(500, e.getMessage(), null);
//            } else {
//                e.printStackTrace();
//            }
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//        return ResultApi.build(500, "服务器异常", null);
//    }
////
////    private ResultApi<List<String>> executeSuccess(String cmd, CommonCallInterface commonCallInterface) {
////
////    }
//
//    private ResultApi<List<String>> processStdout(InputStream in, ResultApiCallInterface<List<String>> resultApiCallInterface) {
//        InputStream stdout = new StreamGobbler(in);
//        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, LinuxAction.DEFAULT_CHART));
//            if (resultApiCallInterface == null) {
//                String line;
//                List<String> list = new LinkedList<>();
//                while ((line = br.readLine()) != null) {
//                    list.add(line);
//                }
//                br.close();
//                return ResultApi.ok(list);
//            } else {
//                return resultApiCallInterface.run(br);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return ResultApi.build(500, "数据读取异常", null);
//    }
//
//    public ResultApi downloadFile(String remoteFile, String remoteTargetDirectory, String newPath) {
//        try {
//
//            if (login()) {
//                SCPClient scpClient = conn.createSCPClient();
//                SCPInputStream sis = scpClient.get(remoteTargetDirectory + "/" + remoteFile);
//                File f = new File(newPath);
//                if (!f.exists()) {
//                    f.mkdirs();
//                }
//                File newFile = new File(newPath + remoteFile);
//                FileOutputStream fos = new FileOutputStream(newFile);
//                byte[] b = new byte[4096];
//                int i;
//                while ((i = sis.read(b)) != -1) {
//                    fos.write(b, 0, i);
//                }
//                fos.flush();
//                fos.close();
//                sis.close();
//                conn.close(new Throwable("Closed due to user request."), true);
//                return ResultApi.ok();
//            } else {
//                return ResultApi.build(500, "连接建立失败");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return ResultApi.build(500, "下载失败");
//    }
//
//    /**
//     * 获取虚拟机上的文件流
//     * @param remoteFile    文件名称
//     * @param remoteTargetDirectory 文件路径
//     * @return  文件输入流
//     */
//    public SCPInputStream downloadFileIS(String remoteFile, String remoteTargetDirectory){
//        try {
//            if (login()) {
//                SCPClient scpClient = conn.createSCPClient();
//                SCPInputStream sis = scpClient.get(remoteTargetDirectory + "/" + remoteFile);
//                return sis;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 获取虚拟机上的文件流
//     * @param file
//     * @return
//     */
//    public SCPInputStream downloadFileIS(String file){
//        try {
//            if (login()) {
//                SCPClient scpClient = conn.createSCPClient();
//                SCPInputStream sis = scpClient.get(file);
//                return sis;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 上传文件到linux的指定目录下
//     *
//     * @param f                     文件对象
//     * @param remoteTargetDirectory linux的目录位置
//     * @param mode
//     * @return 返回格式封装对象
//     */
//    public ResultApi uploadFile(File f, String remoteTargetDirectory, String mode) {
//        try {
//            if (!login()) {
//                return ResultApi.build(500, "连接建立失败");
//            }
//            SCPClient scpClient = new SCPClient(conn);
//            SCPOutputStream os = this.customPut(scpClient, f.getName(), f.length(), remoteTargetDirectory, mode);
//            byte[] b = new byte[4096];
//            FileInputStream fis = new FileInputStream(f);
//            int i;
//            while ((i = fis.read(b)) != -1) {
//                os.write(b, 0, i);
//            }
//            os.flush();
//            fis.close();
//            os.close();
//            conn.close(new Throwable("Closed due to user request."), true);
//            return ResultApi.ok();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return ResultApi.build(500, "上传失败");
//    }
//
//    public ResultApi uploadFile(InputStream is,String fileName,long length, String remoteTargetDirectory, String mode){
//        return uploadFile(is,fileName,length,remoteTargetDirectory,mode,null);
//    }
//
//    public ResultApi uploadFile(InputStream is, String fileName, long length, String remoteTargetDirectory, String mode , StreamListener listener){
//        try {
//            if (!login()) {
//                return ResultApi.build(500, "连接建立失败");
//            }
//            SCPClient scpClient = new SCPClient(conn);
//            SCPOutputStream os = this.customPut(scpClient, fileName,length, remoteTargetDirectory, mode);
//            byte[] b = new byte[4096];
//            int i;
//            long total = 0;
//            while ((i = is.read(b)) != -1) {
//
//                //从技术上讲，一些read(byte[])方法可能返回0，而我们不能接受
//                if (i == 0)
//                {
//                    int singleByte = is.read();
//                    if (singleByte < 0) {
//                        break;
//                    }
//                    os.write(singleByte);
//                    ++total;
//                    if (listener != null) {
//                        listener.bytesTransferred(total, 1);
//                    }
//                    continue;
//                }
//
//                os.write(b, 0, i);
//                os.flush();
//                total = total + i;
//                if(listener != null){
//                    listener.bytesTransferred(total,i);
//                }
//            }
//            os.flush();
//            is.close();
//            os.close();
//            conn.close(new Throwable("Closed due to user request."), true);
//            return ResultApi.ok();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return ResultApi.build(500, "上传失败");
//    }
//
//
//    private SCPOutputStream customPut(SCPClient scp, final String remoteFile, long length, String remoteTargetDirectory, String mode) throws IOException {
//        Session sess;
//        if (null == remoteFile) {
//            throw new IllegalArgumentException("Null argument.");
//        }
//        if (null == remoteTargetDirectory) {
//            remoteTargetDirectory = "";
//        }
//        if (null == mode) {
//            mode = "0600";
//        }
//        if (mode.length() != 4) {
//            throw new IllegalArgumentException("Invalid mode.");
//        }
//        for (int i = 0; i < mode.length(); i++) {
//            if (!Character.isDigit(mode.charAt(i))) {
//                throw new IllegalArgumentException("Invalid mode.");
//            }
//        }
//        remoteTargetDirectory = (remoteTargetDirectory.length() > 0) ? remoteTargetDirectory : ".";
//        String cmd = "sudo scp -t -d \"" + remoteTargetDirectory + "\"";
//        sess = conn.openSession();
//        sess.execCommand(cmd, LinuxAction.DEFAULT_CHART);
//        return new SCPOutputStream(scp, sess, remoteFile, length, mode);
//    }
//
//}
