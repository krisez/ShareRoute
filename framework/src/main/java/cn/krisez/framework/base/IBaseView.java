package cn.krisez.framework.base;

import android.os.Bundle;

public interface IBaseView {
    void handle(HandleType type, Bundle bundle);
    void error(String e);
}
