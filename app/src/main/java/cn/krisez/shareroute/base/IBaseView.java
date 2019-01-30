package cn.krisez.shareroute.base;

import android.os.Bundle;

public interface IBaseView {
    void handle(HandleType type, Bundle bundle);
    void error(String e);
}
