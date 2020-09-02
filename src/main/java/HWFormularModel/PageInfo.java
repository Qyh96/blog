package HWFormularModel;

public class PageInfo {
    private int pageSize = 20;
    private int pageNum = 1;
    private int countSize = 100;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getCountSize() {
        return countSize;
    }

    public void setCountSize(int countSize) {
        this.countSize = countSize;
    }
}
