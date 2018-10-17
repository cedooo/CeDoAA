public class 测试 {
    private String 名称;
    private int 年龄;
    private String 住址;

    public String get名称() {
        return 名称;
    }

    public void set名称(String 名称) {
        this.名称 = 名称;
    }

    public int get年龄() {
        return 年龄;
    }

    public void set年龄(int 年龄) {
        this.年龄 = 年龄;
    }

    public String get住址() {
        return 住址;
    }

    public void set住址(String 住址) {
        this.住址 = 住址;
    }

    public static void main(String[] args){
        测试 测试实例 = new 测试();
        测试实例.set名称("这是测试名称");
        测试实例.set年龄(15);
        测试实例.set住址("中国");
        System.out.println(测试实例);
    }

    @Override
    public String toString() {
        return "测试{" +
                "名称='" + 名称 + '\'' +
                ", 年龄=" + 年龄 +
                ", 住址='" + 住址 + '\'' +
                '}';
    }
}
