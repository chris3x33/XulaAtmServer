package v20170926.xulaAtmModel.result;


import v20170926.xulaAtmModel.XulaATMTransaction;

public class GetTransactionResult extends Result {

    private XulaATMTransaction atmTransaction;

    public GetTransactionResult(
             int status, XulaATMTransaction atmTransaction) {

        super( status);

        this.atmTransaction = atmTransaction;
    }

    public GetTransactionResult(int status, String message) {
        super( status, message);
    }

    public XulaATMTransaction getAtmTransaction() {
        return atmTransaction;
    }
}
