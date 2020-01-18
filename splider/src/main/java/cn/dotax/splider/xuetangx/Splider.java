package cn.dotax.splider.xuetangx;

import cn.dotax.splider.xuetangx.model.Course;
import cn.dotax.splider.xuetangx.model.CourseResult;
import cn.dotax.splider.xuetangx.model.VideoNode;
import com.alibaba.fastjson.JSON;
import org.apache.http.client.fluent.Request;

import java.io.File;
import java.io.IOException;

public class Splider {
    private static final String COOKIE = "csrftoken=GzhO3RfCiFCqScY66ac9ZawosYPcp943; login_type=P; sessionid=5l0vwd4dienop3pek285pd2e6orlkgpd; k=15215537";

    public static void main(String[] args) throws IOException {
        String classroomId = "968608";
        String productSign = "THU12011000342";
        new Splider().start(classroomId, productSign);
    }

    public void start(String classroomId, String productSign) throws IOException {
        Course course = this.getCourse(classroomId, productSign);
        String courseJson = JSON.toJSONString(course);
        System.out.println(courseJson);

        VideoNode videoNode = new VideoNode(course);

        this.fullVideo(classroomId, productSign, videoNode);
        String videoNodeJson = JSON.toJSONString(videoNode);
        System.out.println(videoNodeJson);

        this.saveVideo(videoNode);
    }

    private void saveVideo(VideoNode videoNode) {
        videoNode.foreach((path, node) -> {
            if(node.getVideoUrl() == null) {
                return;
            }
            this.saveVideo(path + "/" + node.getFileName() + ".mp4", node.getVideoUrl());
        });
    }

    private static int count = 0;

    private void saveVideo(String path, String videoUrl) {
        if(!new File(path).getParentFile().exists()) {
            new File(path).getParentFile().mkdirs();
        }
        new Thread() {
            @Override
            public void run() {
                System.out.println("start:" + ++count);
                System.out.println(path);
                try {
                    Request.Get(videoUrl)
                            .addHeader("Cookie", COOKIE)
                            .addHeader("xtbz", "xt")
                            .execute()
                            .saveContent(new File(path));
                    System.out.println("end:" + --count);
                    System.out.println(path);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void fullVideo(String classroomId, String productSign, VideoNode videoNode) {
        videoNode.foreach((path, node) -> {
            if(!node.isLeaf()) {
                return;
            }
            System.out.println(path + "/" + node.getName());
            try {
                String videoId = this.getVideoId(classroomId, productSign, node.getChapterId());
                if(videoId == null) {
                    return;
                }
                node.setVideoId(videoId);
                String videoUrl = this.getVideoUrl(videoId);
                if(videoUrl == null) {
                    return;
                }
                node.setVideoUrl(videoUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public String getVideoUrl(String videoId) throws IOException {
        String url = "https://next.xuetangx.com/api/v1/lms/service/playurl/" + videoId + "/?appid=10000";
        String videoUrlStr = this.request(url);
        String videoUrl = JSON.parseObject(videoUrlStr)
                .getJSONObject("data")
                .getJSONObject("sources")
                .getJSONArray("quality20")
                .getString(0);
        return videoUrl;
    }

    public String getVideoId(String classroomId, String productSign, String chapterId) throws IOException {
        String url = "https://next.xuetangx.com/api/v1/lms/learn/leaf_info/" + classroomId + "/" + chapterId + "/?sign=" + productSign;
        String videoIdStr = this.request(url);
        String videoId = JSON.parseObject(videoIdStr)
                .getJSONObject("data")
                .getJSONObject("content_info")
                .getJSONObject("media")
                .getString("ccid");
        return videoId;
    }

    public Course getCourse(String classroomId, String productSign) throws IOException {
        String url = "https://next.xuetangx.com/api/v1/lms/learn/course/chapter?cid=" + classroomId + "&sign=" + productSign;
        String chaptersStr = this.request(url);
        CourseResult courseResult = JSON.parseObject(chaptersStr, CourseResult.class);
        if(courseResult.getSuccess() == null || !courseResult.getSuccess()) {
            return null;
        }
        return courseResult.getData();
    }

    public String request(String url) throws IOException {
        return Request.Get(url)
                .addHeader("Cookie", COOKIE)
                .addHeader("xtbz", "xt")
                .execute()
                .returnContent()
                .asString();
    }

}
