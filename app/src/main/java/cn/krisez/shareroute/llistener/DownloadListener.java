package cn.krisez.shareroute.llistener;

public interface DownloadListener {
    void onProgress(int progress);
    void onFinish();
}
