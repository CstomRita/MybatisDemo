package pojo;

/**
 * @ Author     ：CstomRita
 * @ Date       ：Created in 下午6:55 2018/9/6
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
public class Item {
    private Long num;
    private Long price;
    private Long id;
    private String title;

    public void setNum(Long num) {
        this.num = num;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getNum() {
        return num;
    }

    public Long getPrice() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
