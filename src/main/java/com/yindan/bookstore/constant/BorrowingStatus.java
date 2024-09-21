package com.yindan.bookstore.constant;

public enum BorrowingStatus{

        BORROWING("借阅中"),
        RETURNED("已归还"),
        SELLOFF("已售出");

        private final String displayName;

        BorrowingStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        // 可以添加从字符串到枚举的转换方法
        public static BorrowingStatus fromDisplayName(String displayName) {
            for (BorrowingStatus status : values()) {
                if (status.getDisplayName().equals(displayName)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid display name: " + displayName);
        }




}
