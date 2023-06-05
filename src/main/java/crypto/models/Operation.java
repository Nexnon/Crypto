package crypto.models;

import crypto.database.OperationDAO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Time;
import java.util.Date;

@Entity
@Table(name="operations")
public class Operation {

    @Id
    private int id;
    private static int max_id;

    private Date date;
    private int code;

    public Operation(){}
    public Operation(int code){
        id = max_id++;

        this.code = code;
        date = new Date();
    }

    public static void setMax_id(){
        max_id = OperationDAO.getMaxID() + 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
