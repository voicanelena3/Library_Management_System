package library.model;

import java.io.Serializable;
import java.util.Date;

public class Request implements Serializable {
    private Date requestDate;

    public Request() {}

    public Request(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getRequestDate() { return requestDate; }
    public void setRequestDate(Date requestDate) { this.requestDate = requestDate; }

    @Override
    public String toString() {
        return "Request{" +
                "requestDate=" + requestDate +
                '}';
    }
}
