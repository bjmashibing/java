package com.mashibing.inject;


   /**
    * 自定义全局删除方法
    */

    public enum MySqlMethod {


    /**
     * 删除全部
     */
    DELETE_ALL("deleteAll", "根据 entity 条件删除记录", "<script>\nDELETE FROM %s %s\n</script>");


    private final String method;
    private final String desc;
    private final String sql;

    MySqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }

}