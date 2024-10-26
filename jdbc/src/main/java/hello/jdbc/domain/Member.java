package hello.jdbc.domain;

import jdk.jfr.DataAmount;
import lombok.Data;

@Data
public class Member {

    private String memberId;
    private int money;

    public Member(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }

    public Member(){

    }
}
