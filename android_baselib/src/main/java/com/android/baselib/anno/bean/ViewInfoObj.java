package com.android.baselib.anno.bean;

/**
 * 控件id实体
 * <p>
 * 本控件类主要是以key的形式存储到ConcurrentHashMap中，所以需要手动重写其equals 和 hashCode方法，以便map判断相同控件id的view为同一对象
 *
 * @author PF-NAN
 * @date 2017/9/29
 */
public class ViewInfoObj {
    /**
     * 控件ID
     */
    public int value;
    /**
     * 父控件ID
     */
    public int parentId;

    /**
     * 构造
     *
     * @param value    控件ID
     * @param parentId 父控件ID
     */
    public ViewInfoObj(int value, int parentId) {
        this.value = value;
        this.parentId = parentId;
    }

    /**
     * 重写equals
     *
     * @param obj 需要对比的对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (null == obj || getClass() != obj.getClass()) {
            return false;
        }
        ViewInfoObj viewInfoObj = (ViewInfoObj) obj;
        if (value != viewInfoObj.value) {
            return false;
        }
        return parentId == viewInfoObj.parentId;
    }

    /**
     * 重写hashCode
     *
     * @return 控件实体唯一标识
     */
    @Override
    public int hashCode() {
        return 31 * value + parentId;
    }
}
