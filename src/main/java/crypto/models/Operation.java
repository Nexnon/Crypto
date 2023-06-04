package crypto.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Time;
import java.util.Date;

@Entity
@Table(name="operations")
public class Operation {

    @Id
    private long id;
    private static long max_id = 0; ///!!!!

    private Date date;
    private int code;

    public Operation(){}
    public Operation(int code){
        id = max_id++;

        this.code = code;
        date = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
