package v20170926.xulaAtmModel.result;

import java.util.ArrayList;

public class GetTransactionIdsResult extends Result{

    private ArrayList<Long> transactionIDs;

    public GetTransactionIdsResult(int status, String message) {
        super(status, message);
    }

    public GetTransactionIdsResult(Result result) {
        super(result.getStatus(),result.getMessage());

    }

    public GetTransactionIdsResult(int status, ArrayList<Long> transactionIDs) {
        super(status);
        this.transactionIDs = transactionIDs;

    }

    public ArrayList<Long> getTransactionIDs() {
        return transactionIDs;
    }

}
