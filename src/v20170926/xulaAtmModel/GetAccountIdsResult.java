package v20170926.xulaAtmModel;

import java.util.ArrayList;

public class GetAccountIdsResult extends Result{

    private ArrayList<Long> accountIDs;

    public GetAccountIdsResult(int status, String message) {
        super(status, message);
    }

    public GetAccountIdsResult(Result result) {
        super(result.getStatus(),result.getMessage());

    }

    public GetAccountIdsResult(int status, ArrayList<Long> accountIDs) {
        super(status);
        this.accountIDs = accountIDs;

    }

    public ArrayList<Long> getAccountIDs() {
        return accountIDs;
    }

}
